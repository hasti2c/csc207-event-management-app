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
    private static List<Event> eventList = new ArrayList<>();

    public EventManager(){

    }

    // === Creation and Deletion ===
    public Event createEvent(String templateName){
        return new Event(new Template(templateName), "Placeholder"); // Placeholder
    }

    public boolean deleteEvent(){
        return true; // Placeholder
    }

    // === Getters and Setters ===
    public Map<String, String> getEventDetails(Event event){
        return new HashMap<>(); // Placeholder
    }

    public static int getNumEvents() {
        return eventList.size();
    }

    // Editing Event variables will be done through this setter as all info is
    // kept in the eventDetails map of the Event object
    public boolean setEventDetails(Event event){
        return true; // Placeholder
    }


    public Event getEvent(int eventID){
        return new Event(new Template(), "Placeholder"); // Placeholder
    }

    public Event getEvent(String eventName){
        return new Event(new Template(), "Placeholder"); // Placeholder
    }

    public ArrayList<Event> getPublicEvents(){
        return new ArrayList<>(); // Placeholder
    }
}
