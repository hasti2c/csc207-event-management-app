package controllers.menus;

import entities.Event;
import utility.UserType;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.Command;
import utility.EventViewType;
import utility.ViewType;

import java.util.ArrayList;
import java.util.List;

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
    protected String getMenuTitle() {
        return "Event List";
    }

    // TODO do names instead of ids
    @Override
    protected List<String> getEntityList(ViewType<Event> viewType, String username) {
        assert viewType instanceof EventViewType; // TODO don't do this
        EventViewType eventViewType = (EventViewType) viewType;
        switch (eventViewType) {
            case OWNED:
                return userManager.getCreatedEvents(username);
            case ATTENDING:
                return userManager.getAttendingEvents(username);
            case NOT_ATTENDING:
                List<String> published = eventManager.returnPublishedEvents();
                published.removeAll(userManager.getAttendingEvents(username));
                return published;
            case PUBLISHED:
                return eventManager.returnPublishedEvents();
            default:
                return new ArrayList<>();
        }
    }

    @Override
    protected boolean verifyPermission(Command command, String username, String eventID) {
        boolean attending = userManager.getAttendingEvents(username).contains(eventID);
        boolean owned = userManager.getAttendingEvents(username).contains(eventID);
        boolean suspended = eventManager.isSuspended(eventID);
        switch (command) {
            case ATTEND_EVENT:
                return !attending;
            case UNATTEND_EVENT:
                return attending;
            case CHANGE_EVENT_PRIVACY:
            case EDIT_EVENT:
            case DELETE_EVENT:
                return owned;
            case SUSPEND_EVENT:
                return !suspended;
            case UNSUSPEND_EVENT:
                return suspended;
            default:
                return true;
        }
    }
}
