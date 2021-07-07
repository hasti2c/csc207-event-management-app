package team1;

import java.util.ArrayList;
import java.util.List;

public class User {
    /**
     * The users of the system
     */
    // === Instance Variables ===
    private String username;
    private String password;
    private String userEmail;
    private boolean loggedIn;
    // Events the user has created. I'm pretty sure it's fine to be private
    private List<Event> userEvents;
    // List of events that the user will attend. The event can be their own or another user's and must be published.
    private List<Event> attendEvents;
    // needs to be public so that it can be used in the constructor and the UserManager class can access it.
    public enum UserType {
        R, A, T
    }
    private UserType userType;
    // === Representation Invariants ===
    // username.length() > 0
    // password.length() > 0
    // === Methods ===
    public User(String username, String password, String userEmail, UserType type) {
        this.userType = type;
        this.username = username;
        this.password = password;
        this.userEmail = userEmail;
        userEvents = new ArrayList<>();
        loggedIn = false;
    }
    // === Getters ===
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public boolean isLoggedIn() {
        return loggedIn;
    }
    public List<Event> getUserEvents() {
        return userEvents;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userEvents=" + userEvents +
                '}';
    }

    // === Setters ===
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    public void setUserEvents(List<Event> userEvents) {
        this.userEvents = userEvents;
    }

}
