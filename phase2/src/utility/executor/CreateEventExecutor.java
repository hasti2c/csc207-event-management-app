package utility.executor;

import controllers.EventController;
import controllers.ExitException;
import utility.CommandExecutor;
import utility.UserType;

public class CreateEventExecutor implements CommandExecutor {
    EventController eventController;
    @Override
    public String execute(String username, UserType userType) throws ExitException {
        eventController.createNewEvent(username);
        return username;
    }

    public void setEventController(EventController eventController) {
        this.eventController = eventController;
    }
}
