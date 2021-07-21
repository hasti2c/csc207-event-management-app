package entitiesAndUseCases;

import java.util.ArrayList;
import java.util.List;

public class TemplateManager {
    /**
     * Manages templates in the system
     */
    // === Class Variables ===
    private List<Template> templateList;
    // === Methods ===

    public TemplateManager() {
        templateList = new ArrayList<>();
    }

    public boolean editTemplateName(String templateName, String newName){
        this.retrieveTemplateByName(templateName).setTemplateName(newName);
        return true;
    }

    public boolean checkNameUniqueness(String newName){
        // Note: this will return false even if the new name is the same as the previous name.
        List<String> templateNames = new ArrayList<>();
        for (Template template : templateList){
            templateNames.add(template.getTemplateName());
        }
        return !templateNames.contains(newName);
    }

    public List<Template> getTemplateList() {
        return templateList;
    }

    public List<FieldSpecs> getFieldSpecs(String templateName){
        return this.retrieveTemplateByName(templateName).getFieldDescriptions();
    }

    public Template retrieveTemplateByName(String templateName){
        List<Template> holderList = new ArrayList<>();
        for (Template template : templateList) {
            if (template.getTemplateName().equals(templateName)) {
                holderList.add(template);
            }
        }
        return holderList.remove(0);
    }
}
