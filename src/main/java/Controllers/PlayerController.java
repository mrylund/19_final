package Controllers;
import Entities.Player;
import Entities.PlayerList;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

import static Logic.Sleep.sleep;

public class PlayerController {
    private PlayerList players = new PlayerList();

    public Player[] createPlayers(String[] spillere) {
        return players.addPlayers(spillere);
    }

    public GUI_Car[] getPlayerCars() {
        return players.getCars();
    }

    public GUI_Player[] getPlayersGUI() {
        GUI_Player[] guiPlayers = new GUI_Player[players.getPlayers().length];
        for (int i = 0; i < players.getPlayers().length ; i++) {
            guiPlayers[i] = players.getPlayerGUI(i);
        }
        return guiPlayers;
    }

    public int getPlayerPos(int playerNumber) {
        return players.getPlayer(playerNumber).getPos();
    }

    public GUI_Player getPlayer(int playerNumber) {
        return players.getPlayerGUI(playerNumber);
    }

    public void movePlayer(int player, int prevpos, int amount) {
        int newpos = prevpos + amount;
        if (newpos > 39) {
            newpos %= 40;
            players.getPlayer(player).addBalance(4000);
        }
        players.getPlayer(player).setPos(newpos);
    }
}
