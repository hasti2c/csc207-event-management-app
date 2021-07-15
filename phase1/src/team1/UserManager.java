package team1;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the Users in the system
 */
public class UserManager {
    // === Instance Variables ===
    private List<User> userList;
    private List<String> usernamesList;
    private List<String> emailList;
    // === Methods ===
    public UserManager() {
        userList = new ArrayList<>();
        usernamesList = new ArrayList<>();
        emailList = new ArrayList<>();
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
        usernamesList.remove(user.getUsername());
        emailList.remove(user.getUserEmail());
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

    /**
     * Logs out a user by checking the inputted password against the User's username
     * @param username The username of the user attempting to log out
     * @return boolean Whether the logout was successful
     */
    public boolean logOut(String username) {
        // returns true if successfully logged out, false otherwise

        User userToLogout = getUser(username);
        if (userToLogout == null){
            return false;
        }
        else {
            userToLogout.setLoggedIn(false);
            return true;
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
     * Delete the Event and unregister everyone who is attending
     * @param user the user who wishes to delete an Event they had created
     * @param eventID the eventID of the event that the user will delete
     * @return whether the user has deleted the event successfully
     */
    public boolean deleteEvent(User user, String eventID){
        if (user.getUserEvents().contains(eventID)) {
            user.getUserEvents().remove(eventID);
            // remove this event's eventID from any user's attendEvents list
            for (User u : userList) {
                if (u.getAttendEvents().contains(eventID)) {
                    u.getAttendEvents().remove(eventID);
                }
            }
            return true;
        }

        else {
            return false;
        }
    }

    /**
     * unregister this user from an Event
     * @param user the user who wishes to unregister from an Event corresponding to the given eventID
     * @param eventID the event ID number of the event the user wishes to no longer attend
     * @return whether the user has unregistered from the event successfully
     */
    public boolean unAttendEvent(User user, String eventID) {
        if (user.getAttendEvents().contains(eventID)) {
            user.getAttendEvents().remove(eventID);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * create an Event that is hosted by the given User
     * @param user the User who is hosting the event
     * @param eventID the eventID that the user is hosting
     * @return whether the Event has been successfully created
     */
    public boolean createEvent(User user, String eventID){
        // Add this event to the list of events the user has created
        user.getUserEvents().add(eventID);
        return true;
    }


    /**
     * Register the user to attend the event
     * @param user The user who wishes to attend the event
     * @param eventID The event ID of the Event that the user wishes to attend
     * @return True if the user was able to register for the event. False if the event has no available space.
     */
    public boolean attendEvent(User user, String eventID) {
        user.getAttendEvents().add(eventID);
        return true;
    }

    /**
     * Retrieve the events IDS for the events that a user has created
     * @param user The user whose created event's event ID's are to be retrieved
     * @return a list of event IDS for the events the user has created
     */
    public List<String> getCreatedEvents(User user) {
        // return the events the user has created
        return user.getUserEvents();
    }

    /**
     * Retrieve the events IDS for the events that a user is attending
     * @param user The user whose attended event's event ID's are to be retrieved
     * @return a list of event IDS for the events the user is attending
     */
    public List<String> getAttendedEvents(User user) {
        // return the events the user is attending
        return user.getAttendEvents();
    }

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
    // TODO: Change to retrieveUser
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
    // TODO: Rename to usernameIsTaken
    public boolean isUsernameTaken(String username){
        return usernamesList.contains(username);
    }

    /**
     * Checks if the email is present within the program
     * @param email The email to check exists
     * @return Whether the email is taken
     */
    // TODO: Rename emailIsTaken
    public boolean isEmailTaken(String email) {
        return emailList.contains(email);
    }

}
