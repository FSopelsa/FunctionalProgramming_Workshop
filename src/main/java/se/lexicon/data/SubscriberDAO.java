package se.lexicon.data;

import se.lexicon.model.Subscriber;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SubscriberDAO {
    private final Map<Integer, Subscriber> subscribers = new LinkedHashMap<>();

    public void save(Subscriber subscriber) {
        Objects.requireNonNull(subscriber, "subscriber must not be null");
        subscribers.put(subscriber.getId(), subscriber);
    }

    public List<Subscriber> findAll() {
        return new ArrayList<>(subscribers.values());
    }

    public Optional<Subscriber> findById(int id) {
        return Optional.ofNullable(subscribers.get(id));
    }
}
