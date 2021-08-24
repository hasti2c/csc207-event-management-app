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

    /**
     * Create a Template
     * @param fieldDescriptions The list of FieldSpecs objects that belong to this template
     * @param templateName The name of this template
     */
    public Template(List<FieldSpecs> fieldDescriptions, String templateName) {
        this.fieldDescriptions = fieldDescriptions;
        this.templateName = templateName;
        this.templateId = UUID.randomUUID().toString();
    }
    // Empty Constructor
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
     * Sets the name of the Template
     * @param templateName The new name for template
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Adds the FieldSpecs object to the list of FieldSpecs in Template
     * @param fieldSpecs The FieldSpecs object being added into the the list of FieldSpecs in template
     */
    public void addFieldSpecs(FieldSpecs fieldSpecs){
        this.fieldDescriptions.add(fieldSpecs);
    }

    @Override
    public String getID() {
        return templateId;
    }
}