package controllersGatewaysPresenters;

import java.util.List;

// TODO save all?
// TODO delete

public interface IGateway <T> {
    T getElement(String elementId);
    void saveElement(T element);
    List<T> getAllElements();
    void saveAllElements(List<T> elements);
}
