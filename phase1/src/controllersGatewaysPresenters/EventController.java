package controllersGatewaysPresenters;

import entitiesAndUseCases.EventManager;
import entitiesAndUseCases.TemplateManager;
import entitiesAndUseCases.UserManager;

import java.util.Map;

/**
 * Controller handling all event related requests.
 */

public class EventController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;

    public EventController(UserManager userManager, EventManager eventManager, TemplateManager templateManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    /**
     * Creates a new event based on chosen template and adds to User's owned events.
     * @param templateName - name of the template
     * @param username - username of the currently logged in user
     */
    private void createEvent(String templateName, String username){
        String newEventID = this.eventManager.createEvent(templateName, username);

        Map<String, String> fieldMap = this.eventManager.returnFieldNameAndType(newEventID);
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            presenter.printText("Enter " + entry.getKey() + "(" + entry.getValue() + "):");
            String userInput = inputParser.readLine();
            boolean accepted = false;
            while (!accepted) {
                if (eventManager.checkDataValidation(entry.getKey(), userInput, newEventID)) {
                    eventManager.enterFieldValue(entry.getKey(), userInput, newEventID);
                    accepted = true;
                }
                else {
                    presenter.printText("Do it right. Enter " + entry.getKey() + "(" + entry.getValue() + "):");
                    userInput = inputParser.readLine();
                }
            }
        }
    }

    /**
     * Prints details of a single event.
     * @param eventID - unique identifier for event
     */
    private void viewEvent(String eventID) {
        this.presenter.printEntity(eventManager.getEventMap(eventID)); // assume this will be implemented
    }

    /**
     * Prints a list of all public events created by all users.
     */
    // TODO Need to figure out how this will be presented
    private void browseEvents() {
        this.presenter.printEntities(this.eventManager.returnPublishedEvents());// needs to return list of maps
    }

    /**
     * Adds event to User's joined event list.
     * @param username - username of the currently logged in user
     * @param eventID - unique identifier for event
     */
    private void attendEvent(String username, String eventID) {
        this.userManager.attendEvent(username, eventID);
    }

    /**
     * Removes selected event from User's joined event list.
     * @param username - username of the currently logged in user
     * @param eventID - unique identifier for event
     */
    private void leaveEvent(String username, String eventID) {
        this.userManager.unAttendEvent(username, eventID);
    }

    /**
     * Completely deletes specified event from system.
     * @param username - username of the currently logged in user
     * @param eventID - unique identifier for event
     */
    private void deleteEvent(String username, String eventID) {
        this.userManager.deleteEvent(username, eventID);
        this.eventManager.deleteEvent(eventID);
    }

}
