package ru.dzhiblavi.sd.es.model;

import java.time.Duration;
import java.time.Instant;

public class Membership {
    private final long id;
    private Instant validUntil;

    public Membership(long id, Instant validUntil) {
        this.id = id;
        this.validUntil = validUntil;
    }

    public long getId() {
        return id;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void prolongate(final Duration validity) {
        this.validUntil = this.validUntil.plus(validity);
    }
}
