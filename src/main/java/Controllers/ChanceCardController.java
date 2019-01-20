package Controllers;
import Logic.ChanceCard;
import Logic.ChanceCardDevmode;

public class ChanceCardController {

    /**
     * The constructor for the ChanceCardController creates a cardset and shuffles it when an instans of the controller is constructed.
     */
    public ChanceCardController(){
        chanceCard.makeCardSet();
        chanceCard.shuffleCard();
    }

    //private ChanceCardDevmode chanceCard = new ChanceCardDevmode();
    /**
     * An instance of the object ChanceCard is created.
     */
    private ChanceCard chanceCard = new ChanceCard();

    /**
     * This is the "drawCard()" method, used to draw a chancecard when a Player lands on a chancecard-field on the Gameboard.
     */
    public void drawCard(){
        chanceCard.drawCard();
    }

    /**
     * The method getCardValues() is used to return an int[] with the length = 2. First index is the cardType and the second is the cardValue.
     * @return
     */
    public int[] getCardValues(){
        int cardType = chanceCard.getCardType();
        int cardValue = chanceCard.getCardValue();
        return new int[]{cardType,cardValue};

    }

    /**
     * The getCardText() returns the drawn chancecard in a text format.
     * @return
     */
    public String getCardText(){
        return chanceCard.getCardText();
    }



}
