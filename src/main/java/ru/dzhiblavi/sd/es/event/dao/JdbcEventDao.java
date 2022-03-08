package ru.dzhiblavi.sd.es.event.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.dzhiblavi.sd.es.event.events.NewMemberEvent;
import ru.dzhiblavi.sd.es.event.events.ProlongationEvent;
import ru.dzhiblavi.sd.es.event.events.VisitEvent;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class JdbcEventDao extends JdbcDaoSupport implements EventDao {
    private static Instant timestampToInstant(final long timestamp) {
        return Instant.ofEpochMilli(timestamp);
    }

    private static long instantToTimestamp(final Instant instant) {
        return instant.toEpochMilli();
    }

    private static Duration timestampToDuration(final long timestamp) {
        return Duration.ofMillis(timestamp);
    }

    private static long durationToTimestamp(final Duration duration) {
        return duration.toMillis();
    }

    private static class NewMemberMapper implements RowMapper<NewMemberEvent> {
        @Override
        public NewMemberEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new NewMemberEvent(
                    rs.getLong("ID"),
                    timestampToInstant(rs.getLong("CREATED_AT")),
                    timestampToDuration(rs.getLong("VALIDITY"))
            );
        }
    }

    private static class ProlongationMapper implements RowMapper<ProlongationEvent> {
        @Override
        public ProlongationEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ProlongationEvent(
                    rs.getLong("ID"),
                    timestampToDuration(rs.getLong("VALIDITY"))
            );
        }
    }

    private static class VisitMapper implements RowMapper<VisitEvent> {
        @Override
        public VisitEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VisitEvent(
                    VisitEvent.Direction.valueOf(rs.getString("DIRECTION")),
                    rs.getLong("ID"),
                    timestampToInstant(rs.getLong("CREATED_AT"))
            );
        }
    }

    public JdbcEventDao(final DataSource dataSource) {
        super();
        setDataSource(dataSource);
        createTables();
    }

    private void createTables() {
        executeSql(
                "CREATE TABLE IF NOT EXISTS NewMemberEvent " +
                        "(ID BIGINT," +
                        " CREATED_AT BIGINT," +
                        " VALIDITY BIGINT)"
        );
        executeSql(
                "CREATE TABLE IF NOT EXISTS ProlongationEvent " +
                        "(ID BIGINT," +
                        " VALIDITY BIGINT)"
        );
        executeSql(
                "CREATE TABLE IF NOT EXISTS VisitEvent " +
                        "(ID BIGINT," +
                        " DIRECTION VARCHAR(8) NOT NULL," +
                        " CREATED_AT BIGINT)"
        );
    }

    private void executeSql(final String request) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().execute(request);
    }

    @Override
    public void addEvent(final NewMemberEvent event) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(
                "INSERT INTO NewMemberEvent (ID, CREATED_AT, VALIDITY) VALUES (?, ?, ?)",
                event.getId(),
                instantToTimestamp(event.getTimestamp()),
                durationToTimestamp(event.getValidity())
        );
    }

    @Override
    public void addEvent(final ProlongationEvent event) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(
                "INSERT INTO ProlongationEvent (ID, VALIDITY) VALUES (?, ?)",
                event.getId(),
                durationToTimestamp(event.getValidity())
        );
    }

    @Override
    public void addEvent(final VisitEvent event) {
        assert getJdbcTemplate() != null;
        getJdbcTemplate().update(
                "INSERT INTO VisitEvent (ID, DIRECTION, CREATED_AT) VALUES (?, ?, ?)",
                event.getId(),
                event.getDirection().toString(),
                instantToTimestamp(event.getTimestamp())
        );
    }

    @Override
    public List<NewMemberEvent> getNewMembershipEvents() {
        return getJdbcTemplate().query(
                "SELECT * FROM NewMemberEvent ORDER BY CREATED_AT ASC",
                new NewMemberMapper()
        );
    }

    @Override
    public List<ProlongationEvent> getProlongationEvents() {
        return getJdbcTemplate().query(
                "SELECT * FROM ProlongationEvent",
                new ProlongationMapper()
        );
    }

    @Override
    public List<NewMemberEvent> getNewMembershipEvents(long id) {
        return getJdbcTemplate().query(
                "SELECT * FROM NewMemberEvent WHERE ID = " + id + " ORDER BY CREATED_AT ASC",
                new NewMemberMapper()
        );
    }

    @Override
    public List<ProlongationEvent> getProlongationEvents(long id) {
        return getJdbcTemplate().query(
                "SELECT * FROM ProlongationEvent WHERE ID = " + id,
                new ProlongationMapper()
        );
    }

    @Override
    public List<VisitEvent> getVisitEventsSince(final Instant timestamp) {
        return getJdbcTemplate().query(
                "SELECT * FROM VisitEvent WHERE CREATED_AT > " + instantToTimestamp(timestamp)
                        + " ORDER BY CREATED_AT ASC",
                new VisitMapper()
        );
    }
}
