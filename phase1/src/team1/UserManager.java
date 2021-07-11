package team1;

import sun.font.TrueTypeFont;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the Users in the system
 */
public class UserManager {
    // === Class Variables ===
    public static List<User> userList = new ArrayList<>();
    // === Instance Variables ===
    private List<String> usernamesList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
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
            usernamesList.add(username);
            emailList.add(userEmail);
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

        User userToLogin = getUser(username);
        if (userToLogin == null){
            return false;
        }
        if (userToLogin.getPassword().equals(password)){
            userToLogin.setLoggedIn(true);
            return true;
        }
        else{
            return false;
        }
    }

    // TODO: Remove password stuff for logout
    /**
     * Logs out a user by checking the inputted password against the User's username
     * @param username The username of the user attempting to log out
     * @param password The password the user has inputted
     * @return boolean Whether the logout was successful
     */
    public boolean logOut(String username, String password) {
        // returns true if successfully logged out, false if otherwise (like if password is wrong)

        User userToLogout = getUser(username);
        if (userToLogout == null){
            return false;
        }
        else if (userToLogout.getPassword().equals(password)){
            userToLogout.setLoggedIn(false);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Update a users password to the newPassword
     * @param user The user whose password is to be updated
     * @param newPassword The users new password
     * @return Whether the password was updated successfully
     */
    public boolean updatePassword(User user, String newPassword){
        // returns true if user is updated successfully
        // access getters and setters of User class
        // User needs to be logged in
        if (user.isLoggedIn()){
            user.setPassword(newPassword);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Update a users username to the newUsername
     * @param user The user whose username is to be updated
     * @param newUsername The users new username
     * @return Whether the username was updated successfully
     */
    public boolean updateUsername(User user, String newUsername) {
        // returns true if user is updated successfully
        // access getters and setters of User class
        // User needs to be logged in
        if (user.isLoggedIn()){
            usernamesList.remove(user.getUsername()); // Remove old usernamesList
            usernamesList.add(newUsername); // Add new usernamesList
            user.setUsername(newUsername); // Set new username
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Update a users email to the newEmail
     * @param user The user whose email is to be updated
     * @param newEmail The users new email
     * @return Whether the email was updated successfully
     */
    public boolean updateEmail(User user, String newEmail){
        // returns true if user is updated successfully
        // access getters and setters of User class
        // User needs to be logged in
        if (user.isLoggedIn()){
            emailList.remove(user.getUserEmail()); // remove old email from emailList
            emailList.add(newEmail); // Add new email to emailList
            user.setUserEmail(newEmail);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * unregister a user from an event
     * @param user the user that is to be removed from the event
     * @param eventID the eventID that the user will be removed from
     * @return whether the user is removed from the event successfully
     */
    public boolean deleteEvent(User user, String eventID){
        if (user.getUserEvents().contains(eventID)) {
            // deleting an event created by the user means they are also not attending it
            // delete the event from both of the appropriate Lists

            // Since the user is deciding to not host the event, the event should be removed from the event
            // list in EventManager ?
            // Something to keep in mind
            user.getUserEvents().remove(eventID);
            user.getAttendEvents().remove(eventID);
        }
        // Else check if the user is only attending the event, and if so, remove it
        else {
            if (user.getAttendEvents().contains(eventID)) {
                user.getAttendEvents().remove(eventID);
            }
        }
        return true;
    }

    // TODO: User creating might not be attending
    /**
     * create an Event that is hosted by the given User
     * @param user the User who is hosting the event
     * @param eventID the eventID that the user is hosting
     * @return whether the Event has been successfully created
     */
    public boolean createEvent(User user, String eventID){
        // Add this event to the list of events the user has created
        user.getUserEvents().add(eventID);
        // Add this event to the list of events the user will attend
        user.getAttendEvents().add(eventID);
        return true;
    }

    // TODO: GET RID OF EVENT STUFF ALSO EVENT IS A STRING NOW!
    /**
     * Register the user to attend the event
     * @param user The user who wishes to attend the event
     * @param event The Event that the user wishes to attend
     * @return True if the user was able to register for the event. False if the event has no available space.
     */
    public boolean attendEvent(User user, Event event) {
        Object maxAttendees = event.getEventDetails().get("max attendees");

        // Register the user under the event since it has no limit
        if (maxAttendees == null) {
            user.getAttendEvents().add(event);
            event.setNumAttendees(event.getNumAttendees() + 1);
            return true;
        }

        // Check if the event has enough space and register the user under the event if so
        else {
            if (event.getNumAttendees() < (int) maxAttendees) {
                user.getAttendEvents().add(event);
                event.setNumAttendees(event.getNumAttendees() + 1);
                return true;
            }
        }

        return false;
    }

    // TODO: What is this method? Why would we want the union between attending events and created events?
    // TODO: It might be better to just have 2 different methods?
    /**
     * Retrieve the events that a user has created or is attending
     * @param user The user whose created events / attending events are to be retrieved
     * @return a list of events the user has created or the user is attending
     */
    public List<String> getEvents(User user) {
        // return the union of events the user has created and is attending
        List<String> events = user.getAttendEvents();
        events.addAll(user.getUserEvents());
        return events;
    }

    // TODO: I removed static from this? Was it needed? It wouldnt let me return the list otherwise
    /**
     * Retrieve all usernames that are registered in UserManager
     * @return a list of all usernames of every User in UserManager's userList
     */
    public List<String> getUsernameList() {
        return usernamesList;
    }


    /**
     * Get the user with the matching username
     * @param username the username to attempt to find a matching user with
     * @return User If the user was found, otherwise return a null object
     * */
    public User getUser(String username){
        for (User user :
                userList) {
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        // If the loop ends and no users with the matching username are found, return null
        return null;
    }

    /**
     * Checks if the username is present within the program
     * @param username The username to check exists
     * @return Whether the username is taken
     */
    public boolean isUsernameTaken(String username){
        return usernamesList.contains(username);
    }

    /**
     * Checks if the email is present within the program
     * @param email The email to check exists
     * @return Whether the email is taken
     */
    public boolean isEmailTaken(String email){
        return emailList.contains(email);
    }

    /**
     * Delete an event from the attending and creation list of each user
     * @param eventIDToDelete The eventID to delete
     * @return Whether the deletion was successful
     */
    public boolean deleteEventFromAllUsers(String eventIDToDelete){
        for (User user :
                userList) {
            if (user.getAttendEvents().contains(eventIDToDelete)){ // If the user is attending this event
                user.getAttendEvents().remove(eventIDToDelete); // Removes the event via aliasing
            }
            if (user.getUserEvents().contains(eventIDToDelete)){ // If the user made this event
                user.getUserEvents().remove(eventIDToDelete); // Removes the event via aliasing
            }
        }
        return true;
    }
}
