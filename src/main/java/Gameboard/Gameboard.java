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

    /**
     * Method responsible for creating the board and all the corresponding fields.
     */
    public void createBoard() {
        fieldInfo = reader.readFile("txtFiles/fieldInfo.txt");
        for (int i = 0; i < fieldInfo.length; i++) {
            String[] sArray = fieldInfo[i].split("; ");
            fields[i] = new Field(sArray);
            gui_fields[i] = fields[i].getField();
        }

        gameboard = new GUI_BoardController(gui_fields);
        gameboard.setChanceCard("Prøv chancen");
    }

    /**
     * @return the GUI_BoardConroller object.
     */
    public GUI_BoardController getBoard() {
        return gameboard;
    }

    /**
     * @param fieldnumber that you wish to check the fieldtype of.
     * @return the type of the field.
     */
    public int getFieldType(int fieldnumber) {
        return fields[fieldnumber - 1].getFieldType();
    }

    /**
     * @param fieldNumber that you wish to check whether has a hotel on it or not.
     * @return whether the field has a hotel on it or not.
     */
    public boolean hasHotel(int fieldNumber) {
        return fields[fieldNumber - 1].hasHotel();
    }

    /**
     * @param fieldNumber that you wish to check the current owner of.
     * @return the current owner of the field.
     */
    public int getFieldOwner(int fieldNumber) {
        return fields[fieldNumber - 1].getOwner();
    }

    /**
     * @param field that you wish to set the owner of.
     * @param player that you wish to own the field.
     * @param color that the field should be colored.
     */
    public void setFieldOwner(int field, int player, Color color) {
        fields[field - 1].setOwner(player, color);
    }

    /**
     * @param fieldNumber that you wish to check the number of houses of.
     * @return the amount of houses on the field.
     */
    public int getHouses(int fieldNumber) {
        return fields[fieldNumber - 1].getHouseCount();
    }

    /**
     * @param player that want to add houses to the field.
     * @param field that the player wish to add a house on.
     */
    public void addHouse(int player, int field) {
        if (fields[field-1].getOwner() == player && !fields[field-1].hasHotel()) {
            int amount = fields[field-1].getHouseCount();
            fields[field-1].setHouses(amount + 1);
        }
    }

    /**
     * @param player that want to add a hotel to the field.
     * @param field that the player wish to place a hotel on.
     */
    public void addHotel(int player, int field) {
        if (fields[field-1].getOwner() == player) {
            fields[field-1].setHouses(0);
            fields[field-1].setHotel(true);
        }
    }

    /**
     * @param field that you wish to get the value of.
     * @return the total value of the field including houses and hotels.
     */
    public int getTotalFieldValue(int field) {
        if (fields[field - 1].getFieldType() == 1) {
            int houses = fields[field - 1].getHouseCount();
            boolean hotel = fields[field - 1].hasHotel();
            int value = Integer.parseInt(reader.getFieldPrice(field));

            if (hotel) {
                value += Integer.parseInt(reader.getBuildPrice(field)) * 5;
            } else {
                value += Integer.parseInt(reader.getBuildPrice(field)) * houses;
            }

            return value;
        } else if (fields[field - 1].getFieldType() == 4) {
            return Integer.parseInt((reader.getFieldPrice(field)));
        }

        return 0;
    }
}
