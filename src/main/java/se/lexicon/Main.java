package se.lexicon;

import se.lexicon.data.SubscriberDAO;
import se.lexicon.function.SubscriberFilter;
import se.lexicon.function.SubscriberProcessor;
import se.lexicon.function.SubscriberRules;
import se.lexicon.model.Plan;
import se.lexicon.model.Subscriber;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SubscriberDAO dao = createSubscriberDAO();
        List<Subscriber> subscribers = dao.findAll();
        SubscriberProcessor processor = new SubscriberProcessor();

        print("Active", processor.findSubscribers(subscribers, SubscriberRules.ACTIVE));
        print("Expiring", processor.findSubscribers(subscribers, SubscriberRules.EXPIRING));
        print("Active and expiring",
                processor.findSubscribers(subscribers, SubscriberRules.ACTIVE_AND_EXPIRING));
        print("BASIC plan",
                processor.findSubscribers(subscribers, SubscriberRules.byPlan(Plan.BASIC)));
        print("Paying", processor.findSubscribers(subscribers, SubscriberRules.PAYING));

        SubscriberFilter activePayingAndExpiring = SubscriberRules.ACTIVE
                .and(SubscriberRules.PAYING)
                .and(SubscriberRules.EXPIRING);

        print("Extended by three months",
                processor.applyToMatching(
                        subscribers,
                        activePayingAndExpiring,
                        SubscriberRules.extendBy(3)
                ));

        print("Deactivated expired FREE",
                processor.applyToMatching(
                        subscribers,
                        SubscriberRules.byPlan(Plan.FREE).and(SubscriberRules.EXPIRED),
                        SubscriberRules.DEACTIVATE
                ));
    }

    private static SubscriberDAO createSubscriberDAO() {
        SubscriberDAO dao = new SubscriberDAO();
        dao.save(new Subscriber(1, "free@example.com", Plan.FREE, true, 0));
        dao.save(new Subscriber(2, "basic@example.com", Plan.BASIC, true, 1));
        dao.save(new Subscriber(3, "pro@example.com", Plan.PRO, true, 6));
        dao.save(new Subscriber(4, "inactive@example.com", Plan.FREE, false, 0));
        dao.save(new Subscriber(5, "annual@example.com", Plan.BASIC, true, 12));
        return dao;
    }

    private static void print(String heading, List<Subscriber> subscribers) {
        System.out.println("\n" + heading + ":");
        subscribers.forEach(System.out::println);
    }
}
