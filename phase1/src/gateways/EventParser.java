package gateways;

import com.google.gson.*;
import utility.Pair;
import entities.Event;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class EventParser extends EntityParser<Event> {
    public EventParser(String path) {
        super(Event.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Event.class, new EventSerializer());
        gsonBuilder.registerTypeAdapter(Event.class, new EventDeserializer());
        return gsonBuilder;
    }

    @Override
    protected String getElementId(Event event) {
        return event.getEventId();
    }

    // source https://futurestud.io/tutorials/gson-advanced-custom-serialization-part-1
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
            json.addProperty("templateId", event.getTemplateId());
            json.addProperty("templateVersion", event.getTemplateVersion());
            json.addProperty("numAttendees", event.getNumAttendees());
        }

        private void addDates(Event event, JsonObject json) {
            json.addProperty("createdTime", event.getCreatedTime().toString());
            json.addProperty("editTime", event.getEditTime().toString());
        }

        private void addFields(Event event, JsonObject json) {
            Map<String, Pair<Class<?>, Boolean>> fieldNameAndFieldSpecs = event.getFieldNameAndFieldSpecsMap();
            Map<String, Object> eventDetails = event.getEventDetails();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonObject fields = new JsonObject();
            for (String fieldName: fieldNameAndFieldSpecs.keySet()) {
                Pair<Class<?>, Boolean> fieldData = fieldNameAndFieldSpecs.get(fieldName);
                String className = fieldData.getFirst().getName();
                Boolean required = fieldData.getSecond();
                String value = gson.toJson(eventDetails.get(fieldName));

                JsonArray array = new JsonArray();
                array.add(className);
                array.add(required);
                array.add(value);

                fields.add(fieldName, array);
            }

            json.add("fields", fields);
        }
    }

    // source https://futurestud.io/tutorials/gson-advanced-custom-deserialization-basics
    private static class EventDeserializer implements JsonDeserializer<Event> {
        @Override
        public Event deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject json = jsonElement.getAsJsonObject();
            Event event = new Event();
            getPrimitives(json, event);
            getDates(json, event);
            getFields(json, event);
            return event;
        }

        private void getPrimitives(JsonObject json, Event event) {
            setField(event, "eventId", json.get("eventId").getAsString());
            setField(event, "published", json.get("published").getAsBoolean());
            setField(event, "eventOwner", json.get("eventOwner").getAsString());
            setField(event, "eventType", json.get("eventType").getAsString());
            setField(event, "templateId", json.get("templateId").getAsString());
            setField(event, "templateVersion", json.get("templateVersion").getAsString());
            setField(event, "numAttendees", json.get("numAttendees").getAsInt());
        }

        private void getDates(JsonObject json, Event event) {
            LocalDateTime createdTime = LocalDateTime.parse(json.get("createdTime").getAsString());
            setField(event, "createdTime", createdTime);
            LocalDateTime editTime = LocalDateTime.parse(json.get("editTime").getAsString());
            setField(event, "editTime", editTime);
        }

        private void getFields(JsonObject json, Event event) {
            Map<String, Pair<Class<?>, Boolean>> fieldNameAndTypeMap = new HashMap<>();
            Map<String, Object> eventDetails = new HashMap<>();

            JsonObject fields = json.get("fields").getAsJsonObject();
            for (String fieldName: fields.keySet()) {
                JsonArray fieldData = fields.get(fieldName).getAsJsonArray();
                Pair<Pair<Class<?>, Boolean>, Object> field = getProperty(fieldData);
                fieldNameAndTypeMap.put(fieldName, field.getFirst());
                eventDetails.put(fieldName, field.getSecond());
            }

            setField(event, "fieldNameAndFieldSpecs", fieldNameAndTypeMap);
            setField(event, "eventDetails", eventDetails);
        }

        private Pair<Pair<Class<?>, Boolean>, Object> getProperty(JsonArray fieldData) {
            try {
                Class<?> dataType = Class.forName(fieldData.get(0).getAsString());
                Boolean required = fieldData.get(1).getAsBoolean();
                Pair<Class<?>, Boolean> typeAndRequired = new Pair<>(dataType, required);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String valueJson = fieldData.get(2).getAsString();
                Object value = gson.fromJson(valueJson, dataType);

                return new Pair<>(typeAndRequired, value);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        private <T> void setField(Event event, String fieldName, T value) {
            try {
                Field field = Event.class.getDeclaredField(fieldName);
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(event, value);
                field.setAccessible(accessible);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
