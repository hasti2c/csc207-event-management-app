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
    public TemplateManager(IGateway<Template> gateway) {
        this.gateway = gateway;
        templateList = gateway.getAllElements();
    }
    
    /**
     * Edit template name
     * @param templateName the name  of the template
     * @param newName new name of the template
     * @return true if edit successful
     */
    public boolean editTemplateName(String templateName, String newName){
        this.retrieveTemplateByName(templateName).setTemplateName(newName);
        return true;
    }

    /**
     * Check if template name does not already exist
     * @param newName new name of the template that is being checked
     * @return true if no current template has name newName
     */
    public boolean checkNameUniqueness(String newName){
        // Note: this will return false even if the new name is the same as the previous name.
        List<String> templateNames = new ArrayList<>();
        for (Template template : templateList){
            templateNames.add(template.getTemplateName());
        }
        return !templateNames.contains(newName);
    }

    /**
     * Return a List of current template names
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
     * Get Template object given its name
     * @param templateName the name  of the template
     * @return corresponding Template object
     */
    public Template retrieveTemplateByName(String templateName){
        List<Template> holderList = new ArrayList<>();
        for (Template template : templateList) {
            if (template.getTemplateName().equals(templateName)) {
                holderList.add(template);
            }
        }
        return holderList.remove(0);
    }

    /**
     * Getter method
     * @return templateList
     */
    public List<Template> getTemplateList() {
        return templateList;
    }

    /**
     * Getter method
     * @return templateList
     */
    public List<FieldSpecs> getFieldSpecs(String templateName){
        return this.retrieveTemplateByName(templateName).getFieldDescriptions();
    }

    /**
     * Save all templates
     */
    public void saveAllTemplates() {
        gateway.saveAllElements(templateList);
    }

    /**
     * Create a Template
     * @param templateName name of new Template
     * @return Name of created Template
     */
    public String createTemplate(String templateName){
        List<FieldSpecs> emptyFieldSpecs = new ArrayList<>();
        Template template = new Template(emptyFieldSpecs, templateName);
        return template.getTemplateName();
    }

    /**
     * Create FieldSpecs object
     * @param inputFieldName name of the field
     * @param inputDataType the type of the field
     * @param inputIsRequired is it required
     * @return FieldSpecs object
     */
    public FieldSpecs createNewFieldSpecs(String inputFieldName, String inputDataType, String inputIsRequired) {
        Class<?> convertedDataType = null;
        boolean convertedIsRequired = false;
        if (inputDataType.equalsIgnoreCase("string")){
            convertedDataType = String.class;
        }
        else if (inputDataType.equalsIgnoreCase("boolean")){
            convertedDataType = Boolean.class;
        }
        else if (inputDataType.equalsIgnoreCase("int")){
            convertedDataType = Integer.class;
        }
        else if (inputDataType.equalsIgnoreCase("localdatetime")){
            convertedDataType = LocalDateTime.class;
        }

        if (inputIsRequired.equalsIgnoreCase("yes")){
            convertedIsRequired = true;
        }

        return new FieldSpecs(inputFieldName, convertedDataType, convertedIsRequired);
    }

    /**
     * Add FieldSpecs object to a Template
     * @param templateName name of the Template
     * @param fieldSpecs the FieldSpecs object
     */
    public void addFieldSpecs(String templateName, FieldSpecs fieldSpecs){
        for (Template template: templateList){
            if(template.getTemplateName().equals(templateName)){
                template.addFieldSpecs(fieldSpecs);
            }
        }
    }

    /**
     * Delete Template from templateList
     * @param templateName name of Template
     */
    public void deleteTemplate(String templateName){
        for (Template template: templateList){
            if(template.getTemplateName().equals(templateName)){
                templateList.remove(retrieveTemplateByName(templateName));
            }
        }
    }
    
    /**
     * Remove FieldSpecs object from field descriptions of Template
     * @param templateName name of Template
     * @param fieldName name of field of FieldSpecs object
     */
    public void deleteFieldSpecs(String templateName, String fieldName){
        for (Template template: templateList){
            if(template.getTemplateName().equals(templateName)){
                for (FieldSpecs fieldSpecs: template.getFieldDescriptions()){
                    if (fieldSpecs.getFieldName().equals(fieldName)){
                        template.getFieldDescriptions().remove(fieldSpecs);
                    }
                }
            }
        }
    }
}
