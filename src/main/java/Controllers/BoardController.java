package Controllers;

import Entities.Player;
import Gameboard.Gameboard;
import gui_fields.GUI_Player;

public class BoardController {
    private Gameboard board = new Gameboard();
    public Gameboard createBoard() {
        board.createBoard();
        return board;
    }

    public void addCars(GUI_Player[] players) {
        for (GUI_Player player : players) {
            board.getBoard().getFields()[0].setCar(player, true);
        }
    }
}
