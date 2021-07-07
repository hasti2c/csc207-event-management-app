package team1;

import java.util.*;

public class Event {
    /**
     * Events
     */
    // === Class Variables ===
    // === Instance Variables ===
    // https://stackoverflow.com/questions/24876188/how-big-is-the-chance-to-get-a-java-uuid-randomuuid-collision
    private String eventId;
    private boolean published;
    private Date createdTime;
    private Date editTime;
    private String eventOwner;
    // This saves the fieldName and dataType from the FieldSpecs class into a map so that we can keep a reference of it
    // for the event, in case the template gets changed. Also we can match the data type to the Object in the
    // eventDetails map so things don't break.
    private Map<String, Class<?>> templateFieldSpec;
    // The actual map containing event details using the same field details from Template class and with the values
    // entered by the user.
    private Map<String, Object> eventDetails;
//    // equal -1 if no max is specified (Don't need this, will be in eventDetails map)
//    private int maxAttendees;
    // the number of people who are attending the event. (We won't be having any tickets at least for Phase 1)
    private int numAttendees;
    // Will essentially be the name of the template e.g. BBQ, concert, wedding
    private String eventType;

    // === Representation Invariants ===
    // Not sure yet
    // === Methods ===
    public Event(Template template, String eventOwner){
        eventId = UUID.randomUUID().toString();
        // need user to explicitly change to published
        published = false;
        createdTime = Calendar.getInstance().getTime();
        editTime = createdTime;
        this.eventOwner = eventOwner;
        this.eventType = template.getTemplateName();
        this.templateFieldSpec = populateFieldSpecMap(template);
    }

    private Map<String, Class<?>> populateFieldSpecMap(Template template){
        // TODO make method to loop through template list and put it in the map.
    }

    private Map<String, Object> addFieldsToEventDetails(Map<String, Class<?>> templateFieldSpec){
        // TODO make method to loop through keys of templateFieldSpec and put into key of eventDetails map set Object to Null
    }

    // Getters
    public String getEventID() {
        return eventId;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public Date getEditTime() {
        return editTime;
    }
    public boolean isPublished() {
        return published;
    }
    public String getEventOwner() {
        return eventOwner;
    }
    public Map<String, Object> getEventDetails() {
        return eventDetails;
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
