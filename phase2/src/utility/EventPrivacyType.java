package utility;

public enum EventPrivacyType {
    PUBLIC ("Public"),
    FRIENDS_ONLY ("Friends Only"),
    PRIVATE ("Private");

    private final String name;

    EventPrivacyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EventPrivacyType byName(String name) {
        for (EventPrivacyType privacyType: values())
            if (privacyType.getName().equals(name))
                return privacyType;
        throw new IllegalArgumentException();
    }
}
