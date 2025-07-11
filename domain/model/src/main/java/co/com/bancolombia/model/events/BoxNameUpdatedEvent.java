package co.com.bancolombia.model.events;

import java.time.Instant;

public class BoxNameUpdatedEvent extends DomainEventPayload{

    private String nameOld;
    private String nameNew;

    public BoxNameUpdatedEvent(String boxId, String createdBy, Instant createdAt) {
        super(boxId, createdBy, createdAt);
    }

    public String getNameOld() {
        return nameOld;
    }

    public void setNameOld(String nameOld) {
        this.nameOld = nameOld;
    }

    public String getNameNew() {
        return nameNew;
    }

    public void setNameNew(String nameNew) {
        this.nameNew = nameNew;
    }
}