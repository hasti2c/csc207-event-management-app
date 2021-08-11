package view;

import entities.Event;
import entities.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.UserManager;
import utility.Command;

import java.util.ArrayList;
import java.util.List;

public class EventViewController {
    private EventManager eventManager;
    private UserManager userManager;
    private MenuManager menuManager;
    private Presenter presenter;
    private InputParser inputParser;

    public EventViewType getEventViewTypeChoice(UserType userType, Command command) {
        List<EventViewType> viewTypes = menuManager.getPermittedViewTypes(userType, command);
        displayViewTypeMenu(viewTypes, command);
        int user_input = inputParser.readInt();
        try {
            return viewTypes.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEventViewTypeChoice(userType, command);
        }
    }

    public String getEventChoice(EventViewType viewType, String username) {
        List<String> events = getEventList(viewType, username);
        displayEventList(events, viewType);
        int user_input = inputParser.readInt();
        try {
            return events.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            invalidInput();
            return getEventChoice(viewType, username);
        }
    }

    private void displayViewTypeMenu(List<EventViewType> viewTypes, Command command) {
        List<String> viewTypeNames = new ArrayList<>();
        for (EventViewType viewType : viewTypes) {
            viewTypeNames.add(viewType.getName());
        }
        presenter.printMenu(command.getName(), viewTypeNames);
    }

    private void displayEventList(List<String> eventList, EventViewType viewType) {
        presenter.printMenu(viewType.getName(), eventList);
    }

    // TODO work with Event insted of EventID maybe?
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

    private void invalidInput() {
        presenter.printText("You did not enter a valid option, try again");
    }
}
