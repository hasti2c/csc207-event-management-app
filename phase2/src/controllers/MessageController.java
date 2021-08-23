package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.MessageBoxManager;
import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static utility.AppConstant.*;

public class MessageController {
    private final UserManager userManager;
    private final MessageBoxManager messageBoxManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    /**
     * Creates an instance of MessageController
     * @param userManager The userManager of the system
     * @param messageBoxManager The messageBoxManager of the system
     */
    public MessageController(UserManager userManager, MessageBoxManager messageBoxManager) {
        this.userManager = userManager;
        this.messageBoxManager = messageBoxManager;

        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
    }

    /**
     * Allows the user to send a message within the program
     * @param username The user who is sending the message
     */
    public void sendMessage(String username){
        try {
            String recipient = readRecipient();
            String headline = readHeadline();
            String body = readBody();
            messageBoxManager.sendMail(username, headline, body, recipient);
            presenter.printText("Message Sent");
        } catch (ExitException e){
            return;
        }
    }

    /**
     * Allows the user to send a message within the program
     * @param username The user who is sending the message
     * @param recipient The user who will receive the message
     */
    public void sendMessage(String username, String recipient){
        try{
            String headline = readHeadline();
            String body = readBody();
            messageBoxManager.sendMail(username, headline, body, recipient);
            presenter.printText("Message Sent");
        } catch (ExitException e){
            return;
        }
    }

    /**
     * Allows an admin to send a message to every User within the system
     */
    public void sendAdminAnnouncement(){
        try{
            String headline = readHeadline();
            String body = readBody();
            messageBoxManager.sendMail(ADMIN_USERNAME, headline, body, ANNOUNCEMENT_INBOX);
            presenter.printText("Announcement Sent");
        } catch (ExitException e){
            return;
        }
    }

    /**
     * Allows a user to view their inbox
     * @param username The user who is viewing their inbox
     */
    public void viewInbox(String username) {
        List<String> headlines = messageBoxManager.getMessageInfo(username);
        List<Map<String, String>> detailMaps = messageBoxManager.getDetailMaps(username);
        if (headlines.size() == 0) {
            presenter.printText("Your inbox is empty.");
            return;
        }

        while (true) {
            try {
                List<String> menuOptions = new ArrayList<>(headlines);
                menuOptions.add(MENU_EXIT_OPTION);
                presenter.printMenu("Inbox", menuOptions);
                int index = inputParser.getMenuChoiceIndex(menuOptions, true);
                presenter.printEntity(detailMaps.get(index));
            } catch (ExitException e) {
                return;
            }
        }
    }

    // ========== Private helpers ===============

    /**
     * Attempts to read who the user wishes to send a message to
     * @return String The recipient of the message
     * @throws ExitException If the user types back
     */
    private String readRecipient() throws ExitException {
        presenter.printText("Enter the recipient of the message, or 'ADMIN' to contact an administrator "
                + TEXT_EXIT_OPTION + ": ");
        while (true){
            String recipient = inputParser.readLine();
            if (recipient.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (recipient.equalsIgnoreCase(ADMIN_USERNAME)){
                return ADMIN_USERNAME;
            }
            else if (recipient.equalsIgnoreCase(ANNOUNCEMENT_INBOX)){
                presenter.printText("You cannot message that inbox. It must be done through the ADMIN menu.");
            }
            else if (!userManager.usernameIsUnique(recipient)){
                return recipient;
            } else {
                presenter.printText("That user does not exist, Enter another username: ");
            }
        }
    }

    /**
     * Attempts to read the headline of a message
     * @return String The headline of the message
     * @throws ExitException If the user types back
     */
    private String readHeadline() throws ExitException {
        presenter.printText("Enter the headline of the message "
                + TEXT_EXIT_OPTION + ": ");
        while (true){
            String headline = inputParser.readLine();
            if (headline.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (headline.length() <= 100 ){
                return headline;
            } else {
                presenter.printText("That headline is too long (larger than 100 characters). Enter another headline: ");
            }
        }
    }

    /**
     * Attempts to read the body of a message
     * @return String The body of the message
     * @throws ExitException If the user types back
     */
    private String readBody() throws ExitException {
        presenter.printText("Enter the body of the message "
                + TEXT_EXIT_OPTION + ": ");
        while (true){
            String body = inputParser.readLine();
            if (body.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (!body.matches("^[ ]+$")){ // Body is not all spaces
                return body;
            } else {
                presenter.printText("Your headline is empty. Enter another headline:");
            }
        }
    }
}
