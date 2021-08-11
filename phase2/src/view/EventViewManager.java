package view;

import entities.Menu;
import utility.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utility.Command.*;

public class EventViewManager {
    Menu eventMenu; // temp

    public EventViewManager() {
        eventMenu = new Menu(LOGIN, BROWSE_EVENTS);
        eventMenu.setSubCommands(Arrays.asList(ATTEND_EVENT, UNATTEND_EVENT, EDIT_EVENT, DELETE_EVENT));
    }
}
