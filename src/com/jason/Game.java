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
     *    Make sure user Q&A I/O works OK on via command line prompts.
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
    private boolean outerGameWon;
    private boolean innerGameWon;
    private boolean isPlayerOnesTurn;
    private Card currentPlayedCard;
    private static final int FAKE_GAME_RUNS = 1;
    private static final int FAKE_MOVES = 20;
    private CardValueMap cvm;
    private Stack<Card> discardPile;
    private int moveCount;
    private String currentColor;
    
    public Game(String userName) {
        cvm = new CardValueMap();
        deck = new Deck();
        Main.out("");
        player2 = new Player("Computer", true);
        player2.setRandomStrategy();
        player1 = new Player(userName, false);
        player1.setStrategy(Player.STRATEGY_NEUTRAL);
        outerGameWon = false;
        isPlayerOnesTurn = Main.getRandomBoolean();
        discardPile = new Stack<>();
        moveCount = 1;
        currentColor = "";
    }

    /**
     * Play the game.
     */
    void play() {

        int fakeGames = 0;
        while(!outerGameWon && fakeGames < FAKE_GAME_RUNS) {
            Main.out("Starting a new game...");
            deck.shuffle();
            dealHands();
            Main.out("The deck has been shuffled and the first hand has been dealt.\n");
            currentPlayedCard = verifyFirstCard(deck.popCard());
            discardPile.add(currentPlayedCard);
            currentColor = currentPlayedCard.getColor();
            // player2.setStrategy(player2.getBestStrategy());
            int fakeMoves = 0;
            innerGameWon = false;
            while(!innerGameWon && fakeMoves < FAKE_MOVES) {
                Main.out("Move number " + moveCount + ".\n");
                printHand(player1);
                Main.out("");
                printHand(player2);

                // Let the players see how many cards are in each other's decks.
                player1.updateOtherPlayersHandCount(player2.showHandCount());
                player2.updateOtherPlayersHandCount(player1.showHandCount());

                if(isPlayerOnesTurn) {
                    playHand(player1);
                } else {
                    playHand(player2);
                }
                if(!innerGameWon) {

                    /* Uncomment out the below block for user interaction.
                    // Player 2 sometimes forgets to call out player 1 on not calling 'Cero!'
                    if (player1.getHand().getSize() == 1 && !player1.isCeroCalled() && Main.getRandomBoolean()) {
                        Main.out("Player 1 forgot to declare 'Cero!' with one card left - must draw two cards.");
                        for(int i = 0; i < 2; i++) {
                            draw(player1);
                        }
                    }

                    boolean answer = Main.askUserYesOrNoQuestion("Would you like to declare that player 2 did not " +
                             "declare 'Cero!' when it should have?");
                    if (answer) {
                        if (player2.getHand().getSize() == 1 && !player1.isCeroCalled()) {
                            Main.out("Player 2 forgot to declare 'Cero!' with one card left - must draw two cards.");
                            for(int i = 0; i < 2; i++) {
                                draw(player2);
                            }
                        } else {
                            Main.out("Player 2 did not incorrectly fail to declare 'Cero!' - " +
                                    "no punishment for Player 2.");
                        }
                    } Uncomment out the above block for user interaction.
                    */

                    // Reset each player's cero called value.
                    player1.resetCeroCalled();
                    player2.resetCeroCalled();

                    // Switch player's turn.
                    isPlayerOnesTurn = !isPlayerOnesTurn;
                }
                fakeMoves++;
                moveCount++;
            }
            if(player1.getScore() > 500 || player2.getScore() > 500) {
                outerGameWon = true;
                if(player1.getScore() > 500) {
                    Main.out("Player 1 is the winner!");
                }
                if(player2.getScore() > 500) {
                    Main.out("Player 2 is the winner!");
                }
            }
            fakeGames++;
        }
    }

    /**
     * Update the current played card by asking a given player to make a move.
     *
     * @param player the player to move
     */
    private void playHand(Player player) {
        Main.out("\n" + player.getName() +  "'s turn.");
        currentPlayedCard = move(player);
        if(currentPlayedCard != null) {
            currentColor = currentPlayedCard.getColor();
        } else {
            currentColor = player.getPreferredColor();
        }
        discardPile.add(currentPlayedCard);
        Main.out(player.getName() + " just discarded " + currentPlayedCard.getPrintString() + "\n");
        printHand(player);
        player.callCero();
        if(player.getHand().getSize() == 0) {
            innerGameWon = true;
            if(!player.isPlayer2()) {
                int playerOneScore = player2.getHand().getHandValue();
                Main.out("Player 1 wins this round!");
                Main.out("Current deck winnings: " + playerOneScore);
                player1.updateScore(playerOneScore);
                Main.out("Player 1's new score is " + player1.getScore());
            } else {
                int playerTwoScore = player1.getHand().getHandValue();
                Main.out("Player 2 wins this round!");
                Main.out("Current deck winnings: " + playerTwoScore);
                player2.updateScore(playerTwoScore);
                Main.out("Player 2's new score is " + player2.getScore());
            }
        }
    }

    /**
     * A player's chance to move.
     *
     * @param player the player to move
     * @return       the discarded card
     */
    private Card move(Player player) {
        if(deck.getDeckStack().empty() && discardPile.isEmpty()) {
            Main.out("ERROR: both deck and discard pile sent to Game.move are empty. No action taken, returned null.");
            return null;
        }
        if (currentPlayedCard == null) {
            Main.out("ERROR: null card passed to Game.move. Cannot make any move.");
            return null;
        } else {
            Main.out("The current played card is " + currentPlayedCard.getPrintString());
            if (player.isPlayer2()) { // computer
                if (currentPlayedCard.isNumberCard()) {
                    Main.out("Handing move for numeric card.");
                    decideMove(player);
                } else {
                    if (currentPlayedCard.getFace().equalsIgnoreCase(Card.SKIP)) {
                        Main.out("Handling move for " + Card.SKIP + ".");
                    } else if (currentPlayedCard.getFace().equalsIgnoreCase(Card.REVERSE)) {
                        Main.out("Handling move for " + Card.REVERSE + ".");
                    } else if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                        Main.out("Handling move for " + Card.DRAW_TWO + ".");
                    } else if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
                        Main.out("Handling move for " + Card.WILD + ".");
                    } else { // WILD_DRAW_FOUR
                        Main.out("Handling move for " + Card.WILD_DRAW_FOUR + ".");
                    }
                }

                // Just discard the first card (for debug).
                Card firstCard = player.getHand().getFirstCard();
                player.getHand().discard(firstCard);

                Main.out("Drawing thirty cards and discarding one (for debug).");
                for(int i = 0; i < 31; i++) {
                    draw(player);
                }

                return firstCard;
            } else {
                // Main.out("Ask the user what to do.");
                Main.out("Just discarding player one's first card and drawing none (for debug).");
                Card firstCard = player.getHand().getFirstCard();
                player.getHand().discard(firstCard);
                return firstCard;
            }
        }
    }

    public Card decideMove(Player player) {
        String strategy = player.getStrategy();

        switch(strategy) {
            case Player.STRATEGY_BOLD:
                Main.out(player.getName() + " is using the " + strategy + " strategy.");
                break;
            case Player.STRATEGY_CAUTIOUS:
                Main.out(player.getName() + " is using the " + strategy + " strategy.");
                break;
            case Player.STRATEGY_DUMB:
                Main.out(player.getName() + " is using the " + strategy + " strategy.");
                break;
        }

        return null;
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
            Main.out("Deck is empty! Replenishing with discard pile...");
            replaceDeckWithDiscardPile();
            Main.out("Shuffling deck...");
            deck.shuffle();
            discardPile.clear();
            Main.out("Deck size is now " + deck.getDeckSize());
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
        } else {
            this.discardPile.clear();
            Iterator<Card> injectedDiscardCards = injectedDiscardPile.iterator();
            while(injectedDiscardCards.hasNext()) {
                this.discardPile.add(injectedDiscardCards.next());
            }
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
    private void printHand(Player player) { // no test needed
        // todo: will neventually need code here to only print the hand if !player.isPlayer1.
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
}