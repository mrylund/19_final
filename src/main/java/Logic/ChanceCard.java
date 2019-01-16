package Logic;

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
        for(int i = 0; i < cardSet.length; i++ ){
            if(i < 9){
                cardSet[i] = cardSet[i].substring(3);
            }else{
                cardSet[i] = cardSet[i].substring(4);
            }
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