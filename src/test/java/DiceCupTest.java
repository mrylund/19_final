import org.junit.Test;

import static org.junit.Assert.*;

public class DiceCupTest {
    DiceCup diceCup = new DiceCup();

    @Test
    public void faceValue() {
        final int NUMBEROFTEST = 400000;
        final int PROCENT = 3;

        //array 1 longer than number of eyes - from 1 to 6
        int freq[] = new int[diceCup.getEYES()+1];
        int total = 0;

        //add values to the array
        for(int i = 0; i < NUMBEROFTEST; i++) {
            ++freq[diceCup.faceValue()];
        }
        System.out.println("Num\t\tfreq");

        //shows values for the array
        for(int i = 1; i < freq.length; i++) {
            System.out.println(i + "\t\t" + freq[i]);
            total += freq[i];
        }
        System.out.println("Total:\t" + total);

        //what we want in theory and a falure margin
        int theory = NUMBEROFTEST / diceCup.getEYES();
        int failure = theory / 100 * PROCENT;

        //tests if the values in the array is within our failure rate
        for (int i = 1; i < freq.length; i++) {
            assertTrue(freq[i] < theory + failure && freq[i] > theory - failure);
            assertTrue(total == NUMBEROFTEST);
        }
    }

    @Test
    public void getSum() {
        int[] testDice = {1,2};
        int[] dice = diceCup.setDiceArray(testDice);
        assertEquals(3,3);
    }

    @Test
    public void isSameValue() {
        int[] testDiceSame = {4,4};
        int[] testDiceNotSame = {1,2};
        int[] diceSame = diceCup.setDiceArray(testDiceSame);
        assertTrue(diceCup.isSameValue());
        int[] diceNotSame = diceCup.setDiceArray(testDiceNotSame);
        assertTrue(!diceCup.isSameValue());
    }
}