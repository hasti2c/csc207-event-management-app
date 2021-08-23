package controllers;

import controllers.menus.EntityMenuController;
import entities.Event;
import entities.User;
import utility.UserType;
import presenter.InputParser;
import presenter.Presenter;
import usecases.EventManager;
import usecases.MenuManager;
import usecases.TemplateManager;
import usecases.UserManager;
import utility.*;
import controllers.menus.EventMenuController;

import static utility.AppConstant.*;
import static utility.Command.*;
import static utility.UserType.*;

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
    private final EntityMenuController<Event> menuController;

    public EventController(UserManager userManager, EventManager eventManager, TemplateManager templateManager, MenuManager menuManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = Presenter.getInstance();
        this.inputParser = InputParser.getInstance();
        this.menuController = new EventMenuController(menuManager, userManager, eventManager);
    }

    // == Viewing ==
    /**
     * Displays the appropriate ViewType options to user, prompts user for choice.
     * Then displays Events to user, prompts user for choice.
     * Finally, calls browseEvent on selected Event.
     *
     * @param userType The userType of the current user.
     * @param username The username of the current user
     */
    public void viewEventTypesList(UserType userType, String username) {
        while (true) {
            try {
                ViewType<Event> viewType = menuController.getViewTypeChoice(userType);
                browseEvent(viewType, userType, username);
            } catch (ExitException e) {
                return;
            }
        }
    }

    private void browseEvent(ViewType<Event> viewType, UserType userType, String username){
        while (true) {
            try {
                String selectedUser = menuController.getEntityChoice(viewType, username);
                viewEvent(userType, username, selectedUser);
            } catch (ExitException e) {
                return;
            }
        }
    }

    private void viewEvent(UserType userType, String username, String eventID) throws ExitException {
        if (userType == ADMIN || userManager.getCreatedEvents(username).contains(eventID))
            viewEventMetaDetails(eventID);
        viewEventDetails(eventID);
        while (true) {
            Command userInput = menuController.getEntityMenuChoice(userType, username, BROWSE_EVENTS, eventID);
            runUserCommand(userInput, username, eventID);
        }
    }

    private void runUserCommand(Command command, String username, String eventId) throws ExitException {
        switch (command) {
            case ATTEND_EVENT:
                attendEvent(username, eventId);
                return;
            case UNATTEND_EVENT:
                unattendEvent(username, eventId);
                return;
            case CHANGE_EVENT_PRIVACY:
                changeEventPrivacy(eventId);
                return;
            case EDIT_EVENT:
                editEvent(username, eventId);
                return;
            case DELETE_EVENT:
                deleteEvent(username, eventId);
                return;
            case SUSPEND_EVENT:
            case UNSUSPEND_EVENT:
                changeSuspensionStatus(eventId);
                return;
            case VIEW_CREATOR:
                // TODO
                return;
            case GO_BACK:
                throw new ExitException();
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
     * Prints the "metadata" of a single event.
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
        if (userManager.retrieveUserType(username) == UserType.TRIAL) {
            presenter.printText("Since you are a trial user, your event will not be saved once you leave the system. " +
                    "You can view the event you've just created by looking at \" owned events \".");
        }
        else {
            changeEventPrivacy(newEventID);
        }
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
            } else if (eventManager.checkDataValidation(eventId, fieldName, userInput) && required) {
                return eventManager.convertToCorrectDataType(eventId, fieldName, userInput);
            } else if (eventManager.checkDataValidation(eventId, fieldName, userInput) && !required) {
                return null;
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
        if (inputParser.readBoolean()) {
            this.userManager.deleteEvent(username, eventID);
            this.eventManager.deleteEvent(eventID);
            presenter.printText("Event was deleted.");
        }
    }

    // == Editing ===
    // TODO refactor
    
    /**
     * Prompts current user of which field of Event with matching eventId to edit.
     * Then prompts current user to edit said field.
     * 
     * @param username The username of the current user
     * @param eventID Id of Event being edited
     */
    public void editEvent (String username, String eventID) {
        Map<String, Pair<Class<?>, Boolean>> eventMap = eventManager.returnFieldNameAndFieldSpecs(eventID);
        List<String> fieldNames = new ArrayList<>(eventMap.keySet());
        fieldNames.add(MENU_EXIT_OPTION);

        while (true) {
            presenter.printMenu("Choose field to edit.", fieldNames);
            try {
                String userInput = inputParser.getMenuChoice(fieldNames, true);
                Object value = readFieldValue(eventID, userInput, eventMap.get(userInput).getFirst().getSimpleName(),
                        eventMap.get(userInput).getSecond());
                eventManager.enterFieldValue(eventID, userInput, value);
            } catch (ExitException e) {
                presenter.printText(EXITING_TEXT);
                return;
            }
        }
    }

    private void changeEventPrivacy(String eventID) {
        String currentPrivacy = eventManager.getPrivacyTypeName(eventID);
        List<String> validPrivacyTypes = eventManager.getValidPrivacyTypeNames(eventID);
        presenter.printText("The current privacy type is " + currentPrivacy + ". Choose one of the following options:");

        ArrayList<String> menuOptions = new ArrayList<>(validPrivacyTypes);
        menuOptions.add(MENU_EXIT_OPTION);
        presenter.printMenu("Event Privacy Types", menuOptions);
        try {
            String newPrivacyName = inputParser.getMenuChoice(menuOptions, true);
            eventManager.setPrivacyType(eventID, newPrivacyName);
            presenter.printText("The privacy type was changed to " + newPrivacyName);
        } catch (ExitException e) {
            presenter.printText("The privacy type was not changed.");
        }
    }

    private void changeSuspensionStatus(String eventID) {
        boolean suspended = eventManager.isSuspended(eventID);
        String current = (suspended ? "" : "not") + "suspended";
        String action = (suspended ? "un" : "") + "suspend";
        presenter.printText("This event is currently " + current + ". Do you want to " + action + " it? (Y/N)");
        if (inputParser.readBoolean()) {
            eventManager.toggleEventSuspension(eventID);
            presenter.printText("The event was " + action + "ed.");
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
}
