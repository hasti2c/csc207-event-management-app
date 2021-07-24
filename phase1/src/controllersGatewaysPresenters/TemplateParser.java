package controllersGatewaysPresenters;

import com.google.gson.*;
import entitiesAndUseCases.Template;

// TODO* Generics

// TODO correct list below
// Data Types Accepted: primitives + String, LocalDateTime, List<E>, enums, Class<?>
// TODO specify input for getter as precondition
// TODO since template doesn't use Class<?> anymore now it's extra

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
        return template.getTemplateId() + "-" + template.getFileVersionNumber();
    }
}
