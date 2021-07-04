package team1.angela;

import java.util.Date;
import java.util.Map;

public class Event {
    /**
     * Events
     */
    // === Class Variables ===
    private static int numEvents;
    // === Instance Variables ===
    private int eventID;
    private boolean published;
    private Date createDate;
    private Date editDate;
    private String eventOwner;

    // From Template
    private Map<String, String> eventDetails;
//    private String eventName;
//    // equal -1 if no max is specified
//    private int maxAttendees;
//    // Will essentially be the name of the template e.g. BBQ, concert, wedding
//    private String eventType;

    // === Representation Invariants ===
    // Not sure yet
    // === Methods ===
    public Event(Template template, String eventOwner){

    }

    // Getters
    public int getEventID() {
        return eventID;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public Date getEditDate() {
        return editDate;
    }
    public boolean isPublished() {
        return published;
    }
    public String getEventOwner() {
        return eventOwner;
    }
    public Map<String, String> getEventDetails() {
        return eventDetails;
    }
    public static int getNumEvents() {
        return numEvents;
    }
    // Setters
    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
    public void setPublished(boolean published) {
        this.published = published;
    }
    public void setEventDetails(Map<String, String> eventDetails) {
        this.eventDetails = eventDetails;
    }
}
