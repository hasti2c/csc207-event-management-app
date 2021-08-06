package entities;
// TODO: Add docstring for getters.

import java.util.Date;

/**
 * A Message within the messaging system. Extends the Observable java class.
 */
public class Message{
    private final String messageHeadLine;
    private final String messageBody;
    private final String sender;
    private final String recipient;
    private final Date date;

    /**
     * Creates an object of type Message
     * @param messageHeadLine The headline of the Message
     * @param messageBody The body of the Message
     * @param sender The sender of the Message
     * @param recipient The recipient of the Message
     */
    public Message(String messageHeadLine, String messageBody, String sender, String recipient) {
        this.messageHeadLine = messageHeadLine;
        this.messageBody = messageBody;
        this.sender = sender;
        this.recipient = recipient;
        this.date = new Date(); // Today's date
    }

    /**
     * Gets the Message headline
     * @return String The Message headline
     */
    public String getMessageHeadLine() {
        return messageHeadLine;
    }

    /**
     * Gets the Message body
     * @return String The Message body
     */
    public String getMessageBody() {
        return messageBody;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public Date getDate() {
        return date;
    }
}
