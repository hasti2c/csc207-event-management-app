package controllers.menus;

import controllers.ExitException;
import utility.UserType;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.AppConstant;
import utility.Command;
import utility.ViewType;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityMenuController <T> extends MenuController {
    protected final UserManager userManager;
    protected final EventManager eventManager;

    /**
     * Constructs an EntityMenuController.
     * @param menuManager A menuManager.
     * @param userManager A UserManager.
     * @param eventManager An EventManager.
     */
    public EntityMenuController(MenuManager menuManager, UserManager userManager, EventManager eventManager) {
        super(menuManager);
        this.userManager = userManager;
        this.eventManager = eventManager;
    }

    // == Getting View Type ==

    /**
     * Displays the appropriate ViewType options to user, gets user choice & returns it.
     * @param userType The userType of the current user.
     * @param command The most recently called command, the view types for which should be shown. This is assumed to be
     *                a view type that needs view type list to be shown.
     * @return ViewType chosen by user.
     * @throws ExitException If user chooses exit option instead of a command.
     */
    public ViewType<T> getViewTypeChoice(UserType userType) throws ExitException {
        List<ViewType<T>> viewTypes = getViewTypePermissions(userType);
        displayViewTypeMenu(viewTypes);
        return getMenuChoice(viewTypes, true);
    }

    private void displayViewTypeMenu(List<ViewType<T>> viewTypes) {
        List<String> viewTypeNames = new ArrayList<>();
        for (ViewType<T> viewType: viewTypes) {
            viewTypeNames.add(viewType.getName());
        }
        viewTypeNames.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(getMenuTitle(), viewTypeNames);
    }

    /**
     * @param userType UserType of the current user.
     * @return Relevant ViewTypePermissions for the current user (retrieved from UserTypePermissions). This is a list of
     * view types a user of this userType has access to.
     */
    protected abstract List<ViewType<T>> getViewTypePermissions(UserType userType);

    protected abstract String getMenuTitle();

    // == Getting Entity Choice ==

    /**
     * Displays the appropriate entity list to the user, gets user choice & returns it.
     * @param viewType The viewType chosen by the user.
     * @param username Username of current user.
     * @return Entity choice of user (the string id).
     * @throws ExitException If user chooses exit option instead of a command.
     */
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

    /**
     * @param viewType ViewType chosen by user, the list for which should be chosen.
     * @param username Username of current user.
     * @return Entity list that should be shown to this specific user. This is based on user type, and the specific
     *         user information.
     */
    protected abstract List<String> getEntityList(ViewType<T> viewType, String username);

    // == Getting Entity Command Choice ==

    /**
     * Displays the appropriate menu of actions on selected entity, gets user command choice & returns it.
     * @param userType UserType of current user.
     * @param username Username of current user.
     * @param command Most recent command chosen by the user, the subcommands of which should be shown.
     * @param selectedEntity The entity selected by the user. The commands shown interact with this specific entity.
     * @return Command choice of the user.
     */
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

    /**
     * Verifies whether the command should be accessible by the user. This is based on the specific user information &
     * the users relation with the selectedEntity. UserType or userTypePermissions are not taken into account here.
     * @param command The command which the user's access to should be verified.
     * @param username Username of current user.
     * @param selectedEntity Entity selected by the user.
     * @return True if and only if this command should be accessible by the user.
     */
    protected abstract boolean verifyPermission(Command command, String username, String selectedEntity);
}