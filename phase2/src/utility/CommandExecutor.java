package utility;

import controllers.ExitException;

public interface CommandExecutor {
    void execute(String username, UserType userType) throws ExitException;
}
