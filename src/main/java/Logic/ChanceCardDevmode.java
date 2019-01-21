package Logic;

import java.util.Random;

public class ChanceCardDevmode {

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
    }

    public int  drawCard() {
        cardDraw = cardSet[0];
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
