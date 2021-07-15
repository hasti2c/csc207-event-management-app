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

    public boolean editTemplateName(String templateName, String newName){
        // returns true if name is updated successfully
        return true;
    }

    public static List<Template> getTemplateList() {
        return templateList;
    }

    public List<FieldSpecs> getFieldSpecs(String templateName){
        return new ArrayList<>(); //placeholder
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
