package controllersGatewaysPresenters;
import entitiesAndUseCases.EventManager;
import entitiesAndUseCases.TemplateManager;
import entitiesAndUseCases.User;
import entitiesAndUseCases.UserManager;

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

    // TODO: Add usertype!
    public void userSignUp(){

        // Entering email
        presenter.printText("Enter an Email: ");
        String email = inputParser.readLine();
        boolean correctEmail = false;
        while(!correctEmail){
            if(userManager.emailIsUnique(email)){ // needs to be implemented
                correctEmail = true;
            }else{
                presenter.printText("Email already exists. Enter another email: ");
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
            if (type.equals("1")) {
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
        presenter.printText("Enter a Username: ");
        String username = inputParser.readLine();
        boolean correctUsername = false;
        while(!correctUsername){
            if(userManager.usernameIsUnique(username)){
                correctUsername = true;
            }else{
                presenter.printText("Username already exists. Enter another username: ");
                username = inputParser.readLine();
            }
        }

        // Choosing a password
        presenter.printText("Enter a Password: ");
        String password = inputParser.readLine();

        userManager.createUser(username, password, email, userType); // this needs to be implemented
        presenter.printText("Account has been created Successfully. Please login.");

    }

    // TODO: Return the username if successful else return empty string. Also do not allow users to create a username
    // of the empty string.
    public String userLogin(){

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
        return username;
    }
//
//    private boolean checkLogin(){
//        /* checks given user credentials from presenter and checks if it is correct or not
//         */
//        return true;
//    }

    /**
     * The controller method that allows the User at the keyboard to update their username
     * @param username The username of the User who is attempting to update their username
     */
    public void changeUsername(String username){
        presenter.printText("Enter your NEW username (or type Cancel to go back!): ");
        String newUsername = inputParser.readLine();
        if (newUsername.toUpperCase().equals("CANCEL")){
            presenter.printText("Returning to previous menu...");
            return;
        }
        else if (userManager.usernameIsUnique(newUsername)){
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
    public void changeEmail(String username){
        presenter.printText("Enter your NEW email (or type Cancel to go back!): ");
        String newEmail = inputParser.readLine();
        if (newEmail.toUpperCase().equals("CANCEL")){
            presenter.printText("Returning to previous menu...");
        }
        else if (userManager.emailIsUnique(newEmail)){
            userManager.updateEmail(username, newEmail);
            presenter.printText("Your email has been updated!");
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
    public void changePassword(String username){
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
    public void changeToRegular(String username){
        presenter.printText("Updating type to Regular");
        userManager.changeUserTypeToRegular(username);
    }

    /**
     * The controller method that allows the User at the keyboard to update their account to the Admin type
     * @param username The username of the User who is attempting to update their account type
     */
    public void changeToAdmin(String username){
        presenter.printText("Updating type to Regular");
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
}
