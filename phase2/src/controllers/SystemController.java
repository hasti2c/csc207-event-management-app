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
    private final MessageController messageBoxController;
    private final Presenter presenter;
    private final InputParser inputParser;

    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final MenuManager menuManager;
    private final MessageBoxManager messageBoxManager;

    private String currentUser;
    private UserType currentUserType;

    // == initializing ==
    public SystemController() {
        IGateway<User> userGateway = new UserGateway("phase2/data/users.json");
        IGateway<Event> eventGateway = new EventGateway("phase2/data/events.json");
        IGateway<Template> templateGateway = new TemplateGateway("phase2/data/templates.json");
        IGateway<Menu> menuGateway = new MenuGateway("phase2/data/menus.json");
        IGateway<Permissions> userPermissionsGateway = new PermissionsGateway("phase2/data/permissions.json");
        IGateway<MessageBox> messageBoxGateway = new MessageBoxGateway("phase2/data/messageboxes.json");

        userManager = new UserManager(userGateway);
        templateManager = new TemplateManager(templateGateway);
        eventManager = new EventManager(eventGateway, templateManager);
        menuManager = new MenuManager(menuGateway, userPermissionsGateway);
        messageBoxManager = new MessageBoxManager(messageBoxGateway);

        presenter = Presenter.getInstance();
        inputParser = InputParser.getInstance();

        messageBoxController = new MessageController(userManager, messageBoxManager);
        eventController = new EventController(userManager, eventManager, templateManager, menuManager);
        userController = new UserController(userManager, eventManager, menuManager, messageBoxManager, messageBoxController);
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
            case SIGN_UP:
                signUp();
                break;
            case MAIN_MENU:
                login();
                break;
            case TRIAL_MENU:
                runTrialMenu();
                break;
            case FORGOT_PASSWORD:
                userController.forgotPassword();
                break;
            case EXIT:
                exit();
                break;
            case CREATE_EVENT:
                eventController.createNewEvent(retrieveTemplateName(), currentUser);
                break;
            case BROWSE_EVENTS:
                eventController.viewEventTypesList(currentUserType, currentUser);
                break;
            case CREATE_TEMPLATE:
                templateController.createNewTemplate();
                break;
            case EDIT_TEMPLATE:
                editTemplate();
                break;
            case BROWSE_USERS:
                userController.viewUserTypesList(currentUserType, currentUser);
                break;
            case ACCOUNT_MENU:
                runAccountMenu();
                break;
            case ADMIN_MENU:
                runMenu(ADMIN_MENU);
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
            case MESSAGING_MENU:
                runMenu(MESSAGING_MENU);
                break;
            case VIEW_MESSAGES:
                messageBoxController.viewInbox(currentUser);
                break;
            case VIEW_ADMIN_INBOX:
                messageBoxController.viewInbox(ADMIN_USERNAME);
                break;
            case SEND_MESSAGE:
                messageBoxController.sendMessage(currentUser);
                break;
            case SEND_ANNOUNCEMENT:
                messageBoxController.sendAdminAnnouncement();
                break;
            case VIEW_ANNOUNCEMENTS:
                messageBoxController.viewInbox(ANNOUNCEMENT_INBOX);
                break;
            case MESSAGE_ADMINS:
                messageBoxController.sendMessage(currentUser, ADMIN_USERNAME);
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
            runMenu(MAIN_MENU);
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
        editTemplateName(retrieveTemplateName());
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
        messageBoxManager.saveAllMessageBoxes();
        presenter.printText("Everything has been successfully saved.");
    }

    private void logOut() throws ExitException {
        saveAll();
        userManager.logOut(currentUser);
        currentUser = null;
        currentUserType = null;
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
        currentUserType = TRIAL;
        userManager.createUser(TRIAL_USERNAME, TRIAL_PASSWORD, TRIAL_EMAIL, TRIAL);
    }

    // == templates == TODO refactor from here

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

