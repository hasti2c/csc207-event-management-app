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
    // TODO map instead of list?
    private final List<Permissions> allPermissions;
    private final IGateway<Menu> menuGateway;
    private final IGateway<Permissions> userPermissionsGateway;

    public MenuManager(IGateway<Menu> menuGateway, IGateway<Permissions> permissionsGateway) {
        this.menuGateway = menuGateway;
        allMenus = menuGateway.getElementMap();
        // TODO should we have menus for all the leaves and then just set the subCommands as null?

        this.userPermissionsGateway = permissionsGateway;
        allPermissions = permissionsGateway.getAllElements();
    }

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

    public void saveAllMenuInfo() {
        menuGateway.saveAllElements(allMenus);
        userPermissionsGateway.saveAllElements(allPermissions);
    }

    // Helpers

    /**
     *
     * @param userType
     * @return
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
