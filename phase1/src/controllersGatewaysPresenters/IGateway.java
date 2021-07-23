package controllersGatewaysPresenters;

import java.util.List;

public interface IGateway <T> {
    T getElement(String elementId);
    void saveElement(T element);
    List<T> getAllElements();
    void saveAllElements(List<T> elements);
    void deleteElement(T element);
    void deleteElement(String elementId);
}
