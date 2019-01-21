package Controllers;

import Gameboard.Gameboard;

public class InputController {
    Gameboard board;

    /**
     * This is the InputController class' constructor, which takes the Gameboard as a parameter.
     * @param board is the parameter of the type Gameboard.
     */
    public InputController(Gameboard board) {
        this.board = board;
    }

    /**
     * The method "getInt(String message, int min, int max)" is a method used to get a user's input in the GUI.
     * @param message is the message displayed to the user through the GUI.
     * @param min is the minimum user Integer input allowed.
     * @param max is the maximum user Integer input allowed.
     * @return the value returned is of type Integer, and is the userinput.
     */
    public int getInt(String message, int min, int max) {
        return board.getBoard().getUserInteger(message, min, max);
    }

    /**
     * This method is used by the GameController in the "InitializeGame() method".
     * This method takes a String[] input, and an amount.
     * @param messages is the String[] input
     * @param amount is used when creating the String[] output.
     * @return output is the String[] output consisting of user's string inputs.
     */
    public String[] getStringArray(String[] messages, int amount) {
        String[] output = new String[amount];

        for (int i = 0; i < amount; i++) {
            output[i] = board.getBoard().getUserString(messages[i]);
        }

        return output;
    }

    /**
     * This method displays a message in the GUI, and creates and buttons which the user can press.
     * @param question is the question displayed in the GUI
     * @param buttons is the buttons created in the GUI, which the user can press.
     * @return output is a String, which can then be used with a str.equals() method to check what the user has chosen.
     */
    public String getButtonpress(String question, String[] buttons) {
        return board.getBoard().getUserButtonPressed(question, buttons);
    }

    /**
     * This void is used to show a message to the user through the GUI.
     * @param text is the text message shown to the user.
     */
    public void showMessage(String text) {
        board.getBoard().showMessage(text);
    }
}
