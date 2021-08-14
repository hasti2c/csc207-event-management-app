package gateways;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import entities.Event;
import entities.User;
import entities.UserTypePermissions;
import utility.EventViewType;
import utility.UserViewType;
import utility.ViewType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class UserTypePermissionsGateway extends EntityGateway<UserTypePermissions> {
    /**
     * Constructs an UserTypePermissionsGateway Element.
     * @param path Path of relevant json file.
     */
    public UserTypePermissionsGateway(String path) {
        super(UserTypePermissions.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = GatewayUtility.getSimpleGsonBuilder();

        Type eventViewTypeClass = new TypeToken<ViewType<Event>>(){}.getType();
        gsonBuilder.registerTypeAdapter(eventViewTypeClass, new ViewTypeDeserializer<>(EventViewType.class));

        Type userViewTypeClass = new TypeToken<ViewType<User>>(){}.getType();
        gsonBuilder.registerTypeAdapter(userViewTypeClass, new ViewTypeDeserializer<>(UserViewType.class));

        return gsonBuilder;
    }

    @Override
    protected String getElementId(UserTypePermissions userTypePermissions) {
        return userTypePermissions.getUserType().toString();
    }

    private static class ViewTypeDeserializer<T> implements JsonDeserializer<ViewType<T>> {
        private final Class<? extends ViewType<T>> viewTypeClass;

        public ViewTypeDeserializer(Class<? extends ViewType<T>> viewTypeClass) {
            this.viewTypeClass = viewTypeClass;
        }

        @Override
        public ViewType<T> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String viewTypename = jsonElement.getAsString();
            return getEnumValue(viewTypename);
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
