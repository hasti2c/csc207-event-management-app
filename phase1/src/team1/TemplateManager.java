package team1;

import java.io.File;
import java.util.List;

public class TemplateManager {
    /**
     * Manages templates in the system
     */
    // === Class Variables ===
    private static List<Template> templateList;
    // === Methods ===

    public TemplateManager() {

    }

    public boolean editTemplateName(String templateName, String newName){
        // returns true if name is updated successfully
        return true;
    }
    public Template generateTemplateFromFile(String templateFileName) {
        File file = new File(templateFileName);
        if(file.exists()){
            return new Template(templateFileName);
        }
        return null;
    }
    public Template createNewTemplate(String templateFileName) {
        Template obj = new Template(templateFileName);
        return obj;
    }
}
