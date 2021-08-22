package gateways;

import com.google.gson.*;
import entities.Template;

// Data Types Accepted: primitives + String, LocalDateTime, List<E>, enums, Class<?>

public class TemplateGateway extends EntityGateway<Template> {
    /**
     * Constructs an TemplateGateway Element.
     * @param path Path of relevant json file.
     */
    public TemplateGateway(String path) {
        super(Template.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return GatewayUtility.getSimpleGsonBuilder();
    }

    @Override
    protected String getElementId(Template template) {
        return template.getTemplateId();
    }
}
