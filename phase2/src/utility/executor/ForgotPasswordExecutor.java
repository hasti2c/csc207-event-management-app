package utility.executor;

import controllers.ExitException;
import controllers.UserController;
import utility.CommandExecutor;

public class ForgotPasswordExecutor implements CommandExecutor {
    private UserController userController;

    @Override
    public void execute() throws ExitException {
        userController.forgotPassword();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
