package presenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

// Inspired by https://codeforces.com/blog/entry/6834?#comment-124539

public class InputParser {
    private static InputParser instance;

    private BufferedReader bufferedReader;
    private StringTokenizer currentToken;
    Presenter presenter = Presenter.getInstance();

    private int defaultIntegerValue = -99999999;

    private InputParser() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        bufferedReader = new BufferedReader(inputStreamReader);
    }

    public static InputParser getInstance() {
        if (instance == null)
            instance = new InputParser();
        return instance;
    }

    /**
     * Reads line from user input.
     * @return String line that user inputted.
     */
    public String readLine() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Reads int from user input.
     * @return int that user inputted.
     */
    public int readInt() {
//        populateCurrentToken();
//        try {
//            return Integer.parseInt(currentToken.nextToken());
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        return defaultIntegerValue;

        // Quick fix to clogging program if user does not actually input an int.
        String line = readLine();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            presenter.printText("That was not an integer. Please try again.");
            return readInt();
        }
    }

    /**
     * Forces user to type either "Y"/"Yes"/"true" or "N"/"No"/"false"
     *
     * @return return true if user typed "Y"/"Yes"/"true" and false if "N"/"No"/"false"
     */
    public boolean readBoolean() {
        String line = readLine();
        List<String> trueStrings = Arrays.asList("y", "yes", "true");
        List<String> falseStrings = Arrays.asList("y", "no", "false");
        if (trueStrings.contains(line.toLowerCase())) {
            return true;
        } else if (falseStrings.contains(line)) {
            return false;
        } else {
            presenter.printText("Please type Y or N.");
            return readBoolean();
        }
    }

    private void populateCurrentToken() {
        if (currentToken == null) {
            try {
                currentToken = new StringTokenizer(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        if (!currentToken.hasMoreTokens()) {
            try {
                currentToken = new StringTokenizer(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
