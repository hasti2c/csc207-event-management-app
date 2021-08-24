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

    /**
     * Creates a FieldSpecs object
     * @param fieldName the name of the field
     * @param dataType the data type of the field
     * @param required is it required
     */
    public FieldSpecs(String fieldName, Class<?> dataType, boolean required) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.required = required;
    }

    /**
     * Empty constructor of FieldSpecs
     */
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

    /**
     * Sets the field name
     * @param fieldName the new field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Sets the field data type
     * @param dataType the new data type
     */
    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }

    /**
     * Sets if the field is required
     * @param required is it required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
}



