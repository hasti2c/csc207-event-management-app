package utility;

import entities.Event;

public enum EventViewType implements ViewType<Event> {
    OWNED ("My Events"),
    ATTENDING ("Attending Events"),
    NOT_ATTENDING ("Not Attending Events"),
    PUBLISHED ("Published Events");

    private final String name;

    EventViewType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
