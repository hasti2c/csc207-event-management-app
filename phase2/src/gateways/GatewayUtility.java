package gateways;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Singleton class that implements utility methods to be used by EntityGateways.
 */
public class GatewayUtility {
    private static GatewayUtility instance;

    private GatewayUtility() {}

    public static GatewayUtility getInstance() {
        if (instance == null)
            instance = new GatewayUtility();
        return instance;
    }

    /**
     * @return Simple GsonBuilder with custom serializer & deserializer for LocalDateTime & Class.
     */
    GsonBuilder getSimpleGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(Class.class, new ClassSerializer());
        gsonBuilder.registerTypeAdapter(Class.class, new ClassDeserializer());
        return gsonBuilder;
    }

    /**
     * Serializes LocalDateTime objects into json.
     * Implementation of JsonSerializer.
     */
    static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(localDateTime.toString());
        }
    }

    /**
     * Deserializes LocalDateTime objects from json.
     * Implementation of JsonDeserializer.
     */
    static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            String jsonString = jsonElement.getAsString();
            return LocalDateTime.parse(jsonString);
        }
    }

    /**
     * Serializes Class objects into json.
     * Implementation of JsonSerializer.
     */
    static class ClassSerializer implements JsonSerializer<Class<?>> {
        @Override
        public JsonElement serialize(Class aClass, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(aClass.getName());
        }
    }

    /**
     * Deserializes LocalDateTime objects from json.
     * Implementation of JsonDeserializer.
     */
    static class ClassDeserializer implements JsonDeserializer<Class<?>> {
        @Override
        public Class<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            String className = jsonElement.getAsString();
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
