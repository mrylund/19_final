package Gameboard;
import Logic.ReadFile;
import gui_fields.GUI_Field;
import gui_main.GUI;


public class Gameboard {
    private ReadFile reader = new ReadFile();
    private String[] fieldInfo;
    private GUI_Field[] gui_fields = new GUI_Field[40];
    private Field[] fields = new Field[40];
    public Gameboard() {
        fieldInfo = reader.readFile("txtFiles/fieldInfo.txt");
        for (int i = 0; i < fieldInfo.length; i++) {
            String[] sArray = fieldInfo[i].split("; ");
            fields[i] = new Field(sArray);
            gui_fields[i] = fields[i].getField();
        }

        GUI gameboard = new GUI(gui_fields);
    }
}
