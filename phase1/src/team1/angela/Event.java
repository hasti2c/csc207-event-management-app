package team1.angela;

import java.util.Date;

public class Event {
    /**
     * Events
     */
    // Might want to include a static variable that counts up to generate unique event ID's each time?
    // Not sure how we can verify that the User is actually a real user... do we need to?
    // === Instance Variables ===
    // Could be an int as well idk which is better...
    public String eventID;
    public boolean published;
    public Date createDate;
    public Date editDate;
    public String eventOwner;
    // === Representation Invariants ===
    // Not sure yet
    // === Methods ===
    public Event(){

    }
    // need getters and setters for editedDate, need getter for everythign else I think
}
