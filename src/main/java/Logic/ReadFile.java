package Logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadFile {

    public String[] readFile(String fileToBeRead) {

        String fileName = fileToBeRead;
        String[] lines;
        String[] empty = new String[0];
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));

            String line;
            int i = 0;
            int counter = 0;

            while(reader.readLine() != null){
                counter++;
            }
            reader = new BufferedReader(new java.io.FileReader(fileName));


            lines = new String[counter];

            while((line = reader.readLine()) != null) {
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

    public static void main(String[] args){
        ReadFile reader = new ReadFile();
        String[] readFilePrint = reader.readFile("txtFiles/fieldInfo.txt");

        for(int i = 0; i < readFilePrint.length; i++){
            System.out.println(readFilePrint[i]);
        }

        System.out.println(readFilePrint[2]);

        System.out.println(readFilePrint[39].split("; ")[5]);


    }
}