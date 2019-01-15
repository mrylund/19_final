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

    public int getInt(String message) {
        return board.getBoard().getUserInteger(message);
    }
}
