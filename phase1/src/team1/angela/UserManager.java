package team1.angela;

import java.util.List;

public class UserManager {
    /**
     * Manages the Users in the system
     */
    // === Class Variables ===
    public static List<User> userList;
    // === Instance Variables ===
    // === Methods ===
    // Need to finalize details about what to include for user creation before finishing this
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
    public boolean updateUser(User user){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        return true;
    }


}
