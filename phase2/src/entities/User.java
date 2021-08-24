package entities;

import utility.Savable;
import utility.UserType;
import utility.Viewable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A user within the program
 */
public class User implements Savable, Viewable {
    // === Instance Variables ===
    private String username;
    private String password;
    private String userEmail;
    private boolean hasTempPass;
    // Events the user has created. I'm pretty sure it's fine to be private
    private List<String> ownedEvents;
    // List of events that the user will attend. The event can be their own or another user's and must be public.
    private List<String> attendingEvents;
    // needs to be public so that it can be used in the constructor and the UserManager class can access it.
    private UserType userType;
    private boolean suspended = false;
    private LocalDateTime suspensionChangeDate;
    private List<String> friends;

    // === Representation Invariants ===
    // username.length() > 0
    // password.length() > 0
    // === Methods ===

    /**
     * Create a User object
     * @param username The username of the User
     * @param password The password of the User
     * @param userEmail The email of the User
     * @param type The type of the User Regular, Admin, Trial, Temporary
     */
    public User(String username, String password, String userEmail, UserType type) {
        this.userType = type;
        this.username = username;
        this.password = password;
        this.userEmail = userEmail;
        this.ownedEvents = new ArrayList<>();
        this.attendingEvents = new ArrayList<>();
        this.hasTempPass = false;
        this.friends = new ArrayList<>();
    }

    public User() {
    }
    // === Getters ===

    /**
     * Gets users temporary password
     * @return boolean representing user's temp password state
     */
    public boolean hasTempPass() {return this.hasTempPass;}

    /**
     * Gets the User's type, R.A.T
     * @return Enum The User's type, R.A.T
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Get the user's username
     * @return String user's username
     */
    public String getUsername() {
        return this.username;
    }
    /**
     * Get the user's password
     * @return String user's password
     */
    public String getPassword() {
        return this.password;
    }
    /**
     * Get the user's email
     * @return String user's email
     */
    public String getUserEmail() {
        return this.userEmail;
    }
    /**
     * Get the user's created events
     * @return List<Event> List of created events
     */
    public List<String> getOwnedEvents() {
        return this.ownedEvents;
    }
    /**
     * Get the user's attended events
     * @return List<Event> List of attended events
     */
    public List<String> getAttendingEvents() {
        return this.attendingEvents;
    }

    /**
     * Get the string representation of a user
     * @return String The string representation of a user.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + this.username + '\'' +
                ", userEmail='" + this.userEmail + '\'' +
                ", userEvents=" + this.ownedEvents +
                '}';
        // Test
    }

    /**
     * Return if the User is suspended
     * @return boolean If the User is suspended
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * Gets the suspension change date for the User
     * @return LocalDateTime The time of suspension change
     */
    public LocalDateTime getSuspensionChangeDate() {
        return suspensionChangeDate;
    }

    /**
     * Gets the list of the User's friends
     * @return List<String> The list of the User's friends
     */
    public List<String> getFriends() {
        return friends;
    }

    // === Setters ===

    /**
     * Set a new username for the user
     * @param username The new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set a new password for the user
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set a new email for the user
     * @param userEmail the new email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Sets the User's type, R.A.T
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Set the User's Suspended field
     * @param suspended Boolean if the User is suspended
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    /**
     * Set the users friends list
     * @param friends List<String> The User's friends list to set
     */
    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    /**
     * Set the suspension change date
     * @param suspensionChangeDate LocalDateTime The time of the User's suspension change date.
     */
    public void setSuspensionChangeDate(LocalDateTime suspensionChangeDate) {
        this.suspensionChangeDate = suspensionChangeDate;
    }

    /**
     * Sets user's temp password
     * @param tempPassState bool representing the state isTempPass will be set to
     */
    public void setHasTempPass(boolean tempPassState){
        this.hasTempPass = tempPassState;
    }

    @Override
    public String getID() {
        return username;
    }
}
