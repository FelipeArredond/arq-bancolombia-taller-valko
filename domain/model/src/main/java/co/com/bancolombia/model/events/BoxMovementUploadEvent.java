package co.com.bancolombia.model.events;

import co.com.bancolombia.model.box.BoxStatus;

import java.time.Instant;

public class BoxMovementUploadEvent extends DomainEventPayload{
    private int validLines;
    private int failedLines;
    private int totalLines;

    public BoxMovementUploadEvent(String boxId, String createdBy, Instant createdAt) {
        super(boxId, createdBy, createdAt);
    }

    public BoxMovementUploadEvent(String boxId, String createdBy, Instant createdAt, int validLines, int failedLines, int totalLines) {
        super(boxId, createdBy, createdAt);
        this.validLines = validLines;
        this.failedLines = failedLines;
        this.totalLines = totalLines;
    }

    public void setValidLines(int validLines) {
        this.validLines = validLines;
    }

    public void setFailedLines(int failedLines) {
        this.failedLines = failedLines;
    }

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    public int getValidLines() {
        return validLines;
    }

    public int getFailedLines() {
        return failedLines;
    }

    public int getTotalLines() {
        return totalLines;
    }
}