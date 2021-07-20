package team2.gateway;

import com.google.gson.*;
import team1.Template;

// TODO Generics
// TODO correct list below
// Data Types Accepted: primitives + String, LocalDateTime, List<E>, enums

public class TemplateParser extends EntityParser<Template> {
    public TemplateParser(String path) {
        super(Template.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        JsonSerializer<Class<?>> serializer = (aClass, type, context) -> new JsonPrimitive(aClass.getName());
        gsonBuilder.registerTypeAdapter(Class.class, serializer);

        JsonDeserializer<Class<?>> deserializer = (jsonElement, type, context) -> {
            try {
                return Class.forName(jsonElement.getAsString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        };
        gsonBuilder.registerTypeAdapter(Class.class, deserializer);

        return gsonBuilder;
    }

    @Override
    protected String getElementId(Template template) {
        return template.getTemplateName();
    }
}
