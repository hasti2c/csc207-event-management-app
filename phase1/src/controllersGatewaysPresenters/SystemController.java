package controllersGatewaysPresenters;

// TODO: ADD LOADING FOR TEMPLATE AND EVENT, USER HAS BEEN DONE CAN BE AN EXAMPLE!


// H TODO: Make Initializer less bulky by using helper functions
// A TODO: Make the Main Run Method
// A TODO: Make the StartupMenu Method
// H TODO: Make the mainMenu Method
// A TODO: Make the trialMenu Method
// H TODO: Make the AdminMenu Method
// A TODO: Make the accountMenu Method

import entitiesAndUseCases.EventManager;

import entitiesAndUseCases.TemplateManager;
import entitiesAndUseCases.User;
import entitiesAndUseCases.UserManager;

import java.util.*;


public class SystemController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;
    private final Map<String, List<String>> menuMap = new HashMap<>();
    private final EventController eventController;
    private final UserController userController;
    private String currentUser;
    private final IGateway<User> userParser;

    public SystemController() {
        userParser = new UserParser("data/users.json");
        this.userManager = new UserManager(userParser);

        this.templateManager = new TemplateManager();
        this.eventManager = new EventManager(this.templateManager);

        this.presenter = new Presenter();
        this.inputParser = new InputParser();
        this.eventController = new EventController(this.userManager, this.eventManager, this.templateManager);
        this.userController = new UserController(this.userManager, this.eventManager, this.templateManager);
        List<String> startupMenu = Arrays.asList("SignUp", "Login", "Trial");
        List<String> mainMenu = Arrays.asList("Create Event", "View Events", "View My Events", "Account Menu", "Save");
        List<String> trialMenu = Arrays.asList("Create Event", "View Events", "Go Back");
        List<String> adminMenu = Arrays.asList("Create Event", "View Events", "View My Events", "Edit Template", "Account Menu", "Save");
        List<String> accountMenu = Arrays.asList("Logout", "Change Username", "Change Password", "Change Email", "Change User Type to Admin", "Delete Account", "Go Back");
        this.menuMap.put("Startup Menu", startupMenu);
        this.menuMap.put("Main Menu", mainMenu);
        this.menuMap.put("Account Menu", accountMenu);
        this.menuMap.put("Trial Menu", trialMenu);
        this.menuMap.put("Admin Menu", adminMenu);
        this.run();
    }



//    // Allow user to go back
//    public void run() {
//        // starts at the main login page and asks to login or signup
//        presenter.printMenu("Main Menu", this.menuMap.get("Main Menu"));
//        int signupChoice = inputParser.readInt();
//        if(signupChoice == 1){
//            this.userSignUp();
//        }
//        this.userLogin();
//        // after this point, the event controller should take over
//        // Bring user to loginOptions
//
//    }
    // add helper methods down here


    /**
     * Run the program, this runs the "StartUp Menu"
     */
    public void run(){
        boolean program_running = true;
        while (program_running) {
            presenter.printMenu("Startup Menu", this.menuMap.get("Startup Menu"));
            String user_input = inputParser.readLine();
            switch (user_input) {
                case "1":
                    userController.userSignUp();
                    break;
                case "2":
                    String username = userController.userLogin();
                    if (username.equals("")) {
                        presenter.printText("Please try to login again");
                    } else {
                        this.currentUser = username;
                        User.UserType userType = userManager.retrieveUserType(username);
                        if (userType == User.UserType.R) {
                            // TODO: Run Main Menu
                        } else if (userType == User.UserType.A) {
                            // TODO: Run Admin Menu
                        }
                    }
                    break;
                case "3":
                    createTrialUser();
                    runTrialMenu();
                    break;
            }
        }
    }

    public void runAccountMenu(){
        boolean accountMenuActive = true;
        while (accountMenuActive) {
            presenter.printMenu("Account Menu", this.menuMap.get("Account Menu"));
            String user_input = inputParser.readLine();
            switch (user_input) {
                case "1":
                    // TODO: Create a logout for the program
                    break;
                case "2":
                    userController.changeUsername(currentUser);
                    break;
                case "3":
                    userController.changePassword(currentUser);
                    break;
                case "4":
                    userController.changeEmail(currentUser);
                    break;
                case "5":
                    userController.changeToAdmin(currentUser);
                    break;
                case "6":
                    userController.deleteUser(currentUser);
                    break;
                case "7":
                    accountMenuActive = false;
                    break;
            }
        }
    }

    public void runTrialMenu(){
        boolean trialMenuActive = true;
        while (trialMenuActive){
            presenter.printMenu("Trial Menu", this.menuMap.get("Trial Menu"));
            String user_input = inputParser.readLine();
            switch (user_input) {
                case "1":
                    // TODO: Call view event method
                    break;
                case "2":
                    // TODO: Call View Events Method
                    break;
                case "3":
                    trialMenuActive = false;
                    break;
            }
        }
    }

    /**
     * Create a trial User in the program
     */
    public void createTrialUser(){
        String trial_username = "TRIAL_USER";
        String trial_password = "TRIAL_PASS";
        String trial_email = "TRIAL@EMAIL.COM";
        this.currentUser = trial_username;
        userManager.createUser(trial_username, trial_password, trial_email, User.UserType.T);
    }


//    public static void main(String[] args) {
//        SystemController main = new SystemController();
//        main.run();
//    }
}

