package entities;

import utility.Pair;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Events of the system
 */
public class Event {

    // === Class Variables ===
    // === Instance Variables ===
    // https://stackoverflow.com/questions/24876188/how-big-is-the-chance-to-get-a-java-uuid-randomuuid-collision
    private String eventId;
    private boolean published;
    private LocalDateTime createdTime;
    private LocalDateTime editTime;
    private String eventOwner;
    // The actual map containing event details using the same field details from Template class and with the values
    // entered by the user.
    private Map<String, Object> eventDetails;
    private Map<String, Pair<Class<?>, Boolean>> fieldNameAndFieldSpecs;
    // the number of people who are attending the event. (We won't be having any tickets at least for Phase 1)
    private int numAttendees;
    // Will essentially be the name of the template e.g. BBQ, concert, wedding
    private String eventType;
    private String templateId;
    private String templateVersion;

    // === Constructors ===

    /**
     * Initializes a new event with the given template and owner.
     * @param template the template used to create the event
     * @param eventOwner the creator of the event
     */
    public Event(Template template, String eventOwner){
        eventId = UUID.randomUUID().toString();
        // need user to explicitly change to published
        published = false;
        createdTime = LocalDateTime.now();
        editTime = createdTime;
        this.templateId = template.getTemplateId();
        this.templateVersion = template.getFileVersionNumber();
        this.eventDetails = new HashMap<>();
        this.fieldNameAndFieldSpecs = new HashMap<>();
        this.eventOwner = eventOwner;
        this.eventType = template.getTemplateName();
    }

    // Empty constructor
    public Event() {
    }

    // === Methods ===

    /**
     * Adds fieldName and sets the values for each fieldName null for this event's eventDetails map.
     * @param template the template used to create the event
     */
    public void addFieldsToEventDetails(Template template) {
        for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
            this.eventDetails.put(fieldSpecs.getFieldName(), null);
        }
    }

    /**
     * Adds field name and field spec info to the event's fieldNameAndFieldSpecs map.
     * @param template the template used to create the event
     */
    public void addFieldNameAndFieldSpecsInfo(Template template) { //creates a map that has fieldName as key,
        // [fieldType, required] as value
        for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
            Pair<Class<?>, Boolean> typeAndRequired = new Pair<>(fieldSpecs.getDataType(), fieldSpecs.isRequired());
            this.fieldNameAndFieldSpecs.put(fieldSpecs.getFieldName(), typeAndRequired);
        }
    }

    /**
     * Returns the maximum number of attendees for this event. If the event doesn't specify a maximum, returns -1.
     * @return int the maximum number of attendees or -1.
     */
    public int returnMaxAttendees() {
        // returns the maximum number of attendees, if the event doesn't have a maximum, returns -1
        Map<String, Object> eventDetailsMap = getEventDetails();
        int maxAttendees;
        if (eventDetailsMap.get("Max Attendees") == null) {
            maxAttendees = -1;
        }
        else {
            maxAttendees = (Integer) eventDetailsMap.get("Max Attendees");
        }
        return maxAttendees;
    }

    /**
     * Returns the event's name. If the event has no name, it is called "Untitled".
     * @return String the name of the event
     */
    public String returnEventName() {
        Map<String, Object> eventDetailsMap = getEventDetails();
        String eventName;
        if (eventDetailsMap.get("Event Name") == null) {
            eventName = "Untitled";
        }
        else {
            eventName = (String) eventDetailsMap.get("Event Name");
        }
        return eventName;
    }

    // Getters

    /**
     * Gets the number of attendees for this event
     * @return int Number of attendees for this event
     */
    public int getNumAttendees() {
        return numAttendees;
    }

    /**
     * Gets the type of this event (Also, this will match the name of the template that was used to create the event.)
     * @return String Type of this event
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets the Id of this event
     * @return String Id of this event
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Gets the time of when this event was created
     * @return LocalDateTime Time when this event was created
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * Gets the time of when this event was edited
     * @return LocalDateTime Time when this event was edited
     */
    public LocalDateTime getEditTime() {
        return editTime;
    }

    /**
     * Gets the event's published status
     * @return boolean Whether the event is published
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * Gets the owner of this event
     * @return String The owner of this event
     */
    public String getEventOwner() {
        return eventOwner;
    }

    /**
     * Gets the details of this event
     * @return Map</String,Object> The details of this event
     */
    public Map<String, Object> getEventDetails() {
        return eventDetails;
    }

    /**
     * Gets the map with FieldName as key and a list as the value, the first object is the field's data type and
     * the second object is whether or not the field is a required one.
     * @return Map</String, List<Object>> The map with FieldName as key and FieldSpecs as value of this event
     */
    public Map<String, Pair<Class<?>, Boolean>> getFieldNameAndFieldSpecsMap() {
        return fieldNameAndFieldSpecs;
    }

    /**
     * Gets the Id of the template associated with this event
     * @return String The Id of the template associated with this event
     */
    public String getTemplateId(){
        return templateId;
    }

    /**
     * Gets the version of the template associated with this event
     * @return String The version number of the template associated with this event
     */
    public String getTemplateVersion(){
        return templateVersion;
    }

    /**
     * Sets a new owner for this event
     * @param eventOwner The new owner of this event
     */
    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    /**
     * Sets a new number of attendees for this event
     * @param numAttendees The new number of attendees for this event
     */
    public void setNumAttendees(int numAttendees) {
        this.numAttendees = numAttendees;
    }

    /**
     * Sets a new time of when this event was edited
     * @param editTime The new time this event was edited
     */
    public void setEditTime(LocalDateTime editTime) {
        this.editTime = editTime;
    }

    /**
     * Sets a new published status for this event
     * @param published The new published status
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

}
