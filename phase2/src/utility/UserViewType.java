package utility;

import entities.User;

public enum UserViewType implements ViewType<User> {
    ALL ("All Users"),
    FRIENDS ("Friends"),
    SUSPENDED ("Suspended Users");

    private final String name;

    UserViewType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
