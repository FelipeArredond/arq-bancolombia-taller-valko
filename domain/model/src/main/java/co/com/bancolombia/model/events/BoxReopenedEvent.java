package co.com.bancolombia.model.events;

import co.com.bancolombia.model.box.BoxStatus;

import java.time.Instant;

public class BoxReopenedEvent extends DomainEventPayload{
    private BoxStatus statusOld;
    private BoxStatus statusNew;

    public BoxReopenedEvent(String boxId, String createdBy, Instant createdAt) {
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