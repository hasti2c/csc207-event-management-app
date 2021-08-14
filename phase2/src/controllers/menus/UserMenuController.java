package controllers.menus;

import entities.User;
import utility.UserType;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.*;

import java.util.ArrayList;
import java.util.List;

public class UserMenuController extends EntityMenuController<User> {
    /**
     * Constructs a UserMenuController.
     * @param menuManager A menuManager.
     * @param userManager A userManager.
     * @param eventManager An eventManager.
     */
    public UserMenuController(MenuManager menuManager, UserManager userManager, EventManager eventManager) {
        super(menuManager, userManager, eventManager);
    }

    @Override
    protected List<ViewType<User>> getViewTypePermissions(UserType userType) {
        return new ArrayList<>(menuManager.getPermissions(userType).getUserViewPermissions());
    }

    @Override
    protected String getMenuTitle() {
        return "User List";
    }

    @Override
    protected List<String> getEntityList(ViewType<User> viewType, String username) {
        assert viewType instanceof UserViewType; // TODO don't do this
        UserViewType userViewType = (UserViewType) viewType;
        switch (userViewType) {
            case ALL:
                return userManager.getUsernameList();
            case FRIENDS:
                return userManager.getFriends(username);
            case SUSPENDED:
                // TODO add getSuspendedUsers to UserManager (and make other methods not return suspended)
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }

    @Override
    protected boolean verifyPermission(Command command, String username, String selectedUser) {
        boolean friend = userManager.getFriends(username).contains(selectedUser);
        boolean suspended = false; // TODO add suspended
        switch (command) {
            case FRIEND_USER:
                return !friend;
            case UNFRIEND_USER:
                return friend;
            case SUSPEND_USER:
                return !suspended;
            case UNSUSPEND_USER:
                return suspended;
            default:
                return true;
        }
    }
}
