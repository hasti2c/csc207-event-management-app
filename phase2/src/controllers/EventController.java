package controllers;

import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.TemplateManager;
import entities.User;
import usecases.UserManager;
import utility.Pair;

import static utility.AppConstant.*;

import java.util.*;

/**
 * Controller handling all event related requests.
 */

public class EventController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;
    public enum ViewType {
        OWNED,
        ATTENDING,
        NOT_ATTENDING,
        PUBLISHED
    }

    public EventController(UserManager userManager, EventManager eventManager, TemplateManager templateManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    // == Viewing ==
    /**
     * Displays a list of events that the user can look through, see details for and register/unregister for.
     * @param username the username of the user
     * @param viewType the type of menu that should be shown
     */
    public void browseEvents(String username, ViewType viewType) {
        while (true) {
            List<String> eventIDList = getEventIdList(viewType, username);
            List<String> eventNameList = eventManager.returnEventNamesListFromIdList(eventIDList);

            int eventIndex = getEventChoice(eventNameList);
            if (eventIndex == eventNameList.size()) {
                break;
            }
            String eventID = eventIDList.get(eventIndex);
            viewEvent(viewType, username, eventID);
            // TODO Should separate view event and menu selection
        }
    }

    // TODO add to command enum
    private void viewEvent(ViewType viewType, String username, String eventID) {
        List<String> menu = getEventMenu(viewType);
        if (viewType == ViewType.OWNED)
            viewEventMetaDetails(eventID);
        viewEventDetails(eventID);
        boolean isRunning = true;
        while (isRunning) {
            presenter.printMenu("Viewing Selected Event", menu);
            int userInput = getChoice(1, menu.size());
            switch (menu.get(userInput - 1)) {
                case "Attend Event":
                    isRunning = !attendEvent(username, eventID);
                    break;
                case "Un-Attend Event":
                    isRunning = !unattendEvent(username, eventID);
                    break;
                case "Delete Event":
                    deleteEvent(username, eventID);
                    return;
                case "Edit Event":
                    editEvent(username, eventID);
                    break;
                case "Change Published Status":
                    changePublishStatus(eventID);
                    break;
                case "Go Back":
                    return;
            }
        }
    }

    // == ViewType ==
    private List<String> getEventIdList(ViewType viewType, String username) {
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

    private List<String> getEventMenu(ViewType viewType) {
        switch (viewType) {
            case OWNED:
                return Arrays.asList("Delete Event", "Edit Event", "Change Published Status", "Go Back");
            case ATTENDING:
                return Arrays.asList("Un-Attend Event", "Go Back");
            case NOT_ATTENDING:
                return Arrays.asList("Attend Event", "Go Back");
            case PUBLISHED:
                return Collections.singletonList("Go Back");
            default:
                return new ArrayList<>();
        }
    }

    // === Viewing Single Event ===
    /**
     * Prints details of a single event.
     *
     * @param eventID - ID of the event
     */
    private void viewEventDetails(String eventID) {
        this.presenter.printEntity(eventManager.returnEventDetails(eventID));
    }

    /**
     * Prints the "meta data" of a single event.
     *
     * @param eventID ID of the event
     */
    private void viewEventMetaDetails(String eventID) {
        this.presenter.printEntity(eventManager.returnEventAsMap(eventID));
    }

    // == Creating & Deleting ==
    /**
     * Creates a new event based on chosen template and adds to User's owned events.
     *
     * @param templateName name of the template
     * @param username username of the currently logged in user
     */
    public void createNewEvent(String templateName, String username) {
        if (templateName == null || templateName.isEmpty() || username == null || username.isEmpty()) {
            return;
        }

        String newEventID = eventManager.createEvent(templateName, username);
        userManager.createEvent(username, newEventID);
        populateFieldValues(newEventID, username);

        presenter.printText("Your event has been successfully created.");
        if (userManager.retrieveUserType(username) == User.UserType.T){
            presenter.printText("Since you are a trial user, your event will not be saved once you leave the system. " +
                    "You may choose to publish the event to view it while you are currently using the program.");
        }
        changePublishStatus(newEventID);
    }

    private void populateFieldValues(String eventId, String username) {
        Map<String, Pair<Class<?>, Boolean>> fieldMap = eventManager.returnFieldNameAndFieldSpecs(eventId);
        for (Map.Entry<String, Pair<Class<?>, Boolean>> entry : fieldMap.entrySet()) {
            try {
                Object value = readFieldValue(eventId, entry.getKey(), entry.getValue().getFirst().getSimpleName(),
                        entry.getValue().getSecond());
                eventManager.enterFieldValue(eventId, entry.getKey(), value);
            } catch (ExitException e) {
                deleteEvent(username, eventId);
                presenter.printText(EXITING_TEXT);
                return;
            }
        }
    }

    private Object readFieldValue(String eventId, String fieldName, String dataType, boolean required) throws ExitException {
        presenter.printText("Enter " + fieldName + " (" + (required? "Required" : "Not Required") + "):");
        while (true) {
            String userInput = inputParser.readLine();
            if (userInput.equalsIgnoreCase(EXIT_TEXT)) {
                throw new ExitException();
            } else if (eventManager.checkDataValidation(eventId, fieldName, userInput)) {
                return eventManager.convertToCorrectDataType(eventId, fieldName, userInput);
            } else {
                presenter.printText("Please try again. Enter " + fieldName + " (" + dataType + "):");
            }
        }
    }

    /**
     * Completely deletes specified event from system.
     *
     * @param username username of the currently logged in user
     * @param eventID  unique identifier for event
     */
    private void deleteEvent(String username, String eventID) {
        presenter.printText("Are you sure you wish to delete your event? This action cannot be undone. (Y/N)");
        if (getYesNo()) {
            this.userManager.deleteEvent(username, eventID);
            this.eventManager.deleteEvent(eventID);
            presenter.printText("Event was deleted.");
        }
    }

    // == Editing ===
    // TODO need to implement edit for phase 2
    public void editEvent (String username, String eventID) {
        Map<String, Pair<Class<?>, Boolean>> eventMap = eventManager.returnFieldNameAndFieldSpecs(eventID);
        List<String> fieldNames = new ArrayList<String>(eventMap.keySet());
        fieldNames.add(MENU_EXIT_OPTION);

        while (true) {
            this.presenter.printMenu("Choose field to edit.", fieldNames);
            int userInput = getChoice(1, fieldNames.size());
            if (fieldNames.get(userInput).equals(MENU_EXIT_OPTION)) {
                return;
            }
            try {
                Object value = readFieldValue(
                        eventID,
                        fieldNames.get(userInput - 1),
                        eventMap.get(fieldNames.get(userInput - 1)).getFirst().getSimpleName(),
                        eventMap.get(fieldNames.get(userInput - 1)).getSecond());
                eventManager.enterFieldValue(eventID, fieldNames.get(userInput), value);
            } catch (ExitException e) {
                presenter.printText(EXITING_TEXT);
            }

        }




    }

    /**
     * If published change to unpublished and vice versa
     *
     * @param eventID unique identifier for an event
     */
    private void changePublishStatus (String eventID) {
        boolean published = eventManager.isPublished(eventID);
        String current = (published ? "" : "un") + "publish";
        String opposite = (published ? "un" : "") + "publish";
        presenter.printText("Your event is currently " + current + "ed, would you like to " + opposite + "? (Y/N)");
        if (getYesNo()) {
            if (eventManager.togglePublish(eventID)){
                presenter.printText("Your event has been successfully " + opposite + "ed.");
            } else {
                presenter.printText("Your event could not be " + opposite + "ed.");
            }
        }
    }

    // == Attending & Unattending ==
    /**
     * Adds selected event to the User's list of events they are attending.
     * @param username username of the currently logged in user
     * @param eventID  unique identifier for event
     * @return true if the user has successfully registered for the event
     */
    private boolean attendEvent(String username, String eventID) {
        boolean result = eventManager.attendEvent(eventID);
        if (result) {
            userManager.attendEvent(username, eventID);
            presenter.printText("You have successfully registered for the event.");
        } else {
            presenter.printText("Sorry this event is full.");
        }
        return result;
    }

    /**
     * Removes selected event to the User's list of events they are attending.
     *
     * @param username username of the currently logged in user
     * @param eventID unique identifier for event
     * @return true if the user has successfully unregistered for the event
     */
    private boolean unattendEvent(String username, String eventID) {
        boolean result = userManager.unAttendEvent(username, eventID);
        if (result) {
            eventManager.unAttendEvent(eventID);
            presenter.printText("You have successfully unregistered for the event.");
        } else {
            presenter.printText("You could not leave this event.");
        }
        return result;
    }

    // TODO refactor from here
    // TODO presenter?
    /**
     * Prints a list of all public events created by all users.
     */
    private int getEventChoice(List<String> eventNameList) {
        List<String> temp = new ArrayList<>(eventNameList);
        temp.add(MENU_EXIT_OPTION);
        presenter.printMenu("Event List", temp);
        return getChoice(1, temp.size()) - 1;
    }

    // TODO presenter?
    /**
     * Forces user to type either "Y" or "N"
     *
     * @return return true if user typed "Y" and false if "N"
     */
    private boolean getYesNo() {
        String userInput = inputParser.readLine();
        while (!userInput.equalsIgnoreCase("Y") && !userInput.equalsIgnoreCase("N")) {
            presenter.printText("Type Y or N");
            userInput = inputParser.readLine();
        }
        return userInput.equalsIgnoreCase("Y");
    }

    // === Helpers ===

    /**
     * Forces user to enter an int in the range.
     *
     * @param lowBound lowest accepted input
     * @param highBound highest accepted input
     * @return the value entered
     */
    private int getChoice(int lowBound, int highBound) {
        int choice = inputParser.readInt();
        while (choice < lowBound || choice > highBound) {
            presenter.printText("Invalid choice. Please enter a number between " + lowBound + " and " + highBound);
            choice = inputParser.readInt();
        }
        return choice;
    }

    /**
     * Prints a list of templates and returns the user's choice. If the choice is longer than the list of templates,
     * it means the user chose to go back.
     * @param username username of the user choosing a template
     * @return returns the index of the chosen template + 1 (starts at 1 instead of 0)
     */
    public int chooseTemplate(String username) {
        List<String> templateList = templateManager.returnTemplateNames();
        templateList.add(MENU_EXIT_OPTION);
        presenter.printMenu("Available Templates", templateList);
        return getChoice(1, templateList.size());
    }
}
