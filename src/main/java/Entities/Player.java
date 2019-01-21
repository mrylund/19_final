package Entities;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

import java.awt.*;

public class Player {
    private String name;
    private int balance;
    private int pos;
    private boolean jailed;
    private boolean jailCard;
    private GUI_Car car;
    private GUI_Player player;
    private boolean hasLost = false;

    /**
     * @param name of the player.
     * @param color of the player vehicle.
     */
    public Player(String name, Color color) {
        this.name = name;
        balance = 30000;
        pos = 0;
        car = new GUI_Car(color, Color.BLACK, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL);
        player = new GUI_Player(name, balance, car);
    }

    /**
     * @return return the player object.
     */
    public GUI_Player getPlayer() {
        return player;
    }

    /**
     * @return the player vehicle.
     */
    public GUI_Car getCar() {
        return car;
    }

    /**
     * @param pos the new position of the player.
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * @return the current position of the player.
     */
    public int getPos() {
        return pos;
    }

    /**
     * @param balance thew new balance the player should have.
     */
    public void setBalance(int balance) {
        this.balance = balance;
        player.setBalance(this.balance);
    }

    /**
     * @param amount of money that should be added to the player balance.
     */
    public void addBalance(int amount) {
        this.balance += amount;
        if (this.balance < 0) {
            player.setBalance(0);
            hasLost = true;
        } else {
            player.setBalance(this.balance);
        }

    }

    /**
     * @return whether the player has lost the game.
     */
    public boolean hasLost() {
        return hasLost;
    }

    /**
     * @return the current balance of the player.
     */
    public int getBalance() {
        return this.balance;
    }

    /**
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * @return if the player is jailed.
     */
    public boolean isJailed() {
        return jailed;
    }

    /**
     * @param jailed if the player should be jailed or not.
     */
    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    /**
     * @return if the player has a jail freecard.
     */
    public boolean hasJailcard() {
        return jailCard;
    }

    /**
     * @param jailcard if the player should have a jail freecard.
     */
    public void setJailcard(boolean jailcard) {
        this.jailCard = jailcard;
    }

}
