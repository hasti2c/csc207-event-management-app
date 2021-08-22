package utility;

import controllers.ExitException;

public interface CommandExecutor {
    void execute() throws ExitException;
}
