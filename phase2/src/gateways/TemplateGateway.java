package gateways;

import com.google.gson.*;
import entities.Template;

// TODO* Generics

// Data Types Accepted: primitives + String, LocalDateTime, List<E>, enums, Class<?>
// TODO javadocs: specify input for getter as precondition

public class TemplateGateway extends EntityGateway<Template> {
    public TemplateGateway(String path) {
        super(Template.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return GatewayUtility.getSimpleGsonBuilder();
    }

    @Override
    protected String getElementId(Template template) {
        return template.getTemplateId() + "-" + template.getFileVersionNumber();
    }
}
