package controllers.menus;

import entities.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.MenuManager;
import utility.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandMenuController extends MenuController {
    public CommandMenuController(MenuManager menuManager) {
        super(menuManager);
    }

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
