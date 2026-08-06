package se.lexicon.function;

import org.junit.jupiter.api.Test;
import se.lexicon.model.Plan;
import se.lexicon.model.Subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubscriberRulesTest {

    @Test
    void extendByRejectsNonPositiveMonths() {
        assertThrows(IllegalArgumentException.class, () -> SubscriberRules.extendBy(0));
        assertThrows(IllegalArgumentException.class, () -> SubscriberRules.extendBy(-1));
    }

    @Test
    void extendByAddsMonths() {
        Subscriber subscriber = new Subscriber(
                1,
                "subscriber@example.com",
                Plan.BASIC,
                true,
                1
        );

        SubscriberRules.extendBy(3).run(subscriber);

        assertEquals(4, subscriber.getMonthsRemaining());
    }

    @Test
    void byPlanRejectsNull() {
        assertThrows(NullPointerException.class, () -> SubscriberRules.byPlan(null));
    }
}
