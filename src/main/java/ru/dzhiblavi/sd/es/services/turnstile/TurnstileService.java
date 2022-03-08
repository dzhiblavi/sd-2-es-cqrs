package ru.dzhiblavi.sd.es.services.turnstile;

import ru.dzhiblavi.sd.es.event.events.VisitEvent;
import ru.dzhiblavi.sd.es.model.DataReader;
import ru.dzhiblavi.sd.es.model.DataWriter;
import ru.dzhiblavi.sd.es.model.Membership;

import java.time.Instant;
import java.util.Optional;

public class TurnstileService {
    private final DataReader dataReader;
    private final DataWriter dataWriter;

    public TurnstileService(final DataReader dataReader, final DataWriter dataWriter) {
        this.dataReader = dataReader;
        this.dataWriter = dataWriter;
    }

    public void pass(final long id, final VisitEvent.Direction direction) {
        final Optional<Membership> membership = dataReader.getMembership(id);
        if (membership.isEmpty()) {
            throw new IllegalArgumentException("Member with id " + id + " has no membership.");
        }
        if (direction == VisitEvent.Direction.ENTER &&
                membership.get().getValidUntil().isBefore(Instant.now())) {
            throw new IllegalArgumentException(
                    "Membership of " + membership.get().getId() + " has expired: cannot enter the GYM."
            );
        }
        dataWriter.visitCommand(id, direction);
    }
}
