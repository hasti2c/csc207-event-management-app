package team2;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVManager {
    private final String path;
    private ArrayList<String[]> data;

    public CSVManager(String path) {
        this.path = path;
    }

    public void addLine(String[] line) {
        readData();
        data.add(line);
        writeData();
    }

    public void changeLine(String id, String[] line) {
        readData();
        for (int i = 0; i < data.size(); i++)
            if (id.equals(data.get(i)[0]))
                data.set(i, line);
        writeData();
    }

    public String[] getLine(String id) {
        readData();
        for (String[] line: data)
            if (id.equals(line[0]))
                return line;
        return null;
    }

    private void readData() {
        try {
            FileReader fileReader = new FileReader(path);
            CSVReader reader = new CSVReader(fileReader);
            data = new ArrayList<>();

            String[] line = reader.readNext();
            while (line != null) {
                data.add(line);
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
            for (String[] line: data)
                writer.writeNext(line);
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
