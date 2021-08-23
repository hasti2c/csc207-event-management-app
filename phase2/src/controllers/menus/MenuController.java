package controllers.menus;

import controllers.ExitException;
import presenter.InputParser;
import presenter.Presenter;
import usecases.MenuManager;

import java.util.List;

/**
 * Controller that handles menu related tasks.
 */
public abstract class MenuController {
    protected MenuManager menuManager;
    protected Presenter presenter;
    protected InputParser inputParser;

    /**
     * Constructs a MenuController.
     * @param menuManager A MenuManager.
     */
    public MenuController(MenuManager menuManager) {
        this.menuManager = menuManager;
        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
    }
}
