package Gameboard;
import Logic.ReadFile;
import gui_codebehind.GUI_BoardController;
import gui_fields.GUI_Field;
import gui_main.GUI;


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
        }

        gameboard = new GUI_BoardController(gui_fields);
    }

    public GUI_BoardController getBoard() {
        return gameboard;
    }

    public int getFieldType(int fieldnumber) {
        return fields[fieldnumber].getFieldType();
    }

    public int getFieldOwner(int fieldNumber) {
        return fields[fieldNumber].getOwner();
    }

    public void setFieldOwner(int field, int player) {
        fields[field].setOwner(player);
    }
}
