package team1.angela;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Template {

    // === Instance Variables ===
    private List<FieldSpecs> fieldDescriptions;
//    // A map where the key is the field name and the value is the data type of the field.
//    Map<String, String> fieldDescription;
    // The type of template eg concert, fundraiser, garage sale... this will also act as the file name
    private String templateName;
//    // stores the path to the file
//    private String templateFileName;
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
