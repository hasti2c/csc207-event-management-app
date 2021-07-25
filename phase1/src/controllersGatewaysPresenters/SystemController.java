package controllersGatewaysPresenters;

import entitiesAndUseCases.*;
import static controllersGatewaysPresenters.AppConstant.*;

import java.util.*;


public class SystemController {
    private final UserController userController;
    private final EventController eventController;
    private final Presenter presenter;
    private final InputParser inputParser;

    private final IGateway<User> userParser;
    private final IGateway<Event> eventParser;
    private final IGateway<Template> templateParser;

    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;

    private final Map<String, List<String>> menuMap = new HashMap<>();
    private String currentUser;

    public SystemController() {
        userParser = new UserParser("phase1/data/users.json");
        eventParser = new EventParser("phase1/data/events.json");
        templateParser = new TemplateParser("phase1/data/templates.json");

        userManager = new UserManager(userParser);
        templateManager = new TemplateManager(templateParser);
        eventManager = new EventManager(eventParser, templateManager);

        presenter = new Presenter();
        inputParser = new InputParser();

        eventController = new EventController(userManager, eventManager, templateManager);
        userController = new UserController(userManager, eventManager, templateManager);

        initMenuMap();
    }

    private void initMenuMap() {
        List<String> startupMenu = Arrays.asList("SignUp", "Login", "Trial", "Exit");
        List<String> mainMenu = Arrays.asList("Create Event", "View Attended Events", "View Not Attended Events",
                "View My Events", "Edit Template", "Account Menu", "Save", "Logout");
        List<String> trialMenu = Arrays.asList("Create Event", "View Published Events", "Go Back");
        List<String> accountMenu = Arrays.asList("Logout", "Change Username", "Change Password", "Change Email",
                "Change User Type to Admin", "Delete My Account", "Go Back");
        menuMap.put("Startup Menu", startupMenu);
        menuMap.put("Main Menu", mainMenu);
        menuMap.put("Account Menu", accountMenu);
        menuMap.put("Trial Menu", trialMenu);
    }

    /**
     * Run the program, this runs the "StartUp Menu"
     */
    public void run(){
        boolean program_running = true;
        presenter.printText(WELCOME_TEXT);
        while (program_running) {
            presenter.printMenu("Startup Menu", this.menuMap.get("Startup Menu"));
            int user_input = inputParser.readInt();
            switch (user_input) {
                case 1:
                    userController.userSignUp();
                    break;
                case 2:
                    this.currentUser = userController.userLogin();
                    runMainMenu();
                    break;
                case 3:
                    createTrialUser();
                    runTrialMenu();
                    break;
                case 4:
                    program_running = false;
                    presenter.printText("Exiting...");
                    break;
                default:
                    presenter.printText("You did not enter a valid option, try again");
            }
        }
    }

    private void runMainMenu() {
        while (true) {
            presenter.printMenu("Main Menu", menuMap.get("Main Menu"));
            int userInput = inputParser.readInt();
            switch (userInput) {
                case 1:
                    int templateChoice = eventController.chooseTemplate(currentUser);
                    String templateName = templateManager.returnTemplateNames().get(templateChoice);
                    eventController.createNewEvent(templateName, currentUser);
                    break;
                case 2:
                    List<String> eventIDList1 = userManager.getAttendingEvents(currentUser);
                    eventController.browseEvents(currentUser, eventIDList1, true);
                    break;
                case 3:
                    List<String> publishedEvents = eventManager.returnPublishedEvents();
                    publishedEvents.removeAll(userManager.getAttendingEvents(currentUser));
                    eventController.browseEvents(currentUser, publishedEvents, false);
                    break;
                case 4:
                    eventController.viewAndEditMyEvents(currentUser);
                    break;
                case 5:
                    if (userManager.retrieveUserType(currentUser) == User.UserType.A){
                        int templateIndex = eventController.chooseTemplate(currentUser);
                        List<String> templateList = templateManager.returnTemplateNames();
                        editTemplateName(templateList.get(templateIndex));
                    }
                    else {
                        presenter.printText("Sorry you do not have permission to edit the templates.");
                    }
                    break;
                case 6:
                    runAccountMenu();
                    break;
                case 7:
                    saveAll();
                    presenter.printText("Everything has been successfully saved.");
                    break;
                case 8:
                    logout();
                    return;
                default:
                    presenter.printText("You did not enter a valid option, try again");
            }
        }
    }

    /**
     * Run the menu that the trial users interact with
     */
    public void runTrialMenu(){
        boolean trialMenuActive = true;
        while (trialMenuActive){
            presenter.printMenu("Trial Menu", this.menuMap.get("Trial Menu"));
            int userInput = inputParser.readInt();
            switch (userInput) {
                case 1:
                    int templateChoice = eventController.chooseTemplate(currentUser);
                    String templateName = templateManager.returnTemplateNames().get(templateChoice);
                    eventController.createNewEvent(templateName, currentUser);
                    break;
                case 2:
                    // Since this is a trial user, the unattended events is all of the events.
                    List<String> eventIDList = eventManager.returnPublishedEvents();
                    eventController.browseEvents(currentUser, eventIDList, false);
                    break;
                case 3:
                    trialMenuActive = false;
                    break;
                default:
                    presenter.printText("You did not enter a valid option, try again");
            }
        }
    }

    /**
     * Run the menu that allows the User to interact with their account
     */
    public void runAccountMenu(){
        boolean accountMenuActive = true;
        while (accountMenuActive) {
            presenter.printMenu("Account Menu", this.menuMap.get("Account Menu"));
            int user_input = inputParser.readInt();
            switch (user_input) {
                case 1:
                    logout();
                    break;
                case 2:
                    userController.changeUsername(currentUser);
                    break;
                case 3:
                    userController.changePassword(currentUser);
                    break;
                case 4:
                    userController.changeEmail(currentUser);
                    break;
                case 5:
                    userController.changeToAdmin(currentUser);
                    break;
                case 6:
                    userController.deleteUser(currentUser);
                    break;
                case 7:
                    accountMenuActive = false;
                    break;
                default:
                    presenter.printText("You did not enter a valid option, try again");
            }
        }
    }

    /**
     * Create a trial User in the program
     */
    public void createTrialUser(){
        this.currentUser = TRIAL_USERNAME;
        userManager.createUser(TRIAL_USERNAME, TRIAL_PASSWORD, TRIAL_EMAIL, User.UserType.T);
    }

    private int showMenu(String menuName) {
        presenter.printMenu(menuName, menuMap.get(menuName));
        return inputParser.readInt();
    }

    private void editTemplateName(String templateName) {
        presenter.printText("Please enter a new name for the template.");
        String newName = inputParser.readLine();
        if (templateManager.checkNameUniqueness(newName)){
            templateManager.editTemplateName(templateName, newName);
            presenter.printText("Template name edited successfully.");
        }
        else if (templateName.equals(newName)) {
            presenter.printText("Please enter a different name.");
        }
        else {
            presenter.printText("This name is already taken by another template.");
        }
    }

    private void saveAll() {
        userManager.saveAllUsers();
        eventManager.saveAllEvents();
        templateManager.saveAllTemplates();
    }

    private void logout() {
        saveAll();
        currentUser = null;
    }
}

