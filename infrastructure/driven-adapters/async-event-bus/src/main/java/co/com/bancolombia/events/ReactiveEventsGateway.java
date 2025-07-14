package co.com.bancolombia.events;

import co.com.bancolombia.model.events.EventTypeEnum;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static co.com.bancolombia.model.events.EventsIdsConstans.*;
import static reactor.core.publisher.Mono.from;

@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    private final DomainEventBus domainEventBus;

    public ReactiveEventsGateway(DomainEventBus domainEventBus) {
        this.domainEventBus = domainEventBus;
    }

    @Override
    public Mono<Void> emit(Object event) {
         return from(domainEventBus.emit(new DomainEvent<>(BOX_CREATED, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> emit(Object event, EventTypeEnum eventType) {
        return from(domainEventBus.emit(new DomainEvent<>(getEventId(eventType), UUID.randomUUID().toString(), event)));
    }

    private String getEventId(EventTypeEnum eventType) {
        return switch (eventType) {
            case BOX_CREATED_EVENT -> BOX_CREATED_EVENT_ID;
            case BOX_DELETED_EVENT -> BOX_DELETED_EVENT_ID;
            case BOX_REOPENED_EVENT -> BOX_REOPENED_EVENT_ID;
            case BOX_MOVEMENT_UPLOAD_EVENT -> BOX_MOVEMENT_UPLOAD_EVENT_ID;
            case BOX_NAME_UPDATED_EVENT -> BOX_NAME_UPDATED_EVENT_ID;
        };
    }

}
