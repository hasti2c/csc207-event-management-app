package utility.executor;

import controllers.ExitException;
import controllers.UserController;
import utility.CommandExecutor;
import utility.UserType;

public class ForgotPasswordExecutor implements CommandExecutor {
    private UserController userController;

    @Override
    public void execute(String username, UserType userType) throws ExitException {
        userController.forgotPassword();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
