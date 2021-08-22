package usecases;

import entities.Message;
import entities.MessageBox;
import gateways.IGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utility.AppConstant.ADMIN_USERNAME;
import static utility.AppConstant.ANNOUNCEMENT_INBOX;

public class MessageBoxManager {
    private final List<MessageBox> messageBoxList;
    private final IGateway<MessageBox> gateway;

    /**
     * Create an instance of MessageBoxManager
     * @param gateway The gateway that loads the MessageBoxes for the system
     */
    public MessageBoxManager(IGateway<MessageBox> gateway) {
        this.gateway = gateway;
        messageBoxList = gateway.getAllElements();
        // Create the default admin MessageBox
        createMessageBox(ADMIN_USERNAME);
        createMessageBox(ANNOUNCEMENT_INBOX);
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
        return new ArrayList<>();
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

    /**
     * Gets the message box of the desired user
     * @param username The user whose MessageBox is to be retrieved
     * @return MessageBox The messagebox of the desired user, if they do not exist, return null
     */
    private MessageBox getMessageBoxOfUser(String username){
        for (MessageBox messageBox : messageBoxList) {
            String owner = messageBox.getOwner();
            if (owner.equals(username)) {
                return messageBox;
            }
        }
        return null;
    }

    /**
     * Return a list of strings of the Headlines of the desired User's MessageBox. If the user does not exist return
     * an empty list
     * @param username The username of the user whose messages will be viewed
     * @return List<String> A list of message headlines
     */
    public List<String> getHeadlines(String username){
        MessageBox messageBoxOfUser = getMessageBoxOfUser(username);
        if (messageBoxOfUser != null) {
            return messageBoxOfUser.getMessageHeadlines();
        }
        else {
            return new ArrayList<>();
        }
    }

    /**
     * Return a list of strings of the Headlines with info of the desired User's MessageBox. If the user does not exist
     * return an empty list
     * @param username The username of the user whose messages will be viewed
     * @return List<String> A list of message headlines with info
     */
    public List<String> getMessageInfo(String username){
        MessageBox messageBoxOfUser = getMessageBoxOfUser(username);
        if (messageBoxOfUser != null) {
            return messageBoxOfUser.getMessageInfo();
        }
        else {
            return new ArrayList<>();
        }
    }

    /**
     * Return a list of Map<String,String> of the details of the desired User's messages
     * @param username The username of the user whose messages will be viewed
     * @return List<Map<String, String>>  A list of message details
     */
    public List<Map<String, String>> getDetailMaps(String username){
        MessageBox messageBoxOfUser = getMessageBoxOfUser(username);
        if (messageBoxOfUser != null) {
            return messageBoxOfUser.getDetailMaps();
        }
        else {
            return new ArrayList<>();
        }
    }

    /**
     * Saves all message boxes.
     */
    public void saveAllMessageBoxes() {
        gateway.saveAllElements(messageBoxList);
    }
}
