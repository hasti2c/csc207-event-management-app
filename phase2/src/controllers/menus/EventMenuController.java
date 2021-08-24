package controllers.menus;

import entities.Event;
import utility.*;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;

import java.util.ArrayList;
import java.util.List;

import static utility.AppConstant.MENU_EXIT_OPTION;

/**
 * EntityMenuController that handles tasks related to menus that have to do with Event lists.
 */
public class EventMenuController extends EntityMenuController<Event> {
    /**
     * Constructs an EventMenuController.
     * @param menuManager A menuManager.
     * @param userManager A userManager.
     * @param eventManager An eventManager.
     */
    public EventMenuController(MenuManager menuManager, UserManager userManager, EventManager eventManager) {
        super(menuManager, userManager, eventManager);
    }

    @Override
    protected List<ViewType<Event>> getViewTypePermissions(UserType userType) {
        return new ArrayList<>(menuManager.getPermissions(userType).getEventViewPermissions());
    }

    @Override
    protected String getListTitle() {
        return "Event List";
    }

    @Override
    protected List<String> getEntityList(ViewType<Event> viewType, String username) {
        assert viewType instanceof EventViewType;
        EventViewType eventViewType = (EventViewType) viewType;

        List<String> eventList;
        boolean suspensionCheck = true, accessibilityCheck = true;
        switch (eventViewType) {
            case OWNED:
                eventList = userManager.getCreatedEvents(username);
                suspensionCheck = false; // We want owner to see their suspended events.
                break;
            case ATTENDING:
                eventList = userManager.getAttendingEvents(username);
                break;
            case NOT_ATTENDING:
                eventList = eventManager.getAllEvents();
                eventList.removeAll(userManager.getAttendingEvents(username));
                break;
            case PUBLIC:
                eventList = eventManager.getPublicEvents();
                break;
            case FRIENDS_ONLY:
                eventList = eventManager.getFriendsOnlyEvents();
                break;
            case ALL:
                eventList = eventManager.getAllEvents();
                accessibilityCheck = false; // 'All' should list all events regardless of privacy.
                break;
            case SUSPENDED:
                eventList = eventManager.getSuspendedEvents();
                accessibilityCheck = false; // 'Suspended' should list all events regardless of privacy.
                suspensionCheck = false; // We don't want suspended events to be removed.
                break;
            default:
                eventList = new ArrayList<>();
        }

        eventList = new ArrayList<>(eventList); // This is done so that original list isn't mutated.
        if (accessibilityCheck)
            eventList.removeIf(eventID -> !isAccessible(eventID, username));
        if (suspensionCheck)
            eventList.removeIf(eventManager::isSuspended);
        return eventList;
    }

    @Override
    protected List<String> getPrintableList(List<String> options) {
        return eventManager.returnEventNamesListFromIdList(options);
    }

    private boolean isAccessible(String eventId, String username) {
        EventPrivacyType privacyType = eventManager.getPrivacyType(eventId);
        String owner = eventManager.getOwner(eventId);
        switch (privacyType) {
            case FRIENDS_ONLY:
                return username.equals(owner) || userManager.areFriends(username, owner);
            case PRIVATE:
                return username.equals(owner);
            case PUBLIC:
            default:
                return true;
        }
    }

    @Override
    protected boolean verifyPermission(Command command, String username, String eventID) {
        boolean attending = userManager.getAttendingEvents(username).contains(eventID);
        boolean owned = userManager.getCreatedEvents(username).contains(eventID);
        boolean suspended = eventManager.isSuspended(eventID);
        switch (command) {
            case ATTEND_EVENT:
                return !attending && !suspended;
            case UNATTEND_EVENT:
                return attending && !suspended;
            case CHANGE_EVENT_PRIVACY:
            case EDIT_EVENT:
            case DELETE_EVENT:
                return owned && !suspended;
            case SUSPEND_EVENT:
                return !suspended;
            case UNSUSPEND_EVENT:
                return suspended;
            default:
                return true;
        }
    }

    @Override
    protected String getMenuTitle() {
        return "Viewing Event";
    }
}
