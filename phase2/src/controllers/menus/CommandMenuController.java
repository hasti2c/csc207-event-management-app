package controllers.menus;

import entities.UserType;
import usecases.MenuManager;
import utility.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandMenuController extends MenuController {
    /**
     * Constructs a CommandMenuController.
     * @param menuManager A menuManager.
     */
    public CommandMenuController(MenuManager menuManager) {
        super(menuManager);
    }

    /**
     * Displays the appropriate menu to user, gets user command choice and returns it.
     * @param userType UserType of current user.
     * @param command The most recently called command, the menu for which should be displayed.
     * @return Command chosen by the user from the menu.
     */
    public Command getUserMenuChoice(UserType userType, Command command) {
        List<Command> menuOptions = menuManager.getPermittedSubMenu(userType, command);
        displayMenu(menuOptions, command);
        return getMenuChoice(menuOptions);
    }

    private void displayMenu(List<Command> menuOptions, Command command) {
        List<String> menuNames = new ArrayList<>();
        for (Command menuOption : menuOptions) {
            menuNames.add(menuOption.getName());
        }
        presenter.printMenu(command.getName(), menuNames);
    }
}
