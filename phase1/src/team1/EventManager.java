package team1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    /**
     * Manages the Events in the system
     */
    // === Class Variables ===
    private static List<Event> eventList;

    public EventManager(){

    }

    // === Creation and Deletion ===
    public void createEvent(String templateName, String eventOwner){
        eventList.add(new Event(new Template(templateName), eventOwner));
    }

    public void deleteEvent(String eventId){
        for (Event event: eventList) {
            if (event.getEventID().equals(eventId)){
                eventList.remove(event);
            };
        }
    }

    // === Getters and Setters ===
    public Map<String, Object> getEventDetails(String eventId){
        return new HashMap<>(); // Placeholder
    }

    public static int getNumEvents() {
        return eventList.size();
    }

    // Editing Event variables will be done through this setter as all info is
    // kept in the eventDetails map of the Event object
    public boolean setEventDetails(String eventId){
        return true; // Placeholder
    }


    public Event getEvent(String eventId){
        List<Event> holderList = new ArrayList<>();
        for (Event event: eventList) {
            if (event.getEventID().equals(eventId)) {
                holderList.add(event);
            }
        }
        return holderList.remove(0);
    }

    public ArrayList<Event> getPublicEvents(){
        return new ArrayList<>(); // Placeholder
    }
}
