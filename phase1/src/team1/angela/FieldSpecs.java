package team1.angela;

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
}
