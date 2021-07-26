package controllers;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.TemplateManager;
import entities.User;
import usecases.UserManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utility.AppConstant.*;

public class UserController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    // Got the email regex from: https://stackoverflow.com/questions/8204680/java-regex-email
    public static final Pattern validEmail =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern validUsername =
            Pattern.compile("^[a-zA-Z0-9._%+-]+$", Pattern.CASE_INSENSITIVE);

    public UserController(UserManager userManager, EventManager eventManager, TemplateManager templateManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    public void userSignUp(){
        // Entering email
        presenter.printText("Enter an Email " + TEXT_EXIT_OPTION + ": ");
        String email = inputParser.readLine();
        boolean correctEmail = false;
        boolean correctPassword = false;
        while(!correctEmail){
            if(email.equalsIgnoreCase(EXIT_TEXT)) {
                presenter.printText(EXITING_TEXT);
                return;
            }
            else if(userManager.emailIsUnique(email) && isValidEmail(email)){ // needs to be implemented
                correctEmail = true;
            }
            else{
                presenter.printText("Email already exists or is not valid. Enter another email " + TEXT_EXIT_OPTION + ": ");
                email = inputParser.readLine();
            }
        }

        // Choosing account type
        presenter.printText("Please choose an account type: ");
        presenter.printText("1) Regular 2) Admin");
        String type = inputParser.readLine();
        User.UserType userType = User.UserType.T;
        boolean validUserType = false;
        while (!validUserType) {
            if (type.equalsIgnoreCase(EXIT_TEXT)) {
                presenter.printText(EXITING_TEXT);
                return;
            } else if (type.equals("1")) {
                userType = User.UserType.R;
                validUserType = true;
            } else if (type.equals("2")) {
                userType = User.UserType.A;
                validUserType = true;
            } else {
                presenter.printText("Please enter either 1 or 2.");
                type = inputParser.readLine();
            }
        }

        // Choosing a Username
        presenter.printText("Enter a Username" + TEXT_EXIT_OPTION + ": ");
        String username = inputParser.readLine();
        boolean correctUsername = false;
        while(!correctUsername){
            if (username.equalsIgnoreCase(EXIT_TEXT)) {
                presenter.printText(EXITING_TEXT);
                return;
            }
            else if (userManager.usernameIsUnique(username) && isValidUsername(username)){
                correctUsername = true;
            }
            else {
                presenter.printText("Username is not valid. Enter another username: ");
                username = inputParser.readLine();
            }
        }


        // Choosing a password
        presenter.printText("Enter a Password" + TEXT_EXIT_OPTION + ": ");
        String password = inputParser.readLine();
        while(!correctPassword){
            if (password.equalsIgnoreCase(EXIT_TEXT)) {
                presenter.printText(EXITING_TEXT);
                return;
            }
            else {
                correctPassword = true;
            }
        }

        userManager.createUser(username, password, email, userType);
        presenter.printText("Account has been created Successfully. You may now login.");

    }

    public String userLogin(){
        boolean validUsername = false;
        boolean validPassword = false;
        String inputted_username = null;
        String inputted_password;
        while(!validUsername){
            presenter.printText("Enter your username " + TEXT_EXIT_OPTION + ": ");
            inputted_username = inputParser.readLine();
            if (!userManager.usernameIsUnique(inputted_username)){
                validUsername = true;
            }
            else if (inputted_username.equalsIgnoreCase(EXIT_TEXT)){
                return null;
            }
            else{
                presenter.printText("Please enter an existing username.");
            }
        }
        while(!validPassword){
            presenter.printText("Enter your password " + TEXT_EXIT_OPTION + ": ");
            inputted_password = inputParser.readLine();
            if (userManager.logIn(inputted_username, inputted_password)){
                validPassword = true;
                presenter.printText(inputted_username + ", you have been logged in");
            }
            else if (inputted_password.equalsIgnoreCase(EXIT_TEXT)){
                return null;
            }
            else{
                presenter.printText("That password is incorrect");
            }
        }
        return inputted_username;
    }

    /**
     * The controller method that allows the User at the keyboard to update their username
     * @param username The username of the User who is attempting to update their username
     */
    public String changeUsername(String username){
        presenter.printText("Enter your NEW username " + TEXT_EXIT_OPTION + ": ");
        String newUsername = inputParser.readLine();
        if (newUsername.toLowerCase().equals(EXIT_TEXT)){
            presenter.printText(EXITING_TEXT);
            return null;
        }
        else if (userManager.usernameIsUnique(newUsername) && isValidUsername(newUsername)){
            userManager.updateUsername(username, newUsername);
            presenter.printText("Your username has been updated!");
            return newUsername;
        }
        else{
            presenter.printText("That username is already taken or is not valid, please try again!");
            changeUsername(username);
        }
        return null;
    }

    /**
     * The controller method that allows the User at the keyboard to update their email
     * @param username The username of the User who is attempting to update their email
     */
    public void changeEmail(String username){
        presenter.printText("Enter your NEW email " + TEXT_EXIT_OPTION + ": ");
        String newEmail = inputParser.readLine();
        if (newEmail.toLowerCase().equals(EXIT_TEXT)){
            presenter.printText(EXITING_TEXT);
        }
        else if (userManager.emailIsUnique(newEmail) && isValidEmail(newEmail)){
            userManager.updateEmail(username, newEmail);
            presenter.printText("Your email has been updated!");
        }
        else{
            presenter.printText("That email is already taken or is not valid, please try again!");
            changeEmail(username);
        }
    }

    /**
     * The controller method that allows the User at the keyboard to update their password
     * @param username The username of the User who is attempting to update their password
     */
    public void changePassword(String username){
        presenter.printText("Enter your NEW password " + TEXT_EXIT_OPTION + ": ");
        String newPassword = inputParser.readLine();
        if (newPassword.toLowerCase().equals(EXIT_TEXT)) {
            presenter.printText(EXITING_TEXT);
            return;
        }
        else{
            userManager.updatePassword(username, newPassword);
            presenter.printText("Your password has been updated!");
        }
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
        presenter.printText("Updating type to Admin");
        userManager.changeUserTypeToAdmin(username);
    }

    /**
     * Delete a user from the program
     * @param username The username of the User who wishes to delete their account
     */
    public void deleteUser(String username){
        presenter.printText("Are you sure you wish to delete your account?");
        presenter.printText("1) Yes 2) Go Back");
        String user_input = inputParser.readLine();
        if (user_input.equals("1")){
            userManager.deleteUser(username);
        }
        else if (user_input.equals("2")){
            return;
        }
        else {
            presenter.printText("You did not enter a valid option, try again");
            deleteUser(username);
        }
    }

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
     * Check if the inputted username is valid according to the validUsername regex. This regex was retrieved from
     * @param username The email to validate
     * @return boolean If the email is valid according to the validUsername regex
     */
    private boolean isValidUsername(String username){
        Matcher matcher = validUsername.matcher(username);
        return matcher.find() && !username.equals(TRIAL_USERNAME);
    }
}
