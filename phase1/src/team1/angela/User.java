package team1.angela;

import java.util.ArrayList;
import java.util.List;

public class User {
    /**
     * The users of the system
     */
    // === Instance Variables ===
    private String userName;
    private String password;
    private String userEmail;
    private boolean loggedIn;
    public List<Event> userEvents;
    public enum UserType {
        R, A, T
    }
    private UserType userType;
    // === Representation Invariants ===
    // username.length() > 0
    // password.length() > 0
    // === Methods ===
    public User(String userName, String password, String userEmail, UserType type) {
        this.userType = type;
        this.userName = userName;
        this.password = password;
        this.userEmail = userEmail;
        userEvents = new ArrayList<>();
        loggedIn = false;
    }
    // === Getters ===
    public String getUserName() {
        return userName;
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
                "username='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userEvents=" + userEvents +
                '}';
    }

    // === Setters ===
    public void setUserName(String userName) {
        this.userName = userName;
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
