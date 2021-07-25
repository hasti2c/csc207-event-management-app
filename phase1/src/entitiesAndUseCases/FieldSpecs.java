package entitiesAndUseCases;

public class FieldSpecs {
    /**
     * Template for what needs to be included in a field in a template
     */
    // === Instance Variables ===
    private String fieldName;
    private Class<?> dataType;
    private boolean required;
    // === Representation Invariants ===
    // === Methods ===
    public FieldSpecs(String fieldName, Class<?> dataType, boolean required) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.required = required;
    }

    public FieldSpecs() {

    }

    // Getters

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public boolean isRequired() {
        return required;
    }


    // Setters
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
