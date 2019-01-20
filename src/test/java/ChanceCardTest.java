import Logic.ChanceCard;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChanceCardTest {

    private ChanceCard chanceCard = new ChanceCard();

    //tjekker om to String[] er ens eller ej
    public boolean checkIfSame(String[] s1, String[] s2) {
        for(int i = 0; i < s1.length; i++) {
            if(!s1[i].equals(s2[i])) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void shuffleCard() {
        int counter = 0;
        chanceCard.makeCardSet();
        String[] cardSet = chanceCard.getCardSet();

        //kører 50 gange for at se om den blander tilfældigt.
        for (int i = 0; i < 50; i++) {
            //blander først 1 kort sæt og gemmer
            chanceCard.makeCardSet();
            chanceCard.shuffleCard();
            String[] shuffled = chanceCard.getCardSet();

            //blander et kortsæt til og gemmer
            chanceCard.makeCardSet();
            chanceCard.shuffleCard();
            String[] shuffled1 = chanceCard.getCardSet();

            //tjekker om de to kortsæt er ens
            if(checkIfSame(shuffled, shuffled1)) {
                counter++;
            }
        }
        assertTrue(counter == 0);
    }

    @Test
    public void drawCard() {
        //lavet et CardSet og blander det.
        chanceCard.makeCardSet();
        chanceCard.shuffleCard();
        String topCard = chanceCard.getCardSet()[0];
        chanceCard.drawCard();
        String bottonCard = chanceCard.getCardSet()[chanceCard.getCardSet().length-1];
        //tjekker om kortet er blevet langt nederst i bunken.
        assertTrue(topCard.equals(bottonCard));
    }
}