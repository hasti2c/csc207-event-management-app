package controllersGatewaysPresenters;

import entitiesAndUseCases.EventManager;
import entitiesAndUseCases.TemplateManager;
import entitiesAndUseCases.User;
import entitiesAndUseCases.UserManager;

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
        int eventChoice = generateEventList(eventManager.returnEventNamesListFromIdList(userManager.getCreatedEvents(username)));

        boolean exitMenu = false;
        while (!exitMenu) {
            if (eventChoice == myEvents.size() - 1) {
                exitMenu = true;
            }
            else{
                String eventID = myEvents.get(eventChoice);
                viewEventDetails(eventID);
                viewEventMetaDetails(eventID);
                presenter.printText("1) Delete Event 2) Edit Event 3) Go Back");
                int userInput = inputParser.readInt();

                boolean correctInput = false;
                while (!correctInput) {
                    switch (userInput) {
                        case 1:
                            deleteEvent(username, eventID);
                            correctInput = true;
                            break;
                        case 2:
                            editEvent(eventID, username);
                            break;
                        case 3:
                            correctInput = true;
                            break;
                        default:
                            presenter.printText("You did not enter a valid option, try again");
                            presenter.printText("1) Delete Event 2) Edit Event 3) Go Back");
                            userInput = inputParser.readInt();
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
        eventNameList.add("Go Back");
        presenter.printMenu("Choose an event by entering a number", eventNameList);
        return getChoice(0, eventNameList.size());
    }

    /**
     * Displays a list of events that the user can look through, see details for and register/unregister for.
     * @param username the username of the user
     * @param eventIDList a list of event IDs that either the user has registered for or has not registered for
     * @param isAttending if true, it means the eventIDList contains a list of events the user has registered for. If false, the user is not registered for the events in the list.
     */
    // TODO This is also pretty long someone help (Angela)
    public void browseEvents(String username, List<String> eventIDList, boolean isAttending) {
        // Changes the list of event IDs into a list of event names
        List<String> eventNameList = eventManager.returnEventNamesListFromIdList(eventIDList);

        // Creates a new menu map to print based on input of the method
        Map<String, List<String>> menuMap = new HashMap<>();
        menuMap.put("IsAttending", new ArrayList<>(Arrays.asList("Un-Attend Event", "Go Back")));
        menuMap.put("IsNotAttending", new ArrayList<>(Arrays.asList("Attend Event", "Go Back")));
        menuMap.put("Trial", new ArrayList<>(Collections.singletonList("Go Back")));

        while (true) {
            // Displays all of the events in the list as well as an option to go back to the previous menu. The returned
            // int is the value that the user selected from the list of options.
            int eventIndex = generateEventList(eventNameList);
            String eventID = eventIDList.get(eventIndex);
            // Internal variable to determine which menu to display in from the menu map.
            String menuMapChoice;
            // Comes here if the user chooses "go back"
            if (eventIndex == eventNameList.size() - 1) {
                break;
            }
            // If the user doesn't choose to go back and instead chooses an event to view one of the following three things happens
            else if (userManager.retrieveUserType(username) == User.UserType.T && !isAttending) {
                menuMapChoice = "Trial";
            }
            else if (isAttending) {
                menuMapChoice = "IsAttending";
            }
            else {
                menuMapChoice = "IsNotAttending";
            }

            // Then they get shown the correct menu along with the details of the event they chose.
            presenter.printMenu("Viewing Selected Event", menuMap.get(menuMapChoice));
            int menuChoice = inputParser.readInt();
            viewEventDetails(eventID);
            boolean exitMenu = false;
            while (!exitMenu) {
                // If the user isn't attending the event, and they choose to attend the event
                if (menuChoice == 1 && menuMapChoice.equals("IsNotAttending")) {
                    // check to make sure there's still room in the event
                    if(attendEvent(username, eventID)){
                        menuMapChoice = "IsAttending";
                        // TODO I'm not sure if this works or and also if the event gets removed from the list when they go back to view the list...
                        presenter.printText("You have successfully registered for the event. Choose 1) to unregister or" +
                                " 2) to go back to the list of events.");
                    }
                    else{
                        presenter.printText("Sorry this event is full. Choose 2) to go back to the list of events.");
                    }
                    menuChoice = inputParser.readInt();
                // If the user is attending the event and they choose to leave the event
                } else if (menuChoice == 1 && menuMapChoice.equals("IsAttending")) {
                    if(leaveEvent(username, eventID)){
                        menuMapChoice = "IsNotAttending";
                        presenter.printText("You have successfully unregistered for the event. Choose 1) to re-register or" +
                                " 2) to go back to the list of events.");
                    }
                // If the user chooses to go back
                } else if (menuChoice == 2 || (menuMapChoice.equals("Trial") && menuChoice == 1)) {
                    exitMenu = true;
                // If the entry is invalid
                } else {
                    presenter.printText("Invalid Choice.");
                    presenter.printMenu("Viewing Selected Event", menuMap.get(menuMapChoice));
                    menuChoice = inputParser.readInt();
                    viewEventDetails(eventID);
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
            presenter.printText("Enter " + entry.getKey() + "(" + entry.getValue() + "):");
            String userInput = inputParser.readLine();
            boolean accepted = false;
            while (!accepted) {
                if (eventManager.checkDataValidation(entry.getKey(), userInput, newEventID)) {
                    eventManager.enterFieldValue(entry.getKey(), userInput, newEventID);
                    accepted = true;
                } else {
                    presenter.printText("Do it right. Enter " + entry.getKey() + "(" + entry.getValue() + "):");
                    userInput = inputParser.readLine();
                }
            }
        }
    }

    // TODO need to implement edit event
    public void editEvent (String eventID, String username) {

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
        }
        return this.userManager.unAttendEvent(username, eventID);
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
        presenter.printText("Are you sure you wish to delete your event? This action cannot be undone.");
        presenter.printText("1) Yes 2) Go Back");
        String user_input = inputParser.readLine();
        if (user_input.equals("1")){
            this.userManager.deleteEvent(username, eventID);
        }
        else if (user_input.equals("2")){
            return;
        }
        else {
            presenter.printText("You did not enter a valid option, try again");
            deleteEvent(username, eventID);
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
        while (choice <= lowBound || choice > highBound) {
            presenter.printText("Do it right. Pick a good number: ");
            choice = inputParser.readInt();
        }
        return choice;
    }

    public int chooseTemplate(String username) {
        List<String> templateList = templateManager.returnTemplateNames();
        presenter.printMenu("Type a number corresponding to a template", templateList);
        return getChoice(0, templateList.size());
    }

}
