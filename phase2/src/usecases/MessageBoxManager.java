package usecases;

import entities.Message;
import entities.MessageBox;

import java.util.ArrayList;
import java.util.List;

public class MessageBoxManager {
    private final List<MessageBox> messageBoxList;

    public MessageBoxManager() {
        messageBoxList = new ArrayList<>();
    }

    /**
     * Given String username, return List<Message> representing the messages in that user's MessageBox.
     * If such a user does not exist, or they do not have a MessageBox, then return an empty List.
     * @param username String username
     * @return List<Message> representing the messages in that user's MessageBox
     */
    public List<Message> getMessages(String username){
        for (MessageBox messageBox : messageBoxList) {
            String owner = messageBox.getOwner();
            if (owner.equals(username)) {
                return messageBox.getMessages();
            }
        }

        // If no MessageBox object has owner username, then return empty List
        return new ArrayList<Message>();
    }

    /**
     * Creates a MessageBox instance for given user.
     * If a MessageBox instance already exists, do nothing.
     * @param username The username of the user
     */
    public void createMessageBox(String username) {
        // If such a MessageBox exists, return
        for (MessageBox messageBox : messageBoxList) {
            String owner = messageBox.getOwner();
            if (owner.equals(username)) return;
        }

        // Create the MessageBox and append it to List
        MessageBox messageBox = new MessageBox(username);
        messageBoxList.add(messageBox);
    }

    /**
     * Creates a new Message instance, adding it to the MessageBox instances for the sender and recipient.
     * @param username The sender's username
     * @param headLine The headLine of the Message
     * @param body The body of the Message
     * @param recipient The recipient of the Message
     */
    public void sendMail(String username, String headLine, String body, String recipient){
        Message newMessage = new Message(headLine, body, username, recipient);

        // Add it to sender's MessageBox
        for (MessageBox messageBox : messageBoxList) {
            String owner = messageBox.getOwner();
            if (owner.equals(username)) {
                messageBox.receiveMessage(newMessage);
            }
        }

        // Add it to recipient's MessageBox
        for (MessageBox messageBox : messageBoxList) {
            String owner = messageBox.getOwner();
            if (owner.equals(recipient)) {
                messageBox.receiveMessage(newMessage);
            }
        }
    }

    /**
     * Update the owner of MessageBox.
     * @param username Old username
     * @param newUsername New username
     */
    public void updateMailBoxUsername(String username, String newUsername){
        for (MessageBox messageBox : messageBoxList) {
            String owner = messageBox.getOwner();
            if (owner.equals(username)) {
                messageBox.setOwner(newUsername);
            }
        }
    }
}
