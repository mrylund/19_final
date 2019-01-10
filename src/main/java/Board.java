import gui_codebehind.GUI_BoardController;
import gui_fields.GUI_Field;
import gui_fields.GUI_Start;
import gui_fields.GUI_Street;

import java.awt.*;

public class Board {
    PropertyField property = new PropertyField();
    private GUI_Field[] fields;
    private GUI_BoardController board;

    public void makeBoard() {
        fields = new GUI_Field[40];

        fields[0] = new GUI_Start();

        for (int i = 1; i < 40; i++) {
            fields[i] = new GUI_Street();
            fields[i].setTitle(property.getFieldName("17"));
        }

        board = new GUI_BoardController(fields, Color.GRAY);
    }

    public static void main(String[] args) {
        Board board = new Board();
        board.makeBoard();
    }
}
