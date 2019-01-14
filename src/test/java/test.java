import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class test {

    @Test
    public void readFile() {
        String fileName = "txtFiles/tests.txt";
        String line = "";
    try {
        BufferedReader testReader = new BufferedReader(new FileReader(fileName));
        line = testReader.readLine();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    assertEquals("1: this is a test",line );
    }

    @Test
    public void makeCardSet() {
    }

    @Test
    public void shuffleCard() {
    }

    @Test
    public void drawCard() {
    }

    @Test
    public void getCardSet() {
    }

    @Test
    public void getCardDraw() {
    }
}
