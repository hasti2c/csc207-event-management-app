package utility;

/**
 * Collection of two values.
 * @param <T> Type of first value.
 * @param <S> Type of second value.
 */
public class Pair <T, S> {
    private T first;
    private S second;

    /**
     * Creates an empty Pair.
     */
    public Pair() {}

    /**
     * Creates a pair with values.
     * @param first The first value.
     * @param second The second value.
     */
    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return First value in pair.
     */
    public T getFirst() {
        return first;
    }

    /**
     * Sets the first value of the pair.
     * @param first Value that should be first value of pair.
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * @return Second value in pair.
     */
    public S getSecond() {
        return second;
    }

    /**
     * Sets the second value of the pair.
     * @param second Value that should be second value of pair.
     */
    public void setSecond(S second) {
        this.second = second;
    }
}
