package co.com.bancolombia.api;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.usecase.closebox.CloseBoxUseCase;
import co.com.bancolombia.usecase.createbox.CreateBoxUseCase;
import co.com.bancolombia.usecase.deletebox.DeleteboxUseCase;
import co.com.bancolombia.usecase.getboxbyid.GetboxbyidUseCase;
import co.com.bancolombia.usecase.listallboxes.ListallboxesUseCase;
import co.com.bancolombia.usecase.openbox.OpenBoxUseCase;
import co.com.bancolombia.usecase.updateboxname.UpdateboxnameUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;

@Component
public class Handler {

    private final CreateBoxUseCase createBoxUseCase;
    private final OpenBoxUseCase openBoxUseCase;
    private final CloseBoxUseCase closeBoxUseCase;
    private final GetboxbyidUseCase getboxbyidUseCase;
    private final ListallboxesUseCase listallboxesUseCase;
    private final UpdateboxnameUseCase updateboxnameUseCase;
    private final DeleteboxUseCase deleteboxUseCase;

    public Handler(CreateBoxUseCase createBoxUseCase, OpenBoxUseCase openBoxUseCase, CloseBoxUseCase closeBoxUseCase, GetboxbyidUseCase getboxbyidUseCase, ListallboxesUseCase listallboxesUseCase, UpdateboxnameUseCase updateboxnameUseCase, DeleteboxUseCase deleteboxUseCase) {
        this.createBoxUseCase = createBoxUseCase;
        this.openBoxUseCase = openBoxUseCase;
        this.closeBoxUseCase = closeBoxUseCase;
        this.getboxbyidUseCase = getboxbyidUseCase;
        this.listallboxesUseCase = listallboxesUseCase;
        this.updateboxnameUseCase = updateboxnameUseCase;
        this.deleteboxUseCase = deleteboxUseCase;
    }

    public Mono<ServerResponse> updateBoxName(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(Box.class).flatMap(box ->
                        updateboxnameUseCase.updateBoxName(id, box.getName()))
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)));
    }

    public Mono<ServerResponse> listAllBoxes(ServerRequest serverRequest) {
        return listallboxesUseCase.listAllBoxes()
                .collectList()
                .flatMap(box -> ServerResponse.ok().body(BodyInserters.fromValue(box)));
    }

    public Mono<ServerResponse> getBoxById(ServerRequest serverRequest) {
        var boxId = serverRequest.pathVariable("id");
        return getboxbyidUseCase.getBoxById(boxId)
                .flatMap(box -> ServerResponse.ok().body(BodyInserters.fromValue(box)));
    }

    public Mono<ServerResponse> createBox(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Box.class).flatMap(box ->
                createBoxUseCase.createBox(box.getId(), box.getName()))
                .flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)));
    }

    public Mono<ServerResponse> open(ServerRequest request){
        String id = request.pathVariable("id");
        return openBoxUseCase.openBox(id, BigDecimal.ZERO).flatMap(currentPet -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(currentPet)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> close(ServerRequest request){
        String id = request.pathVariable("id");
        return closeBoxUseCase.closeBox(id, BigDecimal.ZERO).flatMap(currentPet -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(currentPet)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteBox(ServerRequest request) {
        var boxId = request.pathVariable("id");
        return deleteboxUseCase.deleteUseCase(boxId)
                .flatMap(unused -> ServerResponse.ok().build());
    }

}
