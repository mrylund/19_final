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
            moveAmount = 40-prevPos + newPos;
        }else{
            moveAmount = newPos - prevPos;
        }
        moveCar(player,prevPos - 1,moveAmount);
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
        if (getFieldOwner(2) == player && getFieldOwner(4) == player) {
            return true;
        } else if (getFieldOwner(7)  == player && getFieldOwner(9)  == player && getFieldOwner(10)  == player) {
            return true;
        } else if (getFieldOwner(12)  == player && getFieldOwner(14)  == player && getFieldOwner(15)  == player) {
            return true;
        } else if (getFieldOwner(17)  == player && getFieldOwner(19)  == player && getFieldOwner(20)  == player) {
            return true;
        } else if (getFieldOwner(22)  == player && getFieldOwner(24)  == player && getFieldOwner(25)  == player) {
            return true;
        } else if (getFieldOwner(27)  == player && getFieldOwner(28)  == player && getFieldOwner(30)  == player) {
            return true;
        } else if (getFieldOwner(32)  == player && getFieldOwner(33)  == player && getFieldOwner(35)  == player) {
            return true;
        } else return getFieldOwner(38) == player && getFieldOwner(40) == player;
    }

    public boolean hasAllFields(int player, int fieldNumber) {
        if (fieldNumber == 2 || fieldNumber == 4) {
            return getFieldOwner(2) == player && getFieldOwner(4) == player;
        } else if (fieldNumber == 7 || fieldNumber == 9 || fieldNumber == 10) {
            return getFieldOwner(7) == player && getFieldOwner(9) == player && getFieldOwner(10) == player;
        }else if (fieldNumber == 12 || fieldNumber == 14 || fieldNumber == 15) {
            return getFieldOwner(12) == player && getFieldOwner(14) == player && getFieldOwner(15) == player;
        }else if (fieldNumber == 17 || fieldNumber == 19 || fieldNumber == 20) {
            return getFieldOwner(17) == player && getFieldOwner(19) == player && getFieldOwner(20) == player;
        }else if (fieldNumber == 22 || fieldNumber == 24 || fieldNumber == 25) {
            return getFieldOwner(22) == player && getFieldOwner(24) == player && getFieldOwner(25) == player;
        }else if (fieldNumber == 27 || fieldNumber == 28 || fieldNumber == 30) {
            return getFieldOwner(27) == player && getFieldOwner(28) == player && getFieldOwner(30) == player;
        }else if (fieldNumber == 32 || fieldNumber == 33 || fieldNumber == 35) {
            return getFieldOwner(32) == player && getFieldOwner(33) == player && getFieldOwner(35) == player;
        }else if (fieldNumber == 38 || fieldNumber == 40) {
            return getFieldOwner(38) == player  && getFieldOwner(40) == player;
        }
        return false;
    }


    public boolean purchaseHouse(int player, int field) {
        if (board.getHouses(field) < 4) {
            board.addHouse(player, field);
            return true;
        } else if (board.getHouses(field) == 4) {
            board.addHotel(player, field);
        }

        return false;
    }


    public int getOwnedAmountOfShippingFields(int fieldNumber){
        int counter = 0;
        if(!fieldHasOwner(fieldNumber)){
            return counter;
        }else{
            int ownerOfField = getFieldOwner(fieldNumber);
            for(int i = 6; i <= 36; i = i + 10){
                if(getFieldOwner(i) == ownerOfField){
                    counter++;
                }
            }
            return counter;
        }
    }

}
