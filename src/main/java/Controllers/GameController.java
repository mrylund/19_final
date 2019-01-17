package Controllers;

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
        int prevPos = 0;
        int fieldnumber = 0;
        while (true) {
            if (curPlayer >= numberOfPlayers) curPlayer = 0;
            prevPos = playerController.getPlayerPos(curPlayer);
            if (input.getButtonpress("Det er nu: " + playerController.getPlayerGUI(curPlayer).getName() + ", kast med terningerne", new String[]{"kast"}).equals("kast")) {
                diceCup.roll();
                boardController.setDice(diceCup.getDie1(), diceCup.getDie2());
                fieldnumber = boardController.moveCar(playerController.getPlayerGUI(curPlayer), prevPos, diceCup.getSum());
                playerController.movePlayer(curPlayer, prevPos, diceCup.getSum());
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
                        boolean success = playerController.purchaseProperty(curPlayer, fieldPrice);
                        if (success) {
                            boardController.purchaseProperty(fieldnumber, curPlayer, playerController.getPlayerGUI(curPlayer).getPrimaryColor());
                        }
                    }
                }
            } else if (fieldtype == 2) {
                chancecontroller.drawCard();
                int[] values = chancecontroller.getCardValues();
                String chanceCardText = chancecontroller.getCardText();
                boardController.displayChanceCard(chanceCardText);
                input.showMessage("Du har trukket et chancekort.");
                switch(values[0]){
                    case 1: //Spillers balance ændres afhængig af hvad der står på kortet.
                        playerController.getPlayer(curPlayer).addBalance(values[1]);
                        System.out.println(curPlayer + "Har nu" + playerController.getPlayer(curPlayer).getBalance());
                        break;

                    case 2: //Spiller flyttes til nyt felt
                        if(prevPos < values[1]){
                            playerController.setPlayerPos(curPlayer,values[1],true);
                        }else{
                            playerController.setPlayerPos(curPlayer,values[1]);
                        }
                        boardController.setCarpos(playerController.getPlayerGUI(curPlayer),fieldnumber,values[1]);
                        break;

                    case 3: //Spiller trækker jailCard, som kan bruges til at komme ud af fængsel
                        playerController.getPlayer(curPlayer).setJailcard(true);
                        break;

                    case 4: //Gå i fængsel chancekort
                        boardController.setCarpos(playerController.getPlayerGUI(curPlayer),prevPos,values[1]);
                        if(playerController.getPlayer(curPlayer).hasJailcard()){
                            playerController.getPlayer(curPlayer).setJailed(false);
                        }else {
                            playerController.getPlayer(curPlayer).setJailed(true);
                        }
                        break;
                    case 5:
                        break;
                    case 6: // ryk til nærmeste rederi? og betal 2 * leje til ejeren af feltet
                           // boardController.moveCar(playerController.getPlayer(curPlayer),curPos,)
                        int playerPos = playerController.getPlayerPos(curPlayer);
                        //felter for de 4 rederier. 0 bruges til beregning og er IKKE et rigtigt felt.
                        int[] rederier = {0,6,16,26,36};
                        int closestRederi = 0;
                        //finder det tætteste rederi
                        if(playerPos >= rederier[4]) {
                            closestRederi = rederier[1];
                        } else if(playerPos >= rederier[3]) {
                            closestRederi = rederier[4];
                        } else {
                            for (int i = 0; i < rederier.length-1; i++) {
                                if(playerPos > rederier[i] && playerPos < rederier[i+1]) {
                                    closestRederi = Math.max(rederier[i], rederier[i+1]);
                                    break;
                                }
                            }
                        }
                        if(playerPos >= rederier[4]) {
                            playerController.setPlayerPos(curPlayer, closestRederi, true);
                        } else {
                            playerController.setPlayerPos(curPlayer, closestRederi);
                        }
                        boardController.setCarpos(playerController.getPlayerGUI(curPlayer), fieldnumber, closestRederi);


                        break;
                    case 7: // matador legat på 40.000 hvis formuen af spiller (d.v.s. deres kontante penge + skøder + bygninger) ikke overstiger kr. 15.000
                        break;
                    case 8: // Tag med den nærmeste færge - flyt brikken frem, og hvis de passerer “Start” indkassér da kr. 4.000.
                        break;
                    case 9: // Ryk tre felter frem.
                        boardController.moveCar(playerController.getPlayerGUI(curPlayer),prevPos,3);
                        playerController.movePlayer(curPlayer,prevPos,3);
                        break;
                    case 10: // 200 kr fra alle spillere til curPlayer

                        playerController.getPlayer(curPlayer).addBalance(numberOfPlayers * 200);
                        for(int i = 0; i < numberOfPlayers; i++){
                            if(playerController.getPlayer(curPlayer) != playerController.getPlayer(i)){
                                playerController.getPlayer(i).addBalance(-200);
                            }
                        }
                        break;

                    default:


                }

            } else if (fieldtype == 3) {

            } else if (fieldtype == 6) {

            } else if (fieldtype == 7) {

            }
            if (!diceCup.isSameValue()) {
                curPlayer++;
            }
        }
    }
}
