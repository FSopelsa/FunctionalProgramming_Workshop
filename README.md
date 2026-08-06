# Functional Programming Workshop

A Java subscription-management exercise that demonstrates custom functional
interfaces, lambda expressions, reusable business rules, and rule composition.
The original workshop brief is available in
[`Functional_Programming_Workshop.md`](Functional_Programming_Workshop.md).

## Current status

- Part 1 is complete: subscriber model, plans, in-memory DAO, filters, actions,
  rule composition, subscription extension, and deactivation.
- Part 2 is complete: all six requested JUnit scenarios are implemented.
- Additional DAO coverage verifies lookup, replacement by ID, and defensive
  list handling.
- The test suite currently contains 9 passing tests.

## Implemented behavior

The project can:

- find active subscribers;
- find subscriptions with 0 or 1 month remaining;
- combine rules to find active and expiring subscribers;
- filter subscribers by `FREE`, `BASIC`, or `PRO` plan;
- identify paying subscribers (`BASIC` or `PRO`);
- extend matching subscriptions by a validated number of months;
- deactivate expired `FREE` subscribers; and
- compose filters with `and`, `or`, and `negate`.

`Main` creates sample subscribers and demonstrates each rule and action.

## Project structure

```text
src/
|-- main/java/se/lexicon/
|   |-- Main.java
|   |-- data/
|   |   `-- SubscriberDAO.java
|   |-- function/
|   |   |-- SubscriberAction.java
|   |   |-- SubscriberFilter.java
|   |   |-- SubscriberProcessor.java
|   |   `-- SubscriberRules.java
|   `-- model/
|       |-- Plan.java
|       `-- Subscriber.java
`-- test/java/se/lexicon/
    |-- data/
    |   `-- SubscriberDAOTest.java
    `-- function/
        `-- SubscriberProcessorTest.java
```

## Design choices

### Separate model, storage, and behavior

The `model`, `data`, and `function` packages keep responsibilities focused.
Domain objects hold state, the DAO stores subscribers, and the processor applies
filters and actions.

### Custom functional interfaces

`SubscriberFilter` and `SubscriberAction` are the workshop's functional
interfaces. Business rules are defined as named lambdas in `SubscriberRules`,
and filters can be combined without duplicating conditions.

### Mutable subscriber, immutable ID

Subscription fields are mutable because the workshop actions must extend or
deactivate subscriptions. The subscriber ID remains final so the DAO can use it
as stable identity.

### In-memory DAO

`SubscriberDAO` uses a `LinkedHashMap<Integer, Subscriber>` for unique ID lookup
and predictable insertion order. Saving an existing ID replaces the stored
subscriber. `findAll()` returns a new list so callers cannot change the DAO's
collection structure.

### Expired versus expiring

- Expired means exactly `0` months remaining.
- Expiring means `0` or `1` month remaining.

Keeping these rules separate prevents a subscription with one month left from
being treated as already expired.

## Requirements

- JDK 25 or newer
- Maven 3.9 or newer

The Maven build compiles the project for Java 25, as configured in `pom.xml`.

## Build, test, and run

On Windows PowerShell:

```powershell
mvn.cmd clean test
mvn.cmd package
java -cp target/classes se.lexicon.Main
```

Make sure Maven and `java` use JDK 25 or newer. You can confirm Maven's Java
runtime with:

```powershell
mvn.cmd -version
```

The test suite covers:

1. active subscribers;
2. expiring subscriptions;
3. active and expiring subscribers;
4. extending active, paying, expiring subscriptions;
5. deactivating expired `FREE` subscribers;
6. filtering by plan; and
7. DAO save, lookup, replacement, and list-isolation behavior.
