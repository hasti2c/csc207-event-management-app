package gateways;

import com.google.gson.GsonBuilder;
import entities.User;

/**
 * Gateway that saves & reads Users to & from a json file.
 */
public class UserGateway extends EntityGateway<User> {
    /**
     * Constructs an UserGateway Element.
     * @param path Path of relevant json file.
     */
    public UserGateway(String path) {
        super(User.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return GatewayUtility.getInstance().getSimpleGsonBuilder();
    }
}

