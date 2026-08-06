package se.lexicon.function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.lexicon.model.Plan;
import se.lexicon.model.Subscriber;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriberProcessorTest {
    private SubscriberProcessor processor;
    private List<Subscriber> subscribers;

    @BeforeEach
    void setUp() {
        processor = new SubscriberProcessor();
        subscribers = List.of(
                new Subscriber(1, "free@example.com", Plan.FREE, true, 0),
                new Subscriber(2, "basic@example.com", Plan.BASIC, true, 1),
                new Subscriber(3, "pro@example.com", Plan.PRO, true, 6),
                new Subscriber(4, "inactive@example.com", Plan.FREE, false, 0),
                new Subscriber(5, "annual@example.com", Plan.BASIC, true, 12),
                new Subscriber(6, "free-trial@example.com", Plan.FREE, true, 1)
        );
    }

    @Test
    void scenario1FindsActiveSubscribers() {
        List<Subscriber> result = processor.findSubscribers(
                subscribers,
                SubscriberRules.ACTIVE
        );

        assertEquals(List.of(1, 2, 3, 5, 6), idsOf(result));
    }

    @Test
    void scenario2FindsExpiringSubscriptions() {
        List<Subscriber> result = processor.findSubscribers(
                subscribers,
                SubscriberRules.EXPIRING
        );

        assertEquals(List.of(1, 2, 4, 6), idsOf(result));
    }

    @Test
    void scenario3FindsActiveAndExpiringSubscribers() {
        List<Subscriber> result = processor.findSubscribers(
                subscribers,
                SubscriberRules.ACTIVE_AND_EXPIRING
        );

        assertEquals(List.of(1, 2, 6), idsOf(result));
    }

    @Test
    void scenario4ExtendsActivePayingExpiringSubscriptions() {
        SubscriberFilter activePayingAndExpiring = SubscriberRules.ACTIVE
                .and(SubscriberRules.PAYING)
                .and(SubscriberRules.EXPIRING);

        List<Subscriber> updated = processor.applyToMatching(
                subscribers,
                activePayingAndExpiring,
                SubscriberRules.extendBy(3)
        );

        assertAll(
                () -> assertEquals(List.of(2), idsOf(updated)),
                () -> assertEquals(4, subscriber(2).getMonthsRemaining()),
                () -> assertEquals(6, subscriber(3).getMonthsRemaining()),
                () -> assertEquals(12, subscriber(5).getMonthsRemaining())
        );
    }

    @Test
    void scenario5DeactivatesExpiredFreeSubscribersOnly() {
        SubscriberFilter expiredFree = SubscriberRules.byPlan(Plan.FREE)
                .and(SubscriberRules.EXPIRED);

        List<Subscriber> updated = processor.applyToMatching(
                subscribers,
                expiredFree,
                SubscriberRules.DEACTIVATE
        );

        assertAll(
                () -> assertEquals(List.of(1, 4), idsOf(updated)),
                () -> assertFalse(subscriber(1).isActive()),
                () -> assertFalse(subscriber(4).isActive()),
                () -> assertTrue(subscriber(6).isActive())
        );
    }

    @Test
    void scenario6FiltersSubscribersByPlan() {
        assertAll(
                () -> assertEquals(
                        List.of(1, 4, 6),
                        idsOf(processor.findSubscribers(
                                subscribers,
                                SubscriberRules.byPlan(Plan.FREE)
                        ))
                ),
                () -> assertEquals(
                        List.of(2, 5),
                        idsOf(processor.findSubscribers(
                                subscribers,
                                SubscriberRules.byPlan(Plan.BASIC)
                        ))
                ),
                () -> assertEquals(
                        List.of(3),
                        idsOf(processor.findSubscribers(
                                subscribers,
                                SubscriberRules.byPlan(Plan.PRO)
                        ))
                )
        );
    }

    private Subscriber subscriber(int id) {
        return subscribers.stream()
                .filter(subscriber -> subscriber.getId() == id)
                .findFirst()
                .orElseThrow();
    }

    private static List<Integer> idsOf(List<Subscriber> subscribers) {
        return subscribers.stream()
                .map(Subscriber::getId)
                .toList();
    }
}
