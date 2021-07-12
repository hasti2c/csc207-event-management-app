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
    public void createEvent(String templateName, String eventOwner) {
        eventList.add(new Event(new Template(templateName), eventOwner));
    }

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

    public static int getNumEvents() {
        return eventList.size();
    }

    public Map<String, String> returnFieldNameAndType(String eventId){
        // returns a list (did u mean map?) of the fields associated with this event
        // 1. find event in list of events
        // 2. get the eventDetails map for that event
        // 3. put all the Keys as the key and ... might want to do this in Event class oops
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

    public boolean checkDataValidation(String fieldName, String fieldValue, String eventId) { //check it over for the values
        // if the field value passes validation return true and call enterFieldValue to add it to eventDetails
        // if the field value doesn't pass, return false and do nothing.
        // basically try and catch changing the string into the type it's supposed to be. If it doesn't work then...
        // it's probably wrong.
        for (Event event: eventList){
            if (event.getEventId().equals(eventId)){
                for (Map.Entry<String, List<Object>> fieldSpecEntry : event.getFieldNameAndTypeMap().entrySet()){
                    if ((fieldSpecEntry.getKey().equals(fieldName)) && (fieldSpecEntry.getValue().get(0).equals(fieldValue.getClass().getSimpleName()))
                    && (fieldSpecEntry.getValue().get(1).equals(true))){
                        enterFieldValue(fieldName,fieldValue,eventId);
                        return true;
                        }
                    }
                }
            }
        return false;
    }

    public Event getEvent(String eventId) {
        List<Event> holderList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getEventId().equals(eventId)) {
                holderList.add(event);
            }
        }
        return holderList.remove(0);
    }

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