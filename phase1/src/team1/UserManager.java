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
        if (user.getUserEvents().contains(event)) {
            // deleting an event created by the user means they are also not attending it
            // delete the event from both of the appropriate Lists
            user.getUserEvents().remove(event);
            user.getAttendEvents().remove(event);
        }

        else {
            if (user.getAttendEvents().contains(event)) {
                user.getAttendEvents().remove(event);
            }
        }
        return true;
    }

    public boolean createEvent(User user, Event event){
        // Add this event to the list of events the user has created
        user.getUserEvents().add(event);
        // Add this event to the list of events the user will attend
        user.getAttendEvents().add(event);
        return true;
    }

    public boolean attendEvent(User user, Event event) {
        Object maxAttendees = event.getEventDetails.get("max attendees");

        // Register the user under the event since it has no limit
        if (maxAttendees == null) {
            user.getAttendEvents().add(event);
            event.setNumAttendees(event.getNumAttendees() + 1);
            return true;
        }

        // Check if the event has enough space and register the user under the event if so
        else {
            if ((int) event.getMaxAttendees() != null && event.getNumAttendees() < event.getMaxAttendees()) {
                user.getAttendEvents().add(event);
                event.setNumAttendees(event.getNumAttendees() + 1);
                return true;
            }
            return false;
        }
    }

    // Display list of events
    public List<Event> getEvents(User user) {
        // return the union of events the user has created and is attending
        List<Event> events = user.getAttendEvents();
        events.addAll(user.getUserEvents());
        return events;
    }

    // Returns list of usernames
    // Returns list of usernames
    public static List<String> getUsernameList() {
        // Go through each user object in userList and add their username attribute to usernameList
        List<String> usernameList = new ArrayList<>();
        for (User u : userList) {
            usernameList.add(u.getUsername());
        }
        return usernameList;
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

    public boolean attendEvent(User user, Event event) {
        Object maxAttendees = event.getEventDetails.get("max attendees");

        // Register the user under the event since it has no limit
        if (maxAttendees == null) {
            user.getAttendEvents().add(event);
            event.setNumAttendees(event.getNumAttendees() + 1);
            return true;
        }

        // Check if the event has enough space and register the user under the event if so
        else {
            if ((int) event.getMaxAttendees() != null && event.getNumAttendees() < event.getMaxAttendees()) {
                user.getAttendEvents().add(event);
                event.setNumAttendees(event.getNumAttendees() + 1);
                return true;
            }
            return false;
        }
    }
}
