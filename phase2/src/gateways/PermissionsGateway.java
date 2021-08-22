package gateways;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entities.Event;
import entities.User;
import entities.Permissions;
import utility.EventViewType;
import utility.UserViewType;
import utility.ViewType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class PermissionsGateway extends EntityGateway<Permissions> {
    /**
     * Constructs a PermissionsGateway Element.
     * @param path Path of relevant json file.
     */
    public PermissionsGateway(String path) {
        super(Permissions.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = GatewayUtility.getSimpleGsonBuilder();

        Type eventViewTypeClass = new TypeToken<ViewType<Event>>(){}.getType();
        gsonBuilder.registerTypeAdapter(eventViewTypeClass, new ViewTypeSerializer<>());
        gsonBuilder.registerTypeAdapter(eventViewTypeClass, new ViewTypeDeserializer<>(EventViewType.class));

        Type userViewTypeClass = new TypeToken<ViewType<User>>(){}.getType();
        gsonBuilder.registerTypeAdapter(userViewTypeClass, new ViewTypeSerializer<>());
        gsonBuilder.registerTypeAdapter(userViewTypeClass, new ViewTypeDeserializer<>(UserViewType.class));

        return gsonBuilder;
    }

    @Override
    protected String getElementId(Permissions permissions) {
        return permissions.getUserType().toString();
    }


    private static class ViewTypeSerializer<T> implements JsonSerializer<ViewType<T>> {
        @Override
        public JsonElement serialize(ViewType<T> src, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    private static class ViewTypeDeserializer<T> implements JsonDeserializer<ViewType<T>> {
        private final Class<? extends ViewType<T>> viewTypeClass;

        public ViewTypeDeserializer(Class<? extends ViewType<T>> viewTypeClass) {
            this.viewTypeClass = viewTypeClass;
        }

        @Override
        public ViewType<T> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String viewTypeName = jsonElement.getAsString();
            return getEnumValue(viewTypeName);
        }

        private ViewType<T> getEnumValue(String name) {
            assert viewTypeClass.isEnum();
            try {
                Method method = viewTypeClass.getMethod("valueOf", String.class);
                return (ViewType<T>) method.invoke(null, name);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}