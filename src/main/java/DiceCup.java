public class DiceCup {
    private int EYES = 6;
    private int NUMBEROFDICE = 2;
    private int[] dice;

    public int faceValue() {
        return (int)(Math.random() * EYES + 1);
    }

    public int[] roll() {
        dice = new int[NUMBEROFDICE];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = faceValue();
        }
        return dice;
    }

    public int getSum() {
        int diceSum = 0;
        for (int diceIndex: this.dice) {
            diceSum += diceIndex;
        }
        return diceSum;
    }

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

    public String toString(int[] arr) {
        String arrayString = "";
        for (int i = 0; i < arr.length; i++) {
            arrayString += "[" + arr[i] + "]";
        }
        return arrayString.replace("][", "], [");
    }

    //get og set metoder
    public int getEYES() {
        return EYES;
    }
    public int getNUMBEROFDICE() {
        return NUMBEROFDICE;
    }
    public void setEYES(int eyes) {
        this.EYES = eyes;
    }
    public void setNUMBEROFDICE(int NUMBEROFDICE) {
        this.NUMBEROFDICE = NUMBEROFDICE;
    }
    public int getDie1() {
        return dice[0];
    }
    public int getDie2() {
        return dice[1];
    }
    public int[] setDiceArray(int[] arr) {
        return this.dice = arr;
    }
}