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

    /**
     * Method responsible for making the deck.
     */
    public void makeCardSet(){
        this.cardSet = reader.readFile(chanceCardPath);
    }

    /**
     * Method responsible for shuffling the whole deck.
     */
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

    /**
     * @return the number of the drawn card.
     */
    public int  drawCard() {
        cardDraw = cardSet[0];
        //lægger det bagerst i bunken
        for (int i = 0; i < cardSet.length-1; i++) {
            cardSet[i] = cardSet[i+1];
        }
        cardSet[cardSet.length-1] = cardDraw;
       return Integer.parseInt(cardDraw.split(": ")[0]);
    }

    /**
     * @return the type of the card drawn.
     */
    public int getCardType(){
        return Integer.parseInt(cardDraw.split(": ")[2]);
    }

    /**
     * @return get the value of the card.
     */
    public int getCardValue(){
        return Integer.parseInt(cardDraw.split(": ")[3]);
    }

    /**
     * @return the whole deck.
     */
    public String[] getCardSet() {
        return cardSet;
    }

    /**
     * @return the text of the chancecard.
     */
    public String getCardDraw() {
        return cardDraw.split(": ")[1];
    }


    /**
     * @return the current cards cardnumber.
     */
    public int getCardNr(){
        String[] chanceCardLine = getCardDraw().split(": ");
        return Integer.parseInt(chanceCardLine[0]);
    }

    /**
     * @return the text of the chancecard.
     */
    public String getCardText(){
        return getCardDraw();

    }
}