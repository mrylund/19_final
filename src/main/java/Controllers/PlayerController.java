package Controllers;
import Entities.Player;
import Entities.PlayerList;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

public class PlayerController {

    public PlayerController(){

    }

    private PlayerList players = new PlayerList();

    /**
     * This method is used to create Players.
     * @param spillere is the input of type Array consisting names made by user inputs.
     * @return is a Player[] consisting of Players(name, Color).
     */
    public Player[] createPlayers(String[] spillere) {
        return players.addPlayers(spillere);
    }

    public GUI_Car[] getPlayerCars() {
        return players.getCars();
    }

    /**
     * This method is used in the GameController when adding a GUI_Car to a GUI_Player.
     * @return is an Array of GUI_Players
     */
    public GUI_Player[] getPlayersGUI() {
        GUI_Player[] guiPlayers = new GUI_Player[players.getPlayers().length];
        for (int i = 0; i < players.getPlayers().length ; i++) {
            guiPlayers[i] = players.getPlayerGUI(i);
        }
        return guiPlayers;
    }

    /**
     * This method return a single GUI_Player, and is further used in GameController to get a Player's name.
     * @param playerNumber
     * @return GUI_Player
     */
    public GUI_Player getPlayerGUI(int playerNumber) {
        return players.getPlayerGUI(playerNumber);
    }

    /**
     * This method is used to get a Player's position
     * @param playerNumber is the Player's number.
     * @return
     */
    public int getPlayerPos(int playerNumber) {
        return players.getPlayer(playerNumber).getPos();
    }

    /**
     * This method is used to get a Player.
     * @param playerNumber
     * @return is of type Player.
     */
    public Player getPlayer(int playerNumber) {
        return players.getPlayer(playerNumber);
    }

    /**
     * This method is used to get a Player's current balance.
     * @param playerNumber is the current Player's number.
     * @return Player's balance
     */
    public int getBalance(int playerNumber) {
        return players.getPlayer(playerNumber).getBalance();
    }

    /**
     * This void is used to move a Player to a position.
     * The mod is used, because the are 40 fields, and you can not be moved to a field 41.
     * And an amount of 4000 is added to a Player's balance when "Start" is passed.
     * @param player is the current Player's playerNumber.
     * @param prevPos is the Player's position.
     * @param amount is the amount a Player will be moved.
     */
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

    /**
     * This method is used to set a Player's position to a new field.
     * We also use this method with the boolean startMoney = false, when a player is Jailed.
     * @param player is the current Player's playerNumber.
     * @param newPos is the position the Player will be moved to.
     * @param startMoney is a boolean to tell if a player will receive startMoney or not.
     */
    public void setPlayerPos(int player, int newPos, boolean startMoney){
        if(startMoney){
            players.getPlayer(player).addBalance(4000);
        }
        players.getPlayer(player).setPos(newPos - 1);
    }

    /**
     * This method is used to just set a Player's position to a field.
     * @param player is the current Player's playerNumber.
     * @param newPos is the position a Player will be set to.
     */
    public void setPlayerPos(int player, int newPos){
       setPlayerPos(player,newPos,false);
    }

    /**
     * This method is used to payRent from a current Player to the fieldOwner of the currentPlayer's position.
     * @param curPlayer is the current Player's playerNumber.
     * @param ownerOfField is the owner of the field.
     * @param amount is the amount to be payed in rent.
     * @return
     */
    public void payRent(int curPlayer, int ownerOfField, int amount) {
        players.getPlayer(curPlayer).addBalance(-amount);
        players.getPlayer(ownerOfField).addBalance(amount);
    }

    /**
     * This method is used to check if a player can afford to buy a property or pay rent.
     * @param player is the current Player's playerNumber.
     * @param amount is the amount a Player has to afford.
     * @return true or false.
     */
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

    /**
     * This checks if player has lost through the Player class' addBalance method.
     * @param player is the current Player's playerNumber.
     * @return
     */
    public boolean playerLost(int player) {
        return players.getPlayer(player).hasLost();
    }
}
