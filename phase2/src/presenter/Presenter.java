package presenter;

import controllers.ExitException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Presenter {
    private static Presenter instance;

    private Presenter() {}

    /**
     * @return An instance of Presenter (there is only one singular instance).
     */
    public static Presenter getInstance() {
        if (instance == null)
            instance = new Presenter();
        return instance;
    }

    /**
     * Prints text to console.
     * @param text Text to be printed.
     */
    public void printText(String text) {
        System.out.println(text);
    }

    /**
     * Prints menu to user. Menu numbering starts at 1, and increments by 1 per subsequent option.
     * @param title The title of the table. Title will be printed before the menu is displayed.
     * @param options A List of String options, where the first item in the List corresponds to option 1,
     *                the second option 2, and so on.
     */
    public void printMenu(String title, List<String> options) {
        printText(title);
        for (int i = 0; i < options.size(); i++) {
            printText((i + 1) + ". " + options.get(i));
        }
        printText("Please enter one of the menu options:");
    }

    /**
     * Shows user invalid input error.
     */
    protected void invalidInput() {
        printText("You did not enter a valid option, try again");
    }

    /**
     * Prints the information regarding an entity in table format by using TextTable.
     * @param details A Map<String, String> mapping properties of the entity to its corresponding value.
     */
    public void printEntity(Map<String, String> details) {
        if (details.keySet().size() == 0) return;
        List<List<String>> cells = new ArrayList<>();
        for (String key : details.keySet()) {
            List<String> row = new ArrayList<>();
            row.add(key);
            row.add(details.get(key));
            cells.add(row);
        }
        TextTable textTable = new TextTable(cells);
        printText(textTable.toString());
    }

    /**
     * Prints information regarding multiple entities using TextTable.
     * @param detailsList A List<Map<String, String>>, with each Map containing the information of a single entity,
     *                    every key corresponding to a property, and the value corresponding to the value of that
     *                    property.
     */
    // It is expected that keys of every element of detailsList are the same
    public void printEntities(List<Map<String, String>> detailsList) {
        boolean atLeastOneCell = false;
        for (Map<String, String> details : detailsList) {
            if (details.keySet().size() != 0) atLeastOneCell = true;
        }
        if (!atLeastOneCell) return;
        List<List<String>> cells = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (String key : detailsList.get(0).keySet()) labels.add(key);
        cells.add(labels);
        for (Map<String, String> detail : detailsList) {
            List<String> row = new ArrayList<>();
            for (String label : labels) {
                row.add(detail.get(label));
            }
            cells.add(row);
        }
        TextTable textTable = new TextTable(cells);
        printText(textTable.toString());
    }
}
