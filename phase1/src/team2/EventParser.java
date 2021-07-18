package team2;

// TODO exceptions
// TODO test

import com.google.gson.*;
import javafx.util.Pair;
import testing.Event;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class EventParser {
    private final String path;
    private final Gson gson;
    private Map<String, Event> events;

    public EventParser(String path) {
        this.path = path;
        gson = getGsonBuilder().create();
        readEvents();
    }

    public Event getEvent(String eventId) {
        return events.get(eventId);
    }

    public void saveEvent(Event event) {
        events.replace(event.getEventId(), event);
        writeEvents();
    }

    public void createEvent(Event event) {
        events.put(event.getEventId(), event);
        writeEvents();
    }

    private void readEvents() {
        try {
            FileReader fileReader = new FileReader(path);
            Event[] events = gson.fromJson(fileReader, Event[].class);
            fileReader.close();

            this.events = new HashMap<>();
            for (Event event: events)
                this.events.put(event.getEventId(), event);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void writeEvents() {
        try {
            Event[] events = this.events.values().toArray(new Event[0]);

            FileWriter fileWriter = new FileWriter(path);
            gson.toJson(events, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Event.class, new EventSerializer());
        gsonBuilder.registerTypeAdapter(Event.class, new EventDeserializer());
        return gsonBuilder;
    }

    // TODO source https://futurestud.io/tutorials/gson-advanced-custom-serialization-part-1
    private static class EventSerializer implements JsonSerializer<Event> {
        @Override
        public JsonElement serialize(Event event, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            addPrimitives(event, json);
            addDates(event, json);
            addFields(event, json);
            return json;
        }

        private void addPrimitives(Event event, JsonObject json) {
            json.addProperty("eventId", event.getEventId());
            json.addProperty("published", event.isPublished());
            json.addProperty("eventOwner", event.getEventOwner());
            json.addProperty("eventType", event.getEventType());
            json.addProperty("maxAttendees", event.getMaxAttendees());
            json.addProperty("numAttendees", event.getNumAttendees());
        }

        private void addDates(Event event, JsonObject json) {
            json.addProperty("createdTime", event.getCreatedTime().toString());
            json.addProperty("editTime", event.getEditTime().toString());
        }

        private void addFields(Event event, JsonObject json) {
            Map<String, Class<?>> templateFieldSpec = event.getTemplateFieldSpec();
            Map<String, Object> eventDetails = event.getEventDetails();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonObject fields = new JsonObject();
            for (String fieldName: templateFieldSpec.keySet()) {
                String className = templateFieldSpec.get(fieldName).getName();
                String value = gson.toJson(eventDetails.get(fieldName));

                JsonArray array = new JsonArray();
                array.add(className);
                array.add(value);

                fields.add(fieldName, array);
            }

            json.add("fields", fields);
        }
    }

    // TODO source https://futurestud.io/tutorials/gson-advanced-custom-deserialization-basics
    private static class EventDeserializer implements JsonDeserializer<Event> {
        @Override
        public Event deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject json = jsonElement.getAsJsonObject();
            Map<String, Object> values = new HashMap<>();
            getPrimitives(json, values);
            getDates(json, values);
            getFields(json, values);
            return populateEvent(values);
        }

        private void getPrimitives(JsonObject json, Map<String, Object> values) {
            values.put("eventId", json.get("eventId").getAsString());
            values.put("published", json.get("published").getAsBoolean());
            values.put("eventOwner", json.get("eventOwner").getAsString());
            values.put("eventType", json.get("eventType").getAsString());
            values.put("maxAttendees", json.get("maxAttendees").getAsInt());
            values.put("numAttendees", json.get("numAttendees").getAsInt());
        }

        private void getDates(JsonObject json, Map<String, Object> values) {
            LocalDateTime createdTime = LocalDateTime.parse(json.get("createdTime").getAsString());
            values.put("createdTime", createdTime);
            LocalDateTime editTime = LocalDateTime.parse(json.get("editTime").getAsString());
            values.put("editTime", editTime);
        }

        private void getFields(JsonObject json, Map<String, Object> values) {
            Map<String, Class<?>> templateFieldSpec = new HashMap<>();
            Map<String, Object> eventDetails = new HashMap<>();

            JsonObject fields = json.get("fields").getAsJsonObject();
            for (String fieldName: fields.keySet()) {
                JsonArray fieldData = fields.get(fieldName).getAsJsonArray();
                Pair<Class<?>, Object> field = getField(fieldData);
                templateFieldSpec.put(fieldName, field.getKey());
                eventDetails.put(fieldName, field.getValue());
            }

            values.put("templateFieldSpec", templateFieldSpec);
            values.put("eventDetails", eventDetails);
        }

        private Pair<Class<?>, Object> getField(JsonArray fieldData) {
            try {
                String className = fieldData.get(0).getAsString();
                Class<?> dataType = Class.forName(className);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String valueJson = fieldData.get(1).getAsString();
                Object value = gson.fromJson(valueJson, dataType);

                return new Pair<>(dataType, value);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        private Event populateEvent(Map<String, Object> values) {
            return new Event((String) values.get("eventId"), (boolean) values.get("published"),
                    (LocalDateTime) values.get("createdTime"), (LocalDateTime) values.get("editTime"),
                    (String) values.get("eventOwner"), (Map<String, Class<?>>) values.get("templateFieldSpec"),
                    (Map<String, Object>) values.get("eventDetails"), (int) values.get("maxAttendees"),
                    (int) values.get("numAttendees"), (String) values.get("eventType"));
        }
    }
}
