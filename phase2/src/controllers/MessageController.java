package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.MessageBoxManager;
import usecases.UserManager;

import java.util.List;
import java.util.Map;

import static utility.AppConstant.EXIT_TEXT;
import static utility.AppConstant.TEXT_EXIT_OPTION;

public class MessageController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final Presenter presenter;
    private final InputParser inputParser;
    private final MessageBoxManager messageBoxManager;

    /**
     * Creates an instance of MessageController
     * @param userManager The userManager of the system
     * @param eventManager The eventManager of the system
     * @param presenter The presenter of the system
     * @param inputParser The InputParser of the system
     * @param messageBoxManager The messageBoxManager of the system
     */
    public MessageController(UserManager userManager, EventManager eventManager, Presenter presenter,
                             InputParser inputParser, MessageBoxManager messageBoxManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.presenter = presenter;
        this.inputParser = inputParser;
        this.messageBoxManager = messageBoxManager;
    }

    /**
     * Allows the user to send a message within the program
     * @param username The user who is sending the message
     */
    public void sendMessage(String username){
        try{
            String recipient = readRecipient();
            String headline = readHeadline();
            String body = readBody();
            messageBoxManager.sendMail(username, headline, body, recipient);
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
            for (String username : userManager.getUsernameList()) {
                messageBoxManager.sendMail("admin", headline, body, username);
            }
        } catch (ExitException e){
            return;
        }
    }

    /**
     * Allows a user to view their inbox
     * @param username The user who is viewing their inbox
     */
    public void viewInbox(String username){
        List<String> headlines = messageBoxManager.getHeadlines(username);
        List<Map<String, String>> detailMaps = messageBoxManager.getDetailMaps(username);
        if (headlines.size() == 0) presenter.printText("Your inbox is empty");
        else{
            for (int i = 0; i < headlines.size(); i++) {
                presenter.printText((i + 1) + ". " + headlines.get(i));
            }
            try{
                int option = chooseIntegerOption(headlines.size()) - 1;
                presenter.printEntity(detailMaps.get(option));
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
            } else if (!userManager.usernameIsUnique(recipient) || recipient.equalsIgnoreCase("admin")){
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
                presenter.printText("That headline is too long (larger than 100 characters). Enter another headline: ");
            }
        }
    }

    /**
     * Attempts to read an integer option in the range 1 - max inclusive
     * @return int The integer the user enters
     * @throws ExitException If the user types back
     */
    private int chooseIntegerOption(int max) throws ExitException {
        while (true){
            String option = inputParser.readLine();
            if (option.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (Integer.parseInt(option) <= max && !(Integer.parseInt(option) < 1)){ // Body is not all spaces
                return Integer.parseInt(option);
            } else {
                presenter.printText("The number you entered is not valid. Enter another option: ");
            }
        }
    }
}
