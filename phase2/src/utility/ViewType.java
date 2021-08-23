package utility;

/**
 * A ViewType of elements of type T.
 * @param <T> Type of element that will be viewed.
 */
public interface ViewType <T extends Viewable> {
    /**
     * @return Name of ViewType.
     */
    String getName();
}
