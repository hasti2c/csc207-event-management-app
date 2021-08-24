package utility.executor;

import controllers.ExitException;
import controllers.UserController;
import usecases.UserManager;
import utility.CommandExecutor;
import utility.UserType;

import static utility.Command.MAIN_MENU;

public class LoginExecutor implements CommandExecutor {
    private UserController userController;
    private String currentUser;
    private UserType currentUserType;
    private UserManager userManager;

    @Override
    public void execute(String username, UserType userType) throws ExitException {
        String attemptedLoginUsername = userController.userLogin();
        if (attemptedLoginUsername != null){
            this.currentUser = attemptedLoginUsername;
            this.currentUserType = userManager.retrieveUserType(attemptedLoginUsername);
        }
        username = currentUser;
        userType = currentUserType;
        // TODO run main menu
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setCurrentUserType(UserType currentUserType) {
        this.currentUserType = currentUserType;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
