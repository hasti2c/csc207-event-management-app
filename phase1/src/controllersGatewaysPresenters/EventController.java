package controllersGatewaysPresenters;

import entitiesAndUseCases.EventManager;
import entitiesAndUseCases.TemplateManager;
import entitiesAndUseCases.User;
import entitiesAndUseCases.UserManager;
import sun.font.TrueTypeFont;

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

    // TODO this is kind of long...
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
        temp.add(AppConstant.MENU_EXIT_OPTION);
        presenter.printMenu("Choose an event by entering a number", temp);
        return getChoice(1, temp.size()) - 1;
    }

    /**
     * Displays a list of events that the user can look through, see details for and register/unregister for.
     * @param username the username of the user
     * @param eventIDList a list of event IDs that either the user has registered for or has not registered for
     * @param isAttending if true, it means the eventIDList contains a list of events the user has registered for. If false, the user is not registered for the events in the list.
     */
    // TODO This is also pretty long someone help (Angela)
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

        Map<String, String> fieldMap = this.eventManager.returnFieldNameAndType(newEventID);
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            presenter.printText("Enter " + entry.getKey() + ":");
            String userInput = inputParser.readLine();
            boolean accepted = false;
            while (!accepted) {
                if (eventManager.checkDataValidation(newEventID, entry.getKey(), userInput)) {
                    eventManager.enterFieldValue(newEventID, entry.getKey(), userInput);
                    accepted = true;
                } else {
                    presenter.printText("Please try again. Enter " + entry.getKey() + "(" + entry.getValue() + "):");
                    userInput = inputParser.readLine();
                }
            }
        }
        presenter.printText("Your event has been successfully created.");
        changePublishStatus(newEventID);
    }

    // TODO need to implement edit event
    public void editEvent (String eventID, String username) {

    }

    private boolean getYesNo() {
        String userInput = inputParser.readLine();
        while (!userInput.equals("Y") && !userInput.equals("N")) {
            presenter.printText("Type Y or N");
            userInput = inputParser.readLine();
        }
        if (userInput.equals("Y")){
            return true;
        }
        return false;
    }

    private void changePublishStatus (String eventID) {
        if (eventManager.returnIsPublished(eventID)){
            presenter.printText("Your event is currently published, would you like to unpublish? (Y/N)");
            if (getYesNo()) {
                eventManager.publishEvent(eventID); // this should be unpublish but no method
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
    private int getChoice(int lowBound, int highBound) {
        int choice = inputParser.readInt();
        while (choice < lowBound || choice > highBound) {
            presenter.printText("Do it right. Pick a number between " + lowBound + " and " + highBound);
            choice = inputParser.readInt();
        }
        return choice;
    }

    public int chooseTemplate(String username) {
        List<String> templateList = templateManager.returnTemplateNames();
        presenter.printMenu("Type a number corresponding to a template", templateList);
        return getChoice(0, templateList.size()) - 1;
    }

}
