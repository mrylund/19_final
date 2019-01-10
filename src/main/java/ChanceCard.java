import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ChanceCard {
    private HashMap<String, String> chanceCard = new HashMap<String, String>();

    public void readFile() {
        String fileName = "txtFiles/chanceCard.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lines;
            while((lines = reader.readLine()) != null) {
                String[] lineInfo = lines.split(": ");
                chanceCard.put(lineInfo[0], lineInfo[1]);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getChanceCard(String key) {
        return chanceCard.get(key);
    }
    
    public String[] makeCardSet() {
        String[] cards = new String[chanceCard.size()];
        for (int i = 1; i < chanceCard.size(); i++) {
            cards[i-1] = getChanceCard(Integer.toString(i));
        }
        return cards;
    }
}
