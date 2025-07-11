package co.com.bancolombia.model.events;

import co.com.bancolombia.model.box.BoxStatus;

import java.time.Instant;

public class BoxDeletedEvent extends DomainEventPayload{
    private BoxStatus statusOld;
    private BoxStatus statusNew;

    public BoxDeletedEvent(String boxId, String createdBy, Instant createdAt) {
        super(boxId, createdBy, createdAt);
    }

    public BoxStatus getStatusOld() {
        return statusOld;
    }

    public void setStatusOld(BoxStatus statusOld) {
        this.statusOld = statusOld;
    }

    public BoxStatus getStatusNew() {
        return statusNew;
    }

    public void setStatusNew(BoxStatus statusNew) {
        this.statusNew = statusNew;
    }
}