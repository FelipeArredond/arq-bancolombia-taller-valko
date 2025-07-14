package co.com.bancolombia.usecase.deletebox;

import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxDeletedEvent;
import co.com.bancolombia.model.events.EventTypeEnum;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class DeleteboxUseCase {

    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public DeleteboxUseCase(BoxRepository boxRepository, EventsGateway eventsGateway){
        this.boxRepository = boxRepository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Void> deleteUseCase(String boxId) {
        return boxRepository
                .findById(boxId)
                .flatMap(box -> {
                    box.setStatus(BoxStatus.DELETED);
                    return Mono.just(box);
                })
                .flatMap(boxRepository::save)
                .flatMap(box -> eventsGateway.emit(new BoxDeletedEvent(boxId, "", Instant.now()),
                        EventTypeEnum.BOX_DELETED_EVENT))
                .then();
    }

}
