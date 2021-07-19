package team2;

// TODO saveAll / getAll
// TODO remove create
public interface IGateway <T> {
    T getElement(String elementId);
    void saveElement(T element);
    void createElement(T element);
}
