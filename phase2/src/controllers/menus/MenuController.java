package controllers.menus;

import controllers.ExitException;
import presenter.InputParser;
import presenter.Presenter;
import usecases.MenuManager;

import java.util.List;

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
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    /**
     * Shows user invalid input error.
     */
    protected void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }

    /**
     * Gets & returns user's choice from menu items. (Doesn't display the menu.)
     * Exit option is assumed to be handled by the caller.
     * @param menuOptions The list of menuOptions that have been shown to the user.
     * @param <S> Data type of menuOptions.
     * @return Menu option chosen by user.
     */
    protected <S> S getMenuChoice(List<S> menuOptions) {
        try {
            return getMenuChoice(menuOptions, false);
        } catch (ExitException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets & returns user's choice from menu items. (Doesn't display the menu.)
     * @param menuOptions The list of menuOptions that have been shown to the user.
     * @param checkExit True if exit is the last item & has to be manually checked here. False if exit is handled by the
     *                  caller.
     * @param <S> Data type of menuOptions.
     * @return Menu option chosen by user.
     * @throws ExitException If user chooses exit option & checkExit is true (exit has to be manually checked here).
     */
    protected <S> S getMenuChoice(List<S> menuOptions, boolean checkExit) throws ExitException {
        int user_input = inputParser.readInt();
        if (checkExit && user_input == menuOptions.size()) {
            throw new ExitException();
        }
        try {
            return menuOptions.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getMenuChoice(menuOptions, checkExit);
        }
    }
}
