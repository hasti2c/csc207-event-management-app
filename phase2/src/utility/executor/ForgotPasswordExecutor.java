package utility.executor;

import controllers.ExitException;
import controllers.UserController;
import utility.CommandExecutor;
import utility.UserType;

public class ForgotPasswordExecutor implements CommandExecutor {
    private UserController userController;

    @Override
    public String execute(String username, UserType userType) throws ExitException {
        userController.forgotPassword();
        return username;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
