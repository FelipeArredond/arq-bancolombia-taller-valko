package co.com.bancolombia.api;

import co.com.bancolombia.filereader.LineParser;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.events.BoxMovementUploadEvent;
import co.com.bancolombia.model.events.EventTypeEnum;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.usecase.uploadmovement.UploadMovementsUseCase;
import co.com.bancolombia.usecase.saveerror.SaveErrorUseCase;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MovementHandler {

    private final UploadMovementsUseCase uploadMovementsUseCase;
    private final SaveErrorUseCase saveErrorUseCase;
    private final LineParser lineParser;
    private final EventsGateway events;

    public MovementHandler(UploadMovementsUseCase uploadMovementsUseCase, SaveErrorUseCase saveErrorUseCase, LineParser lineParser, EventsGateway events) {
        this.uploadMovementsUseCase = uploadMovementsUseCase;
        this.saveErrorUseCase = saveErrorUseCase;
        this.lineParser = lineParser;
        this.events = events;
    }

    public Mono<ServerResponse> uploadMovement(ServerRequest request) {
        String boxId = request.pathVariable("id");
        long maxFileSize = 5 * 1024 * 1024;

        return request.multipartData()
                .flatMap(multipartData -> {
                    FilePart filePart = (FilePart) multipartData.getFirst("file");
                    if (filePart == null) {
                        return ServerResponse.badRequest().bodyValue("No file provided");
                    }

                    String filename = filePart.filename();
                    if (fileTypeValidations(filename)) {
                        return ServerResponse.badRequest().bodyValue("Only CSV or TXT files are allowed");
                    }

                    return filePart.content()
                            .map(DataBuffer::readableByteCount)
                            .reduce(0L, Long::sum)
                            .flatMap(size -> {
                                Mono<ServerResponse> bodyValue = fileSizeValidation(size, maxFileSize);
                                if (bodyValue != null) return bodyValue;
                                return processMovementFile(filePart, boxId);
                            });
                });
    }

    private static Mono<ServerResponse> fileSizeValidation(Long size, long maxFileSize) {
        if (size > maxFileSize) {
            return ServerResponse.badRequest()
                    .bodyValue("File size exceeds 5MB limit");
        }
        return null;
    }

    private static boolean fileTypeValidations(String filename) {
        return !filename.toLowerCase().endsWith(".csv") &&
                !filename.toLowerCase().endsWith(".txt");
    }

    private Mono<ServerResponse> processMovementFile(FilePart filePart, String boxId) {
        Set<Integer> registerdMovementsIds = new HashSet<>();
        AtomicInteger proccessedMovements = new AtomicInteger();
        AtomicInteger rejectedMovements = new AtomicInteger();
        return filePart.content()
                .map(dataBuffer -> {
                    var reader = new BufferedReader(
                            new InputStreamReader(dataBuffer.asInputStream(), StandardCharsets.UTF_8)
                    );
                    return reader;
                })
                .concatMap(reader -> Flux.fromStream(reader.lines())
                                .skip(1)
                                .map(line -> lineParser.parseMovementLine(line, boxId))
                        .flatMap(movement -> {
                            movementIdValidation(movement, registerdMovementsIds);
                            movement.executeValidations();
                            return uploadMovementsUseCase.createMovement(movement, boxId)
                                    .doOnSuccess(savedMovement -> {
                                        proccessedMovements.getAndIncrement();
                                        registerdMovementsIds.add(Integer.valueOf(savedMovement.getMovementId()));
                                    })
                                    .onErrorResume(throwable -> {
                                        rejectedMovements.getAndIncrement();
                                        var error = Error.builder()
                                                .movementId(movement.getMovementId())
                                                .boxId(movement.getBoxId())
                                                .date(LocalDateTime.now())
                                                .errorMessage(throwable.getMessage())
                                                .build();
                                        return saveErrorUseCase.saveError(error).then(Mono.empty());
                                    });
                        })
                )
                .collectList()
                .flatMap(avoid -> {
                    var event = new BoxMovementUploadEvent(boxId, "", Instant.now());
                    event.setFailedLines(rejectedMovements.get());
                    event.setValidLines(proccessedMovements.get());
                    event.setTotalLines(proccessedMovements.get() + rejectedMovements.get());
                    return events.emit(event, EventTypeEnum.BOX_MOVEMENT_UPLOAD_EVENT)
                            .then(ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of(
                                            "proccessedMovements", proccessedMovements.get(),
                                            "rejectedMovements", rejectedMovements.get()
                                    )));
                })
                .onErrorResume(e -> ServerResponse.badRequest()
                        .bodyValue("Error processing file: " + e.getMessage())
                );
    }

    private static void movementIdValidation(Movement movement, Set<Integer> registerdMovementsIds) {
        if(registerdMovementsIds.contains(Integer.valueOf(movement.getMovementId()))) {
            throw new IllegalArgumentException("Movement Id already registered in this file");
        }
    }

}
