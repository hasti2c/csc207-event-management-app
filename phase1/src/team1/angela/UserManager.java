package team1.angela;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    /**
     * Manages the Users in the system
     */
    // === Class Variables ===
    public static List<User> userList = new ArrayList<>();
    // === Instance Variables ===
    // === Methods ===
    // Need to finalize details about what to include for user creation before finishing this
    public UserManager() {

    }
    public void createUser() {

    }
    public void deleteUser(User user) {
        // remove User from list of users
    }
    public boolean logIn(String username, String password) {
        // returns true if successfully logged in, false if otherwise (like if password is wrong)
        // updates the loggedIn boolean to True
        return true;
    }
    public boolean logOut(String username, String password) {
        // returns true if successfully logged out, false if otherwise (like if password is wrong)
        return true;
    }
    public boolean updatePassword(User user, String newPassword){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        return true;
    }
    public boolean updateUsername(User user, String newUsername){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        return true;
    }
    public boolean updateEmail(User user, String newEmail){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        return true;
    }
    public boolean deleteEvent(User user, Event event){
        return true;
    }
    public boolean addEvent(User user, Event event){
        return true;
    }
    // Display list of events
    public List<Event> getEvents(User user) {

    }
    // Returns list of usernames
    public static List<String> getUsernameList() {
        // for loop that returns the usernames
        return;
    }
    // Gets user
    public User getUser(String username) {

    }
}
