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
     *    Change user main out warnings to illegal state exceptions -- let showstoppers stop the show.
     *
     *    Automatically draw cards for player one when it receives a draw two or draw four card.
     *
     *    Must call player1.setLastPlayedCard(currentPlayedCard) for player one's turn to prevent unnecessary
     *    automatic draws and turn skips for non-numeric cards. (I think this would happen in game.discard.)
     *
     *    Make all class member variables private (yes, even Card) and create setters / getters for them.
     *
     *    Go through every method and make sure the javadoc comments still make sense.
     *
     *    Required Game methods:
     *
     *        game.startGame: Shuffles deck, deals first hand, pops first card off of deck.
     *        Picks a random first player. Allows player2 to discard if its turn first. Returns
     *        object to servlet that contains player1's hand, the current played card, and a
     *        message ("Your turn first," or "Player2 went first," etc). Would also handle
     *        automatic draws for draw two and turn reversal for skip or reverse cards dealt
     *        to player1. (Does NOT call the Game constructor; game object must keep track of
     *        overall score as each new inner game is created.)
     *
     *        game.discard: user picks a card from the UI and clicks either Discard or Discard
     *        and Declare 'Cero Plus One!' button. Game handles discard, allows the computer to
     *        have its turn, takes care of any non-numeric cards discarded by computer, and
     *        returns gameState object to servlet. (Must handle incorrectly discarded cards.)
     *
     *        game.draw: ads new card to hand, returns gameState object to servlet.
     *
     *
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
    public Player player1; // the user
    public Player player2; // the computer
    private boolean isPlayerOnesTurn;
    public Card currentPlayedCard;
    private CardValueMap cvm;
    private Stack<Card> discardPile;
    private String currentColor;
    private boolean isFirstMove;
    private static final int MAX_P2_DRAW_LOOP = 100;

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
    public void playFirstHand() { // tested
        if (!isFirstMove) {
            // tested
            throw new IllegalStateException("Game.playFirstHand called after first move has already been played.");
        } else {
            if (!isPlayerOnesTurn) {
                Main.out("\n" + player2.getName() + " had the first move.\n");
                printHand(player2);
                if(skipPlayersFirstTurn(player2)) {
                    Main.out("\n" + player2.getName() + " was forbidden from discarding.");
                    isPlayerOnesTurn = true;
                    isFirstMove = false;
                    Main.out("\nOK it's your turn, " + player1.getName() + "\n");
                    printHand(player1);
                } else {
                    playerTwosTurn();
                    isFirstMove = false;
                }
            } else { // player one's turn
                Main.out("\nIt's " + player1.getName() + "'s turn.\n");
                printHand(player1);
                if (skipPlayersFirstTurn(player1)) {
                    Main.out("\n" + player1.getName() + ", you were forbidden from discarding. " +
                            "The first move switches to " + player2.getName() + ".");
                    isPlayerOnesTurn = false;
                    isFirstMove = false;
                    playerTwosTurn();
                } else {
                    isFirstMove = false;
                }
            }
        }
    }

    // public void playSubsequentHand() {

    /**
     * Determine whether or not the first move should be automatically skipped and auto-draws cards as needed.
     *
     * @param player the player who has the current move
     * @return       true if the player's move should be skipped, false otherwise
     */
    public boolean skipPlayersFirstTurn(Player player) { // tested
        if (!isFirstMove) {
            throw new IllegalStateException("skipPlayersFirstTurn was called when isFirstMove == false."); // tested
        } else {
            if (!currentPlayedCard.isNumberCard()) {
                if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD) ||
                        currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    throw new IllegalStateException("skipPlayersFirstTurn was called with wild or " +
                            "wild draw four card - not allowed for first turn."); // tested for wild and WD4
                }
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    draw(player); // tested
                    draw(player); //tested
                }
                // return true if skip, reverse, or draw two.
                return true; // tested for draw two, skip, and reverse.
            } else {
                // return false if it is numeric
                return false; // tested
            }
        }
    }

    /**
     * Determines if the player has to react to a non-numeric card move and skip its turn.
     *
     * @param player the player who has the current move
     * @return true if the player's move should be skipped, false otherwise.
     */
    public boolean skipPlayersSubsequentTurn(Player player) { // *** TESTING IN PROGRESS ***
        if(isFirstMove) {
            // tested
            throw new IllegalStateException("Game.skipPlayersSubsequentTurn called when isFirstMove == true.");
        } else {

            // todo: this method must return exactly ONE true and ONE false.

            // special case for 2nd move - if 1st move is skipped, 2nd player needs to get its last played card
            // from somewhere else. Set it to the current card to prevent auto double skipping.
            // ******* I suspect the block below is useless and should be destroyed. *******
            // ******* this method will only be called for moves 3, 4, 5, ... *******
//            Card lastPlayedCard = currentPlayedCard;
//            if (player.getLastPlayedCard() != null) { // only happens on 2nd move if 1st move auto skipped
//                lastPlayedCard = player.getLastPlayedCard();
//            }

            Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);

            Card lastPlayedCard = player.getLastPlayedCard();

            Main.out("\nOh hai from Game.skipPlayersSubsequentTurn.");
            Main.out("lastPlayedCard = " + lastPlayedCard.getPrintString() + ", currentPlayedCard = " +
                    currentPlayedCard.getPrintString() + "\n");

            boolean lastPlayedCardIsNumeric = lastPlayedCard.isNumberCard();
            boolean lastPlayedCardIsWild = lastPlayedCard.equals(wild);
            boolean lastPlayedCardIsNonNumericAndNonWild = !lastPlayedCard.isNumberCard() && !lastPlayedCard.equals(wild);

            boolean currentPlayedCardIsNumeric = currentPlayedCard.isNumberCard();
            boolean currentPlayedCardIsWild = currentPlayedCard.equals(wild);
            boolean currentPlayedCardIsNonNumericAndNonWild = !currentPlayedCard.isNumberCard()
                    && !currentPlayedCard.equals(wild);

            if (lastPlayedCardIsNumeric && currentPlayedCardIsNumeric) {
                // do not skip player's turn
                return false; // tested 2017-01-03
            }
            if (lastPlayedCardIsWild && currentPlayedCardIsNumeric) {
                // do not skip player's turn
                return false; // tested 2017-01-03
            }
            if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsNonNumericAndNonWild) {
                if(!lastPlayedCard.equals(currentPlayedCard)) {
                    // tested 2017-01-03
                    throw new IllegalStateException("A non-numeric, non-wild card can not come back to the " +
                            "player that dealt it unless it is exactly the same card.");
                }
                // do not skip player's turn
                return false; // tested 2017-01-03
            }


            if (lastPlayedCardIsNumeric && currentPlayedCardIsNonNumericAndNonWild) {
                // draw as needed and skip player's turn
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    draw(player);
                    draw(player);
                    return true; // tested 2017-01-03
                }
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    draw(player);
                    draw(player);
                    draw(player);
                    draw(player);
                    return true;
                }
            }
            if (lastPlayedCardIsWild && currentPlayedCardIsNonNumericAndNonWild) {
                // draw as needed and skip player's turn
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    draw(player);
                    draw(player);
                    return true;
                }
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    draw(player);
                    draw(player);
                    draw(player);
                    draw(player);
                    return true;
                }
            }


            if (lastPlayedCardIsNumeric && currentPlayedCardIsWild) {
                player.chosenColor = getOtherPlayersChosenColor(player);
                // do not skip player's turn
                return false; // tested 2017-01-03
            }
            if (lastPlayedCardIsWild && currentPlayedCardIsWild) {
                player.chosenColor = getOtherPlayersChosenColor(player);
                // do not skip player's turn
                return false;
            }


            if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsNumeric) {
                Main.out("WARN: Game.skipPlayersTurn encountered a state that should never occur. " +
                        "No action taken, returned true.");
                return true;
            }
            if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsWild) {
                Main.out("WARN: Game.skipPlayersTurn encountered a state that should never occur. " +
                        "No action taken, returned true.");
                return true;
            }


            Main.out("WARN: logic in Game.skipPlayersTurn fell through to the bottom. No action taken, returning true.");
            return true;
        }
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
                currentColor = player2.chosenColor; // could be a wild card if not first move
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
        // just discard first card for debug
        Card cardToDiscard = player2.getHand().getFirstCard();
        player2.discard(cardToDiscard, currentPlayedCard, false, currentColor);
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

    public String getOtherPlayersChosenColor(Player player) {  // tested
        if(player.isPlayer2()) {
            return player1.chosenColor;
        } else {
            return player2.chosenColor;
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
            Main.out("WARN: Game.setDiscardPile called with empty discardPile. No action taken.");
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