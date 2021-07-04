package team1.angela;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Event {
    /**
     * Events
     */
    // === Class Variables ===
//    private static int numEvents;
    // === Instance Variables ===
    // https://stackoverflow.com/questions/24876188/how-big-is-the-chance-to-get-a-java-uuid-randomuuid-collision
    private String eventId;
    private boolean published;
    private Date createdTime;
    private Date editTime;
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
        eventId = UUID.randomUUID().toString();
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
