package controllers;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import entities.User;
import usecases.UserManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utility.AppConstant.*;
import static entities.User.UserType;
// TODO: When a user changes their username, update the mailbox.

/**
 * Manages how the User at the keyboard interacts with their account
 */
public class UserController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    // Got the email regex from: https://stackoverflow.com/questions/8204680/java-regex-email
    public static final Pattern validEmail =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern validUsername =
            Pattern.compile("^[a-zA-Z0-9._%+-]+$", Pattern.CASE_INSENSITIVE);

    /**
     * Create a UserController object
     * @param userManager The UserManager of which the UserController interacts with
     * @param eventManager The EventManager of which the UserController interacts with
     */
    public UserController(UserManager userManager, EventManager eventManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    /**
     * Signs a User up within the program.
     * @return boolean If the signup was successful
     */
    public boolean userSignUp(){
        try {
            String email = readEmail();
            UserType userType = readUserType();
            String username = readNewUsername();
            String password = readNewPassword();
            userManager.createUser(username, password, email, userType);
            presenter.printText("Account has been created Successfully. You may now login.");
            return true;
        } catch (ExitException e) {
            presenter.printText(EXITING_TEXT);
            return false;
        }
    }

    /**
     * Log a User in within the program
     * @return String The User's username. If the username is null, the login was not successful.
     */
    public String userLogin(){
        try {
            String username = readExistingUsername();
            validatePassword(username);
            return username;
        } catch (ExitException e) {
            presenter.printText(EXITING_TEXT);
            return null;
        }
    }

    /**
     * The controller method that allows the User at the keyboard to update their username
     * @param username The username of the User who is attempting to update their username
     */
    public String changeUsername(String username){
        try {
            String newUsername = getChangedUsername();
            userManager.updateUsername(username, newUsername);
            eventManager.updateUsername(username, newUsername);
            // mailboxmanager.changeUsername
            return newUsername;
        } catch (ExitException e) {
            return null;
        }
    }

    /**
     * The controller method that allows the User at the keyboard to update their password
     * @param username The username of the User who is attempting to update their password
     */
    public void changePassword(String username){
        try {
            String newPassword = getChangedPassword();
            userManager.updatePassword(username, newPassword);
        } catch (ExitException ignored) {}
    }

    /**
     * The controller method that allows the User at the keyboard to update their email
     * @param username The username of the User who is attempting to update their email
     */
    public void changeEmail(String username){
        try {
            String newEmail = getChangedEmail();
            userManager.updateEmail(username, newEmail);
        } catch (ExitException ignored) {}
    }

    /**
     * The controller method that allows the User at the keyboard to update their account to the Regular type
     * @param username The username of the User who is attempting to update their account type
     */
    public void changeToRegular(String username){
        presenter.printText("Updating type to Regular");
        userManager.changeUserTypeToRegular(username);
    }

    /**
     * The controller method that allows the User at the keyboard to update their account to the Admin type
     * @param username The username of the User who is attempting to update their account type
     */
    public void changeToAdmin(String username){
        if (userManager.retrieveUserType(username) == User.UserType.A) {
            presenter.printText("You are already an admin.");
        }
        presenter.printText("Updating type to Admin");
        userManager.changeUserTypeToAdmin(username);
    }

    /**
     * Delete a user from the program
     * @param username The username of the User who wishes to delete their account
     */
    public boolean deleteUser(String username){
        if (!verifyDeletion(username))
            return false;
        List<String> userEvents = userManager.getCreatedEvents(username);
        for (String eventId : userEvents)
            eventManager.deleteEvent(eventId);
        userManager.deleteUser(username);
        return true;
    }

    // TODO put in manager?
    /**
     * Check if the inputted email is valid according to the validEmail regex. This regex was retrieved from
     * https://stackoverflow.com/questions/8204680/java-regex-email
     * @param email The email to validate
     * @return boolean If the email is valid according to the validEmail regex
     */
    private boolean isValidEmail(String email){
        Matcher matcher = validEmail.matcher(email);
        return matcher.find();
    }

    // TODO put in manager?
    /**
     * Check if the inputted username is valid according to the validUsername regex. This regex was retrieved from
     * @param username The email to validate
     * @return boolean If the email is valid according to the validUsername regex
     */
    private boolean isValidUsername(String username){
        Matcher matcher = validUsername.matcher(username);
        return matcher.find() && !username.equals(TRIAL_USERNAME) && !username.equalsIgnoreCase("admin");
    }

    // == Inputting ==
    private String readEmail() throws ExitException {
        presenter.printText("Enter an Email " + TEXT_EXIT_OPTION + ": ");
        while (true) {
            String email = inputParser.readLine();
            if (email.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (userManager.emailIsUnique(email) && isValidEmail(email)){ // needs to be implemented
                return email;
            } else {
                presenter.printText("Email already exists or is not valid. Enter another email " + TEXT_EXIT_OPTION + ": ");
            }
        }
    }

    private UserType readUserType() throws ExitException {
        presenter.printText("Please choose an account type: ");
        presenter.printText("1) Regular 2) Admin");
        while (true){
            String type = inputParser.readLine();
            if (type.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (type.equals("1")) {
                return UserType.R;
            } else if (type.equals("2")) {
                return UserType.A;
            } else {
                presenter.printText("Please enter either 1 or 2" + TEXT_EXIT_OPTION + ": ");
            }
        }
    }

    private String readNewUsername() throws ExitException {
        presenter.printText("Enter a Username " + TEXT_EXIT_OPTION + ": ");
        while (true){
            String username = inputParser.readLine();
            if (username.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (userManager.usernameIsUnique(username) && isValidUsername(username)){
                return username;
            } else {
                presenter.printText("Username is not valid or is already taken. Enter another username: ");
            }
        }
    }

    private String readNewPassword() throws ExitException {
        presenter.printText("Enter a Password" + TEXT_EXIT_OPTION + ": ");
        String password = inputParser.readLine();
        if (password.equalsIgnoreCase(EXIT_TEXT)) {
            throw new ExitException();
        } else {
            return password;
        }
    }

    private String readExistingUsername() throws ExitException {
        presenter.printText("Enter your username " + TEXT_EXIT_OPTION + ": ");
        while (true) {
            String username = inputParser.readLine();
            if (!userManager.usernameIsUnique(username)){
                return username;
            }
            else if (username.equalsIgnoreCase(EXIT_TEXT)){
                throw new ExitException();
            } else {
                presenter.printText("Please enter an existing username.");
            }
        }
    }

    // TODO maybe remove the login from validation?
    private void validatePassword(String username) throws ExitException {
        while (true) {
            presenter.printText("Enter your password " + TEXT_EXIT_OPTION + ": ");
            String password = inputParser.readLine();
            if (userManager.logIn(username, password)){
                presenter.printText(username + ", you have been logged in");
                return;
            }
            else if (password.equalsIgnoreCase(EXIT_TEXT)){
                throw new ExitException();
            }
            else {
                presenter.printText("That password is incorrect");
            }
        }
    }

    private String getChangedUsername() throws ExitException {
        presenter.printText("Enter your NEW username " + TEXT_EXIT_OPTION + ": ");
        while (true) {
            String username = inputParser.readLine();
            if (username.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (userManager.usernameIsUnique(username) && isValidUsername(username)) {
                presenter.printText("Your username has been updated!");
                return username;
            } else {
                presenter.printText("That username is already taken or is not valid, please try again!");
            }
        }
    }

    private String getChangedPassword() throws ExitException {
        presenter.printText("Enter your NEW password " + TEXT_EXIT_OPTION + ": ");
        String newPassword = inputParser.readLine();
        if (newPassword.equalsIgnoreCase(EXIT_TEXT)) {
            throw new ExitException();
        } else {
            presenter.printText("Your password has been updated!");
            return newPassword;
        }
    }

    private String getChangedEmail() throws ExitException {
        presenter.printText("Enter your NEW email " + TEXT_EXIT_OPTION + ": ");
        while (true) {
            String email = inputParser.readLine();
            if (email.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (userManager.emailIsUnique(email) && isValidEmail(email)) {
                presenter.printText("Your email has been updated!");
                return email;
            } else {
                presenter.printText("That email is already taken or is not valid, please try again!");
            }
        }
    }

    private boolean verifyDeletion(String username) {
        if (userManager.retrieveUser(username).getUserType() == User.UserType.T)
            return true;
        presenter.printText("Are you sure you wish to delete your account?");
        presenter.printText("1) Yes 2) Go Back");
        while (true) {
            String user_input = inputParser.readLine();
            if (user_input.equals("1")) {
                return true;
            } else if (user_input.equals("2")) {
                return false;
            } else {
                presenter.printText("You did not enter a valid option, try again");
            }
        }
    }
}
