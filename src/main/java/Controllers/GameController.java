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
    //private DiceCupDevmode diceCup = new DiceCupDevmode();
    private DiceCup diceCup = new DiceCup();
    private InputController input;
    private ReadFile reader = new ReadFile();
    private int numberOfPlayers;

    /**
     * This constructor "GameController initializes the game through the void "InitializeGame" in devmode, if devmode is enabled (true || false).
     * @param devmode
     */
    public GameController(boolean devmode) {
        InitializeGame(devmode);
    }

    /**
     * As mentioned in the comment for the constructor of the GameController-class, this method is initialized in the constructor.
     * @param devmode
     * In this void, a String[] is initialized and created when the user has created an input numberOfPlayers.
     * The cars are then added through the boardController, and the GameLoop is initialized.
     */
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

    /**
     * This is the GameLoop.
     * The GameLoop will run while the While-loop inside is true.
     * The loop ends when the @curPlayer has a balance < 0, and the break; occurs.
     * After the "break;", a for-loop is created, whih goes through the @numberOfPlayers, and then proceeds to finds the player
     *    - with the greatest fortune ( balance, houses and properties ).
     * And at last it will display the winner and the winner's balance on the GUI.
     */
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

    /**
     * This void will execute the current Player's turn.
     * @param curPlayer is the current player's number.
     * @param prevPos is the current player's position.
     * The boardController uses the method "hasSameType" which takes the curPlayer's number and checks if the player is eligible to buy a house on the property."
     * A input from the player "Ja" || "No" is then required for the game to continue.
     * If the input is "Ja", a new integer input names @fieldAnswer is required with the fieldNumber the current player wants to purchase a house on.
     * Then the input fieldAnswer is used to check is the player actually owns the field, and if so, the price will be read by using the Reader-class taking the @fieldAnswer as a parameter.
     * The price is then used to check if the player can afford to buy the property - if yes, the "purchaseHouse(int curPlayer, fieldAnswer)" method is used through boardController
     * If the boolean is true, the balance from the current Player will be subtracted by the price of the field.
     * When or if the "purchaseHouse" method is executed, the "dceCup.roll()" method is executed - this executes the diceroll for the current player.
     * The boardController.setDice(dice1,dice2) is then called, which shows the rolled numbers on the GUI.
     * The fieldNumber variable is then calculated from the diceCup.getSum() method, and the boardController.moveCar(Player player, int prevPos, int diceSum) is then used.
     * The player is then moved by the movePlayer(curPlayer, prevPos, sum) method in the PlayerController class.
     * At last the doFieldAction(curPlayer, prevPos, fieldNumber) method is executed, because the player has been moved to a new field after the dice roll.
     */
    private void doPlayerTurn(int curPlayer, int prevPos) {
        int fieldNumber = 0;

        if (boardController.hasSameType(curPlayer)) {
            String houseAnswer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nDu har mulighed for at købe et hus, vil du det?", new String[]{"Ja", "Nej"});
            if (houseAnswer.equals("Ja")) {
                int fieldAnswer = input.getInt("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nHvilket felt vil du gerne købe et hus til?", 2, 40);
                if (boardController.hasAllFields(curPlayer, fieldAnswer) ) {
                    int price = Integer.parseInt(reader.getBuildPrice(fieldAnswer));
                    if (playerController.playerCanAfford(curPlayer, price)) {
                        boolean success = boardController.purchaseHouse(curPlayer, fieldAnswer);
                        if (success) playerController.getPlayer(curPlayer).addBalance(-price);
                    }
                }
            }
        }
        input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nKast med terningerne", new String[]{"kast"});

        diceCup.roll();
        boardController.setDice(diceCup.getDie1(), diceCup.getDie2());

        fieldNumber = boardController.moveCar(playerController.getPlayerGUI(curPlayer), prevPos, diceCup.getSum());
        playerController.movePlayer(curPlayer, prevPos, diceCup.getSum());

        doFieldAction(curPlayer, prevPos, fieldNumber);
    }

    /**
     * This method is executed in the GameLoop() if the Player is jailed.
     * @param player is the current Player's number.
     * @param prevPos is the current Player's position.
     * This method checks if the player has a Jailcard, and asks if the players wants to use it to get out of jail.
     * If the player chooses to use the Jailcard to get out of jailed, the setJailed(false) is used to get the player out of prison,
     *  - and the setJailcard(false) is used to remove the Jailcard from the player.
     * If the Player doesn't own a Jailcard, the players is given a chance to come out of jail by rolling 2 of the same dice facevalues in 3 attempts.
     * If the players rolls 2 of the same facevalues in the dice, the player will be moved to a new field with respect to the diceSum.
     * The "doFieldAction()" method is then called on the fieldNumber the player is now on.
     */

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
            input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() +
                    "\nDu er i fængsel, slå 2 ens for at komme ud.\nForsøg " + (i+1) + "/3", new String[]{"kast"});
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

    /**
     *
     * @param curPlayer
     * @param prevPos
     * @param fieldNumber
     *
     *
     */
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

    /**
     *
     * @param player
     * @param fieldNumber
     */
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
            } else if (boardController.getHouses(fieldNumber) > 0) {
                int numOfHouses = boardController.getHouses(fieldNumber);
                int price = 0;
                switch (numOfHouses) {
                    case 1: price = Integer.parseInt(reader.getFieldHouse1Price(fieldNumber)); break;
                    case 2: price = Integer.parseInt(reader.getFieldHouse2Price(fieldNumber)); break;
                    case 3: price = Integer.parseInt(reader.getFieldHouse3Price(fieldNumber)); break;
                    case 4: price = Integer.parseInt(reader.getFieldHouse4Price(fieldNumber)); break;
                }
                input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                + playerController.getPlayer(owner).getName()
                                + ", som har " + numOfHouses + " hus(e). Du skal batale vedkommende "
                                + price + "kr.",
                        new String[]{"ok"});
                playerController.payRent(player, owner, price);
            } else if (boardController.hasAllFields(owner,fieldNumber)){
                int amount = 2 * Integer.parseInt(reader.getFieldRent(fieldNumber));
                input.showMessage("Da ejeren " + playerController.getPlayer(owner).getName() + " ejer alle af denne type felter, så skal du nu betale dobbelt leje ( dvs. " + amount + " )");
                playerController.payRent(player,owner,amount);
            } else {
                int price = Integer.parseInt(reader.getFieldRent(fieldNumber));
                input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                + playerController.getPlayer(owner).getName()
                                + " du skal batale vedkommende "
                                + price + "kr.",
                        new String[]{"ok"});
                playerController.payRent(player, owner, price);
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

    /**
     *
     * @param player
     * @param prevPos
     * @param fieldNumber
     */
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
                doFieldAction(player,values[1],fieldNumber);
                break;

            case 3: //Spiller trækker jailCard, som kan bruges til at komme ud af fængsel
                playerController.getPlayer(player).setJailcard(true);
                break;

            case 4: //Gå i fængsel chancekort
                input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu bliver sendt i fængsel, da du har trukket et fængsels-chancekort!");
                boardController.setCarpos(playerController.getPlayerGUI(player),prevPos,values[1]);
                playerController.setPlayerPos(player,values[1],false);
                break;
            case 5:
                break;

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

            case 7: // matador legat på 40.000 hvis formuen af spiller (d.v.s. deres kontante penge + skøder + bygninger) ikke overstiger kr. 15.000
                int totalValue = boardController.getTotalPropertyValues(player) + playerController.getPlayer(player).getBalance();
                if(totalValue <= 15000){
                    playerController.getPlayer(player).addBalance(40000);
                }else{
                    input.showMessage("Din formue er over kr. 15.000, så derfor modtager du ikke Matador-Legatet på kr. 40.000");
                }
                break;

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

            case 9: // Ryk tre felter frem.
                boardController.moveCar(playerController.getPlayerGUI(player),fieldNumber,3);
                playerController.movePlayer(player,fieldNumber,3);
                int newPos = playerController.getPlayerPos(player);
                doPurchasableField(player,newPos);
                break;

            case 10: // 200 kr fra alle spillere til curPlayer
                int ingamePlayers = playerController.getCurrentNumOfPlayers();
                playerController.getPlayer(player).addBalance(ingamePlayers * 200);
                for(int i = 0; i < numberOfPlayers; i++){
                    if(playerController.getPlayer(player) != playerController.getPlayer(i)){
                        playerController.getPlayer(i).addBalance(-200);
                    }
                }
                break;

            default:
        }
    }

    /**
     *
     * @param player
     * @param fieldNumber
     */
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
            int taxToPay = (int)(((double)playerBalance/100)*10);
            String taxAnswer = input.getButtonpress("Betal 10% af din pengebeholdning (" + taxToPay + "kr) eller 4000kr.", new String[]{"10%", "4000kr"});
            if(taxAnswer.equals("10%")) {
                playerController.getPlayer(player).addBalance(-taxToPay);
            } else {
                if(playerController.playerCanAfford(player, 4000)) {
                    playerController.getPlayer(player).addBalance(-4000);
                } else {
                    input.showMessage("Det har du ikke råd til. Du bliver trykket 10% af din pengebeholdning. (" + taxToPay + "kr)");
                    playerController.getPlayer(player).addBalance(-taxToPay);
                }
            }
        } else if(fieldNumber == 39) {
            input.showMessage(reader.getFieldName(39));
            playerController.getPlayer(player).addBalance(-2000);
        }
    }
}
