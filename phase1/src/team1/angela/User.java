package team1.angela;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
    /**
     * The users of the system
     */
    // === Instance Variables ===
    private String username;
    private String password;
    private String userEmail;
    private boolean loggedIn;
    public List<Event> userEvents;
    private enum userType {
        R, A, T
    }
    // === Representation Invariants ===
    // Not sure yet
    // === Methods ===
    public User(String username, String password, String userEmail, userType type) {
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
