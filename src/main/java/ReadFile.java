import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ReadFile {
    private HashMap<String, String> fieldInfo = new HashMap<String, String>();

    public void fields() {
        String fileName = "txtFiles/fieldInfo.txt";
        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName));
            String lines;
            while((lines = reader.readLine()) != null) {
                String[] lineInfo = lines.split(": ");
                fieldInfo.put(lineInfo[0], lineInfo[1]);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap getFieldInfo() {
        return fieldInfo;
    }
}
