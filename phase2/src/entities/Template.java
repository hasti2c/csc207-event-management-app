package entities;

import utility.Savable;

import java.util.List;
import java.util.*;
/**
 * Templates for the system
 */
public class Template implements Savable {
    // === Instance Variables ===
    private List<FieldSpecs> fieldDescriptions;
    // The type of template eg concert, fundraiser, garage sale...
    private String templateName;
    private String templateId;
    // stores the version number, each time the template is edited the version number increases by one. This way we can
    // store and access previous versions if necessary but only the version number will change. It's a string so that
    // it's easier to concatenate with the file name (not implemented for phase 1)

    // === Methods ===
    public Template(List<FieldSpecs> fieldDescriptions, String templateName) {
        this.fieldDescriptions = fieldDescriptions;
        this.templateName = templateName;
        this.templateId = UUID.randomUUID().toString();
    }
    public Template() {
    }

    // Getters

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

    // Setters

    /**
     * Sets templateName to new name.
     * @param templateName New templateName.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Adds a fieldSpecs to this template.
     * @param fieldSpecs The fieldspecs to be added.
     */
    public void addFieldSpecs(FieldSpecs fieldSpecs){
        this.fieldDescriptions.add(fieldSpecs);
    }

    @Override
    public String getID() {
        return templateId;
    }
}