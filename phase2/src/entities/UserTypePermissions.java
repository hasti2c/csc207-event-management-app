package entities;

import utility.Command;
import view.EventViewType;

import java.util.List;

public class UserTypePermissions {
    private UserType userType;
    private List<Command> commandPermissions;
    private List<EventViewType> eventViewPermissions;

    public UserTypePermissions(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Command> getCommandPermissions() {
        return commandPermissions;
    }

    public List<EventViewType> getEventViewPermissions() {
        return eventViewPermissions;
    }
}
