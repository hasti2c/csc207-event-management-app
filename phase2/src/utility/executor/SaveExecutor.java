package utility.executor;

import controllers.ExitException;
import entities.Menu;
import presenter.Presenter;
import usecases.*;
import utility.CommandExecutor;
import utility.UserType;

import java.util.function.Predicate;

public class SaveExecutor implements CommandExecutor {
    private UserManager userManager;
    private EventManager eventManager;
    private TemplateManager templateManager;
    private MenuManager menuManager;
    private MessageBoxManager messageBoxManager;
    private Presenter presenter;
    @Override
    public String execute(String username, UserType userType) throws ExitException {
        userManager.saveAllUsers();
        eventManager.saveAllEvents();
        templateManager.saveAllTemplates();
        menuManager.saveAllMenuInfo();
        messageBoxManager.saveAllMessageBoxes();
        Presenter.getInstance().printText("Everything has been successfully saved.");
        return username;
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

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
