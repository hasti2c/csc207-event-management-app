package gateways;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Gateway that writes temporary passwords to a txt file.
 */
public class PasswordGateway {
    private final String folderPath;

    public PasswordGateway(String folderPath) {
        this.folderPath = folderPath;
    }

    public void writeTempPass(String username, String tempPass) {
        try {
            FileWriter fileWriter = new FileWriter(getFilePath(username));
            fileWriter.write(tempPass);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilePath(String username) {
        return folderPath + "/" + username + ".txt";
    }
}
