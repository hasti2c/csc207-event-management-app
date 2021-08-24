package controllers.menus;

import entities.User;
import utility.UserType;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.*;

import java.util.ArrayList;
import java.util.List;

/**
 * EntityMenuController that handles tasks related to menus that have to do with User lists.
 */
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
    protected String getListTitle() {
        return "User List";
    }

    @Override
    protected List<String> getEntityList(ViewType<User> viewType, String username) {
        assert viewType instanceof UserViewType;
        UserViewType userViewType = (UserViewType) viewType;

        List<String> userList;
        boolean suspensionCheck = true;
        switch (userViewType) {
            case ALL:
                userList = userManager.getUsernameList();
                break;
            case FRIENDS:
                userList = userManager.getFriends(username);
                break;
            case SUSPENDED:
                userList = userManager.getSuspendedList();
                suspensionCheck = false; // We want suspended users to be included.
                break;
            default:
                return new ArrayList<>();
        }

        userList = new ArrayList<>(userList); // This is done so that original list isn't mutated.
        userList.remove(username);
        if (suspensionCheck)
            userList.removeIf(userManager::isSuspended);
        return userList;
    }

    @Override
    protected List<String> getPrintableList(List<String> options) {
        return new ArrayList<>(options);
    }

    @Override
    protected boolean verifyPermission(Command command, String username, String selectedUser) {
        boolean friend = userManager.getFriends(username).contains(selectedUser);
        boolean suspended = userManager.isSuspended(username);
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

    @Override
    protected String getMenuTitle() {
        return "Viewing User";
    }
}
