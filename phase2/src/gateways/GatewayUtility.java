package gateways;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class GatewayUtility {
    static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(localDateTime.toString());
        }
    }

    static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            String jsonString = jsonElement.getAsString();
            return LocalDateTime.parse(jsonString);
        }
    }

    static class ClassSerializer implements JsonSerializer<Class<?>> {
        @Override
        public JsonElement serialize(Class aClass, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(aClass.getName());
        }
    }

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