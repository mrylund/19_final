package Logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReadFileTest {

    ReadFile readFile = new ReadFile();

    @Test
    //hurtig metode
    public void navn() {
        long time = 0;
        for (int i = 0; i < 50; i++) {
            long t0 = System.currentTimeMillis();
            for (int j = 1; j < 40; j++) {
                System.out.println(readFile.navn(j));
            }
            long t1 = System.currentTimeMillis();
            time += (t1-t0);
        }
        long avgTime = time/50;
        System.out.println(avgTime + " millisek");
        assertTrue(avgTime < 3);
    }

    @Test
    //langsom metode
    public void getFieldName() {
        long time = 0;
        for (int i = 0; i < 50; i++) {
            long t0 = System.currentTimeMillis();
            for (int j = 1; j < 40; j++) {
                System.out.println(readFile.getFieldName(j));
            }
            long t1 = System.currentTimeMillis();
            time += (t1-t0);
        }
        long avgTime = time/50;
        System.out.println(avgTime + " millisek");
        assertTrue(avgTime > 10);
    }
}