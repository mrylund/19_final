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

    public Player(String name, Color color) {
        this.name = name;
        balance = 30000;
        pos = 0;
        car = new GUI_Car(color, Color.BLACK, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL);
        player = new GUI_Player(name, balance, car);
    }

    public GUI_Player getPlayer() {
        return player;
    }

    public GUI_Car getCar() {
        return car;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        player.setBalance(this.balance);
    }

    public void addBalance(int amount) {
        this.balance += amount;
        if (this.balance < 0) {
            player.setBalance(0);
            hasLost = true;
        } else {
            player.setBalance(this.balance);
        }

    }

    public boolean hasLost() {
        return hasLost;
    }

    public int getBalance() {
        return this.balance;
    }

    public String getName() {
        return name;
    }

    /**
     * @return om spilleren er jailed.
     */
    public boolean isJailed() {
        return jailed;
    }

    /**
     * @param jailed om spilleren skal være i jail eller ej.
     */
    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    /**
     * @return om spilleren har et jail frikort.
     */
    public boolean hasJailcard() {
        return jailCard;
    }

    /**
     * @param jailcard om spilleren har et jail frikort.
     */
    public void setJailcard(boolean jailcard) {
        this.jailCard = jailcard;
    }

}
