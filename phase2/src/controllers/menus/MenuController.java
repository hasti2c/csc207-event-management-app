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

    public MenuController(MenuManager menuManager) {
        this.menuManager = menuManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    protected void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }

    protected <S> S getMenuChoice(List<S> menuOptions) {
        try {
            return getMenuChoice(menuOptions, false);
        } catch (ExitException e) {
            e.printStackTrace();
            return null;
        }
    }

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
