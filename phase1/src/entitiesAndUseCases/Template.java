package entitiesAndUseCases;

import java.io.Serializable;
import java.util.List;

public class Template {
    /**
     * Templates
     */
    // === Instance Variables ===
    private List<FieldSpecs> fieldDescriptions;
    // The type of template eg concert, fundraiser, garage sale...
    private String templateName;
    private String templateId;
    // stores the version number, each time the template is edited the version number increases by one. This way we can
    // store and access previous versions if necessary but only the version number will change. It's a string so that
    // it's easier to concatenate with the file name
    private String fileVersionNumber;

    // === Methods ===
    public Template(List<FieldSpecs> fieldDescriptions, String templateName, String templateId, String fileVersionNumber) {
        this.fieldDescriptions = fieldDescriptions;
        this.templateName = templateName;
        this.templateId = templateId;
        this.fileVersionNumber = fileVersionNumber;
    }
    public Template() {
    }

    // Getters
    public String getFileVersionNumber() {
        return fileVersionNumber;
    }

    public String getTemplateName() {
        return templateName;
    }

    public List<FieldSpecs> getFieldDescriptions() {
        return fieldDescriptions;
    }

    public String getTemplateId() {
        return templateId;
    }

    // Setters

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setFileVersionNumber(String fileVersionNumber) {
        this.fileVersionNumber = fileVersionNumber;
    }
    // No setter for the list of field specs
}