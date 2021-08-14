package controllers;

import controllers.menus.CommandMenuController;
import entities.*;
import gateways.*;
import presenter.InputParser;
import presenter.Presenter;
import usecases.*;
import utility.Command;
import utility.UserType;

import static utility.AppConstant.*;
import static utility.UserType.*;
import static utility.Command.*;

import java.util.*;

/**
 * Controller in charge of delegating to user controller and event controller. Runs the full system.
 */
public class SystemController {
    private final UserController userController;
    private final EventController eventController;
    private final TemplateController templateController;
    private final CommandMenuController menuController;
    private final Presenter presenter;
    private final InputParser inputParser;

    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final MenuManager menuManager;

    private String currentUser;
    private UserType currentUserType;

    // TODO factory pattern for initializer maybe?
    // == initializing ==
    public SystemController() {
        IGateway<User> userGateway = new UserGateway("phase2/data/users.json");
        IGateway<Event> eventGateway = new EventGateway("phase2/data/events.json");
        IGateway<Template> templateGateway = new TemplateGateway("phase2/data/templates.json");
        IGateway<Menu> menuGateway = new MenuGateway("phase2/data/menus.json");
        IGateway<UserTypePermissions> userPermissionsGateway = new UserTypePermissionsGateway("phase2/data/usertype_permissions.json");

        userManager = new UserManager(userGateway);
        templateManager = new TemplateManager(templateGateway);
        eventManager = new EventManager(eventGateway, templateManager);
        menuManager = new MenuManager(menuGateway, userPermissionsGateway);

        presenter = new Presenter();
        inputParser = new InputParser();

        eventController = new EventController(userManager, eventManager, templateManager, menuManager);
        userController = new UserController(userManager, eventManager, menuManager);
        menuController = new CommandMenuController(menuManager);
        templateController = new TemplateController(templateManager);
    }

    /**
     * Run the program, this runs the "StartUp Menu"
     */
    public void run(){
        presenter.printText(WELCOME_TEXT);
        runMenu(START_UP);
    }

    private void runMenu(Command currentCommand) {
        while (true) {
            Command userInput = menuController.getUserMenuChoice(currentUserType, currentCommand);
            try {
                runUserCommand(userInput);
            } catch (ExitException e) {
                return;
            }
        }
    }

    // TODO maybe move account menu stuff to UserController
    private void runUserCommand(Command command) throws ExitException {
        switch (command) {
            case START_UP:
                runMenu(START_UP);
                break;
            case SIGN_UP:
                signUp();
                break;
            case LOGIN:
                login();
                break;
            case TRIAL_MENU:
                runTrialMenu();
                break;
            case EXIT:
                exit();
                break;
            case CREATE_EVENT:
                eventController.createNewEvent(retrieveTemplateName(), currentUser);
                break;
            case BROWSE_EVENTS:
                eventController.browseEvents(currentUserType, currentUser);
                break;
            case EDIT_TEMPLATE:
                editTemplate();
                break;
            case BROWSE_USERS:
                userController.browseUsers(currentUserType, currentUser);
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
                userController.changePassword(currentUser);
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
            case EXIT_TRIAL:
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
            this.currentUserType = userManager.retrieveUserType(attemptedLoginUsername);
            runMenu(LOGIN);
        }
    }

    private void runTrialMenu() {
        createTrialUser();
        runMenu(TRIAL_MENU);
        try {
            deleteAccount();
        } catch (ExitException ignored) {}
    }

    private void exit() throws ExitException {
        saveAll();
        presenter.printText("Exiting...");
        throw new ExitException();
    }

    private void editTemplate() {
        if (userManager.retrieveUserType(currentUser) == ADMIN){
            editTemplateName(retrieveTemplateName());
        } else {
            presenter.printText("Sorry you do not have permission to edit the templates.");
        }
    }

    private void runAccountMenu() throws ExitException {
        runMenu(ACCOUNT_MENU);
        if (currentUser == null)
            throw new ExitException();
    }

    private void saveAll() {
        userManager.saveAllUsers();
        eventManager.saveAllEvents();
        templateManager.saveAllTemplates();
        menuManager.saveAllMenuInfo();
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
        userManager.createUser(TRIAL_USERNAME, TRIAL_PASSWORD, TRIAL_EMAIL, TRIAL);
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
        if (list == null) {
            return true;
        }
        return chosenIndex > list.size();
    }
}

