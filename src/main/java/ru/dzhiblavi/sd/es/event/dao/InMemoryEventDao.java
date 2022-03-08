package ru.dzhiblavi.sd.es.event.dao;

import ru.dzhiblavi.sd.es.event.events.NewMemberEvent;
import ru.dzhiblavi.sd.es.event.events.ProlongationEvent;
import ru.dzhiblavi.sd.es.event.events.VisitEvent;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryEventDao implements EventDao {
    private final Set<NewMemberEvent> newMemberEventSet = new HashSet<>();
    private final Set<ProlongationEvent> prolongationEventSet = new HashSet<>();
    private final Set<VisitEvent> visitEventSet = new HashSet<>();

    @Override
    public void addEvent(final NewMemberEvent event) {
        newMemberEventSet.add(event);
    }

    @Override
    public void addEvent(final ProlongationEvent event) {
        prolongationEventSet.add(event);
    }

    @Override
    public void addEvent(final VisitEvent event) {
        visitEventSet.add(event);
    }

    private Stream<NewMemberEvent> getNewMemberEventStream() {
        return newMemberEventSet.stream()
                .sorted(Comparator.comparing(NewMemberEvent::getTimestamp));
    }

    private Stream<ProlongationEvent> getProlongationEventStream() {
        return prolongationEventSet.stream();
    }

    private Stream<VisitEvent> getVisitEventStream() {
        return visitEventSet.stream()
                .sorted(Comparator.comparing(VisitEvent::getTimestamp));
    }

    @Override
    public List<NewMemberEvent> getNewMembershipEvents() {
        return getNewMemberEventStream().collect(Collectors.toList());
    }

    @Override
    public List<ProlongationEvent> getProlongationEvents() {
        return new ArrayList<>(prolongationEventSet);
    }

    @Override
    public List<NewMemberEvent> getNewMembershipEvents(long id) {
        return getNewMemberEventStream()
                .filter(event -> event.getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProlongationEvent> getProlongationEvents(long id) {
        return getProlongationEventStream()
                .filter(event -> event.getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public List<VisitEvent> getVisitEventsSince(Instant timestamp) {
        return getVisitEventStream()
                .filter(event -> event.getTimestamp().isAfter(timestamp))
                .collect(Collectors.toList());
    }
}
