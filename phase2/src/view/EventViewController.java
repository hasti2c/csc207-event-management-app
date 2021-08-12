package view;

import controllers.EventController;
import controllers.ExitException;
import entities.Event;
import entities.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.AppConstant;
import utility.Command;

import java.util.ArrayList;
import java.util.List;

public class EventViewController {
    private final MenuManager menuManager;
    private final EventManager eventManager;
    private final UserManager userManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public EventViewController(MenuManager menuManager, EventManager eventManager, UserManager userManager) {
        this.menuManager = menuManager;
        this.eventManager = eventManager;
        this.userManager = userManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    // ==  Getting View Type ==
    public EventViewType getEventViewTypeChoice(UserType userType, Command command) throws ExitException {
        List<EventViewType> viewTypes = menuManager.getPermissions(userType).getEventViewPermissions();
        displayViewTypeMenu(viewTypes, command);

        int user_input = inputParser.readInt();
        if (user_input == viewTypes.size()) {
            throw new ExitException();
        }
        try {
            return viewTypes.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEventViewTypeChoice(userType, command);
        }
    }

    private void displayViewTypeMenu(List<EventViewType> viewTypes, Command command) {
        List<String> viewTypeNames = new ArrayList<>();
        for (EventViewType viewType : viewTypes) {
            viewTypeNames.add(viewType.getName());
        }
        viewTypeNames.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(command.getName(), viewTypeNames);
    }

    // == Getting Event Choice ==
    public String getEventChoice(EventViewType viewType, String username) throws ExitException {
        List<String> events = getEventList(viewType, username);
        displayEventList(events, viewType);

        int user_input = inputParser.readInt();
        if (user_input == events.size()) {
            throw new ExitException();
        }
        try {
            return events.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEventChoice(viewType, username);
        }
    }

    private List<String> getEventList(EventViewType viewType, String username) {
        switch (viewType) {
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

    private void displayEventList(List<String> eventList, EventViewType viewType) {
        ArrayList<String> menuList = new ArrayList<>(eventList);
        menuList.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu(viewType.getName(), menuList);
    }

    // == Getting Event Command Choice ==
    public Command getEventMenuChoice(UserType userType, String username, Command command, String eventID) {
        List<Command> menuOptions = menuManager.getPermittedSubMenu(userType, command);
        menuOptions.removeIf(c -> !verifyPermission(c, username, eventID));
        displayEventMenu(menuOptions, command);
        int user_input = inputParser.readInt();
        // TODO generalize this part
        try {
            return menuOptions.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEventMenuChoice(userType, username, command, eventID);
        }
    }


    private boolean verifyPermission(Command command, String username, String eventID) {
        boolean attending = userManager.getAttendingEvents(username).contains(eventID);
        boolean owned = userManager.getAttendingEvents(username).contains(eventID);
        // TODO add deleted && suspended
        boolean deleted = false;
        boolean suspended = false;
        switch (command) {
            case ATTEND_EVENT:
                return !attending;
            case UNATTEND_EVENT:
                return attending;
            case CHANGE_EVENT_PRIVACY:
            case EDIT_EVENT:
                return owned;
            case DELETE_EVENT:
                return owned && !deleted;
            case UNDELETE_EVENT:
                return owned && deleted;
            case SUSPEND_EVENT:
                return !suspended;
            case UNSUSPEND_EVENT:
                return suspended;
            default:
                return true;
        }
    }

    private void displayEventMenu(List<Command> menuOptions, Command command) {
        List<String> menuNames = new ArrayList<>();
        for (Command menuOption : menuOptions) {
            menuNames.add(menuOption.getName());
        }
        presenter.printMenu(command.getName(), menuNames);
    }

    private void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }
}
