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

            if (playerController.playerLost(curPlayer)) {
                break;
            }

            if (!diceCup.isSameValue()) {
                curPlayer++;
            }
        }

        int winner = 0;
        int winnerValue = 0;

        for(int i = 0; i < numberOfPlayers; i++) {
            int temp = 0;
            temp += playerController.getPlayer(i).getBalance();
            temp += boardController.getTotalPropertyValues(i);

            if (temp > winnerValue) {
                winner = i;
                winnerValue = temp;
            }
        }

        input.showMessage(playerController.getPlayerGUI(winner).getName() + " har vundet spillet!");



    }

    private void doPlayerTurn(int player, int prevPos) {
        int fieldNumber = 0;

        if (boardController.hasSameType(player)) {
            String husanswer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu har mulighed for at købe et hus, vil du det?", new String[]{"Ja", "Nej"});
            if (husanswer.equals("Ja")) {
                int fieldanswer = input.getInt("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nHvilket felt vil du gerne købe et hus til?", 2, 40);
                if (boardController.hasAllFields(player, fieldanswer) ) {
                    int price = Integer.parseInt(reader.getBuildPrice(fieldanswer));
                    if (playerController.playerCanAfford(player, price)) {
                        boolean success = boardController.purchaseHouse(player, fieldanswer);
                        if (success) playerController.getPlayer(player).addBalance(-price);
                    }
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
// FIXME: 18-01-2019 Felttype 5 er brewery og skal laves
        if (fieldType == 1 || fieldType == 4 || fieldType == 5) {
            doPurchasableField(curPlayer, fieldNumber);

        } else if (fieldType == 2) {
            doChanceField(curPlayer, prevPos, fieldNumber);

        } else if (fieldType == 3) {
            doGoJailField(curPlayer, fieldNumber);

        } else if (fieldType == 6) {
            doTaxField(curPlayer, fieldNumber);
        }
    }

    private void doPurchasableField(int player, int fieldNumber) {
        int owner = boardController.getFieldOwner(fieldNumber);
        if (boardController.fieldHasOwner(fieldNumber)) {
            if(boardController.hasHotel(fieldNumber)) {
                int price = Integer.parseInt(reader.getFieldHotelPrice(fieldNumber));
                input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                + playerController.getPlayer(owner).getName()
                                + ", som har et hotel. Du skal batale vedkommende "
                                + price + "kr.",
                        new String[]{"ok"});
                playerController.payRent(player, owner, price);
            } else if (boardController.hasAllFields(owner,fieldNumber)){
                int amount = 2 * Integer.parseInt(reader.getFieldRent(fieldNumber));
                input.showMessage("Da ejeren " + playerController.getPlayer(owner).getName() + " ejer alle af denne type felter, så skal du nu betale dobbelt leje ( dvs. " + amount + " )");
                playerController.payRent(player,owner,amount);
            } else {
                int numOfHouses = boardController.getHouses(fieldNumber);
                int price = 0;
                switch (numOfHouses) {
                    case 0: price = Integer.parseInt(reader.getFieldRent(fieldNumber)); break;
                    case 1: price = Integer.parseInt(reader.getFieldHouse1Price(fieldNumber)); break;
                    case 2: price = Integer.parseInt(reader.getFieldHouse2Price(fieldNumber)); break;
                    case 3: price = Integer.parseInt(reader.getFieldHouse3Price(fieldNumber)); break;
                    case 4: price = Integer.parseInt(reader.getFieldHouse4Price(fieldNumber)); break;
                }
                if(numOfHouses == 0) {
                    input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                    + playerController.getPlayer(owner).getName()
                                    + " du skal batale vedkommende "
                                    + price + "kr.",
                            new String[]{"ok"});
                    playerController.payRent(player, owner, price);
                } else {
                    input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                    + playerController.getPlayer(owner).getName()
                                    + ", som har " + numOfHouses + " hus(e). Du skal batale vedkommende "
                                    + price + "kr.",
                            new String[]{"ok"});
                    playerController.payRent(player, owner, price);
                }
            }
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

    @SuppressWarnings("Duplicates")
    private void doChanceField(int player, int prevPos, int fieldNumber) {
        chancecontroller.drawCard();
        int[] values = chancecontroller.getCardValues();
        //felter for de 4 rederier. 0 bruges til beregning og er IKKE et rigtigt felt.
        int[] rederier = {0,6,16,26,36};
        int closestRederi = 0;

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
                doFieldAction(player,prevPos,fieldNumber);
                break;

            case 3: //Spiller trækker jailCard, som kan bruges til at komme ud af fængsel
                playerController.getPlayer(player).setJailcard(true);
                break;

            case 4: //Gå i fængsel chancekort
                input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu bliver sendt i fængsel, da du har trukket et fængsels-chancekort!");
                boardController.setCarpos(playerController.getPlayerGUI(player),prevPos,values[1]);
                playerController.setPlayerPos(player,prevPos,false);
                break;
            case 5:
                break;

// TODO: 18-01-2019 Denne case er lavet færdigt
            case 6: // ryk til nærmeste rederi? og betal 2 * leje til ejeren af feltet
                int playerPos = playerController.getPlayerPos(player);
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

// TODO: 18-01-2019 Case 7 mangler at laves
            case 7: // matador legat på 40.000 hvis formuen af spiller (d.v.s. deres kontante penge + skøder + bygninger) ikke overstiger kr. 15.000
                int totalValue = boardController.getTotalPropertyValues(player) + playerController.getPlayer(player).getBalance();
                if(totalValue <= 15000){
                    playerController.getPlayer(player).addBalance(40000);
                }else{
                    input.showMessage("Din formue er over kr. 15.000, så derfor modtager du ikke Matador-Legatet på kr. 40.000");
                }
                break;

// TODO: 18-01-2019 Case 8 er lavet færdigt
            case 8: // Tag med den nærmeste færge - flyt brikken frem, og hvis de passerer “Start” indkassér da kr. 4.000.
                prevPos = playerController.getPlayerPos(player);
                //finder det tætteste rederi
                if(prevPos >= rederier[4]) {
                    closestRederi = rederier[1];
                } else {
                    for (int i = 0; i < rederier.length-1; i++) {
                        if(prevPos > rederier[i] && prevPos < rederier[i+1]) {
                            closestRederi = Math.max(rederier[i], rederier[i+1]);
                            break;
                        }
                    }
                }
                if(prevPos >= rederier[4]) {
                    playerController.setPlayerPos(player, closestRederi, true);
                } else {
                    playerController.setPlayerPos(player, closestRederi);
                }
                boardController.setCarpos(playerController.getPlayerGUI(player), fieldNumber, closestRederi);
                input.showMessage("Du tager nu færgen hen til næste rederi! Hvis feltet du lander på er ejet af en anden spiller, så skal der betales leje!");

                int newPosAfterTravel = closestRederi+10;

                if(newPosAfterTravel > 40){
                    newPosAfterTravel = newPosAfterTravel % 40;
                }

                playerController.setPlayerPos(player,newPosAfterTravel,false);
                boardController.setCarpos(playerController.getPlayerGUI(player),closestRederi,newPosAfterTravel);

                if(boardController.fieldHasOwner(newPosAfterTravel)){
                    int ownedAmount = boardController.getOwnedAmountOfShippingFields(newPosAfterTravel);
                    String ownerName = playerController.getPlayerGUI(boardController.getFieldOwner(newPosAfterTravel)).getName();
                    if(ownedAmount == 1) {
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),500);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr. 500 i leje, da ejeren har 1 af denne type felter. ");
                    }else if(ownedAmount == 2){
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),1000);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr. 1.000 i leje, da ejeren har 2 af denne type felter. ");
                    }else if(ownedAmount == 3){
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),2000);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr 2.000 i leje, da ejeren har 3 af denne type felter. ");
                    }else{
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),4000);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr 4.000 i leje, da ejeren har 4 af denne type felter. ");
                    }
                }else {
                    doPurchasableField(player, newPosAfterTravel);
                }
                break;

// TODO: 18-01-2019 Ændre tekst i chancecard.txt til case 9.
            case 9: // Ryk tre felter frem.
                boardController.moveCar(playerController.getPlayerGUI(player),fieldNumber,3);
                playerController.movePlayer(player,fieldNumber,3);
                int newPos = playerController.getPlayerPos(player);
                doPurchasableField(player,newPos);
                break;

// TODO: 18-01-2019 Lav metode som finder currentNumberofPlayers til case 10
            case 10: // 200 kr fra alle spillere til curPlayer
                int ingamePlayers = playerController.getCurrentNumOfPlayers();
                playerController.getPlayer(player).addBalance(ingamePlayers * 200);
                for(int i = 0; i < numberOfPlayers; i++){
                    if(playerController.getPlayer(player) != playerController.getPlayer(i)){
                        playerController.getPlayer(i).addBalance(-200);
                    }
                }
                break;

// TODO: 18-01-2019 Lav en default
            default:

        }
    }

    private void doGoJailField(int player, int fieldNumber) {
        if (fieldNumber == 31) {
            input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu skal gå i fængsel!");
            playerController.setPlayerPos(player, 11);
            boardController.setCarpos(playerController.getPlayerGUI(player),fieldNumber, 11);
            playerController.getPlayer(player).setJailed(true);
        }
    }

    private void doTaxField(int player, int fieldNumber) {
        if(fieldNumber == 5) {
            int playerBalance = playerController.getBalance(player);
            int taxToPay = (playerBalance/100)*10;
            String taxAnswer = input.getButtonpress("Betal 10% af din pengebeholdning (" + taxToPay + "kr) eller 4000kr.", new String[]{"10%", "4000kr"});
            if(taxAnswer.equals("10%")) {
                playerController.getPlayer(player).addBalance(-taxToPay);
            } else {
                playerController.getPlayer(player).addBalance(-4000);
            }
        } else if(fieldNumber == 39) {
            input.showMessage(reader.getFieldName(39));
            playerController.getPlayer(player).addBalance(-2000);
        }
    }
}
