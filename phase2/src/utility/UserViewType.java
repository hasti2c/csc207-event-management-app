package utility;

import entities.User;

/**
 * Enum of ViewTypes for Users.
 */
public enum UserViewType implements ViewType<User> {
    ALL ("All Users"),
    FRIENDS ("Friends"),
    SUSPENDED ("Suspended Users");

    private final String name;

    /**
     * Creates a UserViewType.
     * @param name Name of ViewType.
     */
    UserViewType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
