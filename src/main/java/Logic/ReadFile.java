package Logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile {

    private String fieldInfoPath = "txtFiles/fieldInfo.txt";
    private String[] theFile = readFile(fieldInfoPath);

    public ReadFile(){}

    public String[] readFile(String fileToBeRead) {

        String fileName = fileToBeRead;
        String[] lines;
        String[] empty = new String[0];
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
            String line;
            int i = 0;
            int counter = 0;
            while (reader.readLine() != null) {
                counter++;
            }
            reader.close();
            reader = new BufferedReader(new java.io.FileReader(fileName));
            lines = new String[counter];
            while ((line = reader.readLine()) != null) {
                lines[i] = line;
                i++;
            }
            reader.close();
            return lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return empty;
    }

    public String getFieldColor(int lineNum) {
        String[] color = theFile[lineNum].split("; ");
        return color[color.length-1];
    }

    public String getFieldType(int lineNum){
        return theFile[lineNum].split("; ")[0];
    }

    public String getFieldName(int lineNum) {
        return theFile[lineNum-1].split("; ")[2];
    }

    public String getFieldPrice(int lineNum){
        return theFile[lineNum-1].split("; ")[3];
    }

    public String getFieldRent(int lineNum){
        return theFile[lineNum-1].split("; ")[4];
    }

    public String getFieldHouse1Price(int lineNum){
        return theFile[lineNum-1].split("; ")[5];
    }

    public String getFieldHouse2Price(int lineNum){
        return theFile[lineNum-1].split("; ")[6];
    }

    public String getFieldHouse3Price(int lineNum){
        return theFile[lineNum-1].split("; ")[7];
    }

    public String getFieldHouse4Price(int lineNum){
        return theFile[lineNum-1].split("; ")[8];
    }

    public String getFieldHotelPrice(int lineNum){
        return theFile[lineNum-1].split("; ")[9];
    }

    public String getBuildPrice(int lineNum) {
        return theFile[lineNum-1].split("; ")[10];
    }

    public String getLine(int lineNum) {
        String[] line = readFile(fieldInfoPath);
        return line[lineNum];
    }

    //brugt til test
    public String name(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[2];
    }

}