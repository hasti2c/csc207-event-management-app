package presenter;

import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;

// Inspired by https://stackoverflow.com/questions/2745206/output-in-a-table-format-in-javas-system-out

public class TextTable {
    private List<List<String>> cells;
    public int width;
    private int height;
    private List<Integer> columnLargestItemSizes = new ArrayList<Integer>();

    // SETTINGS
    private int paddingWidth = 1;
    private int paddingHeight = 0;

    // WIDTH, HEIGHT OF cells MUST BE AT LEAST 1

    /**
     * Store List<List<String>> representing cells of table.
     * Calculate width and height of table.
     * Finally, calculate column with largest item sizes.
     * @param cells Cells of table in List of List of String.
     */
    public TextTable(List<List<String>> cells) {
        this.cells = cells;
        width = calculateWidth();
        height = calculateHeight();
        populateColumnLargestItemSizes();
    }

    /**
     * Calculate width of table
     * @return width of table
     */
    private int calculateWidth() {
        int max = 0;
        for (List<String> row : cells) {
            int length = 1;
            for (String item : row) {
                length += 2 * paddingWidth;
                length += item.length();
                length += 1;
            }
            max = Math.max(max, length);
        }
        return max;
    }

    /**
     * Calculate height of table
     * @return height of table
     */
    private int calculateHeight() {
        int max = 0;
        int width = cells.get(0).size();
        for (int i = 0; i < width; i++) {
            int length = 1;
            for (List<String> row : cells) {
                length += 2 * paddingHeight;
                length += row.get(i).length();
                length += 1;
            }
            max = Math.max(max, length);
        }
        return max;
    }

    private void populateColumnLargestItemSizes() {
        for (String item : cells.get(0)) {
            columnLargestItemSizes.add(item.length());
        }
        for (int i = 1; i < cells.size(); i++) {
            for (int j = 0; j < cells.get(i).size(); j++) {
                if (columnLargestItemSizes.get(j) < cells.get(i).get(j).length()) {
                    columnLargestItemSizes.set(j, cells.get(i).get(j).length());
                }
            }
        }
    }

    /**
     * Constructs a table using cells (List<List<String>>).
     * @return String table made using cells.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int heightCells = cells.size();
        for (int i = 0; i < heightCells; i++) {
            for (int j = 0; j < width; j++) stringBuilder.append("-");
            stringBuilder.append("\n");

            for (int j = 0; j < paddingHeight; j++) {
                stringBuilder.append("|");
                for (int k = 0; k < width - 2; k++) stringBuilder.append(" ");
                stringBuilder.append("|");
                stringBuilder.append("\n");
            }

            int column = 0;
            for (String item : cells.get(i)) {
                stringBuilder.append("|");
                for (int j = 0; j < paddingWidth; j++) stringBuilder.append(" ");
                // https://stackoverflow.com/questions/388461/how-can-i-pad-a-string-in-java
                stringBuilder.append(String.format("%-" + columnLargestItemSizes.get(column) + "s", item));
                for (int j = 0; j < paddingWidth; j++) stringBuilder.append(" ");
                column++;
            }
            stringBuilder.append("|");
            stringBuilder.append("\n");

            for (int j = 0; j < paddingHeight; j++) {
                stringBuilder.append("|");
                for (int k = 0; k < width - 2; k++) stringBuilder.append(" ");
                stringBuilder.append("|");
                stringBuilder.append("\n");
            }
        }
        for (int j = 0; j < width; j++) stringBuilder.append("-");
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}