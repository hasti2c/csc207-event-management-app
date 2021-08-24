package utility.executor;

import controllers.EventController;
import controllers.MessageController;
import controllers.TemplateController;
import controllers.UserController;
import controllers.menus.CommandMenuController;
import presenter.InputParser;
import presenter.Presenter;
import usecases.*;
import utility.UserType;

public class ExecutorBuilder {

    private UserController userController;
    private EventController eventController;
    private TemplateController templateController;
    private CommandMenuController menuController;
    private MessageController messageBoxController;
    private Presenter presenter;
    private InputParser inputParser;

    private UserManager userManager;
    private EventManager eventManager;
    private TemplateManager templateManager;
    private MenuManager menuManager;
    private MessageBoxManager messageBoxManager;

    public ExecutorBuilder(){

    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }

    public void setTemplateController(TemplateController templateController) {
        this.templateController = templateController;
    }

    public void setMenuController(CommandMenuController menuController) {
        this.menuController = menuController;
    }

    public void setMessageBoxController(MessageController messageBoxController) {
        this.messageBoxController = messageBoxController;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void setInputParser(InputParser inputParser) {
        this.inputParser = inputParser;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setTemplateManager(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public void setMenuManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void setMessageBoxManager(MessageBoxManager messageBoxManager) {
        this.messageBoxManager = messageBoxManager;
    }

    public SignUpExecutor buildSignUpExecutor(){
        SignUpExecutor executor = new SignUpExecutor();
        executor.setUserController(userController);
        return executor;
    }

    public ForgotPasswordExecutor buildPasswordExecutor() {
        ForgotPasswordExecutor executor = new ForgotPasswordExecutor();
        executor.setUserController(userController);
        return executor;
    }

    public LoginExecutor buildLoginExecutor() {
        LoginExecutor executor = new LoginExecutor();
        executor.setUserController(userController);
        executor.setUserManager(userManager);
        return executor;
    }

    public SaveExecutor buildSaveExecutor() {
        SaveExecutor executor = new SaveExecutor();
        executor.setEventManager(eventManager);
        executor.setMenuManager(menuManager);
        executor.setPresenter(presenter);
        executor.setMessageBoxManager(messageBoxManager);
        executor.setTemplateManager(templateManager);
        executor.setUserManager(userManager);
        return executor;

    }

    public CreateEventExecutor buildCreateEventExecutor() {
        CreateEventExecutor executor = new CreateEventExecutor();
        executor.setEventController(eventController);
        return executor;
    }

    public BrowseEventsExecutor buildBrowseEventsExecutor() {
        BrowseEventsExecutor executor = new BrowseEventsExecutor();
        executor.setEventController(eventController);
        return executor;
    }

    public CreateTemplateExecutor buildCreateTemplateExecutor() {
        CreateTemplateExecutor executor = new CreateTemplateExecutor();
        executor.setTemplateController(templateController);
        return executor;
    }

    public DeleteTemplateExecutor buildDeleteTemplateExecutor() {
        DeleteTemplateExecutor executor = new DeleteTemplateExecutor();
        executor.setTemplateController(templateController);
        return executor;
    }

    public ChangeTemplateNameExecutor buildChangeTemplateNameExecutor() {
        ChangeTemplateNameExecutor executor = new ChangeTemplateNameExecutor();
        executor.setTemplateController(templateController);
        return executor;
    }
}
