package utility;

import entities.User;

/**
 * Enum of ViewTypes for Users.
 */
public enum UserViewType implements ViewType<User> {
    /**
     * Shows all users.
     * Doesn't show suspended users.
     */
    ALL ("All Users"),

    /**
     * Shows all friends of the current user.
     * Doesn't show suspended users.
     */
    FRIENDS ("Friends"),

    /**
     * Shows all suspended users.
     */
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
