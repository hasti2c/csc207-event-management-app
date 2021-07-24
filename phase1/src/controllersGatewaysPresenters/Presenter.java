package controllersGatewaysPresenters;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Presenter {
    public void printText(String text) {
        System.out.println(text);
    }

    public void printMenu(String title, List<String> options) {
        printText(title);
        printText("Please choose one of the following options.");
        for (int i = 0; i < options.size(); i++) {
            printText((i + 1) + ". " + options.get(i));
        }
    }

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
