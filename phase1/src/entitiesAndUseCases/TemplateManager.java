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
        // Check for uniqueness
        if ()
        }
        this.retrieveTemplateByName(templateName).setTemplateName(newName);
        return true;
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
//    public Template generateTemplateFromFile(String templateFileName) {
//        File file = new File(templateFileName);
//        if(file.exists()){
//            return new Template(templateFileName);
//        }
//        return null;
//    }
//    public Template createNewTemplate(String templateFileName) {
//        Template obj = new Template(templateFileName);
//        return obj;
//    }
}
