package team2;

import java.util.ArrayList;
import java.util.List;

public class Serializer {
    static String[] serializeArray(Object[] data) {
        String[] ret = new String[data.length];
        for (int i = 0; i < data.length; i++)
            ret[i] = serializeValue(data[i]);
        return ret;
    }

    static String serializeValue(Object data) {
        // TODO date & class & maps
        if (data instanceof Integer n) {
            return n + "";
        } else if (data instanceof Boolean b) {
            return b ? "true" : "false";
        } else if (data instanceof String s) {
            return s;
        } else if (data instanceof List list) {
            ArrayList<String> ret = new ArrayList<>();
            for (Object obj: list)
                ret.add(serializeValue(obj));
            return ret.toString();
        } else {
            throw new IllegalArgumentException(); // TODO
        }
    }

    static Object[] deserializeArray(Class<?>[] classes, String[] line) {
        Object[] ret = new Object[line.length];
        for (int i = 0; i < line.length; i++)
            ret[i] = deserializeValue(classes[i], line[i]);
        return ret;
    }

    static Object deserializeValue(Class<?> cls, String data) {
        // TODO
        return null;
    }
}
