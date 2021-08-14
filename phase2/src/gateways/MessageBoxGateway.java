package gateways;

import com.google.gson.GsonBuilder;
import entities.MessageBox;

public class MessageBoxGateway extends EntityGateway<MessageBox> {
    public MessageBoxGateway(String path) {
        super(MessageBox.class, path);
    }

    @Override
    protected GsonBuilder getGsonBuilder() {
        return GatewayUtility.getSimpleGsonBuilder();
    }

    @Override
    protected String getElementId(MessageBox messageBox) {
        return messageBox.getOwner();
    }
}
