package Logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile {

    private String fieldInfoPath = "txtFiles/fieldInfo.txt";
    private String[] theFile = readFile(fieldInfoPath);

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

    public String navn(int lineNum) {
        return theFile[lineNum-1].split("; ")[2];
    }

    public String getLine(int lineNum) {
        String[] line = readFile(fieldInfoPath);
        return line[lineNum];
    }

    public String getFieldType(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum].split("; ")[0];
    }

    public String getFieldName(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[2];
    }

    public String getFieldPrice(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[3];
    }

    public String getFieldRent(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[4];
    }

    public String getFieldHouse1(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[5];
    }

    public String getFieldHouse2(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[6];
    }

    public String getFieldHouse3(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[7];
    }

    public String getFieldHouse4(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[8];
    }

    public String getFieldHotel(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[9];
    }

    public String getFieldBuildPrice(int lineNum) {
        String[] allLines = readFile(fieldInfoPath);
        return allLines[lineNum-1].split("; ")[10];
    }


    public static void main(String[] args){
        ReadFile reader = new ReadFile();
        String[] readFilePrint = reader.readFile("txtFiles/fieldInfo.txt");

        for(int i = 0; i < readFilePrint.length; i++){
            System.out.println(readFilePrint[i]);
        }

        //System.out.println(readFilePrint[2]);
        //System.out.println(readFilePrint[39].split("; ")[5]);
        //System.out.println(reader.getFieldName(40));

        long counter = 0;
        for (int i = 0; i < 50; i++) {
            long t0 = System.currentTimeMillis();
            for (int j = 1; j < 40; j++) {
                //System.out.println(reader.getFieldName(j));
                System.out.println(reader.navn(j));
            }
            long t1 = System.currentTimeMillis();
            counter += (t1-t0);

            /*System.out.println(reader.getFieldName(40) + "\n" +
                    reader.getFieldPrice(40) + "\n" +
                    reader.getFieldHouse1(40) + "\n" +
                    reader.getFieldHouse2(40) + "\n" +
                    reader.getFieldHouse3(40) + "\n" +
                    reader.getFieldHouse4(40) + "\n" +
                    reader.getFieldHotel(40) + "\n" +
                    reader.getFieldBuildPrice(40) + "\n" +
                    reader.getFieldRent(40));*/
        }
        System.out.println((counter/50) + " millisek");
    }
}