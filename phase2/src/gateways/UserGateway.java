package gateways;

import com.google.gson.GsonBuilder;
import entities.User;

public class UserGateway extends EntityGateway<User> {
    public UserGateway(String path) {
        super(User.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return GatewayUtility.getSimpleGsonBuilder();
    }

    @Override
    protected String getElementId(User user) {
        return user.getUsername();
    }
}

