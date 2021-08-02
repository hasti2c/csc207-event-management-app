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
import static controllers.Command.*;
import static controllers.EventController.ViewType.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Controller in charge of delegating to user controller and event controller. Runs the full system.
 */
public class SystemController {
    private final UserController userController;
    private final EventController eventController;
    private final Presenter presenter;
    private final InputParser inputParser;

    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;

    private final Map<String, List<Command>> menuMap = new HashMap<>();
    private String currentUser;

    // == initializing ==
    public SystemController() {
        IGateway<User> userParser = new UserParser("phase2/data/users.json");
        IGateway<Event> eventParser = new EventParser("phase2/data/events.json");
        IGateway<Template> templateParser = new TemplateParser("phase2/data/templates.json");

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
        menuMap.put("Start Up Menu", Arrays.asList(SIGN_UP, LOGIN, TRIAL, EXIT));
        menuMap.put("Main Menu", Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED, EDIT_TEMPLATE,
                ACCOUNT_MENU, SAVE, LOG_OUT));
        menuMap.put("Trial Menu", Arrays.asList(CREATE_EVENT, VIEW_PUBLISHED, GO_BACK));
        menuMap.put("Account Menu", Arrays.asList(CHANGE_USERNAME, CHANGE_PASSWORD, CHANGE_EMAIL, CHANGE_TO_ADMIN,
                DELETE_ACCOUNT, GO_BACK));

    }

    // == menus ==
    /**
     * Run the program, this runs the "StartUp Menu"
     */
    public void run(){
        presenter.printText(WELCOME_TEXT);
        runMenu("Start Up Menu");
    }

    private void runMainMenu() {
        runMenu("Main Menu");
    }

    /**
     * Run the menu that the trial users interact with
     */
    private boolean runTrialMenu(){
        createTrialUser();
        runMenu("Trial Menu");
        deleteCurrentUser();
        return true;
    }

    /**
     * Run the menu that allows the User to interact with their account
     * @return false if the user has been deleted, true if not
     */
    private boolean runAccountMenu(){
        runMenu("Account Menu");
        return currentUser != null;
    }

    // == menu helpers ==
    private void runMenu(String menuName) {
        boolean isRunning = true;
        while (isRunning) {
            Command userInput = getUserCommand(menuName);
            if (userInput != null)
                isRunning = runUserCommand(userInput);
        }
    }

    private Command getUserCommand(String menuName) {
        List<Command> commands = menuMap.get(menuName);
        List<String> commandNames = new ArrayList<>();
        for (Command command: commands) {
            commandNames.add(command.getName());
        }
        presenter.printMenu(menuName, commandNames);
        int user_input = inputParser.readInt();
        try {
            return commands.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return null;
        }
    }

    private boolean runUserCommand(Command command) {
        try {
            return (boolean) command.getMethod().invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return true;
        }
    }

    private void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }

    // == commands ==
    private boolean signUp() {
        if (userController.userSignUp()){
            saveAll();
        }
        return true;
    }

    private boolean login() {
        String attemptedLoginUsername = userController.userLogin();
        if (attemptedLoginUsername != null){
            this.currentUser = attemptedLoginUsername;
            runMainMenu();
        }
        return true;
    }

    private void createTrialUser(){
        currentUser = TRIAL_USERNAME;
        userManager.createUser(TRIAL_USERNAME, TRIAL_PASSWORD, TRIAL_EMAIL, User.UserType.T);
    }

    private boolean deleteCurrentUser() {
        boolean result = userController.deleteUser(currentUser);
        if (result)
            logOut();
        return result;
    }

    private boolean exit() {
        saveAll();
        presenter.printText("Exiting...");
        return false;
    }

    private boolean createEvent() {
        eventController.createNewEvent(retrieveTemplateName(), currentUser);
        return true;
    }

    private boolean viewAttended() {
        eventController.browseEvents(currentUser, ATTENDING);
        return true;
    }

    private boolean viewUnattended() {
        eventController.browseEvents(currentUser, NOT_ATTENDING);
        return true;
    }

    private boolean viewOwned() {
        eventController.browseEvents(currentUser, OWNED);
        return true;
    }

    private boolean viewPublished() {
        eventController.browseEvents(currentUser, PUBLISHED);
        return true;
    }

    private boolean editTemplate() {
        if (userManager.retrieveUserType(currentUser) == User.UserType.A){
            editTemplateName(retrieveTemplateName());
        }
        else {
            presenter.printText("Sorry you do not have permission to edit the templates.");
        }
        return true;
    }

    private boolean saveAll() {
        userManager.saveAllUsers();
        eventManager.saveAllEvents();
        templateManager.saveAllTemplates();
        presenter.printText("Everything has been successfully saved.");
        return true;
    }

    private boolean logOut() {
        saveAll();
        userManager.logOut(currentUser);
        currentUser = null;
        return false;
    }

    private boolean changeUsername() {
        String newUsername = userController.changeUsername(currentUser);
        if (newUsername != null) {
            currentUser = newUsername;
        }
        return true;
    }

    private boolean changePassword() {
        userController.changePassword(currentUser);
        return true;
    }

    private boolean changeEmail() {
        userController.changeEmail(currentUser);
        return true;
    }

    private boolean changeToAdmin() {
        userController.changeToAdmin(currentUser);
        return true;
    }

    private boolean deleteAccount() {
        boolean result = deleteCurrentUser();
        if (result) {
            presenter.printText("Your account has been deleted.");
        }
        return !result;
    }

    private boolean goBack() {
        return false;
    }

    // == templates == TODO clean up

    // TODO change templateVersionNumber
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

