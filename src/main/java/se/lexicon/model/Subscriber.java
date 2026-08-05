package se.lexicon.model;

import java.util.Objects;

public class Subscriber {
    private final int id;
    private String email;
    private Plan plan;
    private boolean active;
    private int monthsRemaining;

    public Subscriber(int id, String email, Plan plan, boolean active, int monthsRemaining) {
        this.id = id;
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.plan = Objects.requireNonNull(plan, "plan must not be null");
        this.active = active;
        setMonthsRemaining(monthsRemaining);
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "email must not be null");
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = Objects.requireNonNull(plan, "plan must not be null");
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMonthsRemaining() {
        return monthsRemaining;
    }

    public void setMonthsRemaining(int monthsRemaining) {
        if (monthsRemaining < 0) {
            throw new IllegalArgumentException("monthsRemaining must be 0 or greater");
        }
        this.monthsRemaining = monthsRemaining;
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", plan=" + plan +
                ", active=" + active +
                ", monthsRemaining=" + monthsRemaining +
                '}';
    }
}
