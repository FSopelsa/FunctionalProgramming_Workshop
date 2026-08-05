package se.lexicon.function;

import se.lexicon.model.Subscriber;

@FunctionalInterface
public interface SubscriberAction {
    void run(Subscriber subscriber);
}
