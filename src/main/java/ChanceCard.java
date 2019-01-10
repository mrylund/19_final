import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class ChanceCard {
    private HashMap<String, String> chanceCard = new HashMap<String, String>();
    private String[] cardSet;
    private String[] shuffleArray;
    private Random random = new Random();
    private String cardDraw;

    @SuppressWarnings("Duplicates")
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
    
    public void makeCardSet() {
        cardSet = new String[chanceCard.size()];
        for (int i = 1; i < chanceCard.size(); i++) {
            cardSet[i-1] = getChanceCard(Integer.toString(i));
        }
    }

    public void shuffleCard() {
        shuffleArray = new String[cardSet.length];
        int randomCard;
        boolean success;

        for (int i = 0; i < cardSet.length; i++) {
            success = false;
            //lægger det sidste kort ind på den sidste tomme plads
            if(i == cardSet.length-1) {
                for (int j = 0; j < cardSet.length; j++) {
                    if(shuffleArray[j] == null) {
                        shuffleArray[j] = cardSet[i];
                        break;
                    }
                }
                //hopper helt ud af i-forloop
                break;
            }
            //finder en random tom plads i shuffleArray og sætter et kort ind
            do {
                randomCard = random.nextInt(cardSet.length);
                if(shuffleArray[randomCard] == null) {
                    shuffleArray[randomCard] = cardSet[i];
                    success = true;
                }
            } while(!success);
        }
        //smider hele det blandede array over i cardSet
        cardSet = shuffleArray;
    }

    public void drawCard() {
        cardDraw = cardSet[0];
        //lægger det bagerst i bunken
        for (int i = 0; i < cardSet.length-1; i++) {
            cardSet[i] = cardSet[i+1];
        }
        cardSet[cardSet.length-1] = cardDraw;
    }

    public String[] getCardSet() {
        return cardSet;
    }

    public String getCardDraw() {
        return cardDraw;
    }
}