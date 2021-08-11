package controllers;

import entities.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.MenuManager;
import utility.Command;

import java.util.ArrayList;
import java.util.List;

public class MenuController {
    private final MenuManager menuManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public MenuController(MenuManager menuManager) {
        this.menuManager = menuManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    public Command getUserMenuChoice(UserType userType, Command command) {
        List<Command> menuOptions = menuManager.getPermittedSubMenu(userType, command);
        displayMenu(menuOptions, command);
        int user_input = inputParser.readInt();
        try {
            return menuOptions.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getUserMenuChoice(userType, command);
        }
    }

    private void displayMenu(List<Command> menuOptions, Command command) {
        List<String> menuNames = new ArrayList<>();
        for (Command menuOption : menuOptions) {
            menuNames.add(menuOption.getName());
        }
        presenter.printMenu(command.getName(), menuNames);
    }

    private void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }
}
