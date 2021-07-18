package team2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import team1.angela.Event;
import team1.angela.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO exceptions
// TODO hash passwords
// TODO test

public class UserParser {
    private final String path;
    private final Gson gson;
    private Map<String, User> users;

    public UserParser(String path) {
        this.path = path;
        gson = new GsonBuilder().setPrettyPrinting().create();
        readUsers();
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void saveUser(User user) {
        users.replace(user.getUsername(), user);
        writeUsers();
    }

    public void createUser(User user) {
        users.put(user.getUsername(), user);
        writeUsers();
    }

    private void readUsers() {
        try {
            FileReader fileReader = new FileReader(path);
            User[] users = gson.fromJson(fileReader, User[].class);
            fileReader.close();

            this.users = new HashMap<>();
            for (User user: users)
                this.users.put(user.getUsername(), user);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void writeUsers() {
        try {
            User[] users = this.users.values().toArray(new User[0]);

            FileWriter fileWriter = new FileWriter(path);
            gson.toJson(users, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

