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

import java.io.File;

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
        String dataPath = "phase2" + File.separator + "data" + File.separator;
        IGateway<User> userGateway = new UserGateway(dataPath + "users.json");
        IGateway<Event> eventGateway = new EventGateway(dataPath + "events.json");
        IGateway<Template> templateGateway = new TemplateGateway(dataPath + "templates.json");
        IGateway<Menu> menuGateway = new MenuGateway(dataPath + "menus.json");
        IGateway<Permissions> userPermissionsGateway = new PermissionsGateway(dataPath + "permissions.json");
        IGateway<MessageBox> messageBoxGateway = new MessageBoxGateway(dataPath + "messageboxes.json");

        userManager = new UserManager(userGateway);
        templateManager = new TemplateManager(templateGateway);
        eventManager = new EventManager(eventGateway, templateManager);
        menuManager = new MenuManager(menuGateway, userPermissionsGateway);
        messageBoxManager = new MessageBoxManager(messageBoxGateway);

        presenter = Presenter.getInstance();
        inputParser = InputParser.getInstance();

        messageBoxController = new MessageController(userManager, messageBoxManager);
        templateController = new TemplateController(templateManager);
        eventController = new EventController(userManager, eventManager, templateManager, menuManager, templateController);
        userController = new UserController(userManager, eventManager, menuManager, messageBoxManager, messageBoxController);
        menuController = new CommandMenuController(menuManager);

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
                exit();
                break;
            case EXIT:
                exit();
                break;
            case CREATE_EVENT:
                eventController.createNewEvent(currentUser);
                break;
            case BROWSE_EVENTS:
                eventController.browseEvents(currentUserType, currentUser);
                break;
            case CREATE_TEMPLATE:
                templateController.createNewTemplate();
                break;
            case DELETE_TEMPLATE:
                templateController.deleteTemplate();
                break;
            case EDIT_TEMPLATE:
                runMenu(EDIT_TEMPLATE);
                break;
            case CHANGE_TEMPLATE_NAME:
                templateController.editTemplateName();
                break;
            case ADD_TEMPLATE_FIELD:
                templateController.addNewField();
                break;
            case DELETE_TEMPLATE_FIELD:
                templateController.deleteField();
                break;
            case BROWSE_USERS:
                userController.browseUsers(currentUserType, currentUser);
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
}

