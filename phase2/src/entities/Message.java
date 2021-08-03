package entities;

import java.util.Observable;
import java.util.Observer;

/**
 * A Message within the messaging system. Extends the Observable java class.
 */
public class Message extends Observable {
    private final String messageTitle;
    private final String messageHeadLine;
    private final String messageBody;

    /**
     * Creates an object of type Message
     * @param messageTitle The title of the Message
     * @param messageHeadLine The headline of the Message
     * @param messageBody The body of the Message
     */
    public Message(String messageTitle, String messageHeadLine, String messageBody) {
        this.messageTitle = messageTitle;
        this.messageHeadLine = messageHeadLine;
        this.messageBody = messageBody;
    }

    /**
     * Gets the Message title
     * @return String The Message title
     */
    public String getMessageTitle() {
        return messageTitle;
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
}
