package co.com.bancolombia.model.events;

import java.time.Instant;

public abstract class DomainEventPayload  {

    private String boxId;
    private String createdBy;
    private Instant createdAt;

    protected DomainEventPayload(String boxId, String createdBy, Instant createdAt) {
        this.boxId = boxId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}