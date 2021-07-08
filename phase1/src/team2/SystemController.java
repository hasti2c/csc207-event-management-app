package team2;

import team1.angela.EventManager;
import team1.angela.TemplateManager;
import team1.angela.UserManager;

public class SystemController {
    private UserManager userManager;
    private EventManager eventManager;
    private TemplateManager templateManager;

    public SystemController() {
        this.run();
    }

    public void run() {
        // starts at the main login page and asks to login or signup

        // if sign up then open the sign up menu and return user credentials to be added to user manager

        // if login, then give login credentials and check if they are correct, if they are, then proceed to main menu
    }
    // add helper methods down here

    private boolean checkLogin(){
        /* checks given user credentials from presenter and checks if it is correct or not
         */
        return true;
    }
}

