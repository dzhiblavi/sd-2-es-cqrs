package ru.dzhiblavi.sd.es.base;

import org.junit.jupiter.api.BeforeEach;
import ru.dzhiblavi.sd.es.event.dao.EventDao;
import ru.dzhiblavi.sd.es.event.dao.InMemoryEventDao;
import ru.dzhiblavi.sd.es.event.events.VisitEvent;
import ru.dzhiblavi.sd.es.model.DataReader;
import ru.dzhiblavi.sd.es.model.DataWriter;
import ru.dzhiblavi.sd.es.services.manager.ManagerService;

import java.time.Duration;
import java.time.Instant;

public class BaseTest {
    protected EventDao eventDao = null;
    protected DataReader dataReader = null;
    protected DataWriter dataWriter = null;
    protected ManagerService managerService = null;

    @BeforeEach
    public void setupServices() {
        this.eventDao = new InMemoryEventDao();
        this.dataReader = new DataReader(eventDao);
        this.dataWriter = new DataWriter(eventDao);
        this.managerService = new ManagerService(dataReader, dataWriter);
    }

    protected void addMembership(final long id, final long validity) {
        managerService.issueNewMembership(id, Duration.ofDays(validity));
    }

    protected void addExpiredMembership(final long id) {
        managerService.issueNewMembership(id, Duration.ofDays(-1));
    }

    protected void prolongateMembership(final long id, final long validity) {
        managerService.prolongateMembership(id, Duration.ofDays(validity));
    }

    protected void registerEnter(final long id, final long timestamp) {
        eventDao.addEvent(new VisitEvent(VisitEvent.Direction.ENTER, id, Instant.ofEpochMilli(timestamp)));
    }

    protected void registerExit(final long id, final long timestamp) {
        eventDao.addEvent(new VisitEvent(VisitEvent.Direction.EXIT, id, Instant.ofEpochMilli(timestamp)));
    }
}
