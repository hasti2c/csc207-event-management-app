package view;

import entities.Event;
import entities.Menu;
import utility.Command;

import java.util.List;

public class EventView {
    private Command command;
    private List<Event> events;
    private Menu subMenu;

    public EventView(Command command, List<Event> events, Menu subMenu) {
        this.command = command;
        this.events = events;
        this.subMenu = subMenu;
    }
}
