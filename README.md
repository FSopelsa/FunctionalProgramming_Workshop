# Functional Programming Workshop

## Implemented scope

- ✔️ Part 1: subscriber model, plans, in-memory DAO, functional filters, actions,
  rule composition, filtering, subscription extension, and deactivation.

 Planned implementations:
- ⬜ Part 2: JUnit tests for all six requested scenarios.
- ⬜ Extra coverage: DAO behavior, invalid rule arguments, and action boundaries.

## Planned Project structure

```text
src/
├── main/java/se/lexicon/
│   ├── Main.java
│   ├── data/SubscriberDAO.java
│   ├── function/
│   │   ├── SubscriberAction.java
│   │   ├── SubscriberFilter.java
│   │   ├── SubscriberProcessor.java
│   │   └── SubscriberRules.java
│   └── model/
│       ├── Plan.java
│       └── Subscriber.java
└── test/java/se/lexicon/
    ├── data/SubscriberDAOTest.java
    └── function/
        ├── SubscriberProcessorTest.java
        └── SubscriberRulesTest.java
```

### Part 1 files

- `model/Subscriber.java` and `model/Plan.java`: domain state from the UML.
- `data/SubscriberDAO.java`: in-memory storage and lookup.
- `function/SubscriberFilter.java`: condition passed into the processor.
- `function/SubscriberAction.java`: change applied to matching subscribers.
- `function/SubscriberRules.java`: named business rules and action factories.
- `function/SubscriberProcessor.java`: reusable list-processing algorithm.
- `Main.java`: sample data and execution of every required rule/action.

### Planned Part 2 files

- `SubscriberProcessorTest.java`: all six workshop scenarios.
- `SubscriberRulesTest.java`: action and argument edge cases.
- `SubscriberDAOTest.java`: storage behavior and defensive list handling.

## Design choices

### Separate model, storage, and behavior

`model`, `data`, and `function` packages keep responsibilities small. Domain
objects hold state, DAO stores objects, and processor applies behavior. This
matches the UML without placing all logic in `Main`.

### Custom functional interfaces

Workshop asks for `SubscriberFilter` and `SubscriberAction`, so processor accepts
those types instead of hard-coded conditions. Rules are lambdas stored under
clear names. `and`, `or`, and `negate` support composition without duplicating
conditions.

### Mutable subscriber, immutable ID

Workshop actions must extend and deactivate subscriptions, so those fields are
mutable. ID remains final because DAO uses it as stable identity.

### In-memory DAO

`LinkedHashMap<Integer, Subscriber>` gives unique ID lookup while preserving
insertion order for predictable output and tests. Saving an existing ID replaces
that entry. `findAll()` returns a new list, preventing callers from changing DAO
collection structure.

### Expired versus expiring

- Expired: exactly `0` months remaining.
- Expiring: `0` or `1` month remaining.

Separate rules prevent an active FREE subscriber with one month left from being
deactivated too early.

### Returned matches

`findSubscribers` returns selected subscribers. `applyToMatching` returns the
subscribers it changed. This follows the UML and makes results visible to callers
and tests.

## Requirements

- JDK 25 or newer
- Maven 3.9+

## Build and run

```powershell
mvn.cmd clean test
mvn.cmd package
java -cp target/classes se.lexicon.Main
```

