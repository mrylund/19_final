package Gameboard;
import Logic.ReadFile;
import gui_codebehind.GUI_BoardController;
import gui_fields.GUI_Field;
import java.awt.*;


public class Gameboard {
    private ReadFile reader = new ReadFile();
    private String[] fieldInfo;
    private GUI_Field[] gui_fields = new GUI_Field[40];
    private Field[] fields = new Field[40];
    private GUI_BoardController gameboard;

    public Gameboard(){}

    public void createBoard() {
        fieldInfo = reader.readFile("txtFiles/fieldInfo.txt");
        for (int i = 0; i < fieldInfo.length; i++) {
            String[] sArray = fieldInfo[i].split("; ");
            fields[i] = new Field(sArray);
            gui_fields[i] = fields[i].getField();
            if (i == 1 || i == 3) {
                fields[i].setOwner(0, Color.white);
            }
        }

        gameboard = new GUI_BoardController(gui_fields);
        gameboard.setChanceCard("FUCK");
    }

    public GUI_BoardController getBoard() {
        return gameboard;
    }

    public int getFieldType(int fieldnumber) {
        return fields[fieldnumber - 1].getFieldType();
    }

    public int getFieldOwner(int fieldNumber) {
        return fields[fieldNumber -  1].getOwner();
    }

    public void setFieldOwner(int field, int player, Color color) {
        fields[field - 1].setOwner(player, color);
    }

    public int getHouses(int fieldNumber) {
        return fields[fieldNumber - 1].getHouseCount();
    }

    public void addHouse(int player, int field) {
        if (fields[field-1].getOwner() == player && !fields[field-1].hasHotel()) {
            int amount = fields[field-1].getHouseCount();
            fields[field-1].setHouses(amount + 1);
        }
    }

    public void addHotel(int player, int field) {
        if (fields[field-1].getOwner() == player) {
            fields[field-1].setHouses(0);
            fields[field-1].setHotel(true);
        }
    }
}
