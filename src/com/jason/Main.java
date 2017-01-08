package com.jason;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Main class. Create a new game and run it.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Main {

    /*
        set PATH=[path\to\jdk\bin];%PATH%, e.g.
        set PATH=C:\Program Files\Java\jdk1.8.0_111\bin;%PATH%
        (must be >= java 1.8)

        javac src/com/jason/*.java
        java -cp ./src com.jason.Main
     */

    private static final String userCorrectionMessage = "\nThat is not a valid response. Please try again.";
    private static final String invalidQuestionWarning =
            "WARN: Main.getUserResponse received a null or empty question. No action taken, returned null.";

    public static void main(String[] args) {

//        outNoReturn("Please enter your name (no special characters): ");
//        String name = System.console().readLine();
//        while(!isValid(name)) {
//            outNoReturn("Please try again. ");
//            out("Allowed characters are a-z, A-Z, and space: ");
//            name = System.console().readLine();
//        }
//        System.out.println("Welcome, " + name + "! Let's begin.");



        // test Q&A:
//        String answer = getUserResponse_yesNo("Do you believe in love?");
//        out("Your answer: " + answer);
//
//        answer = getUserResponse_integer("Please enter an integer between 0 and 5: ", 0, 5);
//        out("Your answer: " + answer);
//
//        answer = getUserResponse_string("Enter any string: ");
//        out("Your answer: " + answer);
//
//        answer = getUserResponse_chosenColor();
//        out("Your answer: " + answer);

        String testName = "David Lightman";
        boolean innerWinnerExists = false;
        int playerOneScore = 0;
        int playerTwoScore = 0;
        int maxTestTurns = 10;
        int winningScore = 500;

        out("\nStarting a new game...");
        Game game = new Game(testName, true);
        game.startGame(null, true);
        String playerTwoName = game.getPlayer2().getName();
        out("\nPlayer Two's name is " + playerTwoName + ".");
        out("\n" + playerTwoName + " is playing with a " + game.getPlayer2().getStrategy() + " strategy.");
        out("\nThe first played card is " + game.getCurrentPlayedCard().getPrintString());
        if(game.isPlayerOnesTurn()) {
            out("\nYou have the first move, " + testName + ".\n");
        } else {
            out("\n" + playerTwoName + " has the first move.\n");
        }
        game.printHand(game.getPlayer2());
        Card playedCard;
        playedCard = game.playFirstHand();
        if(playedCard != null) {
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString());
        }
        // while (playerOneScore < winningScore && playerTwoScore < winningScore) {
        for(int i = 0; i < 5; i++) {
            if (game.getDeck().getSize() < 2) {
                out("\nThere is 1 card left in the deck.");
            } else {
                outNoReturn("\nThere are " + game.getDeck().getSize() + " cards left in the deck");
            }
            if (game.getDiscardPile().size() < 2) {
                out(" and 1 card in the discard pile.");
            } else {
                out(" and " + game.getDiscardPile().size() + " cards in the discard pile.");
            }

            out("\nThe current color is " + game.getCurrentColor() + ".");
            if(game.isPlayerOnesTurn()) {
                if (!game.skipTurn(game.getPlayer1())) {
                    playerOnesTurn(game, testName);
                }
            } else {
                out("\nIt is " + game.getPlayer2().getName() + "'s turn.");
                game.setPlayerOnesTurn(false);
                playedCard = game.playerTwosTurn();
                out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString());
            }
        }
    }

    private static void playerOnesTurn(Game game, String p1Name) {
        int p1HandSize;
        String drawAnswer;
        String discardAnswer;
        String discardNumber;
        List<Card> hand;
        Card currentPlayedCard;
        String currentColor;
        String p1_chosenColor;

        out("\nIt's your turn, " + p1Name + "\n");
        game.printHand(game.getPlayer1());
        out("\n" + game.getPlayer2().getName() + " has " + game.getPlayer2().getHand().getSize() + " cards left.");

        boolean cardDiscarded = false;
        while(!cardDiscarded) {
            drawAnswer = getUserResponse_yesNo("\nWould you like to draw a card?");
            if(drawAnswer == null) {
                throw new IllegalStateException("getUserResponse_yesNo returned a null answer to main.");
            } else {
                if(drawAnswer.equalsIgnoreCase("yes") || drawAnswer.equalsIgnoreCase("y")) {
                    out("\nOK, drawing a card from the deck...");
                    game.draw(game.getPlayer1());
                    out("");
                    game.printHand(game.getPlayer1());
                }
            }
            discardAnswer = getUserResponse_yesNo("\nAre you ready to discard?");
            if(discardAnswer == null) {
                throw new IllegalStateException("getUserResponse_yesNo returned a null answer to main.");
            } else {
                if(discardAnswer.equalsIgnoreCase("yes") || discardAnswer.equalsIgnoreCase("y")) {
                    p1HandSize = game.getPlayer1().getHand().getSize();
                    discardNumber = getUserResponse_integer("\nWhich card would you like to discard?", 0, p1HandSize -1 );
                    if(discardNumber == null) {
                        throw new IllegalStateException("getUserResponse_integer returned null.");
                    }
                    try {
                        int discardNumberInt = Integer.parseInt(discardNumber);
                        hand = game.getPlayer1().getHand().getAllCards();
                        Card cardToDiscard = hand.get(discardNumberInt);
                        out("\nYou have elected to discard the card " + cardToDiscard.getPrintString());
                        currentPlayedCard = game.getCurrentPlayedCard();
                        currentColor = game.getCurrentColor();
                        if(game.getPlayer1().isLegalDiscard(cardToDiscard, currentPlayedCard, currentColor)) {
                            game.getPlayer1().getHand().discard(cardToDiscard);
                            p1_chosenColor = "";
                            if(cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                                p1_chosenColor = getUserResponse_chosenColor();
                            }
                            if(p1_chosenColor.equals("")) {
                                game.setCurrentColor(cardToDiscard.getColor());
                            } else {
                                game.setCurrentColor(p1_chosenColor);
                            }
                            out("\nYou have successfully discarded the card " + cardToDiscard.getPrintString() + ".\n");
                            game.printHand(game.getPlayer1());
                            game.setCurrentPlayedCard(cardToDiscard);
                            game.getDiscardPile().add(cardToDiscard);
                            cardDiscarded = true;
                            game.setPlayerOnesTurn(false);
                        } else {
                            out("\nI'm sorry " + p1Name + ", but that is not a valid card choice. Please try again.");
                            out("\nThe current played card is " + currentPlayedCard.getPrintString()
                                    + ", and the current color is " + currentColor.toLowerCase()
                                    + ". Here is your hand:\n");
                            game.printHand(game.getPlayer1());
                        }

                    } catch (NumberFormatException e) {
                        throw new IllegalStateException("getUserResponse_integer returned a string that cannot be " +
                                "parsed into an int.");
                    }
                }
            }
        }

    }

    /**
     * Checks for invalid characters in submitted name.
     *
     * @param str  The user's name.
     * @return      True if name contains only alphanumeric characters, false otherwise.
     */
    private static boolean isValid(String str) {
        Pattern p = Pattern.compile("[^A-Za-z\\s]");
        return !p.matcher(str).find();
    }

    /**
     * Shortcut to System.out.println().
     *
     * @param s The string to getPrintString.
     */
    public static void out(String s) {
        System.out.println(s);
    }

    /**
     * Shortcut to System.out.getPrintString().
     *
     * @param s The string to getPrintString.
     */
    static void outNoReturn(String s) {
        System.out.print(s);
    }


    /**
     * @return random true or false
     */
    static boolean getRandomBoolean() { // no test needed
        return Math.random() < 0.5;
    }

    /**
     * Ask the user a yes or no question.
     *
     * @param question the question to ask
     * @return         the user's answer
     */
    public static String getUserResponse_yesNo(String question) { // tested
        if(isNullOrEmpty(question, invalidQuestionWarning)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            while(!validAnswerReceived) {
                outNoReturn(question + " ");
                response = System.console().readLine().trim();
                if(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n") ||
                        response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                    validAnswerReceived = true;
                } else {
                    out(userCorrectionMessage);
                }
            }
            return response;
        }
    }

    /**
     * Ask the user to provide an integer within a specified range.
     *
     * @param question        the question to ask
     * @param minIntInclusive the minimum inclusive integer the user must input
     * @param maxIntInclusive the maximum inclusive integer the user must input
     * @return                the user's answer - guaranteed to be integer parsable
     */
    public static String getUserResponse_integer(String question, int minIntInclusive, int maxIntInclusive) { // tested
        if(isNullOrEmpty(question, invalidQuestionWarning)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            if(minIntInclusive < 0 || maxIntInclusive < 0) {
                out("Main.getUserResponse received invalid minInt and maxInt values: minInt = " + minIntInclusive
                        + ", maxInt = " + maxIntInclusive + ". Both must be >= 0. No action taken, returned null.");
                return null;
            }
            if(minIntInclusive >= maxIntInclusive - 1) { // minimum range of two ints to pick from
                out("Main.getUserResponse received invalid minInt and maxInt values: minInt = " + minIntInclusive
                        + ", maxInt = " + maxIntInclusive + ". minInt must be < maxInt. " +
                        "No action taken, returned null.");
                return null;
            } else {
                while(!validAnswerReceived) {
                    outNoReturn(question + " ");
                    response = System.console().readLine();
                    int responseInt = -1;
                    try {
                        responseInt = Integer.parseInt(response);
                    } catch (NumberFormatException e) {
                        out("Your response " + response + " could not be converted to an integer.");
                    }
                    if(responseInt >= minIntInclusive && responseInt <= maxIntInclusive) {
                        validAnswerReceived = true;
                    } else {
                        out(userCorrectionMessage);
                    }
                }
            }
            return response;
        }
    }

    /**
     * Ask the user to provide the answer to a general question.
     *
     * @param question the question to ask
     * @return         the user's response
     */
    public static String getUserResponse_string(String question) { // tested
        if(isNullOrEmpty(question, invalidQuestionWarning)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            while(!validAnswerReceived) {
                outNoReturn(question);
                response = System.console().readLine();
                if(isValid(response)) {
                    validAnswerReceived = true;
                } else {
                    out(userCorrectionMessage + " (Acceptable characters are A-Z, a-z, and space.)");
                }
            }
            return response;
        }
    }


    /**
     * Ask the user to pick a color for discarded wild cards.
     * Force user to pick one of four pre-set colors.
     *
     * @return the chosen color.
     */
    public static String getUserResponse_chosenColor() { // only functionally testable
        boolean validAnswerReceived = false;
        String response = "";

        List<String> validColorChoices = new ArrayList<>();
        validColorChoices.add(Card.BLUE.toLowerCase());
        validColorChoices.add(Card.GREEN.toLowerCase());
        validColorChoices.add(Card.RED.toLowerCase());
        validColorChoices.add(Card.YELLOW.toLowerCase());

        while(!validAnswerReceived) {
            outNoReturn("\nWhat is your chosen color for the next move? ");
            response = System.console().readLine();
            if(validColorChoices.contains(response.toLowerCase().trim())) {
                validAnswerReceived = true;
            } else {
                out(userCorrectionMessage + " Acceptable answers are red, blue, yellow, and green (case insensitive).");
            }
        }
        return response;
    }

    /**
     * Validates a string is not null and not empty. Prints specified error message if string is null.
     *
     * @param  str the string to validate
     * @return true if the string is not null, false otherwise.
     */
    public static boolean isNullOrEmpty(String str, String message) { // tested
        if (str == null || str.equals("")) {
            out(message);
            return true;
        }
        return false;
    }
}