package utility;

import entities.Event;

/**
 * Enum of ViewTypes for Events.
 */
public enum EventViewType implements ViewType<Event> {
    OWNED ("My Events"),
    ATTENDING ("Attending Events"),
    NOT_ATTENDING ("Not Attending Events"),
    PUBLIC ("Public Events"),
    FRIENDS_ONLY ("Friends Only Events"),
    ALL ("All Events"),
    SUSPENDED ("Suspended Events");

    private final String name;

    /**
     * Creates an EventViewType.
     * @param name Name of ViewType.
     */
    EventViewType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
