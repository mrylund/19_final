package Controllers;

import Entities.Player;
import Logic.DiceCup;
import Logic.DiceCupDevmode;
import Logic.ReadFile;
import gui_fields.GUI_Player;

public class GameController {
    private BoardController boardController = new BoardController();
    private PlayerController playerController = new PlayerController();
    private ChanceCardController chancecontroller = new ChanceCardController();
    private DiceCupDevmode diceCup = new DiceCupDevmode();
    //private DiceCup diceCup = new DiceCup();
    private InputController input;
    private ReadFile reader = new ReadFile();
    private int numberOfPlayers;


    public GameController(boolean devmode) {
        InitializeGame(devmode);
    }

    private void InitializeGame(boolean devmode) {
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

    private void GameLoop() {
        int curPlayer = 0;
        int prevPos;

        while (true) {
            if (curPlayer >= numberOfPlayers) curPlayer = 0;
            prevPos = playerController.getPlayerPos(curPlayer);

            if(playerController.getPlayer(curPlayer).isJailed()) {
                doJailedPlayerTurn(curPlayer, prevPos);
            } else {
                doPlayerTurn(curPlayer, prevPos);
            }

            if (!diceCup.isSameValue()) {
                curPlayer++;
            }
        }

    }

    private void doPlayerTurn(int player, int prevPos) {
        int fieldNumber = 0;


        if (boardController.hasSameType(player)) {
            String husanswer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu har mulighed for at købe et hus, vil du det?", new String[]{"Ja", "Nej"});
            if (husanswer.equals("Ja")) {
                int fieldanswer = input.getInt("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nHvilket felt vil du gerne købe et hus til?", 2, 40);
                if (boardController.hasAllFields(player, fieldanswer)) {
                    boardController.purchaseHouse(player, fieldNumber);
                }
            }
        }

        input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nKast med terningerne", new String[]{"kast"});

        diceCup.roll();
        boardController.setDice(diceCup.getDie1(), diceCup.getDie2());
        fieldNumber = boardController.moveCar(playerController.getPlayerGUI(player), prevPos, diceCup.getSum());
        playerController.movePlayer(player, prevPos, diceCup.getSum());


        doFieldAction(player, prevPos, fieldNumber);
    }

    private void doJailedPlayerTurn(int player, int prevPos) {
        int fieldNumber;

        if (playerController.getPlayer(player).hasJailcard()) {
            String answer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu er i fængsel, men du har et fripas, vil du bruge dette?", new String[]{"Ja", "Nej"});
            if (answer.equals("Ja")) {
                playerController.getPlayer(player).setJailcard(false);
                playerController.getPlayer(player).setJailed(false);
                return;
            }
        }

        for (int i = 0; i < 3; i++) {
            input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu er i fængsel, slå 2 ens for at komme ud.\nForsøg " + i + "/3", new String[]{"kast"});
            diceCup.roll();
            boardController.setDice(diceCup.getDie1(), diceCup.getDie2());

            if (diceCup.isSameValue()) {
                playerController.getPlayer(player).setJailed(false);
                fieldNumber = boardController.moveCar(playerController.getPlayerGUI(player), prevPos, diceCup.getSum());
                playerController.movePlayer(player, prevPos, diceCup.getSum());

                doFieldAction(player, prevPos, fieldNumber);
                break;
            }
        }
    }

    private void doFieldAction(int curPlayer, int prevPos, int fieldNumber) {
        int fieldType = boardController.getFieldType(fieldNumber);

        if (fieldType == 1 || fieldType == 4 || fieldType == 5) {
            doPurchasableField(curPlayer, fieldNumber);

        } else if (fieldType == 2) {
            doChanceField(curPlayer, prevPos, fieldNumber);

        } else if (fieldType == 3) {
            doGoJailField(curPlayer, fieldNumber);

        } else if (fieldType == 6) {
            doTaxField();


        } else if (fieldType == 7) {
            doParkingField();

        }
    }

    private void doPurchasableField(int player, int fieldNumber) {
        if (boardController.fieldHasOwner(fieldNumber)) {
            int owner = boardController.getFieldOwner(fieldNumber);
            int price = Integer.parseInt(reader.getFieldRent(fieldNumber));
            input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                            + playerController.getPlayer(owner).getName()
                            + " du skal batale vedkommende "
                            + price + "kr.",
                    new String[]{"ok"});
            playerController.payRent(player, owner, price);
        } else {
            String answer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nVil du gerne købe feltet " + reader.getFieldName(fieldNumber) + " for " + reader.getFieldPrice(fieldNumber) + "?", new String[]{"ja", "nej"});
            if (answer.equals("ja")) {
                int fieldPrice = Integer.parseInt(reader.getFieldPrice(fieldNumber));
                boolean success = playerController.purchaseProperty(player, fieldPrice);
                if (success) {
                    boardController.purchaseProperty(fieldNumber, player, playerController.getPlayerGUI(player).getPrimaryColor());
                }
            }
        }
    }


    private void doChanceField(int player, int prevPos, int fieldNumber) {
        chancecontroller.drawCard();
        int[] values = chancecontroller.getCardValues();
        String chanceCardText = chancecontroller.getCardText();
        boardController.displayChanceCard(chanceCardText);
        input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu har trukket et chancekort.");
        switch(values[0]){
            case 1: //Spillers balance ændres afhængig af hvad der står på kortet.
                playerController.getPlayer(player).addBalance(values[1]);
                break;

            case 2: //Spiller flyttes til nyt felt
                if(prevPos > values[1]){
                    playerController.setPlayerPos(player,values[1],true);
                }else{
                    playerController.setPlayerPos(player,values[1]);
                }
                boardController.setCarpos(playerController.getPlayerGUI(player),fieldNumber,values[1]);
                break;

            case 3: //Spiller trækker jailCard, som kan bruges til at komme ud af fængsel
                playerController.getPlayer(player).setJailcard(true);
                break;

            case 4: //Gå i fængsel chancekort
                boardController.setCarpos(playerController.getPlayerGUI(player),prevPos,values[1]);
                if(playerController.getPlayer(player).hasJailcard()){
                    playerController.getPlayer(player).setJailed(false);
                }else {
                    playerController.getPlayer(player).setJailed(true);
                }
                break;
            case 5:
                break;
            case 6: // ryk til nærmeste rederi? og betal 2 * leje til ejeren af feltet
                int playerPos = playerController.getPlayerPos(player);
                //felter for de 4 rederier. 0 bruges til beregning og er IKKE et rigtigt felt.
                int[] rederier = {0,6,16,26,36};
                int closestRederi = 0;
                //finder det tætteste rederi
                if(playerPos >= rederier[4]) {
                    closestRederi = rederier[1];
                } else {
                    for (int i = 0; i < rederier.length-1; i++) {
                        if(playerPos > rederier[i] && playerPos < rederier[i+1]) {
                            closestRederi = Math.max(rederier[i], rederier[i+1]);
                            break;
                        }
                    }
                }
                if(playerPos >= rederier[4]) {
                    playerController.setPlayerPos(player, closestRederi, true);
                } else {
                    playerController.setPlayerPos(player, closestRederi);
                }
                boardController.setCarpos(playerController.getPlayerGUI(player), fieldNumber, closestRederi);
                int owner = boardController.getFieldOwner(closestRederi);
                int price = (Integer.parseInt(reader.getFieldRent(closestRederi))) * 2;
                if(boardController.fieldHasOwner(closestRederi)) {
                    input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                    + playerController.getPlayer(owner).getName()
                                    + " du skal batale vedkommende dobbelt leje, nemlig "
                                    + price + "kr.",
                            new String[]{"ok"});
                    playerController.payRent(player, owner, price);
                } else {
                    doPurchasableField(player, playerController.getPlayerPos(player)+1);
                }
                break;
            case 7: // matador legat på 40.000 hvis formuen af spiller (d.v.s. deres kontante penge + skøder + bygninger) ikke overstiger kr. 15.000
                break;
            case 8: // Tag med den nærmeste færge - flyt brikken frem, og hvis de passerer “Start” indkassér da kr. 4.000.
                break;
            case 9: // Ryk tre felter frem.
                boardController.moveCar(playerController.getPlayerGUI(player),fieldNumber,3);
                playerController.movePlayer(player,fieldNumber,3);
                break;
            case 10: // 200 kr fra alle spillere til curPlayer

                playerController.getPlayer(player).addBalance(numberOfPlayers * 200);
                for(int i = 0; i < numberOfPlayers; i++){
                    if(playerController.getPlayer(player) != playerController.getPlayer(i)){
                        playerController.getPlayer(i).addBalance(-200);
                    }
                }
                break;

            default:


        }

    }

    private void doGoJailField(int player, int fieldNumber) {
        if (fieldNumber ==  31) {
            input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu skal gå i fængsel!");
            playerController.setPlayerPos(player, 11);
            boardController.setCarpos(playerController.getPlayerGUI(player),fieldNumber, 11);
            playerController.getPlayer(player).setJailed(true);
        }
    }

    private void doTaxField() {

    }

    private void doParkingField() {

    }
}
