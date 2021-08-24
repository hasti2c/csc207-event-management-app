package usecases;

import gateways.IGateway;
import entities.FieldSpecs;
import entities.Template;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TemplateManager {
    /**
     * Manages templates in the system
     */
    // === Class Variables ===
    private List<Template> templateList;
    private IGateway<Template> gateway;

    // === Methods ===

    /**
     * Initializes a TemplateManager object
     * @param gateway A gateway object of type IGateway<User> used to load data
     */
    public TemplateManager(IGateway<Template> gateway) {
        this.gateway = gateway;
        templateList = gateway.getAllElements();
    }

    /**
     * Edits template name
     *
     * @param templateName the name  of the template
     * @param newName new name of the template
     */
    public void editTemplateName(String templateName, String newName) {
        this.retrieveTemplateByName(templateName).setTemplateName(newName);
    }

    /**
     * Checks if template name does not already exist
     *
     * @param newName new name of the template that is being checked
     * @return true if no current template has name newName
     */
    public boolean checkNameUniqueness(String newName) {
        // Note: this will return false even if the new name is the same as the previous name.
        List<String> templateNames = new ArrayList<>();
        for (Template template : templateList) {
            templateNames.add(template.getTemplateName());
        }
        return !templateNames.contains(newName);
    }

    /**
     * Returns a List of current template names
     *
     * @return List of current template names
     */
    public List<String> returnTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        for (Template template : templateList) {
            templateNames.add(template.getTemplateName());
        }
        return templateNames;
    }

    /**
     * Gets Template object given its name
     *
     * @param templateName the name of the template
     * @return corresponding Template object
     */
    public Template retrieveTemplateByName(String templateName) {
        for (Template template : templateList) {
            if (template.getTemplateName().equals(templateName)) {
                return template;
            }
        }
        return null;
    }

    /**
     * Getter method
     *
     * @return templateList
     */
    public List<FieldSpecs> getFieldSpecs(String templateName) {
        return this.retrieveTemplateByName(templateName).getFieldDescriptions();
    }

    /**
     * Returns a List of FieldSpecs associated with the template
     *
     * @param templateName the name of the template
     * @return List of all the FieldSpecs that belong to the template
     */
    public List<String> getFieldNames(String templateName) {
        List<String> fieldNames = new ArrayList<>();
        for (FieldSpecs fieldspecs : getFieldSpecs(templateName)) {
            fieldNames.add(fieldspecs.getFieldName());
        }
        return fieldNames;
    }

    /**
     * Saves all templates
     */
    public void saveAllTemplates() {
        gateway.saveAllElements(templateList);
    }

    /**
     * Creates a Template
     *
     * @param templateName name of new Template
     */
    public void createTemplate(String templateName) {
        List<FieldSpecs> emptyFieldSpecs = new ArrayList<>();
        Template template = new Template(emptyFieldSpecs, templateName);
        templateList.add(template);
    }

    /**
     * Adds the new FieldSpecs Object and calls addFieldSpecs to add the FieldSpecs object to the template
     * @param templateName the name of the template
     * @param fieldName the name of the field
     * @param dataType the type of the field
     * @param isRequired is it required
     */
    public void addNewFieldSpecs(String templateName, String fieldName, String dataType, boolean isRequired) {
        FieldSpecs fieldSpecs = createNewFieldSpecs(fieldName, dataType, isRequired);
        addFieldSpecs(templateName, fieldSpecs);
    }

    /**
     * Creates FieldSpecs object
     *
     * @param fieldName  name of the field
     * @param dataType the type of the field
     * @param isRequired is it required
     * @return FieldSpecs object
     */
    private FieldSpecs createNewFieldSpecs(String fieldName, String dataType, boolean isRequired) {
        Class<?> convertedDataType = null;
        if (dataType.equalsIgnoreCase("string")) {
            convertedDataType = String.class;
        } else if (dataType.equalsIgnoreCase("boolean")) {
            convertedDataType = Boolean.class;
        } else if (dataType.equalsIgnoreCase("int")) {
            convertedDataType = Integer.class;
        } else if (dataType.equalsIgnoreCase("localdatetime")) {
            convertedDataType = LocalDateTime.class;
        }

        return new FieldSpecs(fieldName, convertedDataType, isRequired);
    }

    /**
     * Adds FieldSpecs object to a template
     *
     * @param templateName name of the Template
     * @param fieldSpecs the FieldSpecs object
     */
    public void addFieldSpecs(String templateName, FieldSpecs fieldSpecs) {
        for (Template template : templateList) {
            if (template.getTemplateName().equals(templateName)) {
                template.addFieldSpecs(fieldSpecs);
            }
        }
    }

    /**
     * Deletes template from templateList
     *
     * @param templateName name of Template
     */
    public void deleteTemplate(String templateName) {
        templateList.removeIf(template -> template.getTemplateName().equals(templateName));
    }

    /**
     * Removes FieldSpecs object from field descriptions of template
     *
     * @param templateName name of Template
     * @param fieldName name of field of FieldSpecs object
     */
    public void deleteFieldSpecs(String templateName, String fieldName) {
        for (Template template : templateList) {
            if (template.getTemplateName().equals(templateName)) {
                template.getFieldDescriptions().removeIf(fieldSpecs -> fieldSpecs.getFieldName().equals(fieldName));
            }
        }
    }
}


