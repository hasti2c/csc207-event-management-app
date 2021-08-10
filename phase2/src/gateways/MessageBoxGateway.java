package gateways;

import com.google.gson.GsonBuilder;
import entities.MessageBox;

import java.time.LocalDateTime;

import static gateways.GatewayUtility.*;

public class MessageBoxGateway extends EntityGateway<MessageBox> {
    public MessageBoxGateway(String path) {
        super(MessageBox.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        return gsonBuilder;
    }

    @Override
    protected String getElementId(MessageBox messageBox) {
        return messageBox.getOwner();
    }
}
