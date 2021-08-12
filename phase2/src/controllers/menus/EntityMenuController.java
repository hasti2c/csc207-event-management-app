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

/*
1. view type: -> user type
    published
    attending
    not attending
    owned

2. event list -> user

3. event commands -> user type & user
    attend/unattend
    suspend/unsuspend
    delete/undelete
    edit
    change privacy status
 */

public abstract class EntityMenuController <T> extends MenuController {
    protected final UserManager userManager;
    protected final EventManager eventManager;

    public EntityMenuController(MenuManager menuManager, UserManager userManager, EventManager eventManager) {
        super(menuManager);
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    // == Getting View Type ==
    public ViewType<T> getViewTypeChoice(UserType userType, Command command) throws ExitException {
        List<ViewType<T>> viewTypes = getViewTypePermissions(userType);
        displayViewTypeMenu(viewTypes, command);
        return getMenuChoice(viewTypes, true);
    }

    private void displayViewTypeMenu(List<ViewType<T>> viewTypes, Command command) {
        List<String> viewTypeNames = new ArrayList<>();
        for (ViewType<T> viewType: viewTypes) {
            viewTypeNames.add(viewType.getName());
        }
        viewTypeNames.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(command.getName(), viewTypeNames);
    }

    protected abstract List<ViewType<T>> getViewTypePermissions(UserType userType);

    // == Getting Entity Choice ==
    public String getEntityChoice(ViewType<T> viewType, String username) throws ExitException {
        List<String> entities = getEntityList(viewType, username);
        displayEntityList(entities, viewType);
        return getMenuChoice(entities, true);
    }

    private void displayEntityList(List<String> entities, ViewType<T> viewType) {
        ArrayList<String> menuList = new ArrayList<>(entities);
        menuList.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(viewType.getName(), menuList);
    }

    protected abstract List<String> getEntityList(ViewType<T> viewType, String username);

    // == Getting Entity Command Choice ==
    public Command getEntityMenuChoice(UserType userType, String username, Command command, String selectedEntity) {
        List<Command> menuOptions = menuManager.getPermittedSubMenu(userType, command);
        menuOptions.removeIf(c -> !verifyPermission(c, username, selectedEntity));
        displayEntityMenu(menuOptions, command);
        return getMenuChoice(menuOptions);
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
