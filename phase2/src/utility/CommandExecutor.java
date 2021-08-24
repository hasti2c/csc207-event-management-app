package utility;

import controllers.ExitException;

public interface CommandExecutor {
    /**
     *
     * @param username current user's username
     * @param userType current user's user type
     * @return currentUsername
     * @throws ExitException
     */
    String execute(String username, UserType userType) throws ExitException;
}
