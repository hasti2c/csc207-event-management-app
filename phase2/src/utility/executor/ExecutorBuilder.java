package utility.executor;

import controllers.UserController;
import presenter.Presenter;
import usecases.*;

public class ExecutorBuilder {
    private UserManager userManager;
    private UserController userController;
    private EventManager eventManager;
    private TemplateManager templateManager;
    private MenuManager menuManager;
    private MessageBoxManager messageBoxManager;
    private Presenter presenter;

    public SignUpExecutor buildSignUpExecutor(){
        SignUpExecutor executor = new SignUpExecutor();
        executor.setEventManager(eventManager);
        executor.setMenuManager(menuManager);

        return executor;
    }
}
