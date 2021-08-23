package utility;

/**
 * Enum of PrivacyTypes of Events.
 */
public enum EventPrivacyType {
    /**
     * Every user has access to these events.
     */
    PUBLIC ("Public"),

    /**
     * Only friends of the owner have access to these events.
     */
    FRIENDS_ONLY ("Friends Only"),

    /**
     * Only owner of event has access to these events.
     */
    PRIVATE ("Private");

    private final String name;

    /**
     * Creates an EventPrivacyType.
     * @param name Name of EventPrivacyType.
     */
    EventPrivacyType(String name) {
        this.name = name;
    }

    /**
     * @return Name of EventPrivacyType.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Name of EventPrivacyType.
     * @return EventPrivacyType with the given name.
     */
    public static EventPrivacyType byName(String name) {
        for (EventPrivacyType privacyType: values())
            if (privacyType.getName().equals(name))
                return privacyType;
        throw new IllegalArgumentException();
    }
}
