package Logic;

public class DiceCup {
    private int EYES = 6;
    private int NUMBEROFDICE = 2;
    private int[] dice;

    /**
     * @return a number between 1 and max allowed eyes (6)
     */
    public int faceValue() {
        return (int)(Math.random() * EYES + 1);
    }

    /**
     * @return an integer array containing the dies facevalues.
     */
    public int[] roll() {
        dice = new int[NUMBEROFDICE];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = faceValue();
        }
        return dice;
    }

    /**
     * @return the total amount of eyes of all the dies.
     */
    public int getSum() {
        int diceSum = 0;
        for (int diceIndex: this.dice) {
            diceSum += diceIndex;
        }
        return diceSum;
    }

    /**
     * @return whether the dies have the same facevalue or not.
     */
    public boolean isSameValue() {
        int firstDie = dice[0];
        int counter = 1;
        for (int i = 1; i < dice.length; i++) {
            if(dice[i] == firstDie) {
                counter++;
            }
        }
        return counter == dice.length;
    }

    /**
     * @param arr the Integer array containing the facevalues.
     * @return a formatted String
     */
    public String toString(int[] arr) {
        String arrayString = "";
        for (int i = 0; i < arr.length; i++) {
            arrayString += "[" + arr[i] + "]";
        }
        return arrayString.replace("][", "], [");
    }

    /**
     * @return the max amount of eyes the die has.
     */
    //get og set metoder
    public int getEYES() {
        return EYES;
    }

    /**
     * @return the number of dies in the game.
     */
    public int getNUMBEROFDICE() {
        return NUMBEROFDICE;
    }

    /**
     * @param eyes the amount of eyes a die should have.
     */
    public void setEYES(int eyes) {
        this.EYES = eyes;
    }

    /**
     * @param NUMBEROFDICE the amount of dies the game should have.
     */
    public void setNUMBEROFDICE(int NUMBEROFDICE) {
        this.NUMBEROFDICE = NUMBEROFDICE;
    }

    /**
     * @return the facevalue of die1
     */
    public int getDie1() {
        return dice[0];
    }

    /**
     * @return the facevalue of die2
     */
    public int getDie2() {
        return dice[1];
    }

    /**
     * @param arr
     * @return
     */
    public int[] setDiceArray(int[] arr) {
        return this.dice = arr;
    }
}