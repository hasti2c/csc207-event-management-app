package entities;

import utility.Command;
import utility.EventViewType;
import utility.UserViewType;

import java.util.List;

// TODO use ViewType interface
public class UserTypePermissions {
    private UserType userType;
    private List<Command> commandPermissions;
    private List<EventViewType> eventViewPermissions;
    private List<UserViewType> userViewPermissions;

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

    public List<UserViewType> getUserViewPermissions() {
        return userViewPermissions;
    }
}
