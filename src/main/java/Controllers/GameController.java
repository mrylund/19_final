package Controllers;

import Entities.Player;
import Gameboard.Gameboard;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import jdk.internal.util.xml.impl.Input;

public class GameController {
    private BoardController boardController = new BoardController();
    private PlayerController playerController = new PlayerController();
    private InputController input;


    public GameController() {
        InitializeGame();
    }

    public void InitializeGame() {
        int antalSpillere = 0;
        input = new InputController(boardController.createBoard());
        antalSpillere = input.getInt("Hvor mange spillere?", 3, 6);
        playerController.createPlayers(antalSpillere);
        GUI_Player[] guiPlayers = playerController.getPlayersGUI();
        boardController.addCars(guiPlayers);

    }
}
