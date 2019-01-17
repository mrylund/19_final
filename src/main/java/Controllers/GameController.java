package Controllers;

import Logic.DiceCup;
import Logic.ReadFile;
import gui_fields.GUI_Player;

import static Logic.Sleep.sleep;

public class GameController {
    private BoardController boardController = new BoardController();
    private PlayerController playerController = new PlayerController();
    private DiceCup diceCup = new DiceCup();
    private InputController input;
    private ReadFile reader = new ReadFile();
    int numberOfPlayers;


    public GameController(boolean devmode) {
        InitializeGame(devmode);
    }

    public void InitializeGame(boolean devmode) {
        String[] spillernavne;
        input = new InputController(boardController.createBoard());
        if (devmode) {
            numberOfPlayers = 5;
            spillernavne = new String[] {
                    "Martin",
                    "Patrick",
                    "Athusan",
                    "Lars",
                    "Andreas"
            };
        } else {
            numberOfPlayers = input.getInt("Hvor mange spillere?", 3, 6);
            spillernavne = input.getStringArray(new String[]{"Hvad hedder spiller 1?", "Hvad hedder spiller 2?", "Hvad hedder spiller 3?", "Hvad hedder spiller 4?", "Hvad hedder spiller 5?", "Hvad hedder spiller 6?"}, numberOfPlayers);
        }
        playerController.createPlayers(spillernavne);
        GUI_Player[] guiPlayers = playerController.getPlayersGUI();
        boardController.addCars(guiPlayers);
        GameLoop();
    }

    public void GameLoop() {
        int curSpiller = 0;
        int prevpos = 0;
        int fieldnumber = 0;
        while (true) {
            input.showMessage("Det er nu");
            if (curSpiller >= numberOfPlayers) curSpiller = 0;
            prevpos = playerController.getPlayerPos(curSpiller);
            if (input.getButtonpress("Det er nu: " + playerController.getPlayerGUI(curSpiller).getName() + ", kast med terningerne", new String[]{"kast"}).equals("kast")) {
                diceCup.roll();
                boardController.setDice(diceCup.getDie1(), diceCup.getDie2());
                fieldnumber = boardController.moveCar(playerController.getPlayerGUI(curSpiller), prevpos, diceCup.getSum());
                playerController.movePlayer(curSpiller, prevpos, diceCup.getSum());
            }


            int fieldtype = boardController.getFieldType(fieldnumber);
            if (fieldtype == 1 || fieldtype == 4 || fieldtype == 5) {
                if (boardController.fieldHasOwner(fieldnumber)) {
                    int owner = boardController.getFieldOwner(fieldnumber);
                    int price = Integer.parseInt(reader.getFieldPrice(fieldnumber));
                    input.getButtonpress("Dette felt er ejet af "
                            + playerController.getPlayer(owner).getName()
                            + " du skal batale vedkommende "
                            + price + "kr.",
                            new String[]{"ok"});

                } else {
                    String answer = input.getButtonpress("Vil du gerne købe feltet " + reader.getFieldName(fieldnumber + 1) + " for " + reader.getFieldPrice(fieldnumber + 1) + "?", new String[]{"ja", "nej"});
                    if (answer.equals("ja")) {
                        int fieldPrice = Integer.parseInt(reader.getFieldPrice(fieldnumber + 1));
                        boolean success = playerController.purchaseProperty(curSpiller, fieldPrice);
                        if (success) {
                            boardController.purchaseProperty(fieldnumber, curSpiller, playerController.getPlayerGUI(curSpiller).getPrimaryColor());
                        }
                    }
                }
            } else if (fieldtype == 2) {


            } else if (fieldtype == 3) {

            } else if (fieldtype == 6) {

            } else if (fieldtype == 7) {

            }
            curSpiller++;
        }
    }

}
