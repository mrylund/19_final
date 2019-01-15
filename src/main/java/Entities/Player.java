package Entities;

import gui_fields.GUI_Car;
import gui_fields.GUI_Player;

import java.awt.*;

public class Player {
    private String name;
    private int balance;
    private int pos;
    private GUI_Car car;
    private GUI_Player player;

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
}
