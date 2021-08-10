package entities;

import utility.Command;

import java.util.List;

public class UserTypePermissions {
    private UserType userType;
    private List<Command> permissions;

    public UserTypePermissions(User.UserType userType) {
        this.userType = userType;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    public List<Command> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Command> permissions) {
        this.permissions = permissions;
    }
}
