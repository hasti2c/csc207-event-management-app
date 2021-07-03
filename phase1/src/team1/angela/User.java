package team1.angela;

import java.util.List;
import java.util.Map;

public class User {
    /**
     * The users of the system
     */
    // === Instance Variables ===
    private Map<String, String> userInfo;
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
    public User(Map<String, String> info, userType type) {

    }
    // need to add getters and setters for everything except userEvents

}
