package co.com.bancolombia.usecase.updateboxname;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxNameUpdatedEvent;
import co.com.bancolombia.model.events.EventTypeEnum;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class UpdateboxnameUseCase {

    private final BoxRepository repository;
    private final EventsGateway eventsGateway;

    public UpdateboxnameUseCase(BoxRepository repository, EventsGateway eventsGateway) {
        this.repository = repository;
        this.eventsGateway = eventsGateway;
    }

    public Mono<Box> updateBoxName (String id, String nameUpdated){
        return repository.findById(id)
                .flatMap(box -> {
                    String nameOld = box.getName();
                    box.setName(nameUpdated);
                    return repository.save(box)
                            .flatMap(savedBox -> {
                        BoxNameUpdatedEvent event = new BoxNameUpdatedEvent(savedBox.getId(), "", Instant.now());
                        event.setNameOld(nameOld);
                        event.setNameNew(savedBox.getName());
                        return eventsGateway.emit(event, EventTypeEnum.BOX_NAME_UPDATED_EVENT).thenReturn(savedBox);
                    });
                });
    }

}
