package team2.gateway;

import java.util.List;

// TODO save all?
// TODO delete

public interface IGateway <T> {
    T getElement(String elementId);
    void saveElement(T element);
    List<T> getAllElements();
}
