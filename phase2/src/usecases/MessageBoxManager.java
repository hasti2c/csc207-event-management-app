package usecases;

import entities.Message;
import entities.MessageBox;

import java.util.ArrayList;
import java.util.List;

public class MessageBoxManager {
    private final List<MessageBox> messageBoxList;

    public MessageBoxManager() {
        this.messageBoxList = new ArrayList<>();
    }

    public List<Message> getMessages(String username){
        // Get this users message
    }

    public void createMailBox(String username){
        // Create the mailbox and add it to the list
    }

    public void sendMail(String username, String headLine, String body, String recipient){
        Message newMessage = new Message(headLine, body, username, recipient);
    }

    public void updateMailBoxUsername(String username, String newUsername){
        // Update the mailbox username to the new username
    }
}
