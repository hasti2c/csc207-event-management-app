package team1;

import java.util.*;

public class EventManager {
    /**
     * Manages the Events in the system
     */
    // === Class Variables ===
    private static List<Event> eventList;

    public EventManager() {
        eventList = new ArrayList<>();
    }

    // === Creation and Deletion ===

    /**
     * Creates an event with the given name of template templateName and name of owner of the event eventOwner. Also
     * returns the id of the Event for the controller.
     * @param templateId The Name of the template
     * @param eventOwner The owner of this event
     * @return The Id of the event
     */
    // TODO need to check templateId vs templateName
    public String createEvent(String templateId, String eventOwner) {
        Event e = new Event(TemplateManager.retrieveTemplateById(templateId), eventOwner);
        eventList.add(e);
        return e.getEventId();
    }

    /**
     * Deletes the event with the matching eventId in eventList, a list of events
     * @param eventId The Id of the event that is to be deleted
     */
    public void deleteEvent(String eventId) {
        eventList.removeIf(event -> event.getEventId().equals(eventId));
    }

    // === Getters and Setters ===
    public Map<String, Object> getEventDetails(String eventId) {
        List<Map<String, Object>> holderList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getEventId().equals(eventId)) {
                holderList.add(event.getEventDetails());
            }
        }
        return holderList.remove(0);
    }

    /**
     * Gets the size of eventList, a list of events
     * @return The size of eventList
     */
    public static int getNumEvents() {
        return eventList.size();
    }

    /**
     * Returns a map of the event with the matching event Id, where the key is field name of map eventDetails
     * and the value is data type that associates with each key
     * @param eventId The Id of the event
     * @return Map<String, String> The map of the event with the matching event Id, where the key is field name and
     * the value is data type
     */
    // TODO change to retrieve
    public Map<String, String> returnFieldNameAndType(String eventId){
        // returns a map of the fields associated with this event
        // 1. find event in list of events
        // 2. get the eventDetails map for that event
        // 3. put all the Keys as the key and return the dataType for each key (fieldName)
        Map<String, String> fieldNameAndType = new HashMap<>();
        for (Event event: eventList){
            if (event.getEventId().equals(eventId)){
                for (Map.Entry<String, List<Object>> fieldSpecEntry: event.getFieldNameAndTypeMap().entrySet()) {
                    fieldNameAndType.put(fieldSpecEntry.getKey(), fieldSpecEntry.getValue().get(0).toString());
                }

            }
        }
        return fieldNameAndType;
    }

    private void enterFieldValue (String fieldName, String fieldValue, String eventId) {
        for (Event event : eventList) {
            if (event.getEventId().equals(eventId)) {
                for (Map.Entry<String, Object> eventDetailsEntry : event.getEventDetails().entrySet()) {
                    if (eventDetailsEntry.getKey().equals(fieldName)) {
                        event.getEventDetails().replace(fieldName, fieldValue);
                    }
                }
            }
        }
    }

    /**
     * Checks if the entered field is the same type
     * @param fieldName The name of the field
     * @param fieldValue The value that is
     * @param eventId The Id of the event that is to be checked
     * @return boolean Whether data fieldValue is valid
     */
    public boolean checkDataValidation(String fieldName, String fieldValue, String eventId) {
        // if the field value passes validation return true and call enterFieldValue to add it to eventDetails
        // if the field value doesn't pass, return false and do nothing.
        // basically try and catch changing the string into the type it's supposed to be. If it doesn't work then...
        // it's probably wrong.
        for (Event event: eventList){
            if (event.getEventId().equals(eventId)){
                for (Map.Entry<String, List<Object>> fieldSpecEntry : event.getFieldNameAndTypeMap().entrySet()){
                    if ((fieldSpecEntry.getKey().equals(fieldName)) && (fieldSpecEntry.getValue().get(0).equals
                            (fieldValue.getClass().getSimpleName()))
                    && (fieldSpecEntry.getValue().get(1).equals(true))){
                        enterFieldValue(fieldName,fieldValue,eventId);
                        return true;
                        }
                    }
                }
            }
        return false;
    }

    /**
     * Returns an event that has the matching Id from eventList, a list of events
     * @param eventId The Id of the event that is to be returned
     * @return The event that the event Id
     */
    public Event getEvent(String eventId) {
        List<Event> holderList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getEventId().equals(eventId)) {
                holderList.add(event);
            }
        }
        return holderList.remove(0);
    }

    /**
     * Returns an Arraylist of all the published events from eventList, a list of events
     * @return Arraylist of all the published events from eventList, a list of events
     */
    // TODO change name to retrievePublishedEvents
    public ArrayList<Event> getPublicEvents() {
        ArrayList<Event> holderList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.isPublished()) {
                holderList.add(event);
            }
        }
        return holderList;
    }
}