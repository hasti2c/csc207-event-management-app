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

    public EventController(UserManager userManager, EventManager eventManager, TemplateManager templateManager) {
        this.userManager = userManager;
        this.eventManager = eventManager;
        this.templateManager = templateManager;
        this.presenter = new Presenter();
        this.inputParser = new InputParser();
    }

    // === Viewing List of Event Names ===

    /**
     * Entire menu for managing a users events.
     *
     * @param username used to id user
     */
    public void viewAndEditMyEvents(String username) {
        List<String> myEvents = userManager.getCreatedEvents(username);
        List<String> myEventsMenu = Arrays.asList("Delete Event", "Edit Event", "Change Published Status", "Go Back");
        boolean exitMenu = false;
        while (!exitMenu) {
            int eventChoice = generateEventList(eventManager.returnEventNamesListFromIdList(userManager.getCreatedEvents(username)));
            if (eventChoice == myEvents.size()) {
                exitMenu = true;
            }
            else{
                String eventID = myEvents.get(eventChoice);
                viewEventDetails(eventID);
                viewEventMetaDetails(eventID);
                boolean stopLoop = false;
                while (!stopLoop) {
                    presenter.printMenu("Viewing Event Details", myEventsMenu);
                    int userInput = getChoice(1, myEventsMenu.size());

                    switch (myEventsMenu.get(userInput - 1)) {
                        case "Delete Event":
                            deleteEvent(username, eventID);
                            stopLoop = true;
                            break;
                        case "Edit Event":
                            editEvent(eventID, username);
                            break;
                        case "Change Published Status":
                            changePublishStatus(eventID);
                            break;
                        case "Go Back":
                            stopLoop = true;
                            break;
                    }
                }
            }
        }
    }

    /**
     * Prints a list of all public events created by all users.
     */
    private int generateEventList(List<String> eventNameList) {
        List<String> temp = new ArrayList<>(eventNameList);
        temp.add(MENU_EXIT_OPTION);
        presenter.printMenu("Event List", temp);
        return getChoice(1, temp.size()) - 1;
    }

    /**
     * Displays a list of events that the user can look through, see details for and register/unregister for.
     * @param username the username of the user
     * @param eventIDList a list of event IDs that either the user has registered for or has not registered for
     * @param isAttending if true, it means the eventIDList contains a list of events the user has registered for. If false, the user is not registered for the events in the list.
     */
    public void browseEvents(String username, List<String> eventIDList, boolean isAttending) {
        // Creates a new menu map to print based on input of the method
        Map<String, List<String>> menuMap = new HashMap<>();
        menuMap.put("IsAttending", new ArrayList<>(Arrays.asList("Un-Attend Event", "Go Back")));
        menuMap.put("IsNotAttending", new ArrayList<>(Arrays.asList("Attend Event", "Go Back")));
        menuMap.put("Trial", new ArrayList<>(Collections.singletonList("Go Back")));

        while (true) {
            // Changes the list of event IDs into a list of event names
            List<String> eventNameList = eventManager.returnEventNamesListFromIdList(eventIDList);
            // Displays all of the events in the list as well as an option to go back to the previous menu. The returned
            // int is the value that the user selected from the list of options.
            int eventIndex = generateEventList(eventNameList);
            // Comes here if the user chooses "go back"
            if (eventIndex == eventNameList.size()) {
                break;
            }
            String eventID = eventIDList.get(eventIndex);
            // Internal variable to determine which menu to display in from the menu map.
            String menuMapChoice;
            // If the user doesn't choose to go back and instead chooses an event to view one of the following three things happens
            if (userManager.retrieveUserType(username) == User.UserType.T && !isAttending) {
                menuMapChoice = "Trial";
            }
            else if (isAttending) {
                menuMapChoice = "IsAttending";
            }
            else {
                menuMapChoice = "IsNotAttending";
            }

            // Then they get shown the correct menu along with the details of the event they chose.
            viewEventDetails(eventID);
            presenter.printMenu("Viewing Selected Event", menuMap.get(menuMapChoice));
            int menuChoice = getChoice(1, menuMap.get(menuMapChoice).size());
            boolean exitMenu = false;
            while (!exitMenu) {
                // If the user isn't attending the event, and they choose to attend the event
                if (menuMap.get(menuMapChoice).get(menuChoice - 1).equals("Attend Event")) {
                    // check to make sure there's still room in the event
                    if(attendEvent(username, eventID)){
                        // TODO I'm not sure if this works or and also if the event gets removed from the list when they go back to view the list...
                        presenter.printText("You have successfully registered for the event.");
                        eventIDList.remove(eventID);
                    }
                    else{
                        presenter.printText("Sorry this event is full.");
                    }
                    exitMenu = true;
                // If the user is attending the event and they choose to leave the event
                } else if (menuMap.get(menuMapChoice).get(menuChoice - 1).equals("Un-Attend Event")) {
                    if(leaveEvent(username, eventID)){
                        presenter.printText("You have successfully unregistered for the event.");
                        eventIDList.remove(eventID);
                    }
                    else{
                        presenter.printText("You could not leave this event. Choose 2) to go back to the list of events.");
                    }
                    exitMenu = true;
                // If the user chooses to go back
                } else if (menuMap.get(menuMapChoice).get(menuChoice - 1).equals("Go Back")) {
                    exitMenu = true;
                // If the entry is invalid
                }
            }
        }
    }

    // === Manipulating Events ===

    /**
     * Creates a new event based on chosen template and adds to User's owned events.
     *
     * @param templateName name of the template
     * @param username username of the currently logged in user
     */
    public void createNewEvent(String templateName, String username) {
        String newEventID = this.eventManager.createEvent(templateName, username);

        userManager.createEvent(username, newEventID);

        // TODO include required or not
        Map<String, Pair<Class<?>, Boolean>> fieldMap = this.eventManager.returnFieldNameAndFieldSpecs(newEventID);
        // A map to help display whether or not a field is required.
        Map<Boolean, String> requiredMap = new HashMap<>();
        requiredMap.put(true, " (Required)");
        requiredMap.put(false, " (Not Required)");

        for (Map.Entry<String, Pair<Class<?>, Boolean>> entry : fieldMap.entrySet()) {
            presenter.printText("Enter " + entry.getKey() + requiredMap.get(entry.getValue().getSecond()) + ":");
            String userInput = inputParser.readLine();
            boolean accepted = false;
            while (!accepted) {
                if (userInput.equalsIgnoreCase(EXIT_TEXT)) {
                    deleteEvent(username, newEventID);
                    return;
                } else if (eventManager.checkDataValidation(newEventID, entry.getKey(), userInput)) {
                    Object value = eventManager.convertToCorrectDataType(newEventID, entry.getKey(), userInput);
                    eventManager.enterFieldValue(newEventID, entry.getKey(), value);
                    accepted = true;
                } else {
                    presenter.printText("Please try again. Enter " + entry.getKey() + " (" + entry.getValue().getFirst().getSimpleName() + "):");
                    userInput = inputParser.readLine();
                }
            }
        }
        presenter.printText("Your event has been successfully created.");
        if (userManager.retrieveUserType(username) == User.UserType.T){
            presenter.printText("Since you are a trial user, your event will not be saved once you leave the system. " +
                    "You may choose to publish the event to view it while you are currently using the program.");
        }
        changePublishStatus(newEventID);
    }

    // TODO need to implement edit for phase 2
    public void editEvent (String eventID, String username) {
        presenter.printText("You cannot edit your event at this time.");
    }

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

    /**
     * If published change to unpublished and vice versa
     *
     * @param eventID unique identifier for an event
     */
    private void changePublishStatus (String eventID) {
        if (eventManager.returnIsPublished(eventID)){
            presenter.printText("Your event is currently published, would you like to unpublish? (Y/N)");
            if (getYesNo()) {
                if (eventManager.unPublishEvent(eventID)){
                    presenter.printText("Your event has been successfully unpublished.");
                }
                else {
                    presenter.printText("Your event could not be unpublished.");
                }
            }
        }
        else {
            presenter.printText("Your event is currently unpublished, would you like to publish? (Y/N)");
            if (getYesNo()) {
                eventManager.publishEvent(eventID);
            }
        }
    }
    /**
     * Removes selected event to the User's list of events they are attending.
     *
     * @param username username of the currently logged in user
     * @param eventID unique identifier for event
     * @return true if the user has successfully unregistered for the event
     */
    private boolean leaveEvent(String username, String eventID) {
        if (this.userManager.unAttendEvent(username, eventID)) {
            this.eventManager.unAttendEvent(eventID);
            return true;
        }
        return false;
    }

    /**
     * Adds selected event to the User's list of events they are attending.
     * @param username username of the currently logged in user
     * @param eventID  unique identifier for event
     * @return true if the user has successfully registered for the event
     */
    private boolean attendEvent(String username, String eventID) {
        if (this.eventManager.attendEvent(eventID))
            this.userManager.attendEvent(username, eventID);
        return this.eventManager.attendEvent(eventID);
    }

    /**
     * Completely deletes specified event from system.
     *
     * @param username username of the currently logged in user
     * @param eventID  unique identifier for event
     */
    private void deleteEvent(String username, String eventID) {
        List<String> optionsList = Arrays.asList("Yes", "Go Back");
        presenter.printMenu("Are you sure you wish to delete your event? This action cannot be undone.", optionsList);
        int user_input = getChoice(1, 2);
        if (optionsList.get(user_input - 1).equals("Yes")){
            this.userManager.deleteEvent(username, eventID);
            this.eventManager.deleteEvent(eventID);
            presenter.printText("Event was deleted.");
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

    public int chooseTemplate(String username) {
        List<String> templateList = templateManager.returnTemplateNames();
        templateList.add(MENU_EXIT_OPTION);
        presenter.printMenu("Available Templates", templateList);
        return getChoice(1, templateList.size());
    }

}
