package utility.executor;

import controllers.UserController;
import presenter.Presenter;
import usecases.*;
import utility.CommandExecutor;

public class SignUpExecutor implements CommandExecutor {
    private UserController userController;

    public SignUpExecutor(){

    }

    @Override
    public void execute() {
        userController.userSignUp();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }


}
