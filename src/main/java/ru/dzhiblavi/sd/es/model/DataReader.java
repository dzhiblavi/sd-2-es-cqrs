package ru.dzhiblavi.sd.es.model;

import ru.dzhiblavi.sd.es.event.dao.EventDao;
import ru.dzhiblavi.sd.es.event.events.NewMemberEvent;
import ru.dzhiblavi.sd.es.event.events.ProlongationEvent;
import ru.dzhiblavi.sd.es.event.events.VisitEvent;

import java.time.Instant;
import java.util.*;

public class DataReader {
    private final EventDao eventDao;

    public DataReader(final EventDao eventDao) {
        this.eventDao = eventDao;
    }

    private List<Membership> getMembershipsByData(
            final List<NewMemberEvent> newMemberEvents,
            final List<ProlongationEvent> prolongationEvents) {
        final Map<Long, Membership> memberships = new HashMap<>();

        for (final NewMemberEvent newMemberEvent : newMemberEvents) {
            Membership membership = new Membership(
                    newMemberEvent.getId(),
                    newMemberEvent.getTimestamp().plus(newMemberEvent.getValidity())
            );
            memberships.put(membership.getId(), membership);
        }

        for (final ProlongationEvent prolongationEvent : prolongationEvents) {
            memberships.get(prolongationEvent.getId())
                    .prolongate(prolongationEvent.getValidity());
        }

        return new ArrayList<>(memberships.values());
    }

    public List<Membership> getMemberships() {
        return getMembershipsByData(
                eventDao.getNewMembershipEvents(),
                eventDao.getProlongationEvents()
        );
    }

    public Optional<Membership> getMembership(final long id) {
        final List<Membership> memberships = getMembershipsByData(
                eventDao.getNewMembershipEvents(id),
                eventDao.getProlongationEvents(id)
        );
        if (memberships.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(memberships.get(0));
        }
    }

    public List<VisitEvent> getAllVisitsSince(final Instant timestamp) {
        return eventDao.getVisitEventsSince(timestamp);
    }
}
