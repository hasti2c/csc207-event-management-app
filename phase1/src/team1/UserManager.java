package team1;

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
    public void createUser(String username, String password, String userEmail, User.UserType type) {
        userList.add(new User(username, password, userEmail, type));
    }
    public void deleteUser(User user) {
        userList.remove(user);
    }
    public boolean logIn(String username, String password) {
        // returns true if successfully logged in, false if otherwise (like if password is wrong)
        // updates the loggedIn boolean to True
        for (User u : userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                u.setLoggedIn(true);
                return true;
            }
        }
        return false;
    }
    public boolean logOut(String username, String password) {
        // returns true if successfully logged out, false if otherwise (like if password is wrong)
        for (User u : userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                u.setLoggedIn(false);
                return true;
            }
        }
        return false;
    }

    // this implementation seems a little slow
    // because if the user keeps getting the confirmed password wrong it will have to iterate through userList
    // multiple times and this would be very timely
    //temporary implementation
    public boolean updateUsername(User user, String newUsername){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        if (user.isLoggedIn()) {
            user.setUsername(newUsername);
            return true;
        }
        else {
            System.out.println("User is not logged in!");
            return false;
        }
    }
    public boolean updatePassword(User user, String newPassword){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in

        if (user.isLoggedIn()) {
            user.setPassword(newPassword);
            return true;
        }
        else {
            System.out.println("User is not logged in!");
            return false;
        }
    }

    public boolean updateEmail(User user, String newEmail){
        // returns true if user is updated successfully
        // access getters and setters of useUser class
        // User needs to be logged in
        if (user.isLoggedIn()) {
            user.setUserEmail(newEmail);
            return true;
        }
        else {
            System.out.println("User is not logged in!");
            return false;
        }

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

    // Display list of events
    public List<Event> getEvents(User user) {
        // return the union of events the user has created and is attending
        List<Event> events = user.getAttendEvents();
        events.addAll(user.getUserEvents());
        return events;
    }

    // Returns list of usernames
    public static List<String> getUsernameList() {
        // Go through each user object in userList and add their username attribute to usernameList
        List<String> usernameList = new ArrayList<>();
        for (User u : userList) {
            usernameList.add(u.getUsername());
        }
        return usernameList;
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



    public void getUser(String username) {
        // the return type is User
        // not implemented due to some complications in previous approach and IDE did not like it
    }
}
