package Logic;

public class Sleep {
    public static void sleep() {
        sleep(1000);
    }

    public static void sleep(int n) {
        long t0 = System.currentTimeMillis();

        long t1;
        do {
            t1 = System.currentTimeMillis();
        } while(t1 - t0 < (long)n);

    }
}
