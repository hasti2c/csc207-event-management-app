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

    // Editing Event variables will be done through this setter as all info is
    // kept in the eventDetails map of the Event object
    public void setEventDetails(String eventId) {
        for (Event event: eventList) {
            if (event.getEventId().equals(eventId)){
                event.getEventDetails().put(eventId, ""); //Placeholder like what is going into the values of the map
            }
        }
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