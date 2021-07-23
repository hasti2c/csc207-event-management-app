package entitiesAndUseCases;
import controllersGatewaysPresenters.IGateway;

import java.util.*;
import java.time.format.DateTimeFormatter;

public class EventManager {
    /**
     * Manages the Events in the system
     */
    // === Class Variables ===
    private List<Event> eventList;
    private TemplateManager templateManager;
    private IGateway<Event> parser;
    private static final String FORMATTED_DATE= "yyyy-MM-dd HH:mm:ss";

    public EventManager(IGateway<Event> parser, TemplateManager templateManager) {
        this.parser = parser;
        eventList = parser.getAllElements();
        this.templateManager = templateManager;
    }

    // === Creation and Deletion ===

    /**
     * Creates an event with the given name of template templateName and name of owner of the event eventOwner. Also
     * returns the id of the Event for the controller.
     * @param templateName The Name of the template
     * @param eventOwner The owner of this event
     * @return The Id of the event
     */
    public String createEvent(String templateName, String eventOwner) {
        Event newEvent = new Event(templateManager.retrieveTemplateByName(templateName), eventOwner);
        newEvent.addFieldsToEventDetails(templateManager.retrieveTemplateByName(templateName));
        newEvent.addFieldNameAndFieldSpecsInfo(templateManager.retrieveTemplateByName(templateName));
        eventList.add(newEvent);
        return newEvent.getEventId();
    }

    /**
     * Deletes the event with the matching eventId in eventList, a list of events
     * @param eventId The Id of the event that is to be deleted
     */
    public void deleteEvent(String eventId) {
        eventList.removeIf(event -> event.getEventId().equals(eventId));
    }

    // === Getters and Setters ===
    public Map<String, Object> retrieveEventDetails(String eventId) {
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
    public int getNumEvents() {
        return eventList.size();
    }

    /**
     * Returns a map of the event with the matching event Id, where the key is field name of map eventDetails
     * and the value is data type that associates with each key
     * @param eventId The Id of the event
     * @return Map<String, String> The map of the event with the matching event Id, where the key is field name and
     * the value is data type
     */
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

    /**
     * Returns the event details for the event with the given event id
     * @param eventId The event id of the event
     * @return Map The event details of map for the event with the given event id
     */
    public Map<String, String> returnEventDetails(String eventId) {
        Map<String, String> eventDetailsMap = new HashMap<>();
        for (Event event : eventList) {
            if (event.getEventId().equals(eventId)) {
                for (Map.Entry<String, Object> eventDetailsEntry : event.getEventDetails().entrySet()) {
                    eventDetailsMap.put(eventDetailsEntry.getKey(), eventDetailsEntry.getValue().toString());
                }
            }
        }
        return eventDetailsMap;
    }
    /**
     * Enters a value to a field name
     * @param fieldName
     * @param fieldValue
     * @param eventId
     */
    public void enterFieldValue (String fieldName, String fieldValue, String eventId) {
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
        // if the field value doesn't pass, return false and do nothing.
        // basically try and catch changing the string into the type it's supposed to be. If it doesn't work then...
        // it's probably wrong.
        for (Event event: eventList){
            if (event.getEventId().equals(eventId)){
                for (Map.Entry<String, List<Object>> fieldSpecEntry : event.getFieldNameAndTypeMap().entrySet()){
                    if ((fieldSpecEntry.getKey().equals(fieldName)) && (fieldSpecEntry.getValue().get(0).equals
                            (fieldValue.getClass().getSimpleName()))
                    && (fieldSpecEntry.getValue().get(1).equals(true))){
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
    public Event retrieveEventById(String eventId) {
        List<Event> holderList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getEventId().equals(eventId)) {
                holderList.add(event);
            }
        }
        return holderList.remove(0);
    }

    public LinkedHashMap<String, String> returnEventAsMap(String eventId) {
        LinkedHashMap<String, String> eventMap = new LinkedHashMap<>();
        Event event = retrieveEventById(eventId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTED_DATE);
        String formattedCreatedTime = event.getCreatedTime().format(formatter);
        String formattedEditTime = event.getEditTime().format(formatter);
        eventMap.put("Created Time", formattedCreatedTime);
        eventMap.put("Last Edited", formattedEditTime);
        eventMap.put("Name of Event", event.getEventName());
        eventMap.put("Event Id", event.getEventId());
        eventMap.put("Event Owner", event.getEventOwner());
        eventMap.put("Type of Event", event.getEventType());
        eventMap.put("Number of Attendees", Integer.toString(event.getNumAttendees()));
        return eventMap;
    }

    /**
     * Returns a List of the IDs of all the published events from eventList, a list of events
     * @return Arraylist of all the published events from eventList, a list of events
     */
    public List<String> returnPublishedEvents() {
        List<String> holderList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.isPublished()) {
                holderList.add(event.getEventId());
            }
        }
        return holderList;
    }

    public void saveAllEvents() {
        parser.saveAllElements(eventList);
    }
}