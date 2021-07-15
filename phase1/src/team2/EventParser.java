package team2;

// TODO exceptions/boolean return value
// TODO ask for transient fields
// TODO usernames & file names
// TODO test

import javafx.util.Pair;
import testing.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/* Serialization from examples in
   https://www.tutorialspoint.com/java/java_serialization.htm
 */
public class EventParser {
    private final CSVManager csvManager;
    private Map<String, Event> events;
    private final String[] header = new String[]{
            "eventId", "published", "createdTime", "editTime", "eventOwner",
            "templateFieldSpec", "eventDetails", "maxAttendees", "numAttendees", "eventType"
    };

    public EventParser(String path) {
        csvManager = new CSVManager(path);
        readEvents();
    }

    public Event getEvent(String eventId) {
        return events.get(eventId);
    }

    public void saveEvent(Event event) {
        String[] line = serializeEvent(event);
        events.replace(event.getEventId(), event);
        csvManager.changeLine(line);
    }

    public void createEvent(Event event) {
        String[] line = serializeEvent(event);
        events.put(event.getEventId(), event);
        csvManager.addLine(line);
    }

    private void readEvents() {
        Map<String, String[]> data = csvManager.getData();
        for (String id: data.keySet())
            events.put(id, deserializeEvent(data.get(id)));
    }

    private String[] serializeEvent(Event event) {
        Object[] values = new Object[]{
                event.getEventId(), event.isPublished(), event.getCreatedTime(), event.getEventId(),
                event.getEventOwner(), event.getTemplateFieldSpec(), event.getEventDetails(), event.getMaxAttendees(),
                event.getNumAttendees(), event.getEventType()
        };
        return Serializer.getInstance().serializeArray(values);
    }

    private Event deserializeEvent(String[] line) {
        Class<?>[] classes = new Class<?>[]{String.class, Boolean.class, Date.class, Date.class, String.class,
                Map.class, Map.class, Integer.class, Integer.class, String.class};
        Object[] values = Serializer.getInstance().deserializeArray(classes, line);
        return new Event(
                (String) values[0], (Boolean) values[1], (Date) values[2], (Date) values[3], (String) values[4],
                (Map<String, Class<? extends Serializable>>) values[5], (Map<String, Serializable>) values[6],
                (Integer) values[7], (Integer) values[8], (String) values[9]);
    }
}
