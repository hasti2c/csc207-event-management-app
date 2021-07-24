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

    // === Things Users Can Do ===

    /**
     * Creates an event with the given name of template templateName and name of owner of the event eventOwner. Also
     * returns the id of the Event for the controller.
     * @param templateName The Name of the template
     * @param eventOwner The owner of this event
     * @return The Id of the event
     */
    // Initiates the creation of an event. Requires controller to then enter all the information for the event from the user.
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

    /**
     * Adds an attendee for this event if there is still room in the event.
     * @param eventID the ID of the event that is being attended.
     * @return false if there is no room in the event, true if the event has been successfully singed up for.
     */
    public boolean attendEvent(String eventID) {
        Event currentEvent = retrieveEventById(eventID);
        if (currentEvent.returnMaxAttendees() == -1) {
            return false;
        }
        else if (currentEvent.getNumAttendees() == currentEvent.returnMaxAttendees()) {
            return false;
        }
        else {
            int numAttendees = currentEvent.getNumAttendees();
            currentEvent.setNumAttendees(numAttendees + 1);
            return true;
        }
    }

    /**
     * Removes an attendee from the event.
     * @param eventID the ID of the event that is being unattended.
     * @return if the attendee has been removed successfully.
     */
    public boolean unAttendEvent (String eventID) {
        Event currentEvent = retrieveEventById(eventID);
        int numAttendees = currentEvent.getNumAttendees();
        currentEvent.setNumAttendees(numAttendees - 1);
        return true;
    }

    /**
     * Publishes the event so it is visible by the public.
     * @param eventID ID of the event being published.
     * @return returns true if the event has been successfully published
     */
    public boolean publishEvent(String eventID) {
        retrieveEventById(eventID).setPublished(true);
        return retrieveEventById(eventID).isPublished();
    }

    // === Retrieving information ===
    public boolean returnIsPublished(String eventID) {
        return retrieveEventById(eventID).isPublished();
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
                    if (eventDetailsEntry.getValue() == null) {
                        eventDetailsMap.put(eventDetailsEntry.getKey(), "N/A");
                    }
                    else {
                        eventDetailsMap.put(eventDetailsEntry.getKey(), eventDetailsEntry.getValue().toString());
                    }
                }
            }
        }
        return eventDetailsMap;
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
                for (Map.Entry<String, List<Object>> fieldSpecEntry: event.getFieldNameAndFieldSpecsMap().entrySet()) {
                    fieldNameAndType.put(fieldSpecEntry.getKey(), fieldSpecEntry.getValue().get(0).toString());
                }

            }
        }
        return fieldNameAndType;
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

    /**
     * Returns the name of the event from the event's ID.
     * @param eventId
     * @return eventName
     */
    public String retrieveEventNameById(String eventId) {
        Event event = retrieveEventById(eventId);
        return event.returnEventName();
    }

    // === Helpers for Converting to Different Types ===

    public Map<String, String> returnEventAsMap(String eventId) {
        Map<String, String> eventMap = new LinkedHashMap<>();
        Event event = retrieveEventById(eventId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTED_DATE);
        String formattedCreatedTime = event.getCreatedTime().format(formatter);
        String formattedEditTime = event.getEditTime().format(formatter);
        eventMap.put("Created Time", formattedCreatedTime);
        eventMap.put("Last Edited", formattedEditTime);
        eventMap.put("Event Id", event.getEventId());
        eventMap.put("Event Owner", event.getEventOwner());
        eventMap.put("Type of Event", event.getEventType());
        eventMap.put("Number of Attendees", Integer.toString(event.getNumAttendees()));
        return eventMap;
    }

    // TODO might want to put this method in event controller
    public List <String> returnEventNamesListFromIdList(List <String> eventIdList) {
        List<String> eventNames = new ArrayList<>();
        for (String eventID : eventIdList) {
            eventNames.add(retrieveEventNameById(eventID));
        }
        return eventNames;
    }

    // === Helpers for Data Entry ===

    /**
     * Enters a value to a field name
     * @param eventId ID of the event that is being edited
     * @param fieldName Name of the field that the user wishes to enter a value for
     * @param fieldValue Value for the specified field
     */
    // TODO: need to convert the value to the correct data type before entering. Also if the fieldValue is "", just keep the value as null.
    public void enterFieldValue(String eventId, String fieldName, Object fieldValue) {
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
     * Converts a field value string to its correct data type and returns the object in the correct type.
     * @param eventId
     * @param fieldName
     * @param fieldValue
     * @return The field value converted to the correct type.
     */
    // TODO returns the fieldValue converted to the correct data type. If it doesn't work throw an error.
    // Check data validation can catch the error that this throws
    // Chris, you can use the same local date time formatter that you had before. Use parse in local dat time that has the formatter thing. Link below
    // https://docs.oracle.com/javase/8/docs/api/java/time/LocalDateTime.html
    private Object convertToCorrectDataType(String eventId, String fieldName, String fieldValue) {
        return null;
    }

    /**
     * Checks if the entered field is of the correct format. If the field is required, also checks whether a value has been entered.
     * @param eventId The Id of the event that is to be checked
     * @param fieldName The name of the field
     * @param fieldValue The value that is
     * @return boolean Whether data fieldValue is valid
     */
    // TODO this needs to be fixed
    public boolean checkDataValidation(String eventId, String fieldName, String fieldValue) {
        // if the field value doesn't pass, return false and do nothing.
        // first check if the field value is empty, if it is empty then check if the field is required
        // if it is required, then return false, if it isn't then return true
        // next check if the data type is correct. Call the convertToCorrectDataType method and if it throws an error return false
        // else, return true. (I think this works, if not we can try something different)
        for (Event event: eventList){
            if (event.getEventId().equals(eventId)){
                for (Map.Entry<String, List<Object>> fieldSpecEntry : event.getFieldNameAndFieldSpecsMap().entrySet()){
                    if ((fieldSpecEntry.getKey().equals(fieldName)) && (fieldSpecEntry.getValue().get(0).equals
                            (fieldValue.getClass().getSimpleName()))
                    && (fieldSpecEntry.getValue().get(1).equals(true))){
                        return true;
                        }
                    }
                }
            }
//        return false;
        return true;
    }

//    /**
//     * Gets the size of eventList, a list of events
//     * @return The size of eventList
//     */
//    public int getNumEvents() {
//        return eventList.size();
//    }

    public void saveAllEvents() {
        parser.saveAllElements(eventList);
    }
}