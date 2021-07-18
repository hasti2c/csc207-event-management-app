package team2;

import com.google.gson.*;
import team1.angela.Template;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateParser {
    private final String path;
    private final Gson gson;
    private Map<String, Template> templates;

    public TemplateParser(String path) {
        this.path = path;
        gson = getGsonBuilder().create();
        readTemplates();
    }

    public Template getTemplate (String templateName) {
        return templates.get(templateName);
    }

    public void saveTemplate(Template template) {
        templates.replace(template.getTemplateName(), template);
        writeTemplates();
    }

    public void createEvent(Template template) {
        templates.put(template.getTemplateName(), template);
        writeTemplates();
    }

    private void readTemplates() {
        try {
            FileReader fileReader = new FileReader(path);
            Template[] templates = gson.fromJson(fileReader, Template[].class);
            fileReader.close();

            this.templates = new HashMap<>();
            for (Template template: templates)
                this.templates.put(template.getTemplateName(), template);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void writeTemplates() {
        try {
            Template[] templates = this.templates.values().toArray(new Template[0]);

            FileWriter fileWriter = new FileWriter(path);
            gson.toJson(templates, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GsonBuilder getGsonBuilder() {
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
}
