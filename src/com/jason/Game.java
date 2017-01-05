package com.jason;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Game class. Create players, alternate turns, keep track of the score,
 * and declare a winner. Allows user to remote-control Player 1.
 * (Player 2 is computer.)
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Game {

    /*
     * TODO:
     *
     *    MUST-HAVES:
     *
     *    Need to redesign testPlayer_otherPlayerForgotToCallCero_player2. See comment.
     *
     *    Change user main out warnings to illegal state exceptions -- let showstoppers stop the show.
     *
     *    Go through every method and make sure the javadoc comments still make sense.
     *
     *    NICE-TO-HAVES:
     *
     *    Space out the console output for computer steps; make it look like the
     *    computer is thinking about what its doing, instead of just showing a dump
     *    of steps to the user. Might be fun to have it complain if it has to draw more
     *    than three cards.
     *
     *    Once the user can play the computer, set up another game so that the computer
     *    can play itself. Let each player pick a random strategy each time, and let them
     *    play like 10k games. Each time a game is won, the game.play method will return
     *    an object containing the winning player and the strategy it used to win. Whatever
     *    class calls game.play will take that information, collate it, and display it to the
     *    user after the 10k games have finished.
     *
     */

    private Deck deck;
    private Player player1; // the user
    private Player player2; // the computer
    private boolean isPlayerOnesTurn;
    private Card currentPlayedCard;
    private CardValueMap cvm;
    private Stack<Card> discardPile;
    private String currentColor;
    private boolean isFirstMove;
    private static final int MAX_P2_DRAW_LOOP = 100;
    private Card numeric; // generic starter card

    public Game(String playerOneName) {
        cvm = new CardValueMap();
        deck = new Deck();
        player1 = new Player(playerOneName, false);
        player1.setStrategy(Player.STRATEGY_NEUTRAL);
        player2 = new Player("WOPR", true);
        player2.setRandomStrategy();
        isPlayerOnesTurn = Main.getRandomBoolean();
        discardPile = new Stack<>();
        currentColor = "";
        isFirstMove = true;
        numeric = new Card(Card.RED, Card.NINE, cvm);
    }

    /**
     * Start the game. (Can only be called once.)
     *
     * @param firstPlayedCard   for testing
     * @param _isPlayerOnesTurn for testing
     */
    public void startGame(Card firstPlayedCard, boolean _isPlayerOnesTurn) { // *** NEEDS TESTING ***
        if (!isFirstMove) {
            Main.out("ERROR: Game.startGame called after first move has already been played. No action taken.");
        } else {
            deck.shuffle();
            dealHands();

            // for testing:
            if (firstPlayedCard == null) {
                currentPlayedCard = verifyFirstCard(deck.popCard());
            } else {
                currentPlayedCard = firstPlayedCard;
                setPlayerOnesTurn(_isPlayerOnesTurn);
            }

            Main.out("\nThe current played card is " + currentPlayedCard.getPrintString());

            discardPile.add(currentPlayedCard);
            currentColor = currentPlayedCard.getColor();

            // Let the players see how many cards are in each other's decks.
            player1.updateOtherPlayersHandCount(player2.showHandCount());
            player2.updateOtherPlayersHandCount(player1.showHandCount());
        }
    }

    /**
     * Play the first hand.
     */
    public void playFirstHand() {
        if (!isFirstMove) {
            // tested
            throw new IllegalStateException("Game.playFirstHand called after first move has already been played.");
        } else {
            if (!isPlayerOnesTurn) {
                Main.out("\n" + player2.getName() + " had the first move.\n");
                printHand(player2);
                if(skipFirstTurn(player2)) {
                    Main.out("\n" + player2.getName() + " was forbidden from discarding.");
                    isPlayerOnesTurn = true;
                    isFirstMove = false;
                } else {
                    playerTwosTurn();
                    isFirstMove = false;
                    isPlayerOnesTurn = true;
                }
            } else { // player one's turn
                if (skipFirstTurn(player1)) {
                    Main.out("\n" + player1.getName() + ", you were forbidden from discarding. " +
                            "The first move switches to " + player2.getName() + ".");
                    isPlayerOnesTurn = false;
                    isFirstMove = false;
                    playerTwosTurn();
                } else {
                    isFirstMove = false;
                    isPlayerOnesTurn = true;
                }
            }
        }
    }

    /**
     * Determine whether or not the first move should be automatically skipped and auto-draws cards as needed.
     *
     * @param player the player who has the current move
     * @return       true if the player's move should be skipped, false otherwise
     */
    public boolean skipFirstTurn(Player player) {
        if (!isFirstMove) {
            throw new IllegalStateException("skipFirstTurn was called when isFirstMove == false."); // tested
        } else {
            // necessary to prevent null pointer exception in skipSubsequentTurn:
            getOtherPlayer(player).setLastPlayedCard(currentPlayedCard);
            player.setLastPlayedCard(numeric);
            if (!currentPlayedCard.isNumeric()) {
                if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD) ||
                        currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    throw new IllegalStateException("skipFirstTurn was called with wild or " +
                            "wild draw four card - not allowed for first turn."); // tested for wild and WD4
                }
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    draw(player);
                    draw(player);
                }
                // return true if skip, reverse, or draw two.
                return true;
            } else {
                // return false if it is numeric
                return false;
            }
        }
    }

    public boolean skipTurn(Player player) { // tested
        if(isFirstMove()) {
            throw new IllegalStateException("Method called when isFirstTurn == true. Must be false.");
        }
        if(player.getLastPlayedCard() == null) {
            // skipFirstTurn guarantees this will never happen
            throw new IllegalStateException("Player's last played card is null in Game.skipTurn.");
        }
        if(currentPlayedCard.isNumeric()) {
            return false;
        }
        String cpcFace = currentPlayedCard.getFace();
        if(currentPlayedCard.isNonNumericNonWild()) {
            if(player.getLastPlayedCard().isNonNumericNonWild()) {
                // it's the same non-numeric / non-wild card the player discarded last time
                // (wild cards do not require a skipped turn)
                return false;
            } else {
                if(cpcFace.equalsIgnoreCase(Card.WILD_DRAW_FOUR) || cpcFace.equalsIgnoreCase(Card.SKIP)
                        || cpcFace.equalsIgnoreCase(Card.DRAW_TWO) || cpcFace.equalsIgnoreCase(Card.REVERSE)){
                    if (cpcFace.equalsIgnoreCase(Card.DRAW_TWO)) {
                        draw(player);
                        draw(player);
                    } else if (cpcFace.equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                        draw(player);
                        draw(player);
                        draw(player);
                        draw(player);
                    }
                    return true; // skip player's turn for all non-numeric / non-wild cpcs
                }
            }
        } else if(cpcFace.equalsIgnoreCase(Card.WILD)) {
            player.setChosenColor(getOtherPlayer(player).getChosenColor());
            return false; // do not skip player's turn
        }
        /*
            Blow up — throw an illegal state exception (put user warning in the exception:):
            Lpc nn-nw, Cpc n: (should never happen - other user should have skipped a turn)
            Lpc nn-nw, Cpc w: (should never happen - other user should have skipped a turn)
         */
        throw new IllegalStateException("Logic fell through all conditionals.");
    }

    /**
     * Player two's turn.
     */
    public void playerTwosTurn() { // *** NEEDS TO BE TESTED ***
        if(isPlayerOnesTurn) {
            throw new IllegalStateException("Called while isPlayerOnesTurn == true");
        }
        // printHand(player2);
        if(player2.otherPlayerIncorrectlyForgotToCallCero(player1)) {
            for(int i = 0; i < 2; i++) {
                draw(player1);
            }
        }
        currentPlayedCard = playerTwoMove();
        player2.setLastPlayedCard(currentPlayedCard);
        if(currentPlayedCard == null) {
            throw new IllegalStateException("Game.playerTwoMove returned a null card to Game.playerTwosFirstMove.");
        } else {
            if(!isFirstMove) {
                currentColor = player2.getChosenColor(); // could be a wild card if not first move
            } else {
                currentColor = currentPlayedCard.getColor();
            }
            discardPile.add(currentPlayedCard);
            Main.out("\n" + player2.getName() + " discarded the card " + currentPlayedCard.getPrintString() + "\n");
            printHand(player2);
        }
    }

    /**
     * A player's chance to move.
     *
     * @return the card player two has chosen to discard
     */
    public Card playerTwoMove() { // *** MORE TESTING NEEDED - possibly functional only ***
        if(deck.getDeckStack().empty() && discardPile.isEmpty()) {
            Main.out("ERROR: Game.playerTwoMove - both deck and discard pile are empty. " +
                    "No action taken, returned null.");
            return null;
        }
        if (isPlayerOnesTurn) {
            Main.out("ERROR: Game.playerTwoMove called during player one's turn. No action taken, returned null.");
            return null;
        }
        Card cardToDiscard = player2.getHand().getFirstCard(); // debug
        // Card cardToDiscard = new Card(Card.BLUE, Card.DRAW_TWO, cvm); // debug
        player2.playerTwoDiscard(cardToDiscard, currentPlayedCard, false, currentColor); // remember to set the correct color here
        return cardToDiscard;

        // Currently under construction: *********************************************************************************************************************
//        boolean playerTwoHasDiscarded = false;
//        int i = 0;
//        while(!playerTwoHasDiscarded && i < MAX_P2_DRAW_LOOP) {  // prevent infinite looping
//            cardToDiscard = player2.decidePlayerTwoDiscard(currentPlayedCard, currentColor);
//            if (cardToDiscard == null) {
//                draw(player2);
//            } else {
//                playerTwoHasDiscarded = true;
//            }
//            i++;
//            if(i == (MAX_P2_DRAW_LOOP - 1)) {
//                Main.out("WARN: Game.playerTwoMove was stopped from falling into an infinite loop " +
//                        "while trying to find a playable card in the deck. " +
//                        "Loop aborted at iteration " + (MAX_P2_DRAW_LOOP - 1) + ", null returned.");
//                return null;
//            }
//        }
//        if(player2.discard(cardToDiscard, currentPlayedCard, false, null)) { // *** needs to decide callCero value! ***
//            return cardToDiscard;
//        } else {
//            Main.out("Player two attempted an illegal move. This is a showstopper. Returning null.");
//            return null;
//        }
    }

    /**
     * Get the other player.
     *
     * @param player the player
     * @return       the other player
     */
    public Player getOtherPlayer(Player player) {
        if(player.isPlayer2()) {
            return player1;
        } else {
            return player2;
        }
    }

    /**
     * Draw a card for a player.
     *
     * @param player the player to draw
     */
    public void draw(Player player) { // tested
        if(deck == null || discardPile == null) {
            Main.out("WARN: Game.draw called with a null deck or a null discard pile, or both. " +
                    "No action taken.");
        } else {
            if(deck.getDeckSize() == 0 && discardPile.size() > 0) {
                refreshDeck();
            }
            Card card = deck.popCard();
            player.getHand().addCard(card);
        }
    }

    /**
     * Transfer the discard pile to the deck and shuffle.
     */
    private void refreshDeck() { // tested by testGame_draw_emptyDeck
        if(deck.getDeckSize() == 0) {
            // Main.out("Deck is empty! Replenishing with discard pile...");
            replaceDeckWithDiscardPile();
            // Main.out("Shuffling deck...");
            deck.shuffle();
            discardPile.clear();
            // Main.out("Deck size is now " + deck.getDeckSize());
        }
    }

    /**
     * Replace the current deck of cards with an injected discard pile.
     */
    private void replaceDeckWithDiscardPile() { // tested by testGame_draw_emptyDeck
        if(discardPile == null || discardPile.isEmpty()) {
            Main.out("WARN: Game.replaceDeckWithDiscardPile received a null or empty discard pile. " +
                    "Deck not replaced.");
        } else {
            deck.clearDeck();
            Stack<Card> discardStack = discardPile;
            deck.replaceWithAnotherDeck(discardStack);
        }
    }

    /**
     * Deal 7 cards each from the top of the deck to each player.
     */
    public void dealHands() { // tested
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        for(Player p : players) {
            List<Card> cards = new ArrayList<>();
            for(int i = 0; i < 7; i++) {
                cards.add(deck.popCard());
            }
            p.setHand(cards);
        }
    }

    /**
     * Reshuffle deck and pop the first card until a non-wild and non-wild draw four card appears.
     *
     * @param card  The card to analyze
     * @return      A verified card
     */
    public Card verifyFirstCard(Card card) { // tested
        if (card != null) {
            while (card.equals(new Card(Card.COLORLESS, Card.WILD, cvm))
                    || card.equals(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm))) {
                deck.clearDeck();
                deck.populate();
                deck.shuffle();
                card = deck.popCard();
            }
            return card;
        } else {
            Main.out("WARN: Game.verifyFirstCard called with a null card. No action taken, null returned.");
            return null;
        }
    }

    /**
     * @return The game's deck (for testing).
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Set the current game's deck (for testing).
     *
     * @param _deck the deck to set
     */
    public void setDeck(Deck _deck) {
        deck.replaceWithAnotherDeck(_deck.getDeckStack());
    }

    /**
     * @return the game's discard pile (for testing)
     */
    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * Set the discard pile.
     *
     * @param injectedDiscardPile the discard pile to set
     */
    public void setDiscardPile(Stack<Card> injectedDiscardPile) { // tested by testGame_draw_emptyDeck
        if(injectedDiscardPile.isEmpty()) {
            Main.out("WARN: Game.setDiscardPile called with empty discardPile.");
        }
        this.discardPile.clear();
        Iterator<Card> injectedDiscardCards = injectedDiscardPile.iterator();
        while(injectedDiscardCards.hasNext()) {
            this.discardPile.add(injectedDiscardCards.next());
        }
    }

    /**
     * Clear the current deck (for testing)
     */
    public void clearDeck() { // no test needed
        deck.clearDeck();
    }

    /**
     * @return a list of both game players (for testing)
     */
    public List<Player> getPlayers() { // no test needed
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        return players;
    }

    /**
     * Print the hand for the given player.
     *
     * @param player the given player
     */
    public void printHand(Player player) { // no test needed
        if(player.getHand() == null) {
            Main.out("WARN: Game.printHand called with a null hand. No action taken.");
        } else {
            int count = 1;
            Main.out(player.getName() + "'s hand:");
            List<String> allCards = player.getHand().getHandPrintStringList();
            for(String s : allCards) {
                if(count < 15) { // debug
                    if(count < 10) {
                        Main.out("0" + count + ": " + s);
                        count++;
                    } else {
                        Main.out(count + ": " + s);
                        count++;
                    }
                }
            }
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setIsFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public Card getCurrentPlayedCard() {
        return currentPlayedCard;
    }

    public void setCurrentPlayedCard(Card card) {
        this.currentPlayedCard = card;
    }

    public void setPlayerOnesTurn(boolean playerOnesTurn) {
        isPlayerOnesTurn = playerOnesTurn;
    }

    public boolean isPlayerOnesTurn() {
        return isPlayerOnesTurn;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}