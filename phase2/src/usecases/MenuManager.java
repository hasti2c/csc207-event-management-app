package usecases;

import entities.UserType;
import utility.Command;
import entities.Menu;
import entities.UserTypePermissions;
import entities.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuManager {
    private List<UserTypePermissions> allUserMenu;
    private List<Menu> allMenus;

    public MenuManager() {
        allMenus = new ArrayList<>();
        // initialize Menu and MenuForUserType
        Menu startUp = new Menu();
        startUp.setSuperCommand(null);
        startUp.setCommand(Command.CREATE_EVENT);
        startUp.setSubCommands(Arrays.asList());
        allMenus.add(startUp);

        Menu mainMenu = new Menu();


    }

    public List<Command> listCommand(UserType userType, Command command) {
        if (userType == null){
            // Need to get the menu for the command then return the list of subcommands
        }
        return null;
    }
}
