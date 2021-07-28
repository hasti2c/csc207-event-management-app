package entities;

/**
 * Template for what needs to be included in a field in a template
 */
public class FieldSpecs {
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

    /**
     * Gets the name of the field
     * @return String, field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets the datatype of the field
     * @return Class<?> the data type class
     */
    public Class<?> getDataType() {
        return dataType;
    }

    /**
     * Gets whether the field is required or not.
     * @return boolean, field is required if true.
     */
    public boolean isRequired() {
        return required;
    }


    // Setters (not needed for phase 1)
//    public void setFieldName(String fieldName) {
//        this.fieldName = fieldName;
//    }
//
//    public void setDataType(Class<?> dataType) {
//        this.dataType = dataType;
//    }
//
//    public void setRequired(boolean required) {
//        this.required = required;
//    }
}
