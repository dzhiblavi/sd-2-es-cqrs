package ru.dzhiblavi.sd.es.services.manager;

import ru.dzhiblavi.sd.es.model.DataReader;
import ru.dzhiblavi.sd.es.model.DataWriter;
import ru.dzhiblavi.sd.es.model.Membership;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class ManagerService {
    private final DataReader dataReader;
    private final DataWriter dataWriter;

    public ManagerService(final DataReader dataReader, final DataWriter dataWriter) {
        this.dataReader = dataReader;
        this.dataWriter = dataWriter;
    }

    public void issueNewMembership(final long id, final Duration validity) {
        final Optional<Membership> membershipOptional = dataReader.getMembership(id);
        if (membershipOptional.isPresent() &&
                membershipOptional.get().getValidUntil().isAfter(Instant.now())) {
            final Membership membership = membershipOptional.get();
            throw new IllegalArgumentException(
                    "Membership for id " + id + " is already registered and valid until "
                            + membership.getValidUntil()
            );
        } else {
            dataWriter.newMembershipCommand(id, validity);
        }
    }

    public void prolongateMembership(final long id, final Duration validity) {
        if (dataReader.getMembership(id).isEmpty()) {
            throw new IllegalArgumentException("Member with id " + id + " is not registered.");
        }
        dataWriter.prolongateCommand(id, validity);
    }
}
