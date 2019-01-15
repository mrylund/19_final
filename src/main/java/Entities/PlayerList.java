package Entities;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

import java.awt.*;

public class PlayerList {
    private Player[] players;
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

    public GUI_Player getPlayerGUI(int number) {
        return players[number].getPlayer();
    }

    public Player[] getPlayers() {
        return players;
    }

    public GUI_Car[] getCars() {
        GUI_Car[] cars = new GUI_Car[players.length];
        for(int i = 0; i < players.length; i++) {
            cars[i] = players[i].getCar();
        }

        return cars;
    }
}
