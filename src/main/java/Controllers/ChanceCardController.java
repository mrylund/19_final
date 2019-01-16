package Controllers;
import Logic.ChanceCard;

public class ChanceCardController {

    public ChanceCardController(){
        chanceCard.makeCardSet();
        chanceCard.shuffleCard();
    }

    private ChanceCard chanceCard = new ChanceCard();

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
