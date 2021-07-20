package team2;
import team1.EventManager;
import team1.TemplateManager;
import team1.UserManager;

public class UserController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public UserController(UserManager userManager, EventManager eventManager, TemplateManager templateManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    private void userSignUp(){
        presenter.printText("Enter an Email: ");
        String email = inputParser.readLine();
        boolean correctEmail = false;
        while(!correctEmail){
            if(!userManager.isEmailTaken(email)){ // needs to be implemented
                correctEmail = true;
            }else{
                presenter.printText("Email already exists. Enter another email: ");
                email = inputParser.readLine();
            }
        }

        presenter.printText("Enter a Username: ");
        String username = inputParser.readLine();
        boolean correctUsername = false;
        while(!correctUsername){
            if(!userManager.isUsernameTaken(email)){ // needs to be implemented
                correctUsername = true;
            }else{
                presenter.printText("Username already exists. Enter another username: ");
                email = inputParser.readLine();
            }
        }

        presenter.printText("Enter a Password: ");
        String password = inputParser.readLine();

        userManager.createUser(username, password, email, userType); // this needs to be implemented
        presenter.printText("Account has been created Successfully. Please login.");

    }
    private void userLogin(){

        presenter.printText("Enter your Username: ");
        String username = inputParser.readLine();

        presenter.printText("Enter your Password: ");
        String password = inputParser.readLine();

        boolean correctLogin = false;
        while(!correctLogin){
            if(userManager.logIn(username, password)){
                correctLogin = true;
            }else{
                presenter.printText("Username or Password is incorrect, please try again.");

                presenter.printText("Enter your Username: ");
                username = inputParser.readLine();

                presenter.printText("Enter your Password: ");
                password = inputParser.readLine();
            }
        }
        presenter.printText("Login was successful");
    }

    private boolean checkLogin(){
        /* checks given user credentials from presenter and checks if it is correct or not
         */
        return true;
    }

    /**
     * The controller method that allows the User at the keyboard to update their username
     * @param username The username of the User who is attempting to update their username
     */
    private void changeUsername(String username){
        presenter.printText("Enter your NEW username (or type Cancel to go back!): ");
        String newUsername = inputParser.readLine();
        if (newUsername.toUpperCase().equals("CANCEL")){
            presenter.printText("Returning to previous menu...");
            return;
        }
        else if (!userManager.isUsernameTaken(newUsername)){
            userManager.updateUsername(username, newUsername);
            presenter.printText("Your username has been updated!");
            return;
        }
        else{
            presenter.printText("That username is already taken, please try again!");
            changeUsername(username);
        }
    }

    /**
     * The controller method that allows the User at the keyboard to update their email
     * @param username The username of the User who is attempting to update their email
     */
    private void changeEmail(String username){
        presenter.printText("Enter your NEW email (or type Cancel to go back!): ");
        String newEmail = inputParser.readLine();
        if (newEmail.toUpperCase().equals("CANCEL")){
            presenter.printText("Returning to previous menu...");
            return;
        }
        else if (!userManager.isEmailTaken(newEmail)){
            userManager.updateEmail(username, newEmail);
            presenter.printText("Your email has been updated!");
            return;
        }
        else{
            presenter.printText("That email is already taken, please try again!");
            changeEmail(username);
        }
    }

    /**
     * The controller method that allows the User at the keyboard to update their password
     * @param username The username of the User who is attempting to update their password
     */
    private void changePassword(String username){
        presenter.printText("Enter your NEW password (or type 'Cancel' to go back!): ");
        String newPassword = inputParser.readLine();
        if (newPassword.toUpperCase().equals("CANCEL")) {
            presenter.printText("Returning to previous menu...");
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
    private void changeToRegular(String username){
        presenter.printText("Updating type to Regular");
        userManager.changeUserTypeToRegular(username);
    }

    /**
     * The controller method that allows the User at the keyboard to update their account to the Admin type
     * @param username The username of the User who is attempting to update their account type
     */
    private void changeToAdmin(String username){
        presenter.printText("Updating type to Regular");
        userManager.changeUserTypeToAdmin(username);
    }
}
