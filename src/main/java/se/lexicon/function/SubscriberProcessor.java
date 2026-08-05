package se.lexicon.function;

import se.lexicon.model.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubscriberProcessor {

    public List<Subscriber> findSubscribers(
            List<Subscriber> subscribers,
            SubscriberFilter filter
    ) {
        Objects.requireNonNull(subscribers, "subscribers must not be null");
        Objects.requireNonNull(filter, "filter must not be null");

        List<Subscriber> matches = new ArrayList<>();
        for (Subscriber subscriber : subscribers) {
            if (filter.matches(subscriber)) {
                matches.add(subscriber);
            }
        }
        return matches;
    }

    public List<Subscriber> applyToMatching(
            List<Subscriber> subscribers,
            SubscriberFilter filter,
            SubscriberAction action
    ) {
        Objects.requireNonNull(action, "action must not be null");

        List<Subscriber> matches = findSubscribers(subscribers, filter);
        for (Subscriber subscriber : matches) {
            action.run(subscriber);
        }
        return matches;
    }
}
