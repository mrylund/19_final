package Controllers;

import Gameboard.Gameboard;

public class InputController {
    Gameboard board;
    public InputController(Gameboard board) {
        this.board = board;
    }

    public int getInt(String message, int min, int max) {
        return board.getBoard().getUserInteger(message, min, max);
    }

    public String[] getStringArray(String[] messages, int amount) {
        String[] output = new String[amount];

        for (int i = 0; i < amount; i++) {
            output[i] = board.getBoard().getUserString(messages[i]);
        }

        return output;
    }

    public String getButtonpress(String question, String[] buttons) {
        return board.getBoard().getUserButtonPressed(question, buttons);
    }

    public void showMessage(String text) {
        board.getBoard().showMessage(text);
    }
}
