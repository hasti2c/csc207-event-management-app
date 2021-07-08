package team1;

import sun.font.TrueTypeFont;

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
    public UserManager() {

    }


    /**
     * Create a user in the program
     * @param username the User's username
     * @param password the User's password
     * @param userEmail the User's email
     * @param type the User's type. (R, A, T)
     */
    public void createUser(String username, String password, String userEmail, User.UserType type) {
            User newUser = new User(username, password, userEmail, type);
            userList.add(newUser);
    }

    /**
     * Deletes a user from the program
     * @param user The User to delete
     */
    public void deleteUser(User user) {
        // remove User from list of users
        userList.remove(user);
    }

    /**
     * Logs in a user by checking the inputted password against the User's username
     * @param username The username of the user attempting to log in
     * @param password The password the user has inputted
     * @return boolean Whether the login was successful
     */
    public boolean logIn(String username, String password) {
        // returns true if successfully logged in, false if otherwise (like if password is wrong)
        // updates the loggedIn boolean to True
        try{
            User userToLogin = getUser(username);
            if (userToLogin.getPassword().equals(password)){
                userToLogin.setLoggedIn(true);
                return true;
            }
            else{
                return false;
            }
        } catch (UserNotFound userNotFound) {
            return false;
        }
    }

    /**
     * Logs out a user by checking the inputted password against the User's username
     * @param username The username of the user attempting to log out
     * @param password The password the user has inputted
     * @return boolean Whether the logout was successful
     */
    public boolean logOut(String username, String password) {
        // returns true if successfully logged out, false if otherwise (like if password is wrong)
        try{
            User userToLogout = getUser(username);
            if (userToLogout.getPassword().equals(password)){
                userToLogout.setLoggedIn(false);
                return true;
            }
            else{
                return false;
            }
        } catch (UserNotFound userNotFound) {
            return false;
        }
    }

    /**
     * Update a users password to the newPassword
     * @param user The user whose password is to be updated
     * @param newPassword The users new password
     * @throws UserNotLoggedIn If the User is not logged in
     * @return Whether the password was updated successfully
     */
    public boolean updatePassword(User user, String newPassword) throws UserNotLoggedIn{
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        if (user.isLoggedIn()){
            user.setPassword(newPassword);
            return true;
        }
        else{
            throw new UserNotLoggedIn();
        }
    }

    /**
     * Update a users username to the newUsername
     * @param user The user whose username is to be updated
     * @param newUsername The users new username
     * @throws UserNotLoggedIn If the User is not logged in
     * @return Whether the username was updated successfully
     */
    public boolean updateUsername(User user, String newUsername) throws UserNotLoggedIn{
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        if (user.isLoggedIn()){
            user.setUsername(newUsername);
            return true;
        }
        else{
            throw new UserNotLoggedIn();
        }
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
    public boolean createEvent(User user, Event event){
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

    /**
     * Get the user with the matching username
     * @param username the username to attempt to find a matching user with
     * @return User The user with the given username
     * @throws UserNotFound If the username does not match any users within the program
     * */
    public User getUser(String username) throws UserNotFound{
        for (User user :
                userList) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        // If the loop ends and no users with the matching username are found, throw UserNotFound
        throw new UserNotFound();
    }
}
