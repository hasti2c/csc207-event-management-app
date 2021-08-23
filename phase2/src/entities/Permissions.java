package entities;

import utility.Command;
import utility.Savable;
import utility.UserType;
import utility.ViewType;

import java.util.List;

public class Permissions implements Savable {
    private UserType userType;
    private List<Command> commandPermissions;
    private List<ViewType<Event>> eventViewPermissions;
    private List<ViewType<User>> userViewPermissions;

    public Permissions(UserType userType) {
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

    public List<ViewType<Event>> getEventViewPermissions() {
        return eventViewPermissions;
    }

    public List<ViewType<User>> getUserViewPermissions() {
        return userViewPermissions;
    }

    @Override
    public String getID() {
        return userType.toString();
    }
}
