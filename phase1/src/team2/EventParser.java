package team2;

import team1.angela.Event;

import java.io.*;

// TODO source https://www.tutorialspoint.com/java/java_serialization.htm
// TODO exceptions
// TODO boolean return value
// TODO ask for transient fields
public class EventParser {
    private final String path;

    public EventParser(String path) {
        this.path = path;
    }

    public Event getEvent(int eventId) {
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
            String filePath = getFilePath(event.getEventID());
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
        String filePath = getFilePath(event.getEventID());
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveEvent(event);
    }

    private String getFilePath(int eventId) {
        // TODO eventID int or string?
        return path + "/event-" + eventId + ".ser";
    }
}
