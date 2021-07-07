package team2;

import team1.User;

import java.io.*;

// TODO source https://www.tutorialspoint.com/java/java_serialization.htm
// TODO exceptions
// TODO boolean return value
// TODO ask for username to be okay in file name, and not include dashes.
// TODO ask for transient fields
public class UserParser {
    private final String path;

    public UserParser(String path) {
        this.path = path;
    }

    public User getUser(String username) {
        try {
            String filePath = getFilePath(username);
            FileInputStream input = new FileInputStream(filePath);
            ObjectInputStream deserializer = new ObjectInputStream(input);

            User user = (User) deserializer.readObject();
            input.close();
            deserializer.close();
            return user;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(User user) {
        try {
            String filePath = getFilePath(user.getUsername());
            FileOutputStream output = new FileOutputStream(filePath);
            ObjectOutputStream serializer = new ObjectOutputStream(output);

            serializer.writeObject(user);
            output.close();
            serializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(User user) {
        String filePath = getFilePath(user.getUsername());
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveUser(user);
    }

    private String getFilePath(String username) {
        return path + "/user-" + username + ".ser";
    }
}

