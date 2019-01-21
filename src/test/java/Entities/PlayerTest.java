package Entities;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player = new Player("testName", Color.BLACK);

    @Test
    public void setPos() {
        int startPos = player.getPos();
        int newPos = 5;
        player.setPos(newPos);
        assertTrue(newPos == player.getPos() && newPos != startPos);
    }

    @Test
    public void getPos() {
        int newPos = 5;
        player.setPos(newPos);
        assertEquals(newPos, player.getPos());
    }

    @Test
    public void setBalance() {
        int startBalance = player.getBalance();
        int newBalance = 100;
        player.setBalance(newBalance);
        assertTrue(newBalance == player.getBalance() && newBalance != startBalance);
    }

    @Test
    public void addBalance() {
        int startBalance = player.getBalance();
        int moneyToAdd = 100;
        player.addBalance(moneyToAdd);
        assertTrue(player.getBalance() == startBalance + moneyToAdd);
    }

    @Test
    public void getBalance() {
        int balance = 100;
        player.setBalance(balance);
        assertEquals(balance, player.getBalance());
    }

    @Test
    public void isJailed() {
        
    }

    @Test
    public void setJailed() {
    }
}