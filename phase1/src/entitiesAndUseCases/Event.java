package entitiesAndUseCases;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Event implements Serializable {
    /**
     * The events of the system
     */
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
    private Map<String, List<Object>> fieldNameAndTypeMap;
    // the number of people who are attending the event. (We won't be having any tickets at least for Phase 1)
    private int numAttendees;
    // Will essentially be the name of the template e.g. BBQ, concert, wedding
    private String eventType;
    private String templateId;
    private String templateVersion;

    // === Representation Invariants ===
    // Not sure yet
    // === Methods ===
    public Event(Template template, String eventOwner){
        eventId = UUID.randomUUID().toString();
        // need user to explicitly change to published
        published = false;
        createdTime = LocalDateTime.now();
        editTime = createdTime;
        this.templateId = template.getTemplateId();
        this.templateVersion = template.getFileVersionNumber();
        this.eventDetails = new HashMap<>();
        this.fieldNameAndTypeMap = new HashMap<>();
        this.eventOwner = eventOwner;
        this.eventType = template.getTemplateName();
        // TODO not sure if this is right
        this.addFieldsToEventDetails(template);
        this.addFieldNameAndTypeToMap(template);
    }

    public Event() {
    }

    public void addFieldsToEventDetails(Template template) {
        for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
            this.eventDetails.put(fieldSpecs.getFieldName(), null);
        }
    }

    public void addFieldNameAndTypeToMap(Template template) { //creates a map that has fieldName as key,
        // [fieldType, required] as value
        for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
            List<Object> fieldSpecsTypeAndRequired = new ArrayList<>();
            fieldSpecsTypeAndRequired.add(fieldSpecs.getDataType()); //the first element, 0
            fieldSpecsTypeAndRequired.add(fieldSpecs.isRequired()); //the second element, 1
            this.fieldNameAndTypeMap.put(fieldSpecs.getFieldName(),fieldSpecsTypeAndRequired);
        }
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
     * Gets the type of this event (Also, this will match the name of the template)
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
     * Gets the owner of this Event
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
     * Gets the map with FieldName as key and FieldType as value of this event
     * @return Map</String, List<Object>> The map with FieldName as key and FieldType as value of this event
     */
    public Map<String, List<Object>> getFieldNameAndTypeMap() {
        return fieldNameAndTypeMap;
    }

    /**
     * Gets the Id of the template associated with this event
     * @return String The Id of the template associated with this event
     */
    public String getTemplateId(){return templateId;}

    /**
     * Gets the version of the template associated with this event
     * @return String The version of the template associated with this event
     */
    public String getTemplateVersion(){return templateVersion;}


    // Setters

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

    /**
     * Sets new details for this event
     * @param eventDetails The new details of this event
     */
    public void setEventDetails(Map<String, Object> eventDetails) {
        this.eventDetails = eventDetails;
    }

    /**
     * Sets a new map with FieldName as key and FieldType as value for this event
     * @param fieldNameAndTypeMap The new map with FieldName as key and FieldType as value of this event
     */
    public void setFieldNameAndTypeMap(Map<String, List<Object>> fieldNameAndTypeMap) {
        this.fieldNameAndTypeMap = fieldNameAndTypeMap;
    }
}
