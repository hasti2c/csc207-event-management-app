package entities;

import utility.Command;

import java.util.List;

public class UserTypePermissions {
    private UserType userType;
    private List<Command> permissions;

    public UserTypePermissions(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Command> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Command> permissions) {
        this.permissions = permissions;
    }
}
