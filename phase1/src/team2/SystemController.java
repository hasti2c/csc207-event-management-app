package team2;


import team1.angela.EventManager;

import team1.angela.TemplateManager;
import team1.angela.UserManager;

import java.util.*;


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
        List<String> mainMenuOptions = Arrays.asList("SignUp", "Login");
        List<String> loginOptions = Arrays.asList("Create Event", "View Event", "Logout");
        this.menuMap.put("Main Menu", mainMenuOptions);
        this.menuMap.put("Login Menu", loginOptions);
        this.run();

    }

    public void run() {
        // starts at the main login page and asks to login or signup
        presenter.printMenu("Main Menu", this.menuMap.get("Main Menu"));
        int signupChoice = inputParser.readInt();
        if(signupChoice == 1){
            this.userSignUp();
        }
        this.userLogin();
        // after this point, the event controller should take over
    }
    // add helper methods down here

    private void userSignUp(){
        presenter.printText("Enter an Email: ");
        String email = inputParser.readLine();
        boolean correctEmail = false;
        while(!correctEmail){
            if(userManager.isEmailTaken(email) == false){ // needs to be implemented
                correctEmail = true;
            }else{
                presenter.printText("Email already exists. Enter another email: ");
                email = inputParser.readLine();
            }
        }

        presenter.printText("Enter a Username: ");
        String username = inputParser.readLine();
        boolean correctUsername = false;
        while(!correctUsername){
            if(userManager.isUsernameTaken(email) == false){ // needs to be implemented
                correctUsername = true;
            }else{
                presenter.printText("Username already exists. Enter another username: ");
                email = inputParser.readLine();
            }
        }

        presenter.printText("Enter a Password: ");
        String password = inputParser.readLine();

        userManager.createUser(username, password, email); // this needs to be implemented
        presenter.printText("Account has been created Successfully. Please login.");

    }
    private void userLogin(){

        presenter.printText("Enter your Username: ");
        String username = inputParser.readLine();

        presenter.printText("Enter your Password: ");
        String password = inputParser.readLine();

        boolean correctLogin = false;
        while(!correctLogin){
            if(userManager.logIn(username, password)){
                correctLogin = true;
            }else{
                presenter.printText("Username or Password is incorrect, please try again.");

                presenter.printText("Enter your Username: ");
                username = inputParser.readLine();

                presenter.printText("Enter your Password: ");
                password = inputParser.readLine();
            }
        }
        presenter.printText("Login was successful");
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
        this.userManager.deleteEvent(/*somrhow get user */, eventID);
        this.eventManager.deleteEvent(eventID);
    }
}

