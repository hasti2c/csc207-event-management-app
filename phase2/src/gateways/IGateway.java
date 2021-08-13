package gateways;

import java.util.List;
import java.util.Map;

// TODO class javadocs?
public interface IGateway <T> {
    /**
     * @return Returns the elements (read from json) in a list.
     */
    List<T> getAllElements();

    /**
     * @return Returns the elements (read from json) in a map, where each key is the id and the value is the element.
     */
    Map<String, T> getElementMap();

    /**
     * Saves updated version of all elements, and overrides the previous saved elements. Writes the saved versions to
     * the file.
     * @param elements List containing updated versions of elements.
     */
    void saveAllElements(List<T> elements);

    /**
     * Saves updated version of all elements, and overrides the previous saved elements. Writes the saved versions to
     * the file.
     * @param elements Map containing updated versions of elements, where each key is the id and the value is the
     *                 element.
     */
    void saveAllElements(Map<String, T> elements);
}
