package team2;

import team1.angela.Event;

import java.util.List;
import java.util.ArrayList;

public class Presenter {
    public void printText(String text) {
        System.out.println(text);
    }

    public void printMenu(String title, List<String> options) {
        printText(title);
        printText("Please choose one of the following options.");
        for (int i = 0; i < options.size(); i++) {
            printText(i + ". " + options.get(i));
        }
    }

    public void printEvents(List<Event> events) {
        if (events.size() == 0) {
            printText("There are no events to be displayed.");
            return;
        }
        ArrayList<ArrayList<String>> cells = new ArrayList<>();

        ArrayList<String> firstRow = new ArrayList<>();
        firstRow.add("ID");
        firstRow.add("Owner");

        cells.add(firstRow);

        // We can add more fields after meeting

        for (Event event : events) {
            ArrayList<String> row = new ArrayList<>();
            row.add(String.format("%d", event.getEventID()));
            row.add(event.getEventOwner());

            cells.add(row);
        }

        TextTable textTable = new TextTable("Event Table", cells);
        printText(textTable.toString());
    }
}
