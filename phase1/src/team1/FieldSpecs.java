package team1;

public class FieldSpecs {
    /**
     * Template for what needs to be included in a field in a template
     */
    // === Instance Variables ===
    private String fieldName;
    // private Class<?> dataType;
    private String dataType;
    private boolean required;
    // === Representation Invariants ===
    // === Methods ===
    public FieldSpecs(String fieldName, String dataType, boolean required) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this. required = required;
    }
    // Getters

    public String getFieldName() {
        return fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isRequired() {
        return required;
    }

    // Setters
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
