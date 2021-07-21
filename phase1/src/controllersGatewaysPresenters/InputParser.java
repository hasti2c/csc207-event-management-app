package controllersGatewaysPresenters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// Inspired by https://codeforces.com/blog/entry/6834?#comment-124539

public class InputParser {
    private BufferedReader bufferedReader;
    private StringTokenizer currentToken;

    private int defaultIntegerValue = -99999999;

    public InputParser() {
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        bufferedReader = new BufferedReader(inputStreamReader);
    }

    public String readLine() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public int readInt() {
        populateCurrentToken();
        try {
            return Integer.parseInt(currentToken.nextToken());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return defaultIntegerValue;
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
