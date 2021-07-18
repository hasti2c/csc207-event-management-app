package team2;

import team1.EventManager;
import team1.TemplateManager;
import team1.UserManager;

import java.util.Map;

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

    private void createEvent(String templateString){
        String newEventID = this.eventManager.createEvent(templateString, /* somehow get user*/ ); //i will  assume an id is returned, not full Event

        Map<String, String> fieldMap = this.eventManager.returnFieldNameAndType(newEventID);
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            presenter.printText("Enter " + entry.getKey() + "(" + entry.getValue() + "):");
            String userInput = inputParser.readLine();
            boolean accepted = false;
            while (!accepted) {
                if (eventManager.checkDataValidation(entry.getKey(), userInput)) {
                    accepted = true;
                }
                else {
                    presenter.printText("Do it right. Enter " + entry.getKey() + "(" + entry.getValue() + "):");
                    userInput = inputParser.readLine();
                }
            }
        }
    }

    private void viewEvent(String eventID) {
        this.presenter.printFormattedEvent(eventID); // assume this will be implemented
    }

    private void browseEvents() {
        this.presenter.printEvents(this.eventManager.getPublicEvents()); //assume this will also be implemented
    }

    private void joinEvent(String eventID) {
        this.userManager.attendEvent(userManager.getUser(username), eventID);
    }

    private void leaveEvent(String eventID) {

    }

    private void deleteEvent(String eventID) {
        this.userManager.deleteEvent(/*somehow get user */, eventID);
        this.eventManager.deleteEvent(eventID);
    }

}
