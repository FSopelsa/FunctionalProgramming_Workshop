package se.lexicon.function;

import se.lexicon.model.Plan;

import java.util.Objects;

public final class SubscriberRules {
    public static final SubscriberFilter ACTIVE = subscriber -> subscriber.isActive();

    public static final SubscriberFilter EXPIRING = subscriber ->
            subscriber.getMonthsRemaining() == 0
                    || subscriber.getMonthsRemaining() == 1;

    public static final SubscriberFilter EXPIRED = subscriber ->
            subscriber.getMonthsRemaining() == 0;

    public static final SubscriberFilter ACTIVE_AND_EXPIRING = ACTIVE.and(EXPIRING);

    public static final SubscriberFilter PAYING = subscriber ->
            subscriber.getPlan() == Plan.BASIC
                    || subscriber.getPlan() == Plan.PRO;

    public static final SubscriberAction DEACTIVATE = subscriber ->
            subscriber.setActive(false);

    private SubscriberRules() {
    }

    public static SubscriberFilter byPlan(Plan plan) {
        Objects.requireNonNull(plan, "plan must not be null");
        return subscriber -> subscriber.getPlan() == plan;
    }

    public static SubscriberAction extendBy(int months) {
        if (months <= 0) {
            throw new IllegalArgumentException("months must be greater than 0");
        }

        return subscriber -> subscriber.setMonthsRemaining(
                subscriber.getMonthsRemaining() + months
        );
    }
}
