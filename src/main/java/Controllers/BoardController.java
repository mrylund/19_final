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

    public void moveCar(GUI_Player player, int prevpos, int amount) {
        boolean moving = true;
        int movepos = prevpos + 1;
        int newpos = prevpos + amount;
        movepos %= 40;
        newpos %= 40;

        while(moving) {
            board.getBoard().getFields()[prevpos].setCar(player, false);
            board.getBoard().getFields()[movepos].setCar(player, true);
            prevpos++;
            prevpos %= 40;
            movepos = prevpos + 1;
            movepos %= 40;
            sleep(100);

            if (prevpos == newpos) {
                moving = false;
            }
        }
    }


}
