package Logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile {

    private String fieldInfoPath = "txtFiles/fieldInfo.txt";
    private String[] theFile = readFile(fieldInfoPath);

    public ReadFile(){}

    /**
     * @param fileToBeRead the file that should be read.
     * @return an array containing all lines of the file.
     */
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

    /**
     * @param lineNum get the color of the field from fieldInfo.txt file.
     * @return a string containing RGB values of the field color.
     */
    public String getFieldColor(int lineNum) {
        String[] color = theFile[lineNum].split("; ");
        return color[color.length-1];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the type of the given field.
     */
    public String getFieldType(int lineNum){
        return theFile[lineNum].split("; ")[0];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the name of the given field.
     */
    public String getFieldName(int lineNum) {
        return theFile[lineNum-1].split("; ")[2];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the price of the given field.
     */
    public String getFieldPrice(int lineNum){
        return theFile[lineNum-1].split("; ")[3];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the rent of the given field.
     */
    public String getFieldRent(int lineNum){
        return theFile[lineNum-1].split("; ")[4];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the rent of the given field with 1 house.
     */
    public String getFieldHouse1Rent(int lineNum){
        return theFile[lineNum-1].split("; ")[5];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the rent of the given field with 2 houses.
     */
    public String getFieldHouse2Rent(int lineNum){
        return theFile[lineNum-1].split("; ")[6];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the rent of the given field with 3 houses.
     */
    public String getFieldHouse3Rent(int lineNum){
        return theFile[lineNum-1].split("; ")[7];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the rent of the given field with 4 houses.
     */
    public String getFieldHouse4Rent(int lineNum){
        return theFile[lineNum-1].split("; ")[8];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the rent of the given field with a hotel on it .
     */
    public String getFieldHotelPrice(int lineNum){
        return theFile[lineNum-1].split("; ")[9];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the price that it cost to build on the given field.
     */
    public String getBuildPrice(int lineNum) {
        return theFile[lineNum-1].split("; ")[10];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the whole line as a string.
     */
    public String getLine(int lineNum) {
        String[] line = readFile(fieldInfoPath);
        return line[lineNum];
    }

    /**
     * @param lineNum the line you wish to read from.
     * @return the name of the given line.
     */
    //brugt til test
    public String name(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[2];
    }

}