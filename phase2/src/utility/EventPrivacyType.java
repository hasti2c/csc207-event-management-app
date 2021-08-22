package utility;

/**
 * Enum of PrivacyTypes of Events.
 */
public enum EventPrivacyType {
    PUBLIC ("Public"),
    FRIENDS_ONLY ("Friends Only"),
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
