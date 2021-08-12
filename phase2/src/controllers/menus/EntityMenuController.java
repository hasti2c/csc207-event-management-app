package controllers.menus;

import controllers.ExitException;
import entities.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.AppConstant;
import utility.Command;
import utility.EventViewType;
import utility.ViewType;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityMenuController <T> {
    protected final MenuManager menuManager;
    protected final UserManager userManager;
    protected final EventManager eventManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public EntityMenuController(MenuManager menuManager, UserManager userManager, EventManager eventManager) {
        this.menuManager = menuManager;
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    // == Getting View Type ==
    public ViewType<T> getViewTypeChoice(UserType userType, Command command) throws ExitException {
        List<ViewType<T>> viewTypes = getViewTypePermissions(userType);
        displayViewTypeMenu(viewTypes, command);

        int user_input = inputParser.readInt();
        if (user_input == viewTypes.size()) {
            throw new ExitException();
        }
        try {
            return viewTypes.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getViewTypeChoice(userType, command);
        }
    }

    private void displayViewTypeMenu(List<ViewType<T>> viewTypes, Command command) {
        List<String> viewTypeNames = new ArrayList<>();
        for (ViewType<T> viewType: viewTypes) {
            viewTypeNames.add(viewType.getName());
        }
        viewTypeNames.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(command.getName(), viewTypeNames);
    }

    // TODO remove
    protected abstract List<ViewType<T>> getViewTypePermissions(UserType userType);

    // == Getting Entity Choice ==
    public String getEntityChoice(ViewType<T> viewType, String username) throws ExitException {
        List<String> entities = getEntityList(viewType, username);
        displayEntityList(entities, viewType);

        int user_input = inputParser.readInt();
        if (user_input == entities.size()) {
            throw new ExitException();
        }
        try {
            return entities.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEntityChoice(viewType, username);
        }
    }

    private void displayEntityList(List<String> entities, ViewType<T> viewType) {
        ArrayList<String> menuList = new ArrayList<>(entities);
        menuList.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(viewType.getName(), menuList);
    }

    private void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }

    protected abstract List<String> getEntityList(ViewType<T> viewType, String username);

    // == Getting Entity Command Choice ==
    public Command getEntityMenuChoice(UserType userType, String username, Command command, String selectedEntity) {
        List<Command> menuOptions = menuManager.getPermittedSubMenu(userType, command);
        menuOptions.removeIf(c -> !verifyPermission(c, username, selectedEntity));
        displayEntityMenu(menuOptions, command);

        // TODO generalize this part
        int user_input = inputParser.readInt();
        try {
            return menuOptions.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEntityMenuChoice(userType, username, command, selectedEntity);
        }
    }

    private void displayEntityMenu(List<Command> menuOptions, Command command) {
        List<String> menuNames = new ArrayList<>();
        for (Command menuOption : menuOptions) {
            menuNames.add(menuOption.getName());
        }
        presenter.printMenu(command.getName(), menuNames);
    }

    protected abstract boolean verifyPermission(Command command, String username, String selectedEntity);
}
