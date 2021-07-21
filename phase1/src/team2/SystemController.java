package team2;


import team1.EventManager;

import team1.TemplateManager;
import team1.UserManager;

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

    public SystemController() {
        this.userManager = new UserManager();
        this.templateManager = new TemplateManager();
        this.eventManager = new EventManager(this.templateManager);

        this.presenter = new Presenter();
        this.inputParser = new InputParser();
        this.eventController = new EventController(this.userManager, this.eventManager, this.templateManager);
        this.userController = new UserController(this.userManager, this.eventManager, this.templateManager);
        List<String> startupMenu = Arrays.asList("SignUp", "Login", "Trial");
        List<String> mainMenu = Arrays.asList("Create Event", "View Events", "View My Events", "Account Menu", "Save");
        List<String> trialMenu = Arrays.asList("Create Event", "View Event");
        List<String> adminMenu = Arrays.asList("Create Event", "View Events", "View My Events", "Edit Template", "Account Menu", "Save");
        List<String> accountMenu = Arrays.asList("Logout", "Change Username", "Change Password", "Change Email", "Change User Type", "Go Back");
        this.menuMap.put("Startup Menu", startupMenu);
        this.menuMap.put("Main Menu", mainMenu);
        this.menuMap.put("Account Menu", accountMenu);
        this.menuMap.put("Trial Menu", trialMenu);
        this.menuMap.put("Admin Menu", adminMenu);
        this.run();
    }

    // Allow user to go back
    public void run() {
        // starts at the main login page and asks to login or signup
        presenter.printMenu("Main Menu", this.menuMap.get("Main Menu"));
        int signupChoice = inputParser.readInt();
        if(signupChoice == 1){
            this.userSignUp();
        }
        this.userLogin();
        // after this point, the event controller should take over
        // Bring user to loginOptions

    }
    // add helper methods down here



}

