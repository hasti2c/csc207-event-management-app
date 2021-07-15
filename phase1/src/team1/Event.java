package team1;

import java.io.Serializable;
import java.util.*;

public class Event implements Serializable {
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
        createdTime = Calendar.getInstance().getTime();
        editTime = createdTime;
        this.templateId = template.getTemplateId();
        this.templateVersion = template.getFileVersionNumber();
        this.eventDetails = new HashMap<>();
        this.fieldNameAndTypeMap = new HashMap<>();
        this.eventOwner = eventOwner;
        this.eventType = template.getTemplateName();
    }

    private void addFieldsToEventDetails(Template template) {
        for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
            this.eventDetails.put(fieldSpecs.getFieldName(), null);
        }
    }

    private void addFieldNameAndTypeToMap(Template template) { //creates a map that has fieldName as key, [fieldType, required] as value
        for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
            List<Object> fieldSpecsTypeAndRequired = new ArrayList<>();
            fieldSpecsTypeAndRequired.add(fieldSpecs.getDataType()); //the first element, 0
            fieldSpecsTypeAndRequired.add(fieldSpecs.isRequired()); //the second element, 1
            this.fieldNameAndTypeMap.put(fieldSpecs.getFieldName(),fieldSpecsTypeAndRequired);
        }
    }

    // Getters

    public int getNumAttendees() {
        return numAttendees;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventId() {
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

    public Map<String, List<Object>> getFieldNameAndTypeMap() {
        return fieldNameAndTypeMap;
    }
    public String getTemplateId(){return templateId;}
    public String getTemplateVersion(){return templateVersion;}
    // Setters

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public void setNumAttendees(int numAttendees) {
        this.numAttendees = numAttendees;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }
    public void setPublished(boolean published) {
        this.published = published;
    }
    public void setEventDetails(Map<String, Object> eventDetails) {
        this.eventDetails = eventDetails;
    }
    public void setFieldNameAndTypeMap(Map<String, List<Object>> fieldNameAndTypeMap) {
        this.fieldNameAndTypeMap = fieldNameAndTypeMap;
    }
}
