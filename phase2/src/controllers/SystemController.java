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
import utility.Command;

import static utility.AppConstant.*;
import static utility.Command.*;

import java.util.*;

/**
 * Controller in charge of delegating to user controller and event controller. Runs the full system.
 */
public class SystemController {
    private final UserController userController;
    private final EventController eventController;
    private final TemplateController templateController;
    private final Presenter presenter;
    private final InputParser inputParser;

    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final MenuManager menuManager;

    private final Map<String, List<Command>> menuMap = new HashMap<>();
    private String currentUser;
    private User.UserType currentUserType;

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
        templateController = new TemplateController(templateManager);
//
//        initMenuMap();
    }

//    private void initMenuMap() {
//        menuMap.put("Start Up Menu", Arrays.asList(SIGN_UP, LOGIN, TRIAL, EXIT));
//        menuMap.put("Main Menu", Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED, EDIT_TEMPLATE,
//                ACCOUNT_MENU, SAVE, LOG_OUT));
//        // TODO Change GO_BACK to EXIT_TRIAL
//        menuMap.put("Trial Menu", Arrays.asList(CREATE_EVENT, VIEW_PUBLISHED, GO_BACK));
//        menuMap.put("Account Menu", Arrays.asList(CHANGE_USERNAME, CHANGE_PASSWORD, CHANGE_EMAIL, CHANGE_TO_ADMIN,
//                DELETE_ACCOUNT, GO_BACK));
//    }

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
    private void runTrialMenu(){
        createTrialUser();
        runMenu("Trial Menu");
        try {
            deleteAccount();
        } catch (ExitException ignored) {

        }
    }

    /**
     * Run the menu that allows the User to interact with their account
     * @return false if the user has been deleted, true if not
     */
    private void runAccountMenu() throws ExitException {
        runMenu("Account Menu");
        if (currentUser == null)
            throw new ExitException();
    }

    // == menu helpers ==
    private void runMenu(String menuName) {
        while (true) {
            Command userInput = getUserCommand(menuName);
            if (userInput == null) {
                continue;
            }

            try {
            runUserCommand(userInput);
            } catch (ExitException e) {
                return;
            }
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

    private void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }

    private void runUserCommand(Command command) throws ExitException {
        switch (command) {
            case START_UP:
                runMenu("Start Up Menu");
            case SIGN_UP:
                signUp();
                break;
            case LOGIN:
                login();
                break;
            case TRIAL:
                runTrialMenu();
                break;
            case FORGOT_PASSWORD_OPTION:
                userController.forgotPassword();
                break;
            case EXIT:
                exit();
                break;
            case CREATE_EVENT:
                eventController.createNewEvent(retrieveTemplateName(), currentUser);
                break;
            case VIEW_ATTENDED:
            case VIEW_UNATTENDED:
            case VIEW_OWNED:
            case VIEW_PUBLISHED:
                eventController.browseEvents(currentUser, command.getViewType());
                break;
            case EDIT_TEMPLATE:
                editTemplate();
                break;
            case ACCOUNT_MENU:
                runAccountMenu();
                break;
            case SAVE:
                saveAll();
                break;
            case LOG_OUT:
                logOut();
                break;
            case CHANGE_USERNAME:
                changeUsername();
                break;
            case CHANGE_PASSWORD:
                userController.changePassword(currentUser, false);
                break;
            case CHANGE_EMAIL:
                userController.changeEmail(currentUser);
                break;
            case CHANGE_TO_ADMIN:
                userController.changeToAdmin(currentUser);
                break;
            case DELETE_ACCOUNT:
                deleteAccount();
                break;
            case GO_BACK:
                throw new ExitException();
        }
    }

    // == commands ==
    private void signUp() {
        if (userController.userSignUp()){
            saveAll();
        }
    }

    private void login() {
        String attemptedLoginUsername = userController.userLogin();
        if (attemptedLoginUsername != null){
            this.currentUser = attemptedLoginUsername;
            runMainMenu();
        }
    }

    private void exit() throws ExitException {
        saveAll();
        presenter.printText("Exiting...");
        throw new ExitException();
    }

    private void editTemplate() {
        if (userManager.retrieveUserType(currentUser) == User.UserType.A){
            editTemplateName(retrieveTemplateName());
        } else {
            presenter.printText("Sorry you do not have permission to edit the templates.");
        }
    }

    private void saveAll() {
        userManager.saveAllUsers();
        eventManager.saveAllEvents();
        templateManager.saveAllTemplates();
        presenter.printText("Everything has been successfully saved.");
    }

    private void logOut() throws ExitException {
        saveAll();
        userManager.logOut(currentUser);
        currentUser = null;
        throw new ExitException();
    }

    private void changeUsername() {
        String newUsername = userController.changeUsername(currentUser);
        if (newUsername != null) {
            currentUser = newUsername;
        }
    }

    private void deleteAccount() throws ExitException {
        boolean result = userController.deleteUser(currentUser);
        if (result) {
            logOut();
            presenter.printText("Your account has been deleted.");
        }
    }

    private void createTrialUser(){
        currentUser = TRIAL_USERNAME;
        userManager.createUser(TRIAL_USERNAME, TRIAL_PASSWORD, TRIAL_EMAIL, User.UserType.T);
    }

    // == templates == TODO refactor from here

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

