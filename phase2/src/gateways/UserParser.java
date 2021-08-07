package gateways;

import com.google.gson.GsonBuilder;
import entities.User;

// TODO LocalDateTime serializer & deserializer

public class UserParser extends EntityParser<User> {
    public UserParser(String path) {
        super(User.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return new GsonBuilder().setPrettyPrinting();
    }

    @Override
    protected String getElementId(User user) {
        return user.getUsername();
    }
}

