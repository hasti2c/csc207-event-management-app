package team2;


import team1.angela.EventManager;

import team1.angela.TemplateManager;
import team1.angela.UserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SystemController {
    private final UserManager userManager;
    private final EventManager eventManager;
    private final TemplateManager templateManager;
    private final Presenter presenter;
    private final InputParser inputParser;
    private final Map<String, List<String>> menuMap = new HashMap<>();

    public SystemController() {
        this.userManager = new UserManager();
        this.eventManager = new EventManager();
        this.templateManager = new TemplateManager();
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
        this.run();
    }

    public void run() {
        // starts at the main login page and asks to login or signup

        // if sign up then open the sign up menu and return user credentials to be added to user manager

        // if login, then give login credentials and check if they are correct, if they are, then proceed to main menu
    }
    // add helper methods down here

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

    private boolean checkLogin(){
        /* checks given user credentials from presenter and checks if it is correct or not
         */
        return true;
    }

    private void viewEvent(String eventID) {
        this.presenter.printFormattedEvent(eventID); // assume this will be implemented
    }

    private void browseEvents() {
        this.presenter.printEvents(this.eventManager.getAllEvents()); //assume this will also be implemented
    }

    private void joinEvemt(String eventID) {
        this.userManager.addAttendingEvent(/* somehow get user */, eventID);
    }

    private void deleteEvent(String eventID) {
        this.userManager.completelyWipeEvent(eventID);
        this.eventManager.deleteEvent(eventID);
    }
}

