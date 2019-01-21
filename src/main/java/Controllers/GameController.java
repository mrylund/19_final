package Controllers;

import Logic.DiceCup;
import Logic.DiceCupDevmode;
import Logic.ReadFile;
import gui_fields.GUI_Player;


@SuppressWarnings("Duplicates")
public class GameController {
    private BoardController boardController = new BoardController();
    private PlayerController playerController = new PlayerController();
    private ChanceCardController chanceController = new ChanceCardController();
    //private DiceCupDevmode diceCup = new DiceCupDevmode();
    private DiceCup diceCup = new DiceCup();
    private InputController input;
    private ReadFile reader = new ReadFile();
    private int numberOfPlayers;

    /**
     * This constructor of "GameController" initializes the game through the void "InitializeGame(devmode)".
     * @param devmode is a boolean.
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
                    "Andreas"};
            //Test that make a player own all fields and also builds x amount of houses on them
            /*for (int i = 1; i < 41; i++) {
                if(boardController.getFieldType(i) == 5) {
                    boardController.purchaseProperty(i, 0, Color.red);
                    for (int j = 0; j < 0; j++) {
                        boardController.purchaseHouse(0, i);
                    }
                }
            }*/


        } else {
            numberOfPlayers = input.getInt("Hvor mange spillere?", 3, 6);
            spillernavne = input.getStringArray(new String[]{"Hvad hedder spiller 1?", "Hvad hedder spiller 2?",
                                                             "Hvad hedder spiller 3?", "Hvad hedder spiller 4?",
                                                             "Hvad hedder spiller 5?", "Hvad hedder spiller 6?"}, numberOfPlayers);
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
        input.showMessage(playerController.getPlayerGUI(winner).getName() + " har vundet spillet, vedkommende har " + winnerValue + "kr.");
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
            String houseAnswer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() +
                    "\nDu har mulighed for at købe et hus, vil du det?", new String[]{"Ja", "Nej"});
            if (houseAnswer.equals("Ja")) {
                int fieldAnswer = input.getInt("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() +
                        "\nHvilket felt vil du gerne købe et hus på?", 2, 40);
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
     * This is the doFieldAction() method which has 3 parametres - int curPlayer, prevPos and fieldNumber. This method is used when a Player lands on a field.
     * @param curPlayer is the current Player's number.
     * @param prevPos is the Player's position.
     * @param fieldNumber is the field number.
     * Depending on the fieldType the Player lands on, this method will execute different methods.
     */
    private void doFieldAction(int curPlayer, int prevPos, int fieldNumber) {
        int fieldType = boardController.getFieldType(fieldNumber);
        if (fieldType == 1 || fieldType == 4) {
            if (boardController.getFieldOwner(fieldNumber) != curPlayer) {
                doPurchasableField(curPlayer, fieldNumber);
            }
        } else if (fieldType == 2) {
            doChanceField(curPlayer, prevPos, fieldNumber);

        } else if (fieldType == 3) {
            doGoJailField(curPlayer, fieldNumber);

        }else if(fieldType == 5){
            doBreweryField(curPlayer, fieldNumber);

        } else if (fieldType == 6) {
            doTaxField(curPlayer, fieldNumber);
        }
    }

    /**
     * This method is executed in "doFieldAction(curPlayer, prevPos, fieldNumber)" if the fieldType the Player lands on is equal to "1".
     * @param curPlayer the current Player's number
     * @param fieldNumber the field number the players has landed on.
     * First the fieldNumber is checked to see if it has an owner.
     * If it doesn't have an owner, the "else statement" on the bottom of the method will be executed.
     * Assuming that there's an owner, the next step is to get the owner through the "getFieldOwner(fieldNumber)" method from the BoardController class.
     * Then a check is made to see if the owner has a hotels on the field through the "hasHotel(fieldNumber)" method from the BoardController class.
     * If there's a hotels on the field, the "payRent(curPlayer, owner, price)" method will be executed through the playerController.
     * If there's no hotels, the fieldNumber will be checked for houses through the getHouses(fieldNumber) method in BoardController.
     *      - If there's a house on the fieldNumber, a rent price will be calculated through a switch on int numberOfHouses and a method in the FileReader.
     *      - The FileReader class has a method called getFieldHouseXPrice(fieldNumber) is called, which return the rent in a String format, which is then parsed to an integer.
     * The rent is then payed from the current Player to the owner through the "payRent(curPlayer,owner,price)" method.
     * If there's no houses on the field, the fieldNumber is checked to see if the owner has all the other fields of same type.
     *      - If the owner has all the other field of same type, the rent will be double, and is then payed through the same "payRent()" method as mentioned above.
     * If there's no owner at all, the player is asked if he wants to purchase the property himself.
     *      - If player wants to purchase the property, the price is retrieved through the "getFieldPrice(fieldNumber)" method.
     *      - Then the "purchaseProperty(fieldNumber, curPlayer, Color )" method is called in BoardController.
     */
    private void doPurchasableField(int curPlayer, int fieldNumber) {
        if (boardController.fieldHasOwner(fieldNumber) && boardController.getFieldOwner(fieldNumber) != curPlayer) {
            int owner = boardController.getFieldOwner(fieldNumber);
            if (boardController.hasHotel(fieldNumber)) {
                int price = Integer.parseInt(reader.getFieldHotelPrice(fieldNumber));
                input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nDette felt er ejet af "
                                + playerController.getPlayer(owner).getName()
                                + ", som har et hotel. Du skal betale vedkommende "
                                + price + "kr.",
                        new String[]{"ok"});
                playerController.payRent(curPlayer, owner, price);

            } else if (boardController.getHouses(fieldNumber) > 0) {
                int numOfHouses = boardController.getHouses(fieldNumber);
                int price = 0;
                switch (numOfHouses) {
                    case 1:
                        price = Integer.parseInt(reader.getFieldHouse1Rent(fieldNumber));
                        break;
                    case 2:
                        price = Integer.parseInt(reader.getFieldHouse2Rent(fieldNumber));
                        break;
                    case 3:
                        price = Integer.parseInt(reader.getFieldHouse3Rent(fieldNumber));
                        break;
                    case 4:
                        price = Integer.parseInt(reader.getFieldHouse4Rent(fieldNumber));
                        break;
                }
                input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() +
                                "\nDette felt er ejet af " + playerController.getPlayer(owner).getName()
                                + ", som har " + numOfHouses + " hus(e). Du skal betale vedkommende "
                                + price + "kr.",
                        new String[]{"ok"});
                playerController.payRent(curPlayer, owner, price);

            } else if (boardController.hasAllFields(owner, fieldNumber)) {
                int price = 2 * Integer.parseInt(reader.getFieldRent(fieldNumber));
                input.showMessage("Da ejeren " + playerController.getPlayer(owner).getName() +
                        " ejer alle af denne type felter, så skal du nu betale dobbelt leje ( dvs. " + price + " )");
                playerController.payRent(curPlayer, owner, price);

            } else if (boardController.getFieldType(fieldNumber) == 4) {
                int ownedAmount = boardController.getOwnedAmountOfShippingFields(fieldNumber);
                String ownerName = playerController.getPlayerGUI(boardController.getFieldOwner(fieldNumber)).getName();
                if (ownedAmount == 1) {
                    playerController.payRent(curPlayer,boardController.getFieldOwner(fieldNumber),500);
                    input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr. 500 i leje, da ejeren har 1 af denne type felter. ");
                } else if(ownedAmount == 2) {
                    playerController.payRent(curPlayer,boardController.getFieldOwner(fieldNumber),1000);
                    input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr. 1.000 i leje, da ejeren har 2 af denne type felter. ");
                } else if(ownedAmount == 3) {
                    playerController.payRent(curPlayer,boardController.getFieldOwner(fieldNumber),2000);
                    input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr 2.000 i leje, da ejeren har 3 af denne type felter. ");
                } else {
                    playerController.payRent(curPlayer,boardController.getFieldOwner(fieldNumber),4000);
                    input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr 4.000 i leje, da ejeren har 4 af denne type felter. ");
                }

           } else{
                int price = Integer.parseInt(reader.getFieldRent(fieldNumber));
                input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nDette felt er ejet af "
                                + playerController.getPlayer(owner).getName()
                                + " du skal betale vedkommende "
                                + price + "kr.",
                        new String[]{"ok"});
                playerController.payRent(curPlayer, owner, price);
            }
        } else {
            String answer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() +
                    "\nVil du gerne købe feltet " + reader.getFieldName(fieldNumber) +
                    " for " + reader.getFieldPrice(fieldNumber) + "?", new String[]{"ja", "nej"});
            if (answer.equals("ja")) {
                int fieldPrice = Integer.parseInt(reader.getFieldPrice(fieldNumber));
                boolean success = playerController.purchaseProperty(curPlayer, fieldPrice);
                if (success) {
                    boardController.purchaseProperty(fieldNumber, curPlayer, playerController.getPlayerGUI(curPlayer).getPrimaryColor());
                }
            }
        }
    }

    /**
     * This is the doChanceField(player, prevPos, fieldNumber) method. It will be executed every time a Player lands on a Chance-card field.
     * @param player
     * @param prevPos
     * @param fieldNumber
     * At first, a chancecard is drawn with the "drawCard()" method through chanceController.
     * Then the values are pulled through the "getCardValues()" method in chanceController which return an int Array.
     * The chancecard is displayed in the GUI, and a message is shown.
     * A switch is made on values[0], which then executes methods depending on the case.
     */
    private void doChanceField(int player, int prevPos, int fieldNumber) {
        chanceController.drawCard();
        int[] values = chanceController.getCardValues();
        //felter for de 4 rederier. 0 bruges til beregning og er IKKE et rigtigt felt.
        int[] rederier = {0,6,16,26,36};
        int closestRederi = 0;
        String chanceCardText = chanceController.getCardText();
        boardController.displayChanceCard(chanceCardText);
        input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu har trukket et chancekort.");

        switch(values[0]){
            case 1: //Spillers balance ændres afhængig af hvad der står på kortet.
                playerController.getPlayer(player).addBalance(values[1]);
                break;

            case 2: //Spiller flyttes til nyt felt
                if (prevPos > values[1]) {
                    playerController.setPlayerPos(player,values[1],true);
                } else {
                    playerController.setPlayerPos(player,values[1]);
                }
                boardController.setCarpos(playerController.getPlayerGUI(player),fieldNumber,values[1]);
                doFieldAction(player,fieldNumber,values[1]);
                break;

            case 3: //Spiller trækker jailCard, som kan bruges til at komme ud af fængsel
                playerController.getPlayer(player).setJailcard(true);
                break;

            case 4: //Gå i fængsel chancekort
                input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDu bliver sendt i fængsel, da du har trukket et fængsels-chancekort!");
                boardController.setCarpos(playerController.getPlayerGUI(player),fieldNumber,values[1]);
                playerController.setPlayerPos(player,values[1],false);
                playerController.getPlayer(player).setJailed(true);
                break;

            case 5:
                input.showMessage("Spiller: " + playerController.getPlayerGUI(player).getName());
                int housePrice = Integer.parseInt(reader.getLine(fieldNumber).split("; ")[3]);
                int hotelPrice = housePrice * 3;
                int total = 0;

                total += boardController.getPlayerHouseCount(player) * housePrice;
                total += boardController.getPlayerHotelCount(player) * hotelPrice;

                playerController.getPlayer(player).addBalance(-total);

                break;

            case 6: // ryk til nærmeste rederi? og betal 2 * leje til ejeren af feltet
                int playerPos = playerController.getPlayerPos(player);
                //finder det tætteste rederi
                if (playerPos >= rederier[4]) {
                    closestRederi = rederier[1];
                } else {
                    for (int i = 0; i < rederier.length-1; i++) {
                        if (playerPos > rederier[i] && playerPos < rederier[i+1]) {
                            closestRederi = Math.max(rederier[i], rederier[i+1]);
                            break;
                        }
                    }
                }
                if (playerPos >= rederier[4]) {
                    playerController.setPlayerPos(player, closestRederi, true);
                } else {
                    playerController.setPlayerPos(player, closestRederi);
                }
                boardController.setCarpos(playerController.getPlayerGUI(player), fieldNumber, closestRederi);
                int owner = boardController.getFieldOwner(closestRederi);
                int price = (Integer.parseInt(reader.getFieldRent(closestRederi))) * 2;
                if (boardController.fieldHasOwner(closestRederi)) {
                    input.getButtonpress("Spiller: " + playerController.getPlayerGUI(player).getName() + "\nDette felt er ejet af "
                                    + playerController.getPlayer(owner).getName()
                                    + " du skal betale vedkommende dobbelt leje, nemlig "
                                    + price + "kr.",
                            new String[]{"ok"});
                    playerController.payRent(player, owner, price);
                } else {
                    doPurchasableField(player, playerController.getPlayerPos(player)+1);
                }
                break;

            case 7: // matador legat på 40.000 hvis formuen af spiller (d.v.s. deres kontante penge + skøder + bygninger) ikke overstiger kr. 15.000
                int totalValue = boardController.getTotalPropertyValues(player) + playerController.getPlayer(player).getBalance();
                if (totalValue <= 15000) {
                    playerController.getPlayer(player).addBalance(40000);
                } else {
                    input.showMessage("Din formue er over kr. 15.000, så derfor modtager du ikke Matador-Legatet på kr. 40.000");
                }
                break;

            case 8: // Tag med den nærmeste færge - flyt brikken frem til nærmeste færge, og tag med færgen hen til næste færge, og hvis de passerer “Start” indkassér da kr. 4.000.
                prevPos = playerController.getPlayerPos(player);
                //finder det tætteste rederi
                if (prevPos >= rederier[4]) {
                    closestRederi = rederier[1];
                } else {
                    for (int i = 0; i < rederier.length-1; i++) {
                        if (prevPos > rederier[i] && prevPos < rederier[i+1]) {
                            closestRederi = Math.max(rederier[i], rederier[i+1]);
                            break;
                        }
                    }
                }
                if (prevPos >= rederier[4]) {
                    playerController.setPlayerPos(player, closestRederi, true);
                } else {
                    playerController.setPlayerPos(player, closestRederi);
                }
                boardController.setCarpos(playerController.getPlayerGUI(player), fieldNumber, closestRederi);
                input.showMessage("Du tager nu færgen hen til næste rederi! Hvis feltet du lander på er ejet af en anden spiller, så skal der betales leje!");

                int newPosAfterTravel = closestRederi+10;

                if (newPosAfterTravel > 40) {
                    newPosAfterTravel = newPosAfterTravel % 40;
                }

                playerController.setPlayerPos(player,newPosAfterTravel,false);
                boardController.setCarpos(playerController.getPlayerGUI(player),closestRederi,newPosAfterTravel);

                if (boardController.fieldHasOwner(newPosAfterTravel)) {
                    int ownedAmount = boardController.getOwnedAmountOfShippingFields(newPosAfterTravel);
                    String ownerName = playerController.getPlayerGUI(boardController.getFieldOwner(newPosAfterTravel)).getName();
                    if (ownedAmount == 1) {
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),500);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr. 500 i leje, da ejeren har 1 af denne type felter. ");
                    } else if(ownedAmount == 2) {
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),1000);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr. 1.000 i leje, da ejeren har 2 af denne type felter. ");
                    } else if(ownedAmount == 3) {
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),2000);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr 2.000 i leje, da ejeren har 3 af denne type felter. ");
                    } else {
                        playerController.payRent(player,boardController.getFieldOwner(newPosAfterTravel),4000);
                        input.showMessage("Du landede på et felt ejet af: " + ownerName + " og har betalt: kr 4.000 i leje, da ejeren har 4 af denne type felter. ");
                    }
                } else {
                    doPurchasableField(player, newPosAfterTravel);
                }
                break;

            case 9: // Ryk tre felter frem.
                boardController.moveCar(playerController.getPlayerGUI(player),fieldNumber-1,3);
                playerController.movePlayer(player,fieldNumber-1,3);
                int newPos = playerController.getPlayerPos(player);
                doFieldAction(player, prevPos, newPos + 1);
                break;

            case 10: // 200 kr fra alle spillere til curPlayer
                int ingamePlayers = playerController.getCurrentNumOfPlayers();
                playerController.getPlayer(player).addBalance(ingamePlayers * 200);
                for (int i = 0; i < numberOfPlayers; i++) {
                    if (playerController.getPlayer(player) != playerController.getPlayer(i)) {
                        playerController.getPlayer(i).addBalance(-200);
                    }
                }
                break;

            default:
        }
    }

    /**
     * This method sends a Player to Jail, and is used in "doFieldAction(curPlayer, prevPos, fieldNumber)", if the fieldType is equal to 3.
     * @param curPlayer is the current Player's number.
     * @param fieldNumber is the current Players position.
     * The "setCarpos()" method is used to move a car to a specific position on the board.
     */
    private void doGoJailField(int curPlayer, int fieldNumber) {
        if (fieldNumber == 31) {
            input.showMessage("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nDu skal gå i fængsel!");
            playerController.setPlayerPos(curPlayer, 11);
            boardController.setCarpos(playerController.getPlayerGUI(curPlayer),fieldNumber, 11);
            playerController.getPlayer(curPlayer).setJailed(true);
        }
    }

    /**
     * This void is used in the "doFieldAction(curPlayer, prevPos, fieldNumber)" method, if the fieldType is equal to 6.
     * @param curPlayer is the current Player's number.
     * @param fieldNumber is the current fieldNumber the Player is on.
     * There are two different TaxFields, therefor also an if-else statement in this method.
     */
    private void doTaxField(int curPlayer, int fieldNumber) {
        if (fieldNumber == 5) {
            int playerBalance = playerController.getBalance(curPlayer);
            int taxToPay = (int)(((double)playerBalance/100)*10);
            String taxAnswer = input.getButtonpress("Betal 10% af din pengebeholdning (" + taxToPay + "kr) eller 4000kr.", new String[]{"10%", "4000kr"});
            if (taxAnswer.equals("10%")) {
                playerController.getPlayer(curPlayer).addBalance(-taxToPay);
            } else {
                if (playerController.playerCanAfford(curPlayer, 4000)) {
                    playerController.getPlayer(curPlayer).addBalance(-4000);
                } else {
                    input.showMessage("Det har du ikke råd til. Du bliver trykket 10% af din pengebeholdning. (" + taxToPay + "kr)");
                    playerController.getPlayer(curPlayer).addBalance(-taxToPay);
                }
            }
        } else if(fieldNumber == 39) {
            input.showMessage(reader.getFieldName(39));
            playerController.getPlayer(curPlayer).addBalance(-2000);
        }
    }


    private void doBreweryField(int curPlayer, int fieldNumber) {
        if (boardController.fieldHasOwner(fieldNumber)) {
            if (boardController.getFieldOwner(fieldNumber) != curPlayer) {
                int eyes = diceCup.getSum();
                int owner = boardController.getFieldOwner(fieldNumber);
                int toPay = 100 * eyes;

                if (boardController.getFieldOwner(13) == owner && boardController.getFieldOwner(29) == owner) {
                    toPay = toPay * 2;
                }

                input.showMessage("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() + "\nDu skal betale " + playerController.getPlayerGUI(owner).getName() + " " + toPay + "kr. for leje!");
                playerController.getPlayer(curPlayer).addBalance(-toPay);
                playerController.getPlayer(owner).addBalance(toPay);
            }
        } else {
            String answer = input.getButtonpress("Spiller: " + playerController.getPlayerGUI(curPlayer).getName() +
                    "\nVil du gerne købe feltet " + reader.getFieldName(fieldNumber) +
                    " for " + reader.getFieldPrice(fieldNumber) + "?", new String[]{"ja", "nej"});
            if (answer.equals("ja")) {
                int fieldPrice = Integer.parseInt(reader.getFieldPrice(fieldNumber));
                boolean success = playerController.purchaseProperty(curPlayer, fieldPrice);
                if (success) {
                    boardController.purchaseProperty(fieldNumber, curPlayer, playerController.getPlayerGUI(curPlayer).getPrimaryColor());
                }
            }
        }
    }
}
