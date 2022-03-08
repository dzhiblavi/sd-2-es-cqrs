package ru.dzhiblavi.sd.es.model;

import ru.dzhiblavi.sd.es.event.dao.EventDao;
import ru.dzhiblavi.sd.es.event.events.NewMemberEvent;
import ru.dzhiblavi.sd.es.event.events.ProlongationEvent;
import ru.dzhiblavi.sd.es.event.events.VisitEvent;

import java.time.Duration;
import java.time.Instant;

public class DataWriter {
    private final EventDao eventDao;

    public DataWriter(final EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void newMembershipCommand(final long id, final Duration validity) {
        this.eventDao.addEvent(new NewMemberEvent(id, Instant.now(), validity));
    }

    public void prolongateCommand(final long id, final Duration validity) {
        this.eventDao.addEvent(new ProlongationEvent(id, validity));
    }

    public void visitCommand(final long id, final VisitEvent.Direction direction) {
        this.eventDao.addEvent(new VisitEvent(direction, id, Instant.now()));
    }
}
