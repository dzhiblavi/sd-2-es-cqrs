package ru.dzhiblavi.sd.es.services.report;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dzhiblavi.sd.es.base.BaseTest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.TimeZone;

public class StatisticsTest extends BaseTest {
    private StatisticsService statisticsService = null;

    private void setupStatisticsService() {
        this.statisticsService = new StatisticsService(dataReader);
    }

    private LocalDate visit(final long id, final long day, final long duration) {
        final long timestamp = day * 86400000 + 239;
        registerEnter(id, timestamp);
        registerExit(id, timestamp + duration);
        return LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId());
    }

    private LocalDate visit(final long id, final long day) {
        return visit(id, day, 239);
    }

    @Test
    public void testEmptyDailyStats() {
        setupStatisticsService();
        Assertions.assertEquals(Map.of(), statisticsService.dailyStatistics());
    }

    @Test
    public void testEmptyAverageCount() {
        setupStatisticsService();
        Assertions.assertEquals(0, statisticsService.averageCount());
    }

    @Test
    public void testEmptyAverageDuration() {
        setupStatisticsService();
        Assertions.assertEquals(0, statisticsService.averageDuration());
    }

    @Test
    public void testDailyStats() {
        final LocalDate day1 = visit(1L, 0L);
        final LocalDate day2 = visit(2L, 1L);
        final LocalDate day3as2 = visit(3L, 1L);
        setupStatisticsService();
        Assertions.assertEquals(Map.of(day1, 1, day2, 2), statisticsService.dailyStatistics());
    }

    @Test
    public void testAverageCount() {
        visit(1L, 0L);
        visit(1L, 1L);
        visit(2L, 2L);
        setupStatisticsService();
        Assertions.assertEquals(1.5, statisticsService.averageCount());
    }

    @Test
    public void testAverageDuration() {
        visit(1L, 0L, 100L);
        visit(1L, 0L, 200L);
        setupStatisticsService();
        Assertions.assertEquals(150.0, statisticsService.averageDuration());
    }

    @Test
    public void testDailyStatsWithUpdate() {
        final LocalDate day1 = visit(1L, 0L);
        final LocalDate day2 = visit(2L, 1L);
        setupStatisticsService();
        final LocalDate day3 = visit(3L, 2L);
        Assertions.assertEquals(Map.of(day1, 1, day2, 1, day3, 1), statisticsService.dailyStatistics());
    }

    @Test
    public void testAverageCountWithUpdate() {
        visit(1L, 0L);
        setupStatisticsService();
        visit(1L, 1L);
        visit(2L, 2L);
        Assertions.assertEquals(1.5, statisticsService.averageCount());
    }

    @Test
    public void testAverageDurationWithUpdate() {
        visit(1L, 0L, 100L);
        setupStatisticsService();
        visit(1L, 0L, 200L);
        visit(2L, 1L, 300L);
        Assertions.assertEquals(200.0, statisticsService.averageDuration());
    }
}
