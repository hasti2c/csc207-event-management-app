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
}
