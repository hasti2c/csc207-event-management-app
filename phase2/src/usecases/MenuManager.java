package usecases;

import utility.Command;
import entities.Menu;
import entities.UserTypePermissions;
import entities.User;

import java.util.*;

import static utility.Command.*;

public class MenuManager {
    private List<UserTypePermissions> allUserPermissions;
//    private List<Menu> allMenus;
    private Map<String, Menu> allMenus;

    public MenuManager() {
        allMenus = new HashMap<>();
        // Initialize all Menus
        // Startup Menu
        Menu startUp = new Menu(null, START_UP);
        startUp.setSubCommands(Arrays.asList(SIGN_UP, LOGIN, TRIAL, EXIT));
        allMenus.put(START_UP.getName(), startUp);
        // Main Menu, reaches this menu from LOGIN
        Menu mainLogIn = new Menu(START_UP, LOGIN);
        // Regular users
        mainLogIn.setSubCommands(Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED,
                ACCOUNT_MENU, SAVE, LOG_OUT));
        // Admin users (everything above and also
        mainLogIn.setSubCommands(Collections.singletonList(ADMIN_MENU));
        // Trial users (CREATE_EVENT from regular users)
        mainLogIn.setSubCommands(Arrays.asList(VIEW_PUBLISHED, EXIT_TRIAL));
        allMenus.put(LOGIN.getName(), mainLogIn);
        // Account Menu
        Menu accountMenu = new Menu(LOGIN, ACCOUNT_MENU);
        accountMenu.setSubCommands(Arrays.asList(CHANGE_USERNAME, CHANGE_EMAIL, CHANGE_PASSWORD /*FRIENDS*/, DELETE_ACCOUNT, GO_BACK));
        allMenus.put(ACCOUNT_MENU.getName(), accountMenu);
        // TODO should we have menus for all the leaves and then just set the subCommands as null?
        // Initialize User Type Permissions
        // Admin
        UserTypePermissions adminPerms = new UserTypePermissions(User.UserType.A);
        adminPerms.setPermissions(Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED, ACCOUNT_MENU,
                SAVE, LOG_OUT, ADMIN_MENU, CHANGE_USERNAME, CHANGE_EMAIL, CHANGE_PASSWORD, DELETE_ACCOUNT, GO_BACK));
        allUserPermissions.add(adminPerms);
        // Regular
        UserTypePermissions regularPerms = new UserTypePermissions(User.UserType.R);
        regularPerms.setPermissions(Arrays.asList(CREATE_EVENT, VIEW_ATTENDED, VIEW_UNATTENDED, VIEW_OWNED, ACCOUNT_MENU,
                SAVE, LOG_OUT, CHANGE_USERNAME, CHANGE_EMAIL, CHANGE_PASSWORD, DELETE_ACCOUNT, GO_BACK));
        allUserPermissions.add(regularPerms);
        // Trial
        UserTypePermissions trialPerms = new UserTypePermissions(User.UserType.T);
        trialPerms.setPermissions(Arrays.asList(CREATE_EVENT, VIEW_PUBLISHED, EXIT_TRIAL, GO_BACK));
        allUserPermissions.add(trialPerms);

    }

    public List<Command> generateSubMenu(User.UserType userType, Command command) {
        List<Command> generatedSubMenu = new ArrayList<>();
        List<Command> allSubCommands = allMenus.get(command.getName()).getSubCommands();
        // If the system first starts, there's no user so no user type.
        if (userType == null && command.equals(START_UP)){
            generatedSubMenu = allSubCommands;
        }
        else{

        }
        return generatedSubMenu;
    }
}
