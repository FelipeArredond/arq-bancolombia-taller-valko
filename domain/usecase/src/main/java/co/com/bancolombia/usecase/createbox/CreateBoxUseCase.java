package co.com.bancolombia.usecase.createbox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxCreatedEvent;
import co.com.bancolombia.model.events.EventTypeEnum;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

public class CreateBoxUseCase {
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;
    public CreateBoxUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Box> createBox(String id, String name) {
        return boxRepository.findById(id)
                .flatMap(existing -> Mono.<Box>error(new IllegalStateException("La caja ya existe")))
                .switchIfEmpty(
                        boxRepository.save(new Box.Builder()
                                .id(id)
                                .name(name)
                                .status(BoxStatus.CLOSED)
                                .currentBalance(BigDecimal.ZERO)
                                .build())
                                .flatMap(box -> eventsGateway
                                        .emit(new BoxCreatedEvent(id, "", Instant.now()),
                                                EventTypeEnum.BOX_CREATED_EVENT)
                                        .thenReturn(box)
                                )
                );
    }
}
