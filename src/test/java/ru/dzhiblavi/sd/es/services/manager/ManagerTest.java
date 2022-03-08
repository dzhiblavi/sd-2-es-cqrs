package ru.dzhiblavi.sd.es.services.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dzhiblavi.sd.es.base.BaseTest;
import ru.dzhiblavi.sd.es.model.Membership;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class ManagerTest extends BaseTest {
    private Instant getValidity(final long id) {
        final Optional<Membership> membershipOptional = dataReader.getMembership(id);
        Assertions.assertTrue(membershipOptional.isPresent());
        final Membership membership = membershipOptional.get();
        return membership.getValidUntil();
    }

    @Test
    public void testValidIssue() {
        Assertions.assertDoesNotThrow(() -> addMembership(1L, 1L));
        final Optional<Membership> membershipOptional = dataReader.getMembership(1L);
        Assertions.assertTrue(membershipOptional.isPresent());
        final Membership membership = membershipOptional.get();
        Assertions.assertEquals(1L, membership.getId());
    }

    @Test
    public void testDoubleIssueError() {
        addMembership(1L, 1L);
        Assertions.assertThrows(IllegalArgumentException.class, () -> addMembership(1L, 1L));
    }

    @Test
    public void testProlongate() {
        addMembership(1L, 1L);
        final Instant validBefore = getValidity(1L);
        Assertions.assertDoesNotThrow(() -> prolongateMembership(1L, 22L));
        final Instant validAfter = getValidity(1L);
        Assertions.assertEquals(validBefore.plus(Duration.ofDays(22L)), validAfter);
    }

    @Test
    public void testProlongateNoMemberError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> prolongateMembership(1L, 1L));
    }
}
