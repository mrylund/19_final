package Controllers;
import Logic.ChanceCard;
import Logic.ChanceCardDevmode;

public class ChanceCardController {

    public ChanceCardController(){
        chanceCard.makeCardSet();
        chanceCard.shuffleCard();
    }

    private ChanceCardDevmode chanceCard = new ChanceCardDevmode();
    //private ChanceCard chanceCard = new ChanceCard();

    public void drawCard(){
        chanceCard.drawCard();
    }

    public int[] getCardValues(){
        int cardType = chanceCard.getCardType();
        int cardValue = chanceCard.getCardValue();
        return new int[]{cardType,cardValue};

    }

    public String getCardText(){
        return chanceCard.getCardText();
    }



}
