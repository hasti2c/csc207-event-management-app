package team2;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO first line
public class CSVManager {
    private final String path;
    private Map<String, String[]> data;

    public CSVManager(String path) {
        this.path = path;
        readData();
    }

    // TODO make line[0] different parameter
    public void addLine(String[] line) {
        data.put(line[0], line);
        writeData();
    }

    public void changeLine(String[] line) {
        data.replace(line[0], line);
        writeData();
    }

    public String[] getLine(String id) {
        return data.get(id);
    }

    public Map<String, String[]> getData() {
        return data;
    }

    private void readData() {
        try {
            FileReader fileReader = new FileReader(path);
            CSVReader reader = new CSVReader(fileReader);
            data = new HashMap<>();

            String[] line = reader.readNext();
            while (line != null) {
                data.put(line[0], line);
                line = reader.readNext();
            }

            reader.close();
            fileReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void writeData() {
        try {
            FileWriter fileWriter = new FileWriter(path);
            CSVWriter writer = new CSVWriter(fileWriter);
            for (String[] line: data.values())
                writer.writeNext(line);
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
