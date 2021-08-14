package usecases;

import utility.UserType;
import gateways.IGateway;
import utility.Command;
import entities.Menu;
import entities.UserTypePermissions;

import java.util.*;

import static utility.Command.*;

public class MenuManager {
    private final Map<String, Menu> allMenus;
    // TODO map instead of list?
    private final List<UserTypePermissions> allUserPermissions;
    private final IGateway<Menu> menuGateway;
    private final IGateway<UserTypePermissions> userPermissionsGateway;

    public MenuManager(IGateway<Menu> menuGateway, IGateway<UserTypePermissions> userPermissionsGateway) {
        this.menuGateway = menuGateway;
        allMenus = menuGateway.getElementMap();
        // TODO should we have menus for all the leaves and then just set the subCommands as null?

        this.userPermissionsGateway = userPermissionsGateway;
        allUserPermissions = userPermissionsGateway.getAllElements();
    }

    public List<Command> getPermittedSubMenu(UserType userType, Command command) {
        List<Command> permittedSubMenu = new ArrayList<>();
        List<Command> allSubCommands = allMenus.get(command.getName()).getSubCommands();
        // If the system first starts, there's no user so no user type.
        if (userType == null && command.equals(START_UP)){
            permittedSubMenu = allSubCommands;
        } else {
            List<Command> userTypePermissions = getPermissions(userType).getCommandPermissions();
            for (Command commandItem: allSubCommands){
                if (userTypePermissions.contains(commandItem)) {
                    permittedSubMenu.add(commandItem);
                }
            }
        }
        return permittedSubMenu;
    }

    public void saveAllMenuInfo() {
        menuGateway.saveAllElements(allMenus);
        userPermissionsGateway.saveAllElements(allUserPermissions);
    }

    // Helpers

    /**
     *
     * @param userType
     * @return
     */
    public UserTypePermissions getPermissions(UserType userType) {
        for(UserTypePermissions perms: allUserPermissions) {
            if (perms.getUserType() == userType) {
                return perms;
            }
        }
        return null;
    }
}
