package controllersGatewaysPresenters;

import com.google.gson.*;


import entitiesAndUseCases.Event;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

// TODO* optional field specs (null value)
// TODO* field specs have changed in Event

// TODO make field reading & writing more general
// TODO local date time serializer

public class EventParser extends EntityParser<Event> {
    public EventParser(String path) {
        super(Event.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return null;
    }

    @Override
    protected String getElementId(Event element) {
        return null;
    }

    /*
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

        // TODO update fieldSpecs
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
            Map<String, Class<?>> templateFieldSpec = new HashMap<>();
            Map<String, Object> eventDetails = new HashMap<>();

            JsonObject fields = json.get("fields").getAsJsonObject();
            for (String fieldName: fields.keySet()) {
                JsonArray fieldData = fields.get(fieldName).getAsJsonArray();
                Pair<Class<?>, Object> field = getProperty(fieldData);
                templateFieldSpec.put(fieldName, field.getKey());
                eventDetails.put(fieldName, field.getValue());
            }

            setField(event, "templateFieldSpec", templateFieldSpec);
            setField(event, "eventDetails", eventDetails);
        }

        private Pair<Class<?>, Object> getProperty(JsonArray fieldData) {
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
    // */

}
