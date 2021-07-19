package team1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TemplateManager {
    /**
     * Manages templates in the system
     */
    // === Class Variables ===
    private static List<Template> templateList;
    // === Methods ===

    public TemplateManager() {
        templateList = new ArrayList<>();
    }

    public boolean editTemplateName(String templateId, String newName){
        // returns true if name is updated successfully
        return true;
    }

    public static List<Template> getTemplateList() {
        return templateList;
    }

    public List<FieldSpecs> getFieldSpecs(String templateId){
        return new ArrayList<>(); //placeholder
    }

    public static Template retrieveTemplateById(String templateName){
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
