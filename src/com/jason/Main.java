package com.jason;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

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

        // un-comment out the block below to get the user's name.
//        outNoReturn("Please enter your name (no special characters): ");
//        String userName = System.console().readLine();
//        while(!isValid(name)) {
//            outNoReturn("Please try again. ");
//            out("Allowed characters are a-z, A-Z, and space: ");
//            userName = System.console().readLine();
//        }
//        System.out.println("Welcome, " + name + "! Let's begin.");

        String userName = "David Lightman";
        boolean innerWinnerExists = false;
        boolean winnerIsPlayerOne = false;
        int playerOneScore = 0;
        int playerTwoScore = 0;
        int maxTestTurns = 10;
        int winningScore = 500;

        pause(2);
        out("\nStarting a new game...");
        Game game = new Game(userName, true);



        // game.startGame(null, true);
        CardValueMap cvm = new CardValueMap();
        game.startGame(new Card(Card.GREEN, Card.REVERSE, cvm), true);



        String playerTwoName = game.getPlayer2().getName();

        // un-comment out the block below to display player two's name to the user.
//        pause(2);
//        out("\nPlayer Two's name is " + playerTwoName + ".");

        pause(2);
        out("\nThe first played card is " + game.getCurrentPlayedCard().getPrintString());
        if(game.isPlayerOnesTurn()) {
            pause(2);
            out("\nBy toss of a coin, you have the first move, " + userName + ".");
        } else {
            pause(2);
            out("\nBy toss of a coin, " + playerTwoName + " has the first move.");
        }
        Card playedCard;
        pause(2);
        out("");
        game.printHand(game.getPlayer2());
        playedCard = game.playFirstHand(true);
        if(playedCard != null) { // handle card player two just discarded
            game.setPlayerOnesTurn(true);
            pause(2);
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString() + ".");
//            if(playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
//                pause(2);
//                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
//                game.draw(game.getPlayer1(), true);
//                game.draw(game.getPlayer1(), true);
//                game.draw(game.getPlayer1(), true);
//                game.draw(game.getPlayer1(), true);
//            } else if(playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
//                pause(2);
//                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
//            } else if(playedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
//                game.draw(game.getPlayer1(), true);
//                game.draw(game.getPlayer1(), true);
//            }
        }
        // while (playerOneScore < winningScore && playerTwoScore < winningScore) {
        while(!innerWinnerExists) {
            if(game.isPlayerOnesTurn()) {
                if (!game.skipTurn(game.getPlayer1(), true)) {
                    pause(2);
                    out("\n\n      *** game status update ***\n");
                    if (game.getDeck().getSize() < 2) {
                        out("There is 1 card left in the deck.");
                    } else {
                        outNoReturn("There are " + game.getDeck().getSize() + " cards left in the deck");
                    }
                    if (game.getDiscardPile().size() < 2) {
                        out(" and 1 card in the discard pile.");
                    } else {
                        out(" and " + game.getDiscardPile().size() + " cards in the discard pile.");
                    }
                    if(game.getPlayer2().getHand().getSize() > 1) {
                        out(game.getPlayer2().getName() + " has " + game.getPlayer2().getHand().getSize() + " cards left.");
                    } else {
                        out(game.getPlayer2().getName() + " has 1 card left.");
                    }
                    out("\n      *** game status update *** \n");
                    playerOnesTurn(game);
                    if(game.getPlayer1().getHand().getSize() == 0) {
                        pause(2);
                        out("\n" + game.getPlayer1().getName() + " has discarded the last card!");
                        pause(2);
                        out("\nSetting innerWinnerExists to true...");
                        innerWinnerExists = true;
                        winnerIsPlayerOne = true;
                    }
                    game.setPlayerOnesTurn(false);
                } else {
                    pause(2);
                    out("\n" + userName + ", you were forbidden from discarding.");
                    game.setPlayerOnesTurn(false);
                }
            } else {
                pause(2);
                out("\nIt is " + game.getPlayer2().getName() + "'s turn.");
                // game.printHand(game.getPlayer2());
                if(!game.skipTurn(game.getPlayer2(), true)) {
                    playedCard = game.playerTwosTurn(true);
                    pause(2);
                    out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString());
                    if(playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                        pause(2);
                        out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
                    }
                    if(game.getPlayer2().getHand().getSize() == 0) {
                        pause(2);
                        out("\n" + game.getPlayer2().getName() + " has discarded the last card!");
                        innerWinnerExists = true;
                        winnerIsPlayerOne = false;
                    }
                    game.setPlayerOnesTurn(true);
                } else {
                    pause(2);
                    out("\n" + game.getPlayer2().getName() + " was forbidden from discarding.");
                    game.setPlayerOnesTurn(true);
                }
            }
            if(innerWinnerExists) {
//                pause(2);
//                out("\nAn inner winner exists! Starting a new game...");
//                game = new Game(userName, true);
//                game.startGame(null, true);
//                innerWinnerExists = false;
                pause(2);
                out("\nWe have a winner!");
                String winnerName = "";
                if(winnerIsPlayerOne) {
                    winnerName = game.getPlayer1().getName();
                } else {
                    winnerName = game.getPlayer2().getName();
                }
                pause(2);
                out("\nCongratulations, " + winnerName + "! You've won this round. Tallying your score...");
                if(winnerIsPlayerOne) {
                    if(game.getPlayer2().getHand().getSize() < 1) {
                        pause(2);
                        out("WARN: Can't tally score; player two has no cards!");
                    }
                    for(Card c : game.getPlayer2().getHand().getAllCards()) {
                        playerOneScore += c.getValue();
                    }
                    pause(2);
                    out("\nYour score is " + playerOneScore);
                } else {
                    if(game.getPlayer1().getHand().getSize() < 1) {
                        pause(2);
                        out("WARN: Can't tally score; player one has no cards!");
                    }
                    for(Card c : game.getPlayer1().getHand().getAllCards()) {
                        playerTwoScore += c.getValue();
                    }
                    pause(2);
                    out("\nYour score is " + playerTwoScore + ".\n");
                }
            }
        }
    }

    private static void playerOnesTurn(Game game) {
        int p1HandSize;
        String drawAnswer;
        String discardAnswer = "";
        String discardNumber;
        List<Card> hand;
        Card currentPlayedCard;
        String currentColor;
        String p1_chosenColor;
        String p1Name = game.getPlayer1().getName();
        
        pause(2);
        out("\nIt's your turn, " + p1Name + "\n");
        pause(2);
        game.printHand(game.getPlayer1());

        boolean cardDiscarded = false;
        while(!cardDiscarded) {
            pause(2);
            if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD)
                    || game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                discardAnswer = getUserResponse_yesNo("\nThe current played card is "
                        + game.getCurrentPlayedCard().getPrintString() + " and the current color is "
                        + game.getCurrentColor().toLowerCase() + ".\n\nAre you ready to discard?");
            } else {
                discardAnswer = getUserResponse_yesNo("\nThe current played card is "
                        + game.getCurrentPlayedCard().getPrintString() + ". Are you ready to discard?");
            }
            if(discardAnswer == null) {
                throw new IllegalStateException("getUserResponse_yesNo returned a null answer to main.");
            } else {
                // pause(2);
                if(discardAnswer.equalsIgnoreCase("yes") || discardAnswer.equalsIgnoreCase("y")) {
                    p1HandSize = game.getPlayer1().getHand().getSize();
                    discardNumber = getUserResponse_integer("\nWhich card would you like to discard?", 1, p1HandSize);
                    if(discardNumber == null) {
                        throw new IllegalStateException("getUserResponse_integer returned null.");
                    }
                    try {
                        int discardNumberInt = Integer.parseInt(discardNumber);
                        discardNumberInt--;
                        hand = game.getPlayer1().getHand().getAllCards();
                        Card cardToDiscard = hand.get(discardNumberInt);
                        p1_chosenColor = "";
                        if(cardToDiscard.getFace().equalsIgnoreCase(Card.WILD)
                                || cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                            p1_chosenColor = getUserResponse_chosenColor();
                        }
                        if(p1_chosenColor.equals("")) {
                            game.setCurrentColor(cardToDiscard.getColor());
                        } else {
                            game.setCurrentColor(p1_chosenColor);
                        }
                        currentPlayedCard = game.getCurrentPlayedCard();
                        currentColor = game.getCurrentColor();
                        if(game.getPlayer1().isLegalDiscard(cardToDiscard, currentPlayedCard, currentColor)) {
                            game.getPlayer1().getHand().discard(cardToDiscard);
                            pause(2);
                            out("\nYou have successfully discarded the card " + cardToDiscard.getPrintString() + ".");
                            game.setCurrentPlayedCard(cardToDiscard);
                            game.getPlayer1().setLastPlayedCard(cardToDiscard);
                            game.getDiscardPile().add(cardToDiscard);
                            cardDiscarded = true;
                        } else {
                            pause(2);
                            out("\nI'm sorry " + p1Name + ", but that is not a valid card choice. Please try again.");
                            pause(2);
                            out("\nThe current played card is " + currentPlayedCard.getPrintString()
                                    + ", and the current color is " + currentColor.toLowerCase()
                                    + ". \n\nHere is your hand:\n");
                            game.printHand(game.getPlayer1());
                        }

                    } catch (NumberFormatException e) {
                        throw new IllegalStateException("getUserResponse_integer returned a string that cannot be " +
                                "parsed into an int.");
                    }
                }
            }
            if(!cardDiscarded) {
                drawAnswer = getUserResponse_yesNo("\nWould you like to draw a card?");
                if(drawAnswer == null) {
                    throw new IllegalStateException("getUserResponse_yesNo returned a null answer to main.");
                } else {
                    if(drawAnswer.equalsIgnoreCase("yes") || drawAnswer.equalsIgnoreCase("y")) {
                        pause(2);
                        out("\nOK, drawing a card from the deck...");
                        game.draw(game.getPlayer1(), true);
                        out("");
                        pause(2);
                        game.printHand(game.getPlayer1());
                    }
                }
            }
        }
    }

    public static void pause(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException("TimeUnit.SECONDS.sleep threw an interrupted exception.");
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
                    pause(2);
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
                        pause(2);
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
                    pause(2);
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
            pause(2);
            outNoReturn("\nWhat is your chosen color for the next move? ");
            response = System.console().readLine();
            if(validColorChoices.contains(response.toLowerCase().trim())) {
                validAnswerReceived = true;
            } else {
                pause(2);
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