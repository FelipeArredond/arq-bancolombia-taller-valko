package co.com.bancolombia.model.events.gateways;

import co.com.bancolombia.model.events.EventTypeEnum;
import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> emit(Object event);
    Mono<Void> emit(Object event, EventTypeEnum eventType);
}
