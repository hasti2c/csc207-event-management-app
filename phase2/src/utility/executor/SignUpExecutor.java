package utility.executor;

import controllers.UserController;
import presenter.Presenter;
import usecases.*;
import utility.CommandExecutor;
import utility.UserType;

public class SignUpExecutor implements CommandExecutor {
    private UserController userController;

    public SignUpExecutor(){

    }

    @Override
    public void execute(String username, UserType userType) {
        userController.userSignUp();
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }


}
