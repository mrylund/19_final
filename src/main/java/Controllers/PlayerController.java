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

    public int getPlayerPos(int playerNumber) {
        return players.getPlayer(playerNumber).getPos();
    }

    public GUI_Player getPlayerGUI(int playerNumber) {
        return players.getPlayerGUI(playerNumber);
    }

    public Player getPlayer(int playerNumber) {
        return players.getPlayer(playerNumber);
    }

    public void movePlayer(int player, int prevPos, int amount) {
        int newpos = prevPos + amount;
        if (newpos > 39) {
            newpos %= 40;
            players.getPlayer(player).addBalance(4000);
        }

        players.getPlayer(player).setPos(newpos);
    }

    public boolean purchaseProperty(int player, int amount) {
        if (players.playerCanAfford(player, amount)) {
            players.getPlayer(player).addBalance(-amount);
            return true;
        } else {
            return false;
        }
    }

    public void setPlayerPos(int player, int newPos, boolean startMoney){
        if(startMoney){
            players.getPlayer(player).addBalance(4000);
        }
        players.getPlayer(player).setPos(newPos - 1);
    }

    public void setPlayerPos(int player, int newPos){
       setPlayerPos(player,newPos,false);
    }

    public boolean payRent(int curPlayer, int ownerOfField, int amount) {
        players.getPlayer(curPlayer).addBalance(-amount);
        players.getPlayer(ownerOfField).addBalance(amount);
        return true;
    }

    public boolean playerCanAfford(int player, int amount) {
        return players.playerCanAfford(player, amount);
    }

    public int getCurrentNumOfPlayers() {
        Player[] allPlayers = players.getPlayers();
        int counter = 0;
        for(Player player: allPlayers) {
            if(player.getBalance() > 0) {
                counter++;
            }
        }
        return counter-1;
    }

    public boolean playerLost(int player) {
        return players.getPlayer(player).hasLost();
    }
}
