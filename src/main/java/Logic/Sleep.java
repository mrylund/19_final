package Logic;

public class Sleep {
    public static void sleep() {
        sleep(1000);
    }

    /**
     * This method is used when wanting to create a delay - fx. when moving a Player's car on the GUI board.
     * @param n is the integer input in ms which determines the delay amount.
     */
    public static void sleep(int n) {
        long t0 = System.currentTimeMillis();

        long t1;
        do {
            t1 = System.currentTimeMillis();
        } while(t1 - t0 < (long)n);

    }
}
