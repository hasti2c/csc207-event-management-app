package controllers;
import controllers.menus.EntityMenuController;
import controllers.menus.UserMenuController;
import usecases.MessageBoxManager;
import utility.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import entities.User;
import usecases.MenuManager;
import usecases.UserManager;
import utility.Command;
import utility.ViewType;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utility.AppConstant.*;
import static utility.UserType.*;
import static utility.Command.BROWSE_USERS;

/**
 * Manages how the User at the keyboard interacts with their account
 */
public class UserController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final Presenter presenter;
    private final InputParser inputParser;
    private final MessageBoxManager messageBoxManager;
    private final EntityMenuController<User> menuController;

    // Got the email regex from: https://stackoverflow.com/questions/8204680/java-regex-email
    public final Pattern validEmail =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public final Pattern validUsername =
            Pattern.compile("^[a-zA-Z0-9._%+-]+$", Pattern.CASE_INSENSITIVE);
    // https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation
    public final Pattern validPassword = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");

    /**
     * Create a UserController object
     * @param userManager The UserManager of which the UserController interacts with
     * @param eventManager The EventManager of which the UserController interacts with
     */
    public UserController(UserManager userManager, EventManager eventManager, MenuManager menuManager,
                          MessageBoxManager messageBoxManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
        this.menuController = new UserMenuController(menuManager, userManager, eventManager);
        this.messageBoxManager = messageBoxManager;
    }

    // == Creating User ==

    /**
     * Signs a User up within the program.
     * @return boolean If the signup was successful
     */
    public boolean userSignUp(){
        try {
            String email = readNewEmail();
            UserType userType = readUserType();
            String username = readNewUsername();
            String password = readNewPassword();
            userManager.createUser(username, password, email, userType);
            messageBoxManager.createMessageBox(username);
            presenter.printText("Account has been created Successfully. You may now login.");
            return true;
        } catch (ExitException e) {
            presenter.printText(EXITING_TEXT);
            return false;
        }
    }

    /**
     * Log a User in within the program. If password used was temp password, prompt to change immediately.
     * @return String The User's username. If the username is null, the login was not successful.
     */
    public String userLogin(){
        try {
            String username = readExistingUsername();
            validatePassword(username);
            if (userManager.tempPassState(username)) {
                changePassword(username);
            }
            userManager.updateAllUserSuspension();
            if (userManager.isSuspended(username)) {
                // TODO error message
                return null;
            }
            return username;
        } catch (ExitException e) {
            presenter.printText(EXITING_TEXT);
            return null;
        }
    }

    public void forgotPassword() throws ExitException {
        presenter.printText("Enter email: ");
        String email = inputParser.readLine();
        boolean existingEmail = !userManager.emailIsUnique(email);
        while (!existingEmail) {
            if (email.equals(EXIT_TEXT)) {
                throw new ExitException();
            }
            presenter.printText("Email does not exist in the system. Try again: ");
            email = inputParser.readLine();
            existingEmail = !userManager.emailIsUnique(email);
        }
        String username = userManager.getUsernameByEmail(email);
        userManager.createTempPass(username);
        presenter.printText("A temporary password has been created, please log in with the temporary password.");
    }

    // == Viewing User List ==

    public void browseUsers(UserType userType, String username) {
        while (true) {
            try {
                ViewType<User> viewType = menuController.getViewTypeChoice(userType);
                String selectedUser = menuController.getEntityChoice(viewType, username);
                viewUser(userType, username, selectedUser);
            } catch (ExitException e) {
                return;
            }
        }
    }

    private void viewUser(UserType userType, String username, String selectedUser) {
        // TODO figure out what viewUserDetails is supposed to be
//        viewUserDetails(selectedUser);
        while (true) {
            Command userInput = menuController.getEntityMenuChoice(userType, username, BROWSE_USERS, selectedUser);
            try {
                runUserCommand(userInput, username, selectedUser);
            } catch (ExitException e) {
                return;
            }
        }
    }

    private void runUserCommand(Command command, String username, String selectedUser) throws ExitException {
        switch (command) {
            case FRIEND_USER:
                addFriend(username, selectedUser);
                return;
            case UNFRIEND_USER:
                removeFriend(username, selectedUser);
                return;
            case SUSPEND_USER:
                suspendUser(selectedUser);
                return;
            case UNSUSPEND_USER:
                unsuspendUser(selectedUser);
                return;
            case GO_BACK:
                throw new ExitException();
        }
    }

    // == Interacting with Other Users ==
    private void addFriend(String username, String selectedUser) {
        userManager.addFriend(username, selectedUser);
        presenter.printText("You have added " + selectedUser + " to your friends list.");
    }

    private void removeFriend(String username, String selectedUser) {
        userManager.removeFriend(username, selectedUser);
        presenter.printText("You have removed " + selectedUser + " from your friend list.");
    }

    // TODO ban login for suspended users
    private void suspendUser(String selectedUser) {
        presenter.printText("You are suspending " + selectedUser + ". Do you want to suspend this user permanently? (Y/N)");
        if (getYesNo())
            suspendPermanently(selectedUser);
        else
            suspendTemporarily(selectedUser);
    }

    private void suspendPermanently(String selectedUser) {
        userManager.suspendUser(selectedUser);
        privateUserEvents(selectedUser);
        presenter.printText(selectedUser + " has been suspended permanently.");
    }

    private void suspendTemporarily(String selectedUser) {
        presenter.printText("For how many days do you want to suspend this user?");
        int dayCount = getDayCount();
        Duration duration = Duration.ofDays(dayCount);

        userManager.suspendUser(selectedUser, duration);
        privateUserEvents(selectedUser);
        presenter.printText(selectedUser + " has been suspended for " + dayCount + " days.");
    }

    private void privateUserEvents(String selectedUser) {
        List<String> events = userManager.getCreatedEvents(selectedUser);
        for (String eventID : events)
            eventManager.setPrivacyType(eventID, "Private");
    }

    // TODO do we want to allow temporary un-suspension?
    private void unsuspendUser(String selectedUser) {
        userManager.unsuspendUser(selectedUser);
        presenter.printText(selectedUser + " was unsuspended.");
    }

    // TODO implement back?
    private int getDayCount() {
        int dayCount = inputParser.readInt();
        while (dayCount <= 0) {
            presenter.printText("Please enter a positive number.");
            dayCount = inputParser.readInt();
        }
        return dayCount;
    }

    // == Changing User Info ==

    /**
     * The controller method that allows the User at the keyboard to update their username
     * @param username The username of the User who is attempting to update their username
     */
    public String changeUsername(String username){
        try {
            String newUsername = getChangedUsername();
            userManager.updateUsername(username, newUsername);
            eventManager.updateUsername(username, newUsername);
            messageBoxManager.updateMailBoxUsername(username, newUsername);
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
        boolean tempPassState = userManager.tempPassState(username);
        if (!tempPassState) {
            try {
                String newPassword = getChangedPassword();
                userManager.updatePassword(username, newPassword);
            } catch (ExitException ignored) {
            }
        } else {
            userManager.createTempPass(username);
        }
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

    // TODO: Not used, maybe remove
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
        if (userManager.retrieveUserType(username) == ADMIN) {
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

    /**
     * Checks if password matches regex
     * @param password String to check
     * @return True or false depending on whether the password is accepted by the regex
     */
    private boolean isValidPassword(String password) {
        Matcher matcher = validPassword.matcher(password);
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
        return matcher.find() && !username.equals(TRIAL_USERNAME) && !username.equalsIgnoreCase(ADMIN_USERNAME)
                && !username.equalsIgnoreCase(ANNOUNCEMENT_INBOX);
    }

    // == Inputting ==
    private String readNewEmail() throws ExitException {
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
        presenter.printText("1) Regular 2) Temporary (Lasts for 30 days)");
        while (true){
            String type = inputParser.readLine();
            if (type.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (type.equals("1")) {
                return REGULAR;
            } else if (type.equals("2")) {
                return TEMPORARY;
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

    /**
     * Prompts user to enter a password and returns it if valid
     * @return String of new password
     * @throws ExitException If user decides to go back
     */
    private String getChangedPassword() throws ExitException {
        presenter.printText("Enter your NEW password " + TEXT_EXIT_OPTION + ": ");
        String newPassword = inputParser.readLine();
        while (true) {
            if (newPassword.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (!isValidPassword(newPassword)) {
                presenter.printText("Must be at least 8 characters with an upper case, lower case, number");
                presenter.printText("Enter your NEW password " + TEXT_EXIT_OPTION + ": ");
                newPassword = inputParser.readLine();

            }else {
                presenter.printText("Your password has been updated!");
                return newPassword;
            }
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
        if (userManager.retrieveUser(username).getUserType() == TRIAL)
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

    // TODO move to presenter
    private boolean getYesNo() {
        String userInput = inputParser.readLine();
        while (!userInput.equalsIgnoreCase("Y") && !userInput.equalsIgnoreCase("N")) {
            presenter.printText("Type Y or N");
            userInput = inputParser.readLine();
        }
        return userInput.equalsIgnoreCase("Y");
    }
}
