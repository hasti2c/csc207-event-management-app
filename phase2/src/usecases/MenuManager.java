package usecases;

import utility.UserType;
import gateways.IGateway;
import utility.Command;
import entities.Menu;
import entities.Permissions;

import java.util.*;

import static utility.Command.*;

public class MenuManager {
    private final Map<String, Menu> allMenus;
    private final List<Permissions> allPermissions;
    private final IGateway<Menu> menuGateway;
    private final IGateway<Permissions> userPermissionsGateway;

    /**
     * Creates a new MenuManager.
     * @param menuGateway Gateway for reading and saving menus.
     * @param permissionsGateway Gateway for reading and saving permissions.
     */
    public MenuManager(IGateway<Menu> menuGateway, IGateway<Permissions> permissionsGateway) {
        this.menuGateway = menuGateway;
        allMenus = menuGateway.getElementMap();

        this.userPermissionsGateway = permissionsGateway;
        allPermissions = permissionsGateway.getAllElements();
    }

    /**
     * Based on user type and command, return List<Command> appropriate to the 
     * userType of current user
     * 
     * @return List of Commands
     */
    public List<Command> getPermittedSubMenu(UserType userType, Command command) {
        List<Command> permittedSubMenu = new ArrayList<>();
        List<Command> allSubCommands = allMenus.get(command.getName()).getSubCommands();
        // If the system first starts, there's no user so no user type.
        if (userType == null && command.equals(START_UP)){
            permittedSubMenu = allSubCommands;
        } else {
            List<Command> permissions = getPermissions(userType).getCommandPermissions();
            for (Command commandItem: allSubCommands){
                if (permissions.contains(commandItem)) {
                    permittedSubMenu.add(commandItem);
                }
            }
        }
        return permittedSubMenu;
    }

    /**
     * Save all menu info
     */
    public void saveAllMenuInfo() {
        menuGateway.saveAllElements(allMenus);
        userPermissionsGateway.saveAllElements(allPermissions);
    }

    // Helpers

    /**
     * Get matching Permissions object based on matching UserType
     * @param userType UserType whose permissions we want.
     * @return Permissions object
     */
    public Permissions getPermissions(UserType userType) {
        for(Permissions perms: allPermissions) {
            if (perms.getUserType() == userType) {
                return perms;
            }
        }
        return null;
    }
}
