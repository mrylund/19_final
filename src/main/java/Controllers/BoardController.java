package Controllers;

import Entities.Player;
import Gameboard.Gameboard;
import gui_fields.GUI_Player;

import static Logic.Sleep.sleep;

public class BoardController {
    private Gameboard board = new Gameboard();
    public Gameboard createBoard() {
        board.createBoard();
        return board;
    }

    public void addCars(GUI_Player[] players) {
        for (GUI_Player player : players) {
            board.getBoard().getFields()[0].setCar(player, true);
            board.getBoard().addPlayer(player);
        }
    }

    public void setDice(int dice1, int dice2) {
        board.getBoard().setDice(dice1, dice2);
    }

    public void setCarpos(GUI_Player player, int prevpos, int pos) {
        board.getBoard().getFields()[pos].setCar(player, true);
    }

    public void moveCar(GUI_Player player, int prevpos, int newpos) {
        int movepos;
        while(prevpos < newpos) {
            movepos = prevpos + 1;
            board.getBoard().getFields()[prevpos].setCar(player, false);
            board.getBoard().getFields()[movepos].setCar(player, true);
            prevpos++;
            sleep(300);
        }
    }
}
