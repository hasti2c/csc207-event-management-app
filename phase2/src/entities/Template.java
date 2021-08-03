package entities;

import java.util.List;

/**
 * Templates for the system
 */
public class Template {
    // === Instance Variables ===
    private List<FieldSpecs> fieldDescriptions;
    // The type of template eg concert, fundraiser, garage sale...
    private String templateName;
    private String templateId;
    // stores the version number, each time the template is edited the version number increases by one. This way we can
    // store and access previous versions if necessary but only the version number will change. It's a string so that
    // it's easier to concatenate with the file name (not implemented for phase 1)
    private int fileVersionNumber;

    // === Methods ===
    public Template(List<FieldSpecs> fieldDescriptions, String templateName) {
        this.fieldDescriptions = fieldDescriptions;
        this.templateName = templateName;
        this.templateId = UUID.randomUUID().toString();
        this.fileVersionNumber = 0;
    }
    public Template() {
    }

    // Getters

    /**
     * Gets the version number of the template
     * @return String, the version number as a string.
     */
    public int getFileVersionNumber() {
        return fileVersionNumber;
    }

    /**
     * Gets the name of the template.
     * @return String, the template's name
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Gets a list of field specifications for this template.
     * @return List<FieldSpecs> field spec objects.
     */
    public List<FieldSpecs> getFieldDescriptions() {
        return fieldDescriptions;
    }

    /**
     * gets the ID of the template
     * @return String template ID
     */
    public String getTemplateId() {
        return templateId;
    }

    // Setters

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

//    // Not for phase 1
//    public void setFileVersionNumber(String fileVersionNumber) {
//        this.fileVersionNumber = fileVersionNumber;
//    }
    // No setter for the list of field specs
}