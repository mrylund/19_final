package Entities;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

import java.awt.*;

public class PlayerList {
    /**
     * PlayerList creates an instant of Player
     */
    private Player[] players;

    /**
     * This method return a Player[] containing all the Players added to the array.
     * @param spillere is an String[] containing the names of the Player's, and is defined in the GameController.
     * @return type is a Player[] containing Players(String name, Color) with a color assigned to them.
     */
    public Player[] addPlayers(String[] spillere) {
        Color color;
        players = new Player[spillere.length];
        for(int i = 0; i < spillere.length; i++) {
            switch (i) {
                case 0: color = new Color(255, 255, 255); break;
                case 1: color = new Color(0, 0,0); break;
                case 2: color = new Color(255, 0, 0); break;
                case 3: color = new Color(0, 255, 0); break;
                case 4: color = new Color(0, 0, 255); break;
                case 5: color = new Color(255, 255, 0); break;
                default: color = new Color(255, 255, 255); break;
            }
            players[i] = new Player(spillere[i], color);
        }
        return players;
    }

    /**
     *
     * @param number
     * @return
     */
    public GUI_Player getPlayerGUI(int number) {
        return players[number].getPlayer();
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber];
    }

    public GUI_Car[] getCars() {
        GUI_Car[] cars = new GUI_Car[players.length];
        for(int i = 0; i < players.length; i++) {
            cars[i] = players[i].getCar();
        }

        return cars;
    }

    public boolean playerCanAfford(int player, int amount) {
        if (players[player].getBalance() > amount) {
            return true;
        } else { return false;}
    }


}
