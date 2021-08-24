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

    /**
     * Creates a new Permissions instance.
     * @param userType The userType to whom this permission belongs.
     */
    public Permissions(UserType userType) {
        this.userType = userType;
    }

    /**
     * Get userType instance variable
     * @return userType
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Set userType instance variable
     * @param userType UserType to be set
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Get a List of command permissions
     * @return List of command permissions
     */
    public List<Command> getCommandPermissions() {
        return commandPermissions;
    }

    /**
     * Get a List of Event ViewTypes
     * @return List of Event ViewTypes
     */
    public List<ViewType<Event>> getEventViewPermissions() {
        return eventViewPermissions;
    }

    /**
     * Get a List of User ViewTypes
     * @return List of User ViewTypes
     */
    public List<ViewType<User>> getUserViewPermissions() {
        return userViewPermissions;
    }

    @Override
    public String getID() {
        return userType.toString();
    }
}
