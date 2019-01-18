package Logic;

public class DiceCupDevmode {
    private int[] dice = {2,0};

    public int[] roll() {
        return dice;
    }

    public int getDie1() {
        return dice[0];
    }
    public int getDie2() {
        return dice[1];
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
}
