package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MessageBox implements Observer {
    private List<Message> messages;
    private List<Message> sentMessages;
    private List<Message> newMessages;

    private String owner;

    /**
     * Creates a new MessageBox for the designated owner
     * @param owner The owner of the MessageBox
     */
    public MessageBox(String owner) {
        this.messages = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.newMessages = new ArrayList<>();
        this.owner = owner;
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        // newMessage.add(arg)???
    }
}
