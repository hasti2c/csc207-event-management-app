package team2;

import team1.angela.EventManager;
import team1.angela.FieldSpecs;
import team1.angela.TemplateManager;
import team1.angela.UserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SystemController {
    private UserManager userManager;
    private EventManager eventManager;
    private TemplateManager templateManager;
    private Presenter presenter;

    public SystemController() {
        this.run();
    }

    public void run() {
        // starts at the main login page and asks to login or signup

        // if sign up then open the sign up menu and return user credentials to be added to user manager

        // if login, then give login credentials and check if they are correct, if they are, then proceed to main menu
    }
    // add helper methods down here
    private Object reqUserInput(String reqName, String reqType) {
        // this needs to be extended but i was told in phase 1 we would only be dealing with Strings as type requirements

        Scanner reader = new Scanner(System.in);

        this.presenter.printText("Enter a " + reqName + "(" + reqType +"):"); //I do not want to directly print cause presenter should handle all ui

        String userInput = reader.nextLine();
        while (true) {
            if (reqType.equals("int")) {
                try {
                    return Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    this.presenter.printText("Enter " + reqType);
                }
            }
            else if (reqType.equals("String")) {
                return userInput;
            }
        }

    }

    private void createEvent(String templateString){
        String newEventID = this.eventManager.createEvent(templateString, /* somehow get user*/ ); //i will  assume an id is returned, not full Event

        List<FieldSpecs> field_specs = this.templateManager.getFieldSpecs(templateString);
        Map<String, Object> userFields = new HashMap<String, Object>();

        for (FieldSpecs fieldData: field_specs) {
            Object user_input = this.reqUserInput(fieldData.getFieldName(), fieldData.getDataType());

            while (fieldData.getRequired && user_input == null) {
                user_input = this.reqUserInput(fieldData.getFieldName(), fieldData.getDataType());
            }
            userFields.put(fieldData.getFieldName(), this.reqUserInput());
        }
        this.eventManager.setEventDetails(newEventID, userFields);
    }

    private void viewEvent(String eventID) {
        this.presenter.printFormattedEvent(eventID); // assume this will be implemented
    }

    private void browseEvents() {
        this.presenter.printEvents(this.eventManager.getAllEvents()); //assume this will also be implemented
    }

}

