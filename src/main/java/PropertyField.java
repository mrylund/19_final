import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class PropertyField {
    public PropertyField() {
        readFile();
    }

    private HashMap<String, String> fieldInfo = new HashMap<String, String>();

    @SuppressWarnings("Duplicates")
    public void readFile() {
        String fileName = "txtFiles/fieldInfo.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String lines;
            while((lines = reader.readLine()) != null) {
                String[] lineInfo = lines.split(": ");
                fieldInfo.put(lineInfo[0], lineInfo[1]);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getFieldValue(String tile) {
        String valueToSplit = fieldInfo.get(tile);
        return valueToSplit.split("; ");
    }

    public String getFieldName(String tile) {
        return getFieldValue(tile)[2];
    }
    public int getFieldPrice(String tile) {
        return Integer.parseInt(getFieldValue(tile)[1]);
    }
    public int getFieldRent(String tile) {
        return Integer.parseInt(getFieldValue(tile)[2]);
    }
    public int getFieldhouse1(String tile) {
        return Integer.parseInt(getFieldValue(tile)[3]);
    }
    public int getFieldhouse2(String tile) {
        return Integer.parseInt(getFieldValue(tile)[4]);
    }
    public int getFieldhouse3(String tile) {
        return Integer.parseInt(getFieldValue(tile)[5]);
    }
    public int getFieldhouse4(String tile) {
        return Integer.parseInt(getFieldValue(tile)[6]);
    }
    public int getFieldHotel(String tile) {
        return Integer.parseInt(getFieldValue(tile)[7]);
    }
    public int getFieldBuildPrice(String tile) {
        return Integer.parseInt(getFieldValue(tile)[8]);
    }

    public static void main(String[] args) {
        int name = 0, price = 1, rent = 2, house1 = 3, house2 = 4, house3 = 5, house4 = 6, hotel = 7, build = 8;

        PropertyField prop = new PropertyField();
        ChanceCard chance = new ChanceCard();
        prop.readFile();
        System.out.println(prop.getFieldBuildPrice("17"));

        chance.readFile();
        chance.makeCardSet();
        chance.shuffleCard();
        System.out.println(Arrays.toString(chance.getCardSet()));
        System.out.println(chance.getCardSet().length);
        chance.drawCard();
        System.out.println(chance.getCardDraw());
        System.out.println(Arrays.toString(chance.getCardSet()));
    }
}