package utility.executor;

import controllers.EventController;
import controllers.MessageController;
import controllers.TemplateController;
import controllers.UserController;
import controllers.menus.CommandMenuController;
import presenter.InputParser;
import presenter.Presenter;
import usecases.*;

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

    public SignUpExecutor buildSignUpExecutor(){
        SignUpExecutor executor = new SignUpExecutor();
        executor.setUserController(userController);
        return executor;
    }
}
