<<<<<<< HEAD:phase1/src/team2/gateway/UserParser.java
package team2.gateway;
=======
package controllersGatewaysPresenters;
>>>>>>> testing:phase1/src/controllersGatewaysPresenters/UserParser.java

import com.google.gson.GsonBuilder;
import entitiesAndUseCases.User;

// TODO hash passwords
// TODO ask for enum UserType to be transient
// TODO loggedIn probably transient

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

