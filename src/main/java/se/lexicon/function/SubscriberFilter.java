package se.lexicon.function;

import se.lexicon.model.Subscriber;

import java.util.Objects;

@FunctionalInterface
public interface SubscriberFilter {
    boolean matches(Subscriber subscriber);

    default SubscriberFilter and(SubscriberFilter other) {
        Objects.requireNonNull(other, "other filter must not be null");
        return subscriber -> matches(subscriber) && other.matches(subscriber);
    }

    default SubscriberFilter or(SubscriberFilter other) {
        Objects.requireNonNull(other, "other filter must not be null");
        return subscriber -> matches(subscriber) || other.matches(subscriber);
    }

    default SubscriberFilter negate() {
        return subscriber -> !matches(subscriber);
    }
}
