package ru.dzhiblavi.sd.es.services.report;

import ru.dzhiblavi.sd.es.event.events.VisitEvent;
import ru.dzhiblavi.sd.es.model.DataReader;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsService {
    private final List<VisitEvent> visitEventList = new ArrayList<>();
    private final DataReader dataReader;
    private Instant maxTimestamp = Instant.MIN;

    public StatisticsService(final DataReader dataReader) {
        this.dataReader = dataReader;
        this.updateVisitEventList();
    }

    private void updateVisitEventList() {
        this.visitEventList.addAll(this.dataReader.getAllVisitsSince(maxTimestamp));
        if (!this.visitEventList.isEmpty()) {
            this.maxTimestamp = visitEventList.get(visitEventList.size() - 1).getTimestamp();
        }
    }

    private Map<LocalDate, List<VisitEvent>> getDailyEnterEvents() {
        return visitEventList.stream()
                .filter(event -> event.getDirection() == VisitEvent.Direction.ENTER)
                .collect(Collectors.groupingBy(event -> LocalDate.ofInstant(
                        event.getTimestamp(), TimeZone.getDefault().toZoneId()
                )));
    }

    private Map<Long, List<VisitEvent>> getMemberEnterEvents() {
        return visitEventList.stream()
                .filter(event -> event.getDirection() == VisitEvent.Direction.ENTER)
                .collect(Collectors.groupingBy(VisitEvent::getId));
    }

    public Map<LocalDate, Integer> dailyStatistics() {
        updateVisitEventList();
        return getDailyEnterEvents().entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size()
                )
        );
    }

    public double averageCount() {
        updateVisitEventList();
        return getMemberEnterEvents().values().stream()
                .map(List::size).mapToDouble(Double::valueOf).average().orElse(0.0);
    }

    public double averageDuration() {
        updateVisitEventList();
        final Map<Long, Instant> memberToEnter = new HashMap<>();
        final List<Double> durations = new ArrayList<>();

        for (final VisitEvent event : visitEventList) {
            long id = event.getId();
            Instant timestamp = event.getTimestamp();

            switch (event.getDirection()) {
                case ENTER:
                    memberToEnter.put(id, timestamp);
                    break;
                case EXIT:
                    if (!memberToEnter.containsKey(id)) {
                        throw new IllegalStateException("Member " + id + " exited without entering.");
                    }
                    durations.add((double) timestamp.toEpochMilli() - memberToEnter.get(id).toEpochMilli());
                    break;
            }
        }

        return durations.stream().mapToDouble(Double::valueOf).average().orElse(0.0);
    }
}
