package ru.dzhiblavi.sd.es.event.events;

import java.time.Duration;
import java.time.Instant;

public class NewMemberEvent {
    private final long id;
    private final Instant timestamp;
    private final Duration validity;

    public NewMemberEvent(long membershipId, Instant timestamp, Duration validity) {
        this.id = membershipId;
        this.timestamp = timestamp;
        this.validity = validity;
    }

    public long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Duration getValidity() {
        return validity;
    }
}
