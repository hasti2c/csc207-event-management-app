package team2;

import javafx.util.Pair;
import team1.angela.Event;
import team1.angela.Template;
import team1.angela.User;

import java.util.List;

public class Presenter {
    private String currentPage;
    public enum Command {
        SIGN_UP,
        LOGIN,
        LOG_OUT,
        CREATE_EVENT,
        BROWSE_EVENTS,
        VIEW_EVENT,
        JOIN_EVENT,
        DELETE_EVENT,
        EDIT_EVENT,
        EDIT_TEMPLATE
    }

    public Pair<Command, List<String>> menu() {
        return null;
    }

    public void printEvents(List<Event> events) {

    }

    public void printTemplates(List<Template> templates) {

    }

    public void printUsers(List<User> users) {

    }
}
