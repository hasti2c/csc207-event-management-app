package utility;

import entities.Event;

public enum EventViewType implements ViewType<Event> {
    OWNED ("My Events"),
    ATTENDING ("Attending Events"),
    NOT_ATTENDING ("Not Attending Events"),
    PUBLIC ("Public Events"),
    FRIENDS_ONLY ("Friends Only Events"),
    ALL ("All Events"),
    SUSPENDED ("Suspended Events");

    private final String name;

    EventViewType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
