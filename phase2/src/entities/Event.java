package entities;

import utility.*;

import java.time.LocalDateTime;
import java.util.*;

import static utility.EventPrivacyType.*;

/**
 * Events of the system
 */
public class Event implements Savable, Viewable {
    // === Class Variables ===
    // === Instance Variables ===

    private String eventId;
    private String eventName;
    private EventPrivacyType privacyType;
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
    private boolean suspended;

    // === Constructors ===

    /**
     * Initializes a new event with the given template and owner.
     * @param template the template used to create the event
     * @param eventOwner the creator of the event
     */
    public Event(Template template, String eventName, String eventOwner){
        eventId = UUID.randomUUID().toString();
        // need user to explicitly change to public
        privacyType = PRIVATE;
        createdTime = LocalDateTime.now();
        editTime = createdTime;
        this.eventDetails = new HashMap<>();
        this.fieldNameAndFieldSpecs = new HashMap<>();
        this.eventName = eventName;
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
     * Returns the event's name.
     * @return String the name of the event
     */
    public String getEventName() {
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
     * @return The event's privacy type.
     */
    public EventPrivacyType getPrivacyType() {
        return privacyType;
    }

    public boolean isSuspended() {
        return suspended;
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
     * Sets the privacy type of this event
     * @param privacyType The new privacy type
     */
    public void setPrivacyType(EventPrivacyType privacyType) {
        this.privacyType = privacyType;
    }

    /**
     * Sets the suspended status of this event
     * @param suspended The suspended status
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public String getID() {
        return eventId;
    }
}
