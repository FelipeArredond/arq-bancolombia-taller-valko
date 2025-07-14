package co.com.bancolombia.events;
import co.com.bancolombia.events.handlers.EventsHandler;
import co.com.bancolombia.model.events.EventsIdsConstans;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {

    // see more at: https://reactivecommons.org/reactive-commons-java/#_handlerregistry_2
    @Bean
    public HandlerRegistry handlerRegistry(EventsHandler events) {
        return HandlerRegistry.register()
                .listenEvent(EventsIdsConstans.BOX_CREATED, events::handleEventA, Object.class)
                .listenEvent(EventsIdsConstans.BOX_CREATED_EVENT_ID, events::handleEventA, Object.class)
                .listenEvent(EventsIdsConstans.BOX_REOPENED_EVENT_ID, events::handleEventA, Object.class)
                .listenEvent(EventsIdsConstans.BOX_DELETED_EVENT_ID, events::handleEventA, Object.class)
                .listenEvent(EventsIdsConstans.BOX_MOVEMENT_UPLOAD_EVENT_ID, events::handleEventA, Object.class)
                .listenEvent(EventsIdsConstans.BOX_NAME_UPDATED_EVENT_ID, events::handleEventA, Object.class);
    }
}
