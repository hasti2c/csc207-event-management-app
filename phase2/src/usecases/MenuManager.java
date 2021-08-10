package usecases;

import entities.UserType;
import gateways.IGateway;
import utility.Command;
import entities.Menu;
import entities.UserTypePermissions;

import java.util.*;

import static utility.Command.*;

public class MenuManager {
    private List<UserTypePermissions> allUserPermissions;
//    private List<Menu> allMenus;
    private Map<String, Menu> allMenus;
    private IGateway<Menu> menuGateway;

    public MenuManager(IGateway<Menu> menuGateway) {
        this.menuGateway = menuGateway;
        allMenus = menuGateway.getElementMap();

        // TODO should we have menus for all the leaves and then just set the subCommands as null?
        // Initialize User Type Permissions
        // Admin
        UserTypePermissions adminPerms = new UserTypePermissions(UserType.ADMIN);
        adminPerms.setPermissions(Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED, ACCOUNT_MENU,
                SAVE, LOG_OUT, ADMIN_MENU, CHANGE_USERNAME, CHANGE_EMAIL, CHANGE_PASSWORD, DELETE_ACCOUNT, GO_BACK));
        allUserPermissions.add(adminPerms);
        // Regular
        UserTypePermissions regularPerms = new UserTypePermissions(UserType.REGULAR);
        regularPerms.setPermissions(Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED, ACCOUNT_MENU,
                SAVE, LOG_OUT, CHANGE_USERNAME, CHANGE_EMAIL, CHANGE_PASSWORD, DELETE_ACCOUNT, GO_BACK));
        allUserPermissions.add(regularPerms);
        // Trial
        UserTypePermissions trialPerms = new UserTypePermissions(UserType.TRIAL);
        trialPerms.setPermissions(Arrays.asList(CREATE_EVENT, VIEW_PUBLISHED, EXIT_TRIAL, GO_BACK));
        allUserPermissions.add(trialPerms);
    }

    public List<Command> getPermittedSubMenu(UserType userType, Command command) {
        List<Command> permittedSubMenu = new ArrayList<>();
        List<Command> allSubCommands = allMenus.get(command.getName()).getSubCommands();
        List<Command> userTypePermissions = getPermissions(userType);
        // If the system first starts, there's no user so no user type.
        if(userTypePermissions == null) {
            return permittedSubMenu;
        }
        if (userType == null && command.equals(START_UP)){
            permittedSubMenu = allSubCommands;
        } else{
            for (Command commandItem: allSubCommands){
                if (userTypePermissions.contains(commandItem)) {
                    permittedSubMenu.add(commandItem);
                }
            }
        }
        return permittedSubMenu;
    }

    public void saveAllMenus() {
        menuGateway.saveAllElements(allMenus);
    }

    // Helpers

    /**
     *
     * @param userType
     * @return
     */
    private List<Command> getPermissions(UserType userType) {
        for(UserTypePermissions perms: allUserPermissions) {
            if(perms.getUserType() == userType) {
                return perms.getPermissions();
            }
        }
        return null;
    }
}
