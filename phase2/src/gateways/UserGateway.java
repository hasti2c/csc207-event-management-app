package gateways;

import com.google.gson.GsonBuilder;
import entities.User;

import java.time.LocalDateTime;

public class UserGateway extends EntityGateway<User> {
    public UserGateway(String path) {
        super(User.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new GatewayUtility.LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new GatewayUtility.LocalDateTimeDeserializer());
        return gsonBuilder;
    }

    @Override
    protected String getElementId(User user) {
        return user.getUsername();
    }
}

