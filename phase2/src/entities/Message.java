package entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A Message within the messaging system. Extends the Observable java class.
 */
public class Message{
    private String messageHeadLine;
    private String messageBody;
    private String sender;
    private String recipient;
    private LocalDateTime sentDate;

    // Datetime stuff
    private final transient String FORMATTED_DATE= "yyyy-MM-dd HH:mm";
    private final transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTED_DATE);

    /**
     * Empty constructor for the gateway
     */
    public Message() {}

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
        this.sentDate = LocalDateTime.now();
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

    /**
     * Gets the sender of the Message
     * @return String The sender of the Message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Gets the recipient of the Message
     * @return String The recipient of the Message
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Gets the date when the Message was sent
     * @return LocalDateTime The date when the Message was sent
     */
    public LocalDateTime getDate() {
        return sentDate;
    }

    /**
     * Gets a String which states the headline and the sender of the message. This is in the form:
     * "headline | Sent by: sender"
     * @return String The info of the message
     */
    public String messageInfo(){
        String sent_time = sentDate.format(formatter);
        return messageHeadLine + " | Sent by: " + sender + ", at: " + sent_time;
    }

    /**
     * The string representation of a message
     * @return String The String representation of a message
     */
    @Override
    public String toString() {
        return messageHeadLine + " | Sent by: " + sender + "\n \n" + messageBody;
    }

    /**
     * Return a map of the message that can be utilised by a presenter.
     * @return Map<String, String> A map of the details where the key is the detail title, and the value is the detail.
     */
    public Map<String, String> getDetails(){
        String sent_time = sentDate.format(formatter);
        Map<String, String> detailsMap = new LinkedHashMap<>();
        detailsMap.put("Sender", sender);
        detailsMap.put("Sent Time", sent_time);
        detailsMap.put("Headline", messageHeadLine);
        detailsMap.put("Body", messageBody);
        return detailsMap;
    }
}
