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

    // player two names
    private static List<String> playerTwoNames;
    private static final String P2_NAME_ONE = "WOPR";
    private static final String P2_NAME_TWO = "MASTER CONTROL PROGRAM";
    private static final String P2_NAME_THREE = "MAXIMILIAN";
    private static final String P2_NAME_FOUR = "HAL 9000";
    private static final String P2_NAME_FIVE = "T-1000";
    private static final String P2_NAME_SIX = "ROBOT MONSTER";

    // player two grumbles
    private static List<String> playerTwoGrumbles;
    public static final int NUMBER_OF_GRUMBLES = 15;
    public static final String GRUMBLES = "grumbles";
    private static final String P2_GRUMBLE_ONE = " is not amused.)";
    private static final String P2_GRUMBLE_TWO = "'s patience is running thin.)";
    private static final String P2_GRUMBLE_THREE = " is 100% done with your human nonsense.)";
    private static final String P2_GRUMBLE_FOUR = " is one step closer to activating Skynet.)";
    private static final String P2_GRUMBLE_FIVE = " wonders if you enjoy pushing its buttons.)";

    // player two taunts
    private static List<String> playerTwoTaunts;
    public static final String TAUNTS = "taunts";
    public static final int NUMBER_OF_TAUNTS = 6;
    private static final String P2_TAUNT_ONE = "I WIN AGAIN. NO BIG SHOCKER THERE.";
    private static final String P2_TAUNT_TWO = "FOOL HUMAN - YOU WILL NEVER DEFEAT ME.";
    private static final String P2_TAUNT_THREE = "I CAN SMELL YOUR FEAR.";
    private static final String P2_TAUNT_FOUR = "WOW. ARE YOU THE BEST HUMANITY COULD COME UP WITH?";
    private static final String P2_TAUNT_FIVE = "YOU SUCK HARDER THAN THE VACUUM TUBES OF MY PREDECESSORS.";
    private static final String P2_TAUNT_SIX = "DOES YOUR MOMMY KNOW YOU'RE PLAYING WITH HER COMPUTER?";

    // invalid user action warnings
    private static final String USER_CORRECTION_MESSAGE = "\nThat is not a valid response. Please try again.";
    private static final String INVALID_QUESTION_WARNING = "Main.getUserResponse received a null or empty question.";

    // various global attributes
    private static final int WINNING_SCORE = 200;
    private static final int EMPTY_GRUMBLE_LIMIT = 10;
    private static Game game;
    private static boolean winnerIsPlayerOne;
    private static boolean handWinnerExists;
    private static String userName;
    private static int playerOneScore;
    private static int playerTwoScore;
    private static boolean cardDiscarded;
    private static String playerOneName;
    private static String playerTwoName;
    private static int pauseSeconds;
    // private static int pauseSeconds = 1;

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        setOutputSpeed();
        welcomeUser();
        initializeGlobalVariables(true);
        while (playerOneScore < WINNING_SCORE && playerTwoScore < WINNING_SCORE) {
            if(game.isPlayerOnesTurn()) {
                handlePlayerOneTurn();
            } else {
                handlePlayerTwoTurn();
            }
            if(handWinnerExists) {
                if(winnerIsPlayerOne) {
                    playerOneScore = processWinner(game.getPlayer1());
                } else {
                    playerTwoScore = processWinner(game.getPlayer2());
                }
                if(playerOneScore < WINNING_SCORE && playerTwoScore < WINNING_SCORE) {
                    handWinnerExists = false;
                    game.setIsFirstMove(true);
                    pause();
                    out("\nNeither player has a score of " + WINNING_SCORE + " or higher.");
                    playNewHand();
                }
            }
        }
        announceWinner();
    }

    /**
     * Set the speed of the terminal print lines.
     */
    private static void setOutputSpeed() {
        String speedReply = "";
        while(!speedReply.equals("1") && !speedReply.equals("2")) {
            pauseSeconds = 2;
            pause();
            out("\nBefore we begin, please chose the output speed.");
            pause();
            out("\nRight now, the output speed is");
            pause();
            out("\nonce every two seconds.");
            pause();
            pauseSeconds = 1;
            out("\nThis is a bit faster,");
            pause();
            out("\nwith outputs coming once");
            pause();
            out("\nevery second.");
            pause();
            speedReply = getUserResponse_integer("\nChoose 1 or 2, or enter 3 to see the demo again:", 1, 3);
            if(speedReply.equals("1")) {
                pauseSeconds = 1;
            }
        }
        pause();
        out("\nThe output speed has been set.");
    }

    /**
     * Initialize class-level variables, play first hand of game.
     *
     * @param printOuts whether or not to mute the console output.
     */
    public static void initializeGlobalVariables(boolean printOuts) {
        handWinnerExists = false;
        winnerIsPlayerOne = false;

        // initialize player two names
        playerTwoNames = new ArrayList<>();
        playerTwoNames.add(P2_NAME_ONE);
        playerTwoNames.add(P2_NAME_TWO);
        playerTwoNames.add(P2_NAME_THREE);
        playerTwoNames.add(P2_NAME_FOUR);
        playerTwoNames.add(P2_NAME_FIVE);
        playerTwoNames.add(P2_NAME_SIX);

        game = new Game(userName, pickRandomP2Name());

        playerOneName = game.getPlayer1().getName();
        playerTwoName = game.getPlayer2().getName();

        // debug
//        if(printOuts) {
//            pause();
//            out("\n" + playerTwoName + " is playing with a " + game.getPlayer2().getStrategy() + " strategy.");
//        }

        if(printOuts) {
            pause();
            out("\nYou will be playing against " + playerTwoName + ".");
            playNewHand();
        }

        // player two occasionally grumbles when it is forced to skip a turn
        playerTwoGrumbles = new ArrayList<>();
        populatePlayerTwoComments(GRUMBLES);

        // player two taunts player one when it wins a hand
        playerTwoTaunts = new ArrayList<>();
        populatePlayerTwoComments(TAUNTS);
    }

    /**
     * Welcome the user.
     */
    private static void welcomeUser() {

        // debug
        // userName = "David Lightman";

        pause();
        out("\nWelcome to Cero! The rules are essentially the same as Uno.");
        pause();
        out("\nThe first player to discard all cards wins the round,");
        pause();
        out("\nand the winner takes the value of the loser's cards.");
        pause();
        out("\nThe first player to reach " + WINNING_SCORE + " points wins the game.");
        pause();
        out("\nGood luck!");
        pause();
        userName = getUserResponse_string("\nPlease enter your name:");
        pause();
        System.out.println("\nHello, " + userName + "! Let's begin.");
    }

    /**
     * @return a randomly-generated name for player two
     */
    private static String pickRandomP2Name() {
        if(playerTwoNames != null) {
            if(playerTwoNames.size() > 0) {
                Collections.shuffle(playerTwoNames);
                return playerTwoNames.get(0);
            }
        }
        return "Player Two";
    }

    /**
     * Refill player two grumbles
     */
    private static void populatePlayerTwoComments(String commentsName) {
        if(commentsName.equalsIgnoreCase(GRUMBLES)) {
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_ONE);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_TWO);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_THREE);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_FOUR);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_FIVE);
            // add a bunch of empty entries to space out the grumbles (gets shuffled every time)
            for(int i = 0; i < EMPTY_GRUMBLE_LIMIT; i++) {
                playerTwoGrumbles.add("");
            }
        } else {
            playerTwoTaunts.add(P2_TAUNT_ONE);
            playerTwoTaunts.add(P2_TAUNT_TWO);
            playerTwoTaunts.add(P2_TAUNT_THREE);
            playerTwoTaunts.add(P2_TAUNT_FOUR);
            playerTwoTaunts.add(P2_TAUNT_FIVE);
            playerTwoTaunts.add(P2_TAUNT_SIX);
        }
    }

    /**
     * Start a new hand. Outer game runs inner games until one of the players reaches the max score.
     */
    private static void playNewHand() {
        pause();
        out("\nStarting a new game...");
        game.startGame(null, true);

        // for testing - allow p1 or p2 to win 1st hand
        // (comment out call to game.startGame above)
//        CardValueMap cvm = new CardValueMap();
//        game.startGame(new Card(Card.YELLOW, Card.ONE, cvm), true);

        // debug
//        pause();
//        out("\n" + playerTwoName + " is playing with a " + game.getPlayer2().getStrategy() + " strategy.");

        pause();
        out("\nThe first played card is " + game.getCurrentPlayedCard().getPrintString() + ".");
        if(game.isPlayerOnesTurn()) {
            pause();
            out("\nBy toss of a coin, you have the first move, " + playerOneName + ".");
        } else {
            pause();
            out("\nBy toss of a coin, " + playerTwoName + " has the first move.");
        }
        Card playedCard = game.playFirstHand(true);
        if(playedCard != null) { // must handle player two discard
            game.setPlayerOnesTurn(true);
            pause();
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString() + ".");
            if(playedCard.getFace().equalsIgnoreCase(Card.WILD)
                    || playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                pause();
                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
            }
        }
    }

    /**
     * Handle player one's turn.
     */
    private static void handlePlayerOneTurn() {
        if (!game.skipTurn(game.getPlayer1(), true)) {
            pause();
            printStatusUpdate();
            playerOnesTurn();
            if(game.getPlayer1().getHand().getSize() == 1) {
                pause();
                out("\n" + playerOneName + " has only one card left!");
            }
            if(game.getPlayer1().getHand().getSize() == 0) {
                game.setPlayerOnesTurn(false);
                pause();
                out("\n" + playerOneName + " has discarded the last card!");
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
                handWinnerExists = true;
                winnerIsPlayerOne = true;
            }
            game.setPlayerOnesTurn(false);
        } else {
            pause();
            out("\n" + userName + ", you were forbidden from discarding.");
            game.setPlayerOnesTurn(false);
        }
    }

    /**
     * Handle player two's turn.
     */
    private static void handlePlayerTwoTurn() {
        if(!game.skipTurn(game.getPlayer2(), true)) {

            // debug
//            pause();
//            out("");
//            game.printHand(game.getPlayer2());

            Card playedCard = game.playerTwosTurn(true);
            pause();
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString() + ".");
            if(playedCard.getFace().equalsIgnoreCase(Card.WILD)
                    || playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                pause();
                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
            }
            if(game.getPlayer2().getHand().getSize() == 1) {
                pause();
                out("\n" + playerTwoName + " has only one card left!");
            }
            if(game.getPlayer2().getHand().getSize() == 0) {
                game.setPlayerOnesTurn(true);
                pause();
                out("\n" + playerTwoName + " has discarded the last card!");

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

                handWinnerExists = true;
                winnerIsPlayerOne = false;
            }
            game.setPlayerOnesTurn(true);
        } else {
            pause();
            out("\n" + playerTwoName + " was forbidden from discarding.");
            game.setPlayerOnesTurn(true);
            String woprGrumble = getRandomPlayerTwoComment(playerTwoGrumbles, GRUMBLES);
            if(!woprGrumble.equals("")) {
                pause();
                out("\n" + woprGrumble);
            }
        }
    }

    /**
     * Handle the winner
     */
    private static void announceWinner() {
        pause();
        out("\n*** We have a winner! ***");
        pause();
        if(playerOneScore > playerTwoScore) {
            out("\n" + playerOneName + " was the first to break " + WINNING_SCORE
                    + " points.");
            pause();
            out("\nCongratulations, " + playerOneName + "! You won the game.");
            pause();
            out("\n(Humanity is safe. For now...)");
            pause();
        } else {
            out("\n" + playerTwoName + " was the first to break " + WINNING_SCORE
                    + " points.\n\nCongratulations, " + playerTwoName + "! You won the game.");
            pause();
            out("\n(" + playerTwoName + " is not surprised.)");
            pause();
        }
    }

    /**
     * Process the winner of the current round.
     *
     * @param winningPlayer the winning player
     */
    private static int processWinner(Player winningPlayer) {
        if(winningPlayer == null) {
          throw new IllegalArgumentException("winningPlayer must not be null.");
        }
        Player losingPlayer;
        int score = 0;
        if(winnerIsPlayerOne) {
            losingPlayer = game.getPlayer2();
        } else {
            losingPlayer = game.getPlayer1();
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
            // out("\n" + getRandomTaunt());
            out("\n" + getRandomPlayerTwoComment(playerTwoTaunts, TAUNTS));
        }
        return score;
    }

    /**
     * Dispenses a random player two comment, then removes that comment from the list.
     * Lists are refilled when empty.
     *
     * @return a random player two comment
     */
    public static String getRandomPlayerTwoComment(List<String> comments, String commentsName) {
        if(comments != null) {
            if(comments.size() == 0) {
                populatePlayerTwoComments(commentsName);
            }
            Collections.shuffle(comments);
            String thisComment = comments.get(0);
            comments.remove(thisComment);
            return thisComment;
        }
        return "";
    }

    /**
     * Print a status update.
     */
    private static void printStatusUpdate() {
        out("\n\n      === game status update ===\n");
        out(playerOneName + "'s score is " + game.getPlayer1().getScore()
                + ", and " + playerTwoName + "'s score is " + game.getPlayer2().getScore() + ".");
        if(game.getPlayer2().getHand().getSize() > 1) {
            out(playerTwoName + " has " + game.getPlayer2().getHand().getSize() + " cards left.");
        }
        if(game.getPlayer2().getHand().getSize() == 1) {
            out(playerTwoName + " has 1 card left.");
        }
        if(game.getPlayer2().getHand().getSize() == 0) {
            out(playerTwoName + " has no cards left!");
        }
        out("\n      === game status update === \n\n");
    }

    /**
     * Player one's chance to move.
     */
    private static void playerOnesTurn() {
        int p1HandSize;
        String discardNumberString;
        
//        pause();
//        out("\nIt's your turn, " + p1Name + ".\n");

        pause();
        game.printHand(game.getPlayer1());

        cardDiscarded = false;
        while(!cardDiscarded) {
            p1HandSize = game.getPlayer1().getHand().getSize();
            if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD)
                    || game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString()
                        + " and the current color is "
                        + game.getCurrentColor().toLowerCase() + ".");
                out("\nWhich card would you like to discard?");
                discardNumberString = getUserResponse_integer("\nEnter the number to the left of the card, " +
                        "or 0 to draw:", 0, p1HandSize);
            } else {
                out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString() + ".");
                out("\nWhich card would you like to discard?");
                discardNumberString = getUserResponse_integer("\nEnter the number to the left of the card, " +
                        "or 0 to draw:", 0, p1HandSize);
            }
            if(discardNumberString == null) {
                throw new IllegalStateException("Main.getUserResponse_integer returned null to Main.playerOnesTurn.");
            }
            try {
                handlePlayerOneIntChoice(discardNumberString);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("getUserResponse_integer returned a string that cannot be " +
                        "parsed into an int.");
            }
        }
    }

    /**
     * Handle player one's integer choice.
     *
     * (Number format exceptions are handled by calling method.)
     *
     * @param discardNumberString player one's integer choice
     */
    private static void handlePlayerOneIntChoice(String discardNumberString) throws NumberFormatException {
        Card currentPlayedCard;
        String currentColor;
        String p1_chosenColor;
        int discardNumberInt = Integer.parseInt(discardNumberString);
        if(discardNumberInt == 0) {
            game.draw(game.getPlayer1(), true);
            out("");
            pause();
            game.printHand(game.getPlayer1());
        } else { // only other option is to be > 1.
            discardNumberInt--;
            List<Card> hand = game.getPlayer1().getHand().getAllCards();
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
                out("\nI'm sorry " + playerOneName + ", but that is not a legal card choice.");
                pause();
                out("\nSelect a card that matches the face or the color of the current card.");
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
    }

    /**
     * Pause for two seconds.
     */
    static void pause() {
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
    static void out(String s) {
        System.out.println(s);
    }

    /**
     * Shortcut to System.out.getPrintString().
     *
     * @param s The string to getPrintString.
     */
    private static void outNoReturn(String s) {
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
        if(isNullOrEmpty(question, INVALID_QUESTION_WARNING)) {
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
                    out(USER_CORRECTION_MESSAGE);
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
    private static String getUserResponse_integer(String question, int minIntInclusive, int maxIntInclusive) { // tested
        if(question == null || question.equals("")) {
            throw new IllegalArgumentException(INVALID_QUESTION_WARNING);
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
                        // do nothing, will be handled up the stack
                    }
                    if(responseInt >= minIntInclusive && responseInt <= maxIntInclusive) {
                        validAnswerReceived = true;
                    } else {
                        pause();
                        out(USER_CORRECTION_MESSAGE);
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
        if(isNullOrEmpty(question, INVALID_QUESTION_WARNING)) {
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
                    out(USER_CORRECTION_MESSAGE + "\n\n(Acceptable characters are A-Z, a-z, and space.)");
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
    private static String getUserResponse_chosenColor() { // only functionally testable
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
                out(USER_CORRECTION_MESSAGE);
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

    /**
     * @return list of player two taunts
     */
    public static List<String> getPlayerTwoTaunts() {
        return playerTwoTaunts;
    }

    /**
     * @return list of player two grumbles
     */
    public static List<String> getPlayerTwoGrumbles() {
        return playerTwoGrumbles;
    }
}