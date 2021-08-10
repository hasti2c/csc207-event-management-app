package gateways;

import java.util.List;
import java.util.Map;

// TODO get element map
// TODO menu files

public interface IGateway <T> {
    List<T> getAllElements();
    Map<String, T> getElementMap();
    void saveAllElements(List<T> elements);
    void saveAllElements(Map<String, T> elements);
}
