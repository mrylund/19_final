package Logic;

import Entities.Player;
import Gameboard.Gameboard;
import Logic.ReadFile;

import java.util.Random;

public class ChanceCard {

    private String[] shuffleArray;
    private Random random = new Random();
    private String cardDraw;
    private ReadFile reader = new ReadFile();
    private String chanceCardPath = "txtFiles/chanceCard.txt";
    private String[] cardSet;

    public void makeCardSet(){
        this.cardSet = reader.readFile(chanceCardPath);
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

    public int  drawCard() {
        cardDraw = cardSet[0];
        //lægger det bagerst i bunken
        for (int i = 0; i < cardSet.length-1; i++) {
            cardSet[i] = cardSet[i+1];
        }
        cardSet[cardSet.length-1] = cardDraw;
       return Integer.parseInt(cardDraw.split(": ")[0]);

    }

    public int getCardType(){
        return Integer.parseInt(cardDraw.split(": ")[2]);
    }

    public int getCardValue(){
        return Integer.parseInt(cardDraw.split(": ")[3]);
    }

    public String[] getCardSet() {
        return cardSet;
    }

    public String getCardDraw() {
        return cardDraw.split(": ")[1];
    }
    public int getCardNr(){
        String[] chanceCardLine = getCardDraw().split(": ");
        return Integer.parseInt(chanceCardLine[0]);
    }

    public String getCardText(){
        return getCardDraw();

    }
}