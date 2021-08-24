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
    public String execute(String username, UserType userType) {
        userController.userSignUp();
        return username;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }


}
