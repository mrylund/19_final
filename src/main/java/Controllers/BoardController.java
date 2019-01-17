package Controllers;


import Gameboard.Gameboard;
import gui_fields.GUI_Player;
import java.awt.*;
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

    public void displayChanceCard(String text){
        board.getBoard().setChanceCard(text);
        board.getBoard().displayChanceCard();
    }

    public void setDice(int dice1, int dice2) {
        board.getBoard().setDice(dice1, dice2);
    }

    public void setCarpos(GUI_Player player, int prevPos, int newPos) {
        int moveAmount;
        if(prevPos > newPos){
            moveAmount = 40-prevPos + newPos + 1;
        }else{
            moveAmount = newPos - prevPos - 1;
        }
        moveCar(player,prevPos - 1,moveAmount-1);
    }

    public int moveCar(GUI_Player player, int prevpos, int amount) {
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
            int speed = 200;
            //acceleration
            int newSpeed = (int)(speed - (amount * 1.4));
            sleep(newSpeed);

            if (prevpos == newpos) {
                moving = false;
            }
        }

        return newpos + 1;
    }

    public int getFieldType(int fieldnumber) {
        return board.getFieldType(fieldnumber);
    }

    public void purchaseProperty(int field, int playerNumber, Color color) {
        board.setFieldOwner(field, playerNumber, color);
    }

    public boolean fieldHasOwner(int field) {
        int owner = board.getFieldOwner(field);
        if (owner >= 0 && owner <= 6) {
            return true;
        }
        return false;
    }

    public int getFieldOwner(int field) {
        return board.getFieldOwner(field);
    }

    public boolean hasSameType(int player) {
        if (getFieldOwner(1) == player && getFieldOwner(3) == player) {

        }

        return true;
    }



}
