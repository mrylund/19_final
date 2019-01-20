package Controllers;

import Gameboard.Gameboard;
import gui_fields.GUI_Player;
import java.awt.*;
import static Logic.Sleep.sleep;

public class BoardController {

    private Gameboard board = new Gameboard();

    /**
     * This is the constructor for the Gameboard class.
     * @return output is the board created with the createBoard() method.
     */
    public Gameboard createBoard() {
        board.createBoard();
        return board;
    }

    /**
     * This void is used to add cars to the Gameboard.
     * @param players is the GUI_Player[] input, which is then used to assign a car to each player with help of the "for-each" loop.
     */
    public void addCars(GUI_Player[] players) {
        for (GUI_Player player : players) {
            board.getBoard().getFields()[0].setCar(player, true);
            board.getBoard().addPlayer(player);
        }
    }

    /**
     * This void is primarily used to display a field's properties when a Player lands on it, but is also used to display the chancecard a user has drawn.
     * @param text is input that will be displayed on the GUI, if this method is used.
     */
    public void displayChanceCard(String text){
        board.getBoard().setChanceCard(text);
        board.getBoard().displayChanceCard();
    }

    /**
     * This method is used to display the user's diceroll on the GUI board.
     * @param dice1 is the first die's facevalue.
     * @param dice2 is the second die's facevalue.
     */
    public void setDice(int dice1, int dice2) {
        board.getBoard().setDice(dice1, dice2);
    }

    /**
     * This void is used to set a Player's car's position to a specific spot on the GUI board.
     * @param player is a parameter of the type Player.
     * @param prevPos is the Player's position.
     * @param newPos is where the Player will be moved to.
     */
    public void setCarpos(GUI_Player player, int prevPos, int newPos) {
        int moveAmount;
        if(prevPos > newPos){
            moveAmount = 40-prevPos + newPos;
        }else{
            moveAmount = newPos - prevPos;
        }
        moveCar(player,prevPos - 1,moveAmount);
    }

    /**
     * This method is used to calculate where a Player's car needs to be moved to.
     * @param player is an input parameter of type GUI_Player.
     * @param prevPos is the Player's position.
     * @param amountOfFields is the amount of fields the player needs to be moved.
     * @return is the fieldNumber the player will be moved to.
     * The while-loop uses the board to remove a players car from a "prevPos" and sets a car on the next position,
     * until the player has reached the destination.
     */
    public int moveCar(GUI_Player player, int prevPos, int amountOfFields) {
        boolean moving = true;
        int movePos = prevPos + 1;
        int newPos = prevPos + amountOfFields;
        movePos %= 40;
        newPos %= 40;

        while(moving) {
            board.getBoard().getFields()[prevPos].setCar(player, false);
            board.getBoard().getFields()[movePos].setCar(player, true);
            prevPos++;
            prevPos %= 40;
            movePos = prevPos + 1;
            movePos %= 40;
            int speed = 200;
            //acceleration
            int newSpeed = (int)(speed - (amountOfFields * 1.4));
            sleep(newSpeed);

            if (prevPos == newPos) {
                moving = false;
            }
        }
        return newPos + 1;
    }

    /**
     * This method is used to get the amount of houses on a specific fieldNumber.
     * @param fieldNumber is fieldNumber to be checked for houses.
     * @return type is an Integer, which is found through the Gameboard class' method getHouses(fieldNumber).
     */
    public int getHouses(int fieldNumber) {
        return board.getHouses(fieldNumber);
    }

    /**
     * This method is used to get the type of a field with the help of it's number.
     * @param fieldNumber
     * @return type is an Integer, and is used in GameController to determine what methods to execute,
     * when a player lands on a field.
     */
    public int getFieldType(int fieldNumber) {
        return board.getFieldType(fieldNumber);
    }

    /**
     * This void is used when a Player want's to purchase a property.
     * @param field is the fieldNumber to be purchased.
     * @param playerNumber is the Player's playerNumber.
     * @param color is the color the field will be bordered by.
     */
    public void purchaseProperty(int field, int playerNumber, Color color) {
        board.setFieldOwner(field, playerNumber, color);
    }

    /**
     * This method is used to check if a field has an owner already.
     * @param field is the fieldNumber to be checked for an owner.
     * @return type is a boolean meaning it will either be "true" or "false".
     */
    public boolean fieldHasOwner(int field) {
        int owner = board.getFieldOwner(field);
        if (owner >= 0 && owner <= 6) {
            return true;
        }
        return false;
    }

    /**
     * This method is used to get a fieldOwner's playerNumber.
     * @param field is the fieldNumber.
     * @return is an Integer of fieldOwner's playerNumber.
     */
    public int getFieldOwner(int field) {
        return board.getFieldOwner(field);
    }

    /**
     * This boolean is used to check if a player is allowed to buy a house on his property.
     * The requirement for a player to be able to purchase a house is that he owns all the same type of fields.
     * @param curPlayer is the curPlayer's number.
     * @return true if the curPlayer owns all the same type of fields.
     */
    public boolean hasSameType(int curPlayer) {
        if (getFieldOwner(2) == curPlayer && getFieldOwner(4) == curPlayer) {
            return true;
        } else if (getFieldOwner(7)  == curPlayer && getFieldOwner(9)  == curPlayer && getFieldOwner(10)  == curPlayer) {
            return true;
        } else if (getFieldOwner(12)  == curPlayer && getFieldOwner(14)  == curPlayer && getFieldOwner(15)  == curPlayer) {
            return true;
        } else if (getFieldOwner(17)  == curPlayer && getFieldOwner(19)  == curPlayer && getFieldOwner(20)  == curPlayer) {
            return true;
        } else if (getFieldOwner(22)  == curPlayer && getFieldOwner(24)  == curPlayer && getFieldOwner(25)  == curPlayer) {
            return true;
        } else if (getFieldOwner(27)  == curPlayer && getFieldOwner(28)  == curPlayer && getFieldOwner(30)  == curPlayer) {
            return true;
        } else if (getFieldOwner(32)  == curPlayer && getFieldOwner(33)  == curPlayer && getFieldOwner(35)  == curPlayer) {
            return true;
        } else return getFieldOwner(38) == curPlayer && getFieldOwner(40) == curPlayer;
    }

    /**
     * This method is used to check if a player has all the same type of fields.
     * @param player is the player's number.
     * @param fieldNumber is the field number the player is on.
     * @return true if player has all of the same type of fields.
     */
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

    /**
     * Method checks if there's a hotel
     * @param fieldNumber is the fieldNumber to be checked for hotels.
     * @return type is a boolean.
     */
    public boolean hasHotel(int fieldNumber) {
        return board.hasHotel(fieldNumber);
    }

    /**
     *
     * @param fieldNumber
     * @return
     */
    public boolean hasHouses(int fieldNumber) {
        return board.hasHotel(fieldNumber);
    }

    /**
     * This method is used to purchase to purchase a house on a field.
     * @param player is the Player's playerNumber.
     * @param field is the field a Player wants to purchase a house on.
     * @return
     */
    public boolean purchaseHouse(int player, int field) {
        if (board.getHouses(field) < 4) {
            board.addHouse(player, field);
            return true;
        } else if (board.getHouses(field) == 4) {
            board.addHotel(player, field);
        }
        return false;
    }

    /**
     * This method is used to find the amount of ShippingFields a Player owns.
     * It's used to calculate the rent others have to pay the owner.
     * @param fieldNumber is the fieldNumber to be checked for owners.
     * @return is an Integer with the ammount of ShippingFields owned.
     */
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

    /**
     * This methods goes to all the fields and checks if a Player owns any of them,
     * and then assigns the value of the fields to an Integer.
     * @param player is the Player's playerNumber.
     * @return is the value of a Player's properties.
     */
    public int getTotalPropertyValues(int player) {
        int value = 0;
        for(int i = 1; i < 40; i++) {
            if (board.getFieldOwner(i) == player) {
                value += board.getTotalFieldValue(i);
            }
        }
        return value;
    }

    /**
     * This method is used to get the amount of houses a Player owns.
     * @param player is the Player's playerNumber.
     * @return is an Integer and is the amount of houses.
     */
    public int getPlayerHouseCount(int player) {
        int houses = 0;
        for(int i = 1; i < 40; i++) {
            if (board.getFieldOwner(i) == player) {
                houses += board.getHouses(i);
            }
        }
        return houses;
    }

    public int getPlayerHotelCount(int player) {
        int hotels = 0;
        for(int i = 1; i < 40; i++) {
            if (board.getFieldOwner(i) == player && board.hasHotel(i)) {
                hotels++;
            }
        }
        return hotels;
    }

}
