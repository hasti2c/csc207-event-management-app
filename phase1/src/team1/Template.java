package team1;

import java.util.List;

public class Template {  //are we allowed to have multiple genres as templates like bbq, concerts? yes
    /**
     * Templates
     */
    // === Instance Variables ===
    private List<FieldSpecs> fieldDescriptions;
    // The type of template eg concert, fundraiser, garage sale... this will also act as the file name. Template name
    // must be unique... we might need to fix this later this might cause issues
    private String templateName;
    // stores the version number, each time the template is edited the version number increases by one. This way we can
    // store and access previous versions if necessary but only the version number will change. It's a string so that
    // it's easier to concatenate with the file name
    private String fileVersionNumber;

    // === Methods ===
    public Template(String templateName) {
        this.templateName = templateName;
        this.fileVersionNumber = "1";
    }

    // Getters
    public String getFileVersionNumber() {
        return fileVersionNumber;
    }

    public String getTemplateName() {
        return templateName;
    }
    // not sure about this one
    public List<FieldSpecs> getFieldDescriptions() {
        return fieldDescriptions;
    }
    // Setters

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setFileVersionNumber(String fileVersionNumber) {
        this.fileVersionNumber = fileVersionNumber;
    }
    // No setter for the list of field specs
}