package Controllers;
import Entities.Player;
import Entities.PlayerList;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

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
}
