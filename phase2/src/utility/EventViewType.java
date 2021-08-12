package utility;

public enum EventViewType {
    OWNED ("My Events"),
    ATTENDING ("Attending Events"),
    NOT_ATTENDING ("Not Attending Events"),
    PUBLISHED ("Published Events");

    private String name;

    EventViewType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
