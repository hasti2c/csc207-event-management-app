package controllers;

import entities.Event;
import entities.Template;
import entities.User;
import gateways.EventParser;
import gateways.IGateway;
import gateways.TemplateParser;
import gateways.UserParser;
import presenter.InputParser;
import presenter.Presenter;
import usecases.*;
import static utility.AppConstant.*;

import java.util.*;

/**
 * Controller in charge of delegating to user controller and event controller. Runs the full system.
 */
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
        userController = new UserController(userManager, eventManager);

        initMenuMap();
    }

    private void initMenuMap() {
        List<String> startupMenu = Arrays.asList("Sign Up", "Login", "Trial", "Exit");
        List<String> mainMenu = Arrays.asList("Create Event", "View Attended Events", "View Not Attended Events",
                "View My Events", "Edit Template", "Account Menu", "Save", "Logout");
        List<String> trialMenu = Arrays.asList("Create Event", "View Published Events", "Go Back");
        List<String> accountMenu = Arrays.asList("Change Username", "Change Password", "Change Email",
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
                    if (userController.userSignUp()){
                        saveAll();
                    }
                    break;
                case 2:
                    String attemptedLoginUsername = userController.userLogin();
                    if (attemptedLoginUsername != null){
                        this.currentUser = attemptedLoginUsername;
                        runMainMenu();
                    }
                    break;
                case 3:
                    createTrialUser();
                    runTrialMenu();
                    deleteCurrentUser();
                    break;
                case 4:
                    program_running = false;
                    saveAll();
                    presenter.printText("Exiting...");
                    break;
                default:
                    presenter.printText("You did not enter a valid option, try again");
            }
        }
    }

    private void runMainMenu() {
        boolean runningMainMenu = true;
        while (runningMainMenu) {
            presenter.printMenu("Main Menu", menuMap.get("Main Menu"));
            int userInput = inputParser.readInt();
            switch (userInput) {
                case 1:
                    eventController.createNewEvent(retrieveTemplateName(), currentUser);
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
                        editTemplateName(retrieveTemplateName());
                    }
                    else {
                        presenter.printText("Sorry you do not have permission to edit the templates.");
                    }
                    break;
                case 6:
                    runningMainMenu = runAccountMenu();
                    break;
                case 7:
                    saveAll();
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
    private void runTrialMenu(){
        boolean trialMenuActive = true;
        while (trialMenuActive){
            presenter.printMenu("Trial Menu", this.menuMap.get("Trial Menu"));
            int userInput = inputParser.readInt();
            switch (userInput) {
                case 1:
                    eventController.createNewEvent(retrieveTemplateName(), currentUser);
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
     * @return false if the user has been deleted, true if not
     */
    private boolean runAccountMenu(){
        while (true) {
            presenter.printMenu("Account Menu", this.menuMap.get("Account Menu"));
            int user_input = inputParser.readInt();
            switch (user_input) {
                case 1:
                    String newUsername = userController.changeUsername(currentUser);
                    if (newUsername != null) {
                        currentUser = newUsername;
                    }
                    break;
                case 2:
                    userController.changePassword(currentUser);
                    break;
                case 3:
                    userController.changeEmail(currentUser);
                    break;
                case 4:
                    userController.changeToAdmin(currentUser);
                    break;
                case 5:
                    boolean result = deleteCurrentUser();
                    if (result) {
                        presenter.printText("Your account has been deleted.");
                    }
                    return !result;
                case 6:
                    return true;
                default:
                    presenter.printText("You did not enter a valid option, try again");
            }
        }
    }

    /**
     * Create a trial User in the program
     */
    private void createTrialUser(){
        currentUser = TRIAL_USERNAME;
        userManager.createUser(TRIAL_USERNAME, TRIAL_PASSWORD, TRIAL_EMAIL, User.UserType.T);
    }

    private boolean deleteCurrentUser() {
        boolean result = userController.deleteUser(currentUser);
        if (result)
            logout();
        return result;
    }

    private int showMenu(String menuName) {
        presenter.printMenu(menuName, menuMap.get(menuName));
        return inputParser.readInt();
    }

    private void editTemplateName(String templateName) {
        presenter.printText("Please enter a new name for the template.");
        String newName = inputParser.readLine();
        if (newName.equals("back")) {
            presenter.printText("You have been sent back.");
        }

        else if (templateManager.checkNameUniqueness(newName)){
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
        presenter.printText("Everything has been successfully saved.");
    }

    private void logout() {
        saveAll();
        userManager.logOut(currentUser);
        currentUser = null;
    }

    // === Helpers ===
    private String retrieveTemplateName() {
        int templateChoice = eventController.chooseTemplate(currentUser);
        List<String> templateNames = templateManager.returnTemplateNames();
        return retrieveName(templateNames, templateChoice);
    }

    private String retrieveName(List<String> nameList, int chosenIndex) {
        if(chosenIndexLargerThanTheSize(nameList, chosenIndex)) {
            return null;
        }
        return nameList.get(chosenIndex - 1);
    }

    private boolean chosenIndexLargerThanTheSize(List<?> list, int chosenIndex) {
        if(list == null) {
            return true;
        }
        return chosenIndex > list.size();
    }

}

