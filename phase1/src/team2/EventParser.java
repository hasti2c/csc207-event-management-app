package team2;

// TODO exceptions/boolean return value
// TODO ask for transient fields
// TODO usernames & file names
// TODO test

import testing.Event;

import java.io.*;

/* Serialization from examples in
   https://www.tutorialspoint.com/java/java_serialization.htm
 */
public class EventParser {
    private final String path;

    public EventParser(String path) {
        this.path = path;
    }

    public Event getEvent(String eventId) {
        try {
            String filePath = getFilePath(eventId);
            FileInputStream input = new FileInputStream(filePath);
            ObjectInputStream deserializer = new ObjectInputStream(input);

            Event event = (Event) deserializer.readObject();
            input.close();
            deserializer.close();
            return event;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveEvent(Event event) {
        try {
            String filePath = getFilePath(event.getEventId());
            FileOutputStream output = new FileOutputStream(filePath);
            ObjectOutputStream serializer = new ObjectOutputStream(output);

            serializer.writeObject(event);
            output.close();
            serializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createEvent(Event event) {
        String filePath = getFilePath(event.getEventId());
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveEvent(event);
    }

    private String getFilePath(String eventId) {
        return path + "/event-" + eventId + ".csv";
    }
}
