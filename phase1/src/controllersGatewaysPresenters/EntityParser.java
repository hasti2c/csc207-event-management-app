package controllersGatewaysPresenters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

// TODO exceptions
// TODO test

public abstract class EntityParser <T> {
    private final Class<T> dataType;
    private final String path;
    private final Gson gson;
    private Map<String, T> elements;

    public EntityParser(Class<T> dataType, String path) {
        this.dataType = dataType;
        this.path = path;
        gson = getGsonBuilder().create();
        readElements();
    }

    public T getElement(String elementId) {
        return elements.get(elementId);
    }

    public void saveElement(T element) {
        elements.replace(getElementId(element), element);
        writeElements();
    }

    public void createElement(T element) {
        elements.put(getElementId(element), element);
        writeElements();
    }

    private void readElements() {
        try {
            FileReader fileReader = new FileReader(path);
            Class<T[]> arrayType = (Class<T[]>) Array.newInstance(dataType, 0).getClass();
            T[] elements = gson.fromJson(fileReader, arrayType);
            fileReader.close();

            this.elements = new HashMap<>();
            for (T element: elements)
                this.elements.put(getElementId(element), element);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeElements() {
        try {
            T[] emptyArray = (T[]) Array.newInstance(dataType);
            T[] elements = this.elements.values().toArray(emptyArray);

            FileWriter fileWriter = new FileWriter(path);
            gson.toJson(elements, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract GsonBuilder getGsonBuilder();

    protected abstract String getElementId(T element);
}
