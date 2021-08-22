package utility.executor;

import controllers.UserController;
import presenter.Presenter;
import usecases.*;
import utility.CommandExecutor;

public class SignUpExecutor implements CommandExecutor {
    private UserManager userManager;
    private UserController userController;
    private EventManager eventManager;
    private TemplateManager templateManager;
    private MenuManager menuManager;
    private MessageBoxManager messageBoxManager;
    private Presenter presenter;

    public SignUpExecutor(){

    }

    @Override
    public void execute() {
        userController.userSignUp();
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
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

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
