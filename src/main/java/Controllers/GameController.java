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


    public GameController(boolean devmode) {
        InitializeGame(devmode);
    }

    public void InitializeGame(boolean devmode) {
        int antalSpillere;
        String[] spillernavne;
        input = new InputController(boardController.createBoard());
        if (devmode) {
            antalSpillere = 5;
            spillernavne = new String[] {
                    "Martin",
                    "Patrick",
                    "Athusan",
                    "Lars",
                    "Andreas"
            };
        } else {
            antalSpillere = input.getInt("Hvor mange spillere?", 3, 6);
            spillernavne = input.getStringArray(new String[]{"Hvad hedder spiller 1?", "Hvad hedder spiller 2?", "Hvad hedder spiller 3?", "Hvad hedder spiller 4?", "Hvad hedder spiller 5?", "Hvad hedder spiller 6?"}, antalSpillere);
        }
        playerController.createPlayers(spillernavne);
        GUI_Player[] guiPlayers = playerController.getPlayersGUI();
        boardController.addCars(guiPlayers);

    }
}
