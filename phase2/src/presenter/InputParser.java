package presenter;

import controllers.ExitException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static utility.AppConstant.*;

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

    /**
     * @return An instance of InputParser (there is only one singular instance).
     */
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
        List<String> falseStrings = Arrays.asList("n", "no", "false");
        if (trueStrings.contains(line.toLowerCase())) {
            return true;
        } else if (falseStrings.contains(line.toLowerCase())) {
            return false;
        } else {
            presenter.printText("Please type Y or N.");
            return readBoolean();
        }
    }

    /**
     * Gets & returns user's choice from menu items. (Doesn't display the menu.)
     * Exit option is assumed to be handled by the caller.
     * @param menuOptions The list of menuOptions that have been shown to the user.
     * @param <S> Type of menu options.
     * @return Menu option chosen by user.
     */
    public <S> S getMenuChoice(List<S> menuOptions) {
        try {
            return getMenuChoice(menuOptions, false);
        } catch (ExitException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets & returns user's choice from menu items. (Doesn't display the menu.)
     * @param menuOptions The list of menuOptions that have been shown to the user.
     * @param checkExit True if exit is the last item & has to be manually checked here. False if exit is handled by the
     *                  caller.
     * @param <S> Type of menu options.
     * @return Menu option chosen by user.
     * @throws ExitException If user chooses exit option & checkExit is true (exit has to be manually checked here).
     */
    public <S> S getMenuChoice(List<S> menuOptions, boolean checkExit) throws ExitException {
        int user_input = readInt();
        S option;
        try {
            option = menuOptions.get(user_input - 1);
        } catch (IndexOutOfBoundsException e) {
            Presenter.getInstance().invalidInput();
            return getMenuChoice(menuOptions, checkExit);
        }

        if (checkExit && option.equals(MENU_EXIT_OPTION)) {
            throw new ExitException();
        }
        return option;
    }

    /**
     * Gets & returns index of user's choice from menu items. (Doesn't display the menu.)
     * @param menuOptions The list of menuOptions that have been shown to the user.
     * @param checkExit True if exit is the last item & has to be manually checked here. False if exit is handled by the
     *                  caller.
     * @param <S> Type of menu options.
     * @return Index of option chosen by user.
     * @throws ExitException If user chooses exit option & checkExit is true (exit has to be manually checked here).
     */
    public <S> int getMenuChoiceIndex(List<S> menuOptions, boolean checkExit) throws ExitException {
        S choice = getMenuChoice(menuOptions, checkExit);
        return menuOptions.indexOf(choice);
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
