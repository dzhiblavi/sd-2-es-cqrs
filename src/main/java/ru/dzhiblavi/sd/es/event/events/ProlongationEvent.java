package ru.dzhiblavi.sd.es.event.events;

import java.time.Duration;

public class ProlongationEvent {
    private final long id;
    private final Duration validity;

    public ProlongationEvent(long membershipId, Duration validity) {
        this.id = membershipId;
        this.validity = validity;
    }

    public long getId() {
        return id;
    }

    public Duration getValidity() {
        return validity;
    }
}
