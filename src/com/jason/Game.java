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
     *    Go through all test classes and see if any repeating code blocks can be moved up to the class level and shared.
     *
     *    Change user main out warnings to illegal state exceptions -- let showstoppers stop the show.
     *
     *    Go through every method and make sure the javadoc comments still make sense.
     *
     *    Make sure every single method is tested. If not, leave a comment as to why.
     *
     *    NICE-TO-HAVES:
     *
     *    Let the computer pick a phrase at random when it wins a game. Something like, "[player_two_name] has
     *    a message for you: I win again. WAH WAH WAAAAAAAAAH..."
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

    public Game(String playerOneName, boolean showPlayerTwoActions) {
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
            throw new IllegalStateException("Game.startGame called after first move has already been played.");
        } else {
            deck.shuffle();
            dealHands();
            // for testing:
            if (firstPlayedCard == null) {
                currentPlayedCard = verifyFirstCard(deck.popCard());
                // Main.out("oh hai from Game.startGame. cpc has just been changed to " + currentPlayedCard.getPrintString());
            } else {
                currentPlayedCard = firstPlayedCard;
                // Main.out("oh hai from Game.startGame. cpc has just been changed to " + currentPlayedCard.getPrintString());
                setPlayerOnesTurn(_isPlayerOnesTurn);
                // Main.out("isPlayerOnesTurn was just set to " + _isPlayerOnesTurn + " in Game.startGame.");
            }

            // Main.out("\nThe current played card is " + currentPlayedCard.getPrintString());

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
    public Card playFirstHand(boolean printOuts) { // tested
        if (!isFirstMove) {
            // tested
            throw new IllegalStateException("Game.playFirstHand called after first move has already been played.");
        } else {
            Card returnCard = null;
            if (!isPlayerOnesTurn) {
                if(skipFirstTurn(player2, printOuts)) {
                    if(printOuts) {
                        Main.pause(2);
                        Main.out("\n" + player2.getName() + " was forbidden from discarding.");
                    }
                    isPlayerOnesTurn = true;
                    // Main.out("isPlayerOnesTurn was just set to true in Game.playFirstHand.");
                    isFirstMove = false;
                } else {
                    returnCard = playerTwosTurn(printOuts);
                    isFirstMove = false;
                    isPlayerOnesTurn = true;
                    // Main.out("isPlayerOnesTurn was just set to true in Game.playFirstHand.");
                }
            } else { // player one's turn
                if (skipFirstTurn(player1, printOuts)) {
                    if(printOuts) {
                        Main.pause(2);
                        Main.out("\n" + player1.getName() + ", you were forbidden from discarding.");
                        Main.pause(2);
                        Main.out("\nThe first move switches to " + player2.getName() + ".");
                    }
                    isPlayerOnesTurn = false;
                    // Main.out("isPlayerOnesTurn was just set to false in Game.playFirstHand.");
                    isFirstMove = false;
                    returnCard = playerTwosTurn(printOuts);
                } else {
                    isFirstMove = false;
                    isPlayerOnesTurn = true;
                    // Main.out("isPlayerOnesTurn was just set to true in Game.playFirstHand.");
                }
            }
            return returnCard;
        }
    }

    /**
     * Determine whether or not the first move should be automatically skipped. Auto-draws cards as needed.
     *
     * @param player the player who has the current move
     * @return       true if the player's move should be skipped, false otherwise
     */
    public boolean skipFirstTurn(Player player, boolean printOuts) { // tested
        if (!isFirstMove) {
            throw new IllegalStateException("skipFirstTurn was called when isFirstMove == false."); // tested
        } else {
            // necessary to prevent null pointer exception in skipSubsequentTurn:
            // Main.out("oh hai from Game.skipFirstTurn. About to set the other player's lpc:");
            // Main.out("Other player's lpc is " + getOtherPlayer(player).getLastPlayedCard());
            getOtherPlayer(player).setLastPlayedCard(currentPlayedCard);
            // Main.out("Switch done. Other player's lpc is now " + getOtherPlayer(player).getLastPlayedCard().getPrintString());
            player.setLastPlayedCard(numeric);
            if (!currentPlayedCard.isNumeric()) {
                if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD) ||
                        currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    throw new IllegalStateException("skipFirstTurn was called with wild or " +
                            "wild draw four card - not allowed for first turn."); // tested for wild and WD4
                }
                if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    draw(player, printOuts);
                    draw(player, printOuts);
                }
                // return true if skip, reverse, or draw two.
                return true;
            } else {
                // return false if it is numeric
                return false;
            }
        }
    }

    /**
     * Decide if a player's turn should be skipped. Also handles auto-drawing for draw two and wild draw four.
     *
     * @param player the player
     * @return       true if the player's turn should be skipped, false otherwise.
     */
    public boolean skipTurn(Player player, boolean printOuts) { // tested
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
                        draw(player, printOuts);
                        draw(player, printOuts);
                    } else if (cpcFace.equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                        draw(player, printOuts);
                        draw(player, printOuts);
                        draw(player, printOuts);
                        draw(player, printOuts);
                    }
                    return true; // skip player's turn for all non-numeric / non-wild cpcs
                }
            }
        } else if(cpcFace.equalsIgnoreCase(Card.WILD)) {
            player.setChosenColor(getOtherPlayer(player).getChosenColor());
            return false; // do not skip player's turn
        }
        /*
            Blow up
            Lpc nn-nw, Cpc n: (should never happen - other user should have skipped a turn)
            Lpc nn-nw, Cpc w: (should never happen - other user should have skipped a turn)
         */
        Main.out("currentPlayedCard: " + currentPlayedCard + ", currentColor: " + currentColor + ".\n");
        printHand(player2);
        throw new IllegalStateException("Logic fell through all conditionals.");
    }

    /**
     * Player two's turn.
     */
    public Card playerTwosTurn(boolean printOuts) { // *** NEEDS TO BE TESTED ***
        if(isPlayerOnesTurn) {
            throw new IllegalStateException("Called while isPlayerOnesTurn == true");
        }
        Card returnCard = playerTwoMove(printOuts);
        if(returnCard == null) {
            Main.out("currentPlayedCard: " + currentPlayedCard + ", currentColor: " + currentColor + ".\n");
            printHand(player2);
            throw new IllegalStateException("Game.playerTwoMove returned a null card to Game.playerTwosFirstMove.");
        } else {
            discardPile.add(returnCard);
            player2.setLastPlayedCard(returnCard);
            currentPlayedCard = returnCard;
            // setPlayerOnesTurn(true);
            // Main.out("setPlayerOnesTurn was just set to (true) in Game.playerTwosTurn.");
        }
        return returnCard;
    }

    /**
     * A player's chance to move.
     *
     * @return the card player two has chosen to discard
     */
    public Card playerTwoMove(boolean printOuts) { // *** MORE TESTING NEEDED - possibly functional only ***
        if(deck.getDeckStack().empty() && discardPile.isEmpty()) {
            Main.out("WARN: Game.playerTwoMove: both deck and discard pile are empty.");
        }
        if (isPlayerOnesTurn) {
            throw new IllegalStateException("ERROR: Game.playerTwoMove called during player one's turn.");
        }

        // switch from bold to cautious strategy if needed
        if(player2.getStrategy().equalsIgnoreCase(Player.STRATEGY_BOLD) && player1.getHand().getSize() < 4) {
            player2.setStrategy(Player.STRATEGY_CAUTIOUS); // tested in TestPlayer
        }
        Card cardToDiscard = null;
        boolean playerTwoHasDiscarded = false;
        int i = 0;
        // Main.out("\nPlayer Two is playing with a " + player2.getStrategy() + " strategy.");
        // discard or draw until a legal card is found
        // Main.out("oh hai from Game.playerTwoMove. cpc: " + currentPlayedCard.getPrintString());
        while(!playerTwoHasDiscarded && i < MAX_P2_DRAW_LOOP) {  // prevent infinite looping while debug
            cardToDiscard = player2.decidePlayerTwoDiscard(currentPlayedCard, currentColor);
            if (cardToDiscard == null) {
                if(printOuts) {
                }
                draw(player2, printOuts);
            } else {
                playerTwoHasDiscarded = true;
            }
            i++;
            if(i == (MAX_P2_DRAW_LOOP - 1)) {
                Main.out("WARN: Game.playerTwoMove was stopped from falling into an infinite loop " +
                        "while trying to find a playable card in the deck. " +
                        "Loop aborted at iteration " + (MAX_P2_DRAW_LOOP - 1) + ", null returned.");
                return null;
            }
        }
        if(!player2.isLegalDiscard(cardToDiscard, currentPlayedCard, currentColor)) {
            // Main.pause(2);
            Main.out("Player two attempted an illegal move. cardToDiscard: " + cardToDiscard.getPrintString()
                    + ", currentPlayedCard: " + currentPlayedCard + ", currentColor: " + currentColor + ".\n");
            printHand(player2);
            throw new IllegalStateException("Player two attempted an illegal move.");
        } else {
            player2.setLastPlayedCard(cardToDiscard);
            player2.getHand().discard(cardToDiscard);
            if(cardToDiscard.isColorlessCard()) { // wild or wd4
                String playerTwosChosenColor = player2.selectNewColor();
                if(playerTwosChosenColor == null) {
                    Main.out("Player two attempted an illegal move. cardToDiscard: " + cardToDiscard.getPrintString()
                            + ", currentPlayedCard: " + currentPlayedCard + ", currentColor: " + currentColor + ".\n");
                    printHand(player2);
                    throw new IllegalStateException("playerTwosChosenColor may not be null here.");
                } else {
                    player2.setChosenColor(playerTwosChosenColor);
                    setCurrentColor(playerTwosChosenColor);
                }
            } else {
                setCurrentColor(cardToDiscard.getColor());
                player2.setChosenColor(cardToDiscard.getColor());
            }
            return cardToDiscard;
        }
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
    public void draw(Player player, boolean printOuts) { // tested (just needs exception testing as well)
        if(deck == null || discardPile == null) {
            throw new IllegalStateException("Game.draw called with a null deck or a null discard pile, or both. " +
                    "No action taken."); // *** NEEDS TESTING ***
        } else {
            if(deck.getSize() == 0 && discardPile.size() > 0) {
                refreshDeck();
            }
            Card card = deck.popCard();
            if(printOuts) {
                if(isPlayerOnesTurn) {
                    Main.pause(2);
                    Main.out("\nAdding to " + player.getName()+ "'s hand: " + card.getPrintString());
                } else {
                    Main.pause(2);
                    Main.out("\n" + player.getName()+ " is drawing a card.");
                }
            }
            player.getHand().addCard(card);
        }
    }

    /**
     * Transfer the discard pile to the deck and shuffle.
     */
    private void refreshDeck() { // tested by testGame_draw_emptyDeck
        if(deck.getSize() == 0) {
            replaceDeckWithDiscardPile();
            deck.shuffle();
            discardPile.clear();
        }
    }

    /**
     * Replace the current deck of cards with an injected discard pile.
     */
    private void replaceDeckWithDiscardPile() { // tested by testGame_draw_emptyDeck
        if(discardPile == null || discardPile.isEmpty()) {
            throw new IllegalStateException("Game.replaceDeckWithDiscardPile received a null or empty discard pile.");
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
        // Main.out("oh hai from Game.dealHands. Deck size before dealing is " + deck.getSize());
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
        // Main.out("oh hai from Game.dealHands. Deck size after dealing is " + deck.getSize());
        if(deck.getSize() != 94) {
            Main.out("Game.dealHands did not deal 7 cards to each player!");
            printHand(player1);
            printHand(player2);
            throw new IllegalStateException("Game.dealHands did not deal 7 cards to each player.");
        }
    }

    /**
     * Reshuffle deck and pop the first card until a non-wild and non-wild draw four card appears.
     *
     * @param card  The card to analyze
     * @return      A verified card
     */
    public Card verifyFirstCard(Card card) { // tested
        if (card == null) {
            throw new IllegalStateException("Game.verifyFirstCard called with a null card.");
        } else {
            while (card.equals(new Card(Card.COLORLESS, Card.WILD, cvm))
                    || card.equals(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm))) {
                deck.clearDeck();
                deck.populate();
                deck.shuffle();
                card = deck.popCard();
            }
            return card;
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
                        Main.out(" " + count + ": " + s);
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

    public void setCurrentColor(String color) {
        currentColor = color;
    }

    public Card getCurrentPlayedCard() {
        return currentPlayedCard;
    }

    public void setCurrentPlayedCard(Card card) {
        this.currentPlayedCard = card;
        // Main.out("oh hai from Game.setCurrentPlayedCard. cpc has just been changed to: " + currentPlayedCard.getPrintString());
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