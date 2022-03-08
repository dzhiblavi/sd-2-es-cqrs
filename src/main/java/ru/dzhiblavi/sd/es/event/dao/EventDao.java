package ru.dzhiblavi.sd.es.event.dao;

import ru.dzhiblavi.sd.es.event.events.NewMemberEvent;
import ru.dzhiblavi.sd.es.event.events.ProlongationEvent;
import ru.dzhiblavi.sd.es.event.events.VisitEvent;

import java.time.Instant;
import java.util.List;

public interface EventDao {
    void addEvent(final NewMemberEvent event);

    void addEvent(final ProlongationEvent event);

    void addEvent(final VisitEvent event);

    List<NewMemberEvent> getNewMembershipEvents();

    List<ProlongationEvent> getProlongationEvents();

    List<NewMemberEvent> getNewMembershipEvents(final long id);

    List<ProlongationEvent> getProlongationEvents(final long id);

    List<VisitEvent> getVisitEventsSince(final Instant timestamp);
}
