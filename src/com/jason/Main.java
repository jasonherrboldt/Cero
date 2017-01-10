package com.jason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

/**
 * Main class. Create a new game and run it. See README for compile / run instructions.
 *
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */
public class Main {

    private static final String userCorrectionMessage = "\nThat is not a valid response. Please try again.";
    private static final String invalidQuestionWarning =
            "WARN: Main.getUserResponse received a null or empty question. No action taken, returned null.";

    private static final int pauseSeconds = 1;

    // player two taunts
    private static List<String> taunts;
    private static final String TAUNT_ONE = "I WIN AGAIN. NO BIG SHOCKER THERE.";
    private static final String TAUNT_TWO = "FOOL HUMAN - YOU WILL NEVER DEFEAT ME.";
    private static final String TAUNT_THREE = "I CAN SMELL YOUR FEAR.";
    private static final String TAUNT_FOUR = "WOW. ARE YOU THE BEST HUMANITY COULD COME UP WITH?";
    private static final String TAUNT_FIVE = "YOU SUCK HARDER THAN THE VACUUM TUBES OF MY PREDECESSORS.";
    private static final String TAUNT_SIX = "DOES YOUR MOMMY KNOW YOU'RE PLAYING WITH HER COMPUTER?";

    private static Game game;

    private static List<String> grumbles;

    private static final int winningScore = 200;

    /**
     * Public constructor.
     *
     * @param args arguments
     */
    public static void main(String[] args) {

        // hardcoding the user name for debug.
        String userName = "David Lightman"; // debug

        // un-comment out this block to get the user's name:
//        pause();
//        out("\nWelcome to Cero! The rules are essentially the same as Uno.");
//        pause();
//        out("\nThe first player to discard all cards wins the round,");
//        pause();
//        out("\nand the winner takes the value of the loser's cards.");
//        pause();
//        out("\nThe first player to reach " + winningScore + " points wins the game.");
//        pause();
//        out("\nTo interact with the game, simply follow the prompts");
//        pause();
//        out("\nand hit enter to submit.");
//        pause();
//        out("\nGood luck!");
//        pause();
//        String userName = getUserResponse_string("\nPlease enter your name:");
//        pause();
//        System.out.println("\nHello, " + userName + "! Let's begin.");

        boolean innerWinnerExists = false;
        boolean winnerIsPlayerOne = false;
        int playerOneScore = 0;
        int playerTwoScore = 0;
        game = new Game(userName);
        Card playedCard;
        game = startGame();

        taunts = new ArrayList<>();
        taunts.add(TAUNT_ONE);
        taunts.add(TAUNT_TWO);
        taunts.add(TAUNT_THREE);
        taunts.add(TAUNT_FOUR);
        taunts.add(TAUNT_FIVE);
        taunts.add(TAUNT_SIX);

        grumbles = new ArrayList<>();
        grumbles.add("(" + game.getPlayer2().getName() + " is not amused.)");
        grumbles.add("(" + game.getPlayer2().getName() + "'s patience is running thin.)");
        grumbles.add("");
        grumbles.add("");
        grumbles.add("");
        grumbles.add("");
        grumbles.add("");
        grumbles.add("");

        while (playerOneScore < winningScore && playerTwoScore < winningScore) {
            if(game.isPlayerOnesTurn()) {
                if (!game.skipTurn(game.getPlayer1(), true)) {
                    pause();
                    printStatusUpdate();
                    playerOnesTurn();
                    if(game.getPlayer1().getHand().getSize() == 1) {
                        pause();
                        out("\n" + game.getPlayer1().getName() + " has only one card left!");
                    }
                    if(game.getPlayer1().getHand().getSize() == 0) {
                        pause();
                        out("\n" + game.getPlayer1().getName() + " has discarded the last card!");

                        // gotta add extra cards here
                        if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                            game.draw(game.getPlayer2(), true);
                            game.draw(game.getPlayer2(), true);
                        }
                        if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                            game.draw(game.getPlayer2(), true);
                            game.draw(game.getPlayer2(), true);
                            game.draw(game.getPlayer2(), true);
                            game.draw(game.getPlayer2(), true);
                        }
                        innerWinnerExists = true;
                        winnerIsPlayerOne = true;
                    }
                    game.setPlayerOnesTurn(false);
                } else {
                    pause();
                    out("\n" + userName + ", you were forbidden from discarding.");
                    game.setPlayerOnesTurn(false);
                }
            } else {
                if(!game.skipTurn(game.getPlayer2(), true)) {

                    // debug
                    pause();
                    out("");
                    game.printHand(game.getPlayer2());

                    playedCard = game.playerTwosTurn(true);
                    pause();
                    out("\n" + game.getPlayer2().getName() + " discarded the card " + playedCard.getPrintString() + ".");
                    if(playedCard.getFace().equalsIgnoreCase(Card.WILD)
                            || playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                        pause();
                        out("\n" + game.getPlayer2().getName() + " set the current color to " + game.getCurrentColor() + ".");
                    }
                    if(game.getPlayer2().getHand().getSize() == 1) {
                        pause();
                        out("\n" + game.getPlayer2().getName() + " has only one card left!");
                    }
                    if(game.getPlayer2().getHand().getSize() == 0) {
                        pause();
                        out("\n" + game.getPlayer2().getName() + " has discarded the last card!");

                        // gotta add extra cards here
                        if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                            game.draw(game.getPlayer1(), true);
                            game.draw(game.getPlayer1(), true);
                        }
                        if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                            game.draw(game.getPlayer1(), true);
                            game.draw(game.getPlayer1(), true);
                            game.draw(game.getPlayer1(), true);
                            game.draw(game.getPlayer1(), true);
                        }

                        innerWinnerExists = true;
                        winnerIsPlayerOne = false;
                    }
                    game.setPlayerOnesTurn(true);
                } else {
                    pause();
                    out("\n" + game.getPlayer2().getName() + " was forbidden from discarding.");
                    game.setPlayerOnesTurn(true);
                    String woprGrumble = getWoprGrumble();
                    if(!woprGrumble.equals("")) {
                        pause();
                        out("\n" + woprGrumble);
                    }
                }
            }
            if(innerWinnerExists) {
                if(winnerIsPlayerOne) {
                    playerOneScore = processWinner(game.getPlayer1());
                } else {
                    playerTwoScore = processWinner(game.getPlayer2());
                }
                if(playerOneScore < winningScore && playerTwoScore < winningScore) {
                    innerWinnerExists = false;
                    game.setIsFirstMove(true);
                    pause();
                    out("\nNeither player has a score of " + winningScore + " or higher.");
                    startGame();
                }
            }
        }
        pause();
        out("\n*** We have a winner! ***");
        pause();
        if(playerOneScore > playerTwoScore) {
            out("\n" + game.getPlayer1().getName() + " was the first to break " + winningScore + " points.\n\nCongratulations, " + game.getPlayer1().getName() + "!");
            pause();
            out("\n(Humanity is safe. For now...)\n");
        } else {
            out("\n" + game.getPlayer2().getName() + " was the first to break " + winningScore + " points.\n\nCongratulations, " + game.getPlayer2().getName() + "!");
            pause();
            out("\n(" + game.getPlayer2().getName() + " is not surprised.)");
        }
    }

    /**
     * Start a new inner game. Outer game runs inner games until one of the players reaches the max score.
     */
    public static Game startGame() {
        pause();
        out("\nStarting a new game...");
        game.startGame(null, true);

        // in case I need to inject the 1st played card:
//        CardValueMap cvm = new CardValueMap(); // debug
//        game.startGame(new Card(Card.YELLOW, Card.ONE, cvm), true); // debug

        // debug
        pause();
        out("\n" + game.getPlayer2().getName() + " is playing with a " + game.getPlayer2().getStrategy() + " strategy.");

        String playerTwoName = game.getPlayer2().getName();
        pause();
        out("\nThe first played card is " + game.getCurrentPlayedCard().getPrintString());
        if(game.isPlayerOnesTurn()) {
            pause();
            out("\nBy toss of a coin, you have the first move, " + game.getPlayer1().getName() + ".");
        } else {
            pause();
            out("\nBy toss of a coin, " + playerTwoName + " has the first move.");
        }
        Card playedCard;
        playedCard = game.playFirstHand(true);
        if(playedCard != null) { // handle card player two just discarded
            game.setPlayerOnesTurn(true);
            pause();
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString() + ".");
            if(playedCard.getFace().equalsIgnoreCase(Card.WILD)
                    || playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                pause();
                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
            }
        }
        return game;
    }

    /**
     * Process the winner of the current round.
     *
     * @param winningPlayer the winning player
     */
    public static int processWinner(Player winningPlayer) {
        boolean winnerIsPlayerOne;
        Player losingPlayer;
        int score = 0;
        if(winningPlayer.getName().equalsIgnoreCase(game.getPlayer1().getName())) {
            winnerIsPlayerOne = true;
            losingPlayer = game.getPlayer2();
        } else {
            losingPlayer = game.getPlayer1();
            winnerIsPlayerOne = false;
        }
        score = winningPlayer.getScore();
        String winnerName = "";
        winnerName = winningPlayer.getName();
        pause();
        out("\nCongratulations, " + winnerName + "! You won this round.");
        pause();
        out("\nTallying " + winnerName + "'s score...");
        if(game.getOtherPlayer(winningPlayer).getHand().getSize() < 1) {
            pause();
            out("WARN: Can't tally score; other player has no cards!");
        }
        for(Card c : losingPlayer.getHand().getAllCards()) {
            score += c.getValue();
        }
        pause();
        out("\n" + winnerName + "'s score is now " + score + ".");
        winningPlayer.setScore(score);
        if(!winnerIsPlayerOne) {
            pause();
            out("\nHey, " + losingPlayer.getName() + " -- " + winningPlayer.getName() + " has a message for you...");
            pause();
            out("\n" + getRandomTaunt());
        }
        return score;
    }

    /**
     * Print a status update.
     */
    public static void printStatusUpdate() {
        out("\n\n      === game status update ===\n");
//        if (game.getDeck().getSize() < 2) {
//            out("There is 1 card left in the deck.");
//        } else {
//            outNoReturn("There are " + game.getDeck().getSize() + " cards left in the deck");
//        }
//        if (game.getDiscardPile().size() < 2) {
//            out(" and 1 card in the discard pile.");
//        } else {
//            out(" and " + game.getDiscardPile().size() + " cards in the discard pile.");
//        }
        out(game.getPlayer1().getName() + "'s score is " + game.getPlayer1().getScore()
                + ", and " + game.getPlayer2().getName() + "'s score is " + game.getPlayer2().getScore() + ".");
        if(game.getPlayer2().getHand().getSize() > 1) {
            out(game.getPlayer2().getName() + " has " + game.getPlayer2().getHand().getSize() + " cards left.");
        }
        if(game.getPlayer2().getHand().getSize() == 1) {
            out(game.getPlayer2().getName() + " has 1 card left.");
        }
        if(game.getPlayer2().getHand().getSize() == 0) {
            out(game.getPlayer2().getName() + " has no cards left!");
        }
        out("\n      === game status update === \n\n");
    }

    /**
     * @return a taunt to display when the computer wins.
     */
    public static String getRandomTaunt() {
        Collections.shuffle(taunts);
        if(taunts.size() > 0) {
            return taunts.get(0);
        }
        return "";
    }


    /**
     * @return a grumble from player two (only returns non-empty string 25% of the time)
     */
    public static String getWoprGrumble() {
        Collections.shuffle(grumbles);
        if(grumbles.size() > 0) {
            return grumbles.get(0);
        }
        return "";
    }

    /**
     * Player one's chance to move.
     */
    private static void playerOnesTurn() {
        int p1HandSize;
        String discardNumber;
        List<Card> hand;
        Card currentPlayedCard;
        String currentColor;
        String p1_chosenColor;
        String p1Name = game.getPlayer1().getName();
        
//        pause();
//        out("\nIt's your turn, " + p1Name + ".\n");

        pause();
        game.printHand(game.getPlayer1());

        boolean cardDiscarded = false;
        while(!cardDiscarded) {
            p1HandSize = game.getPlayer1().getHand().getSize();
            if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD)
                    || game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString()
                        + " and the current color is "
                        + game.getCurrentColor().toLowerCase() + ".");
                out("\nWhich card would you like to discard?");
                discardNumber = getUserResponse_integer("\nEnter the number to the left of the card, " +
                        "or 0 to draw:", 0, p1HandSize);
            } else {
                out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString() + ".");
                out("\nWhich card would you like to discard?");
                discardNumber = getUserResponse_integer("\nEnter the number to the left of the card, " +
                        "or 0 to draw:", 0, p1HandSize);
            }
            if(discardNumber == null) {
                throw new IllegalStateException("Main.getUserResponse_integer returned null to Main.playerOnesTurn.");
            }
            try {
                int discardNumberInt = Integer.parseInt(discardNumber);
                if(discardNumberInt == 0) {
                    game.draw(game.getPlayer1(), true);
                    out("");
                    pause();
                    game.printHand(game.getPlayer1());
                } else { // only other option is to be > 1.
                    discardNumberInt--;
                    hand = game.getPlayer1().getHand().getAllCards();
                    Card cardToDiscard = hand.get(discardNumberInt);
                    currentPlayedCard = game.getCurrentPlayedCard();
                    currentColor = game.getCurrentColor();
                    if(game.getPlayer1().isLegalDiscard(cardToDiscard, currentPlayedCard, currentColor)) {
                        if(cardToDiscard.getFace().equalsIgnoreCase(Card.WILD)
                                || cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                            p1_chosenColor = getUserResponse_chosenColor();
                            game.setCurrentColor(p1_chosenColor);
                        } else {
                            game.setCurrentColor(cardToDiscard.getColor());
                        }
                        game.getPlayer1().getHand().discard(cardToDiscard);
                        pause();
                        out("\nYou have successfully discarded the card " + cardToDiscard.getPrintString() + ".");
                        game.setCurrentPlayedCard(cardToDiscard);
                        game.getPlayer1().setLastPlayedCard(cardToDiscard);
                        game.getDiscardPile().add(cardToDiscard);
                        cardDiscarded = true;
                    } else {
                        pause();
                        out("\nI'm sorry " + p1Name + ", but that is not a legal card choice.");
                        pause();
                        out("\nPlease try again.");
                        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)
                                || currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                            pause();
                            out("\nThe current played card is " + currentPlayedCard.getPrintString()
                                    + ", and the current color is " + currentColor.toLowerCase() + ".");
                        }
                        pause();
                        out("");
                        game.printHand(game.getPlayer1());
                    }
                }
            } catch (NumberFormatException e) {
                throw new IllegalStateException("getUserResponse_integer returned a string that cannot be " +
                        "parsed into an int.");
            }
        }
    }

    /**
     * Pause for two seconds.
     */
    public static void pause() {
        try {
            TimeUnit.SECONDS.sleep(pauseSeconds);
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
                    pause();
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
            if(minIntInclusive > maxIntInclusive) { // minimum range of two ints to pick from
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
                        pause();
                        out("\nYour response " + response + " could not be converted to a number.");
                    }
                    if(responseInt >= minIntInclusive && responseInt <= maxIntInclusive) {
                        validAnswerReceived = true;
                    } else {
                        pause();
                        out(userCorrectionMessage);
                        pause();
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
                outNoReturn(question + " ");
                response = System.console().readLine();
                if(isValid(response)) {
                    validAnswerReceived = true;
                } else {
                    pause();
                    out(userCorrectionMessage + "\n\n(Acceptable characters are A-Z, a-z, and space.)");
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
        validColorChoices.add("b");
        validColorChoices.add("g");
        validColorChoices.add("r");
        validColorChoices.add("y");

        while(!validAnswerReceived) {
            pause();
            outNoReturn("\nEnter the first letter of your chosen color: ");
            response = System.console().readLine();
            if(validColorChoices.contains(response.toLowerCase().trim())) {
                validAnswerReceived = true;
            } else {
                pause();
                out(userCorrectionMessage);
                pause();
                out("\nAcceptable answers are b, g, r, and y (case insensitive).");
            }
        }
        if(response.equalsIgnoreCase("b")) {
            return "blue";
        } else if(response.equalsIgnoreCase("g")) {
            return "green";
        } else if(response.equalsIgnoreCase("r")) {
            return "red";
        } else { // only other possibility is "y"
            return "yellow";
        }
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