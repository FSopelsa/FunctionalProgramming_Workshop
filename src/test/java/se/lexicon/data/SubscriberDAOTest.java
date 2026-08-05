package se.lexicon.data;

import org.junit.jupiter.api.Test;
import se.lexicon.model.Plan;
import se.lexicon.model.Subscriber;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriberDAOTest {

    @Test
    void savesAndFindsSubscriberById() {
        SubscriberDAO dao = new SubscriberDAO();
        Subscriber subscriber = subscriber(1, "first@example.com");

        dao.save(subscriber);

        assertSame(subscriber, dao.findById(1).orElseThrow());
        assertTrue(dao.findById(99).isEmpty());
    }

    @Test
    void savingExistingIdReplacesSubscriber() {
        SubscriberDAO dao = new SubscriberDAO();
        dao.save(subscriber(1, "old@example.com"));
        Subscriber replacement = subscriber(1, "new@example.com");

        dao.save(replacement);

        assertEquals(List.of(replacement), dao.findAll());
    }

    @Test
    void findAllReturnsIndependentList() {
        SubscriberDAO dao = new SubscriberDAO();
        dao.save(subscriber(1, "first@example.com"));

        List<Subscriber> result = dao.findAll();
        result.clear();

        assertEquals(1, dao.findAll().size());
    }

    private static Subscriber subscriber(int id, String email) {
        return new Subscriber(id, email, Plan.FREE, true, 1);
    }
}
