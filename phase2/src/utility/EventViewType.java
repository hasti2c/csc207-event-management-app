package utility;

import entities.Event;

/**
 * Enum of ViewTypes for Events.
 */
public enum EventViewType implements ViewType<Event> {
    /**
     * Shows events owned by user.
     * Shows suspended events.
     */
    OWNED ("My Events"),

    /**
     * Shows events that the user is attending.
     * Doesn't show events that user has no access to. (User might be attending events without access if the privacy
     * type was changed after user attended, those are not shown.)
     * Doesn't show suspended events.
     */
    ATTENDING ("Attending Events"),

    /**
     * Shows events that the user is not attending.
     * Doesn't show events that user has no access to.
     * Doesn't show suspended events.
     */
    NOT_ATTENDING ("Not Attending Events"),

    /**
     * Shows all public events.
     * Doesn't show suspended events.
     */
    PUBLIC ("Public Events"),

    /**
     * Shows friend-only events owned by the user's friends.
     * Doesn't show suspended events.
     */
    FRIENDS_ONLY ("Friends Only Events"),

    /**
     * Shows every event. (ViewType intended for admins).
     * Shows events regardless of access.
     * Doesn't show suspended events.
     */
    ALL ("All Events"),

    /**
     * Shows suspended events. (ViewType intended for admins).
     * Shows events regardless of access.
     */
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
