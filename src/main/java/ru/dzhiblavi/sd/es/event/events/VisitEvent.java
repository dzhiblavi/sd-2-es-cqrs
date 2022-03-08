package ru.dzhiblavi.sd.es.event.events;

import java.time.Instant;

public class VisitEvent {
    public enum Direction { ENTER, EXIT };

    private final Direction direction;
    private final long id;
    private final Instant timestamp;

    public VisitEvent(Direction direction, long id, Instant timestamp) {
        this.direction = direction;
        this.id = id;
        this.timestamp = timestamp;
    }

    public Direction getDirection() {
        return direction;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public long getId() {
        return id;
    }
}
