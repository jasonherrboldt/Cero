package com.jason;

import com.sun.istack.internal.Nullable;

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
     *    Space out the console output for computer steps; make it look like the
     *    computer is thinking about what its doing, instead of just showing a dump
     *    of steps to the user. Might be fun to have it complain if it has to draw more
     *    than three cards.
     *
     *    Wouldn't it be cool if player 2 could keep track of what strategy it used for each deck,
     *    and whether or not it won that deck? It could then pick future strategies by whichever
     *    strategy produced the most wins.
     *
     *    "A defensive strategy would advise playing a high card in order to reduce the point value of the hand.
     *    However, an offensive strategy would suggest playing a 0 when the player wants to continue on the current
     *    color, because it is less likely to be matched by another 0 of a different color (there is only one 0 of
     *    each color, but two of each 1–9)." - uno wiki page
     *
     *    The computer might want to adopt a cautious strategy if it detects that player 1 is down to just a few
     *    cards.
     *
     */
    
    private Deck deck;
    private Player player1; // the user
    private Player player2; // the computer
    private boolean gameWinnerExists;
    private boolean deckWinnerExists;
    private boolean isPlayerOnesTurn;
    private Card currentPlayedCard;
    private static final int FAKE_GAME_RUNS = 1;
    private CardValueMap cvm;
    private Stack<Card> discardPile;
    private int deckCounter;
    
    public Game(String userName) {
        cvm = new CardValueMap();
        deck = new Deck();
        Main.out("");
        player2 = new Player("Computer", true);
        if(Main.getRandomBoolean()) {
            player2.setStrategy(Player.STRATEGY_BOLD);
        } else {
            player2.setStrategy(Player.STRATEGY_CAUTIOUS);
        }
        player1 = new Player(userName, false);
        player1.setStrategy(Player.STRATEGY_NEUTRAL);
        gameWinnerExists = false;
        deckWinnerExists = false;
        isPlayerOnesTurn = Main.getRandomBoolean();
        discardPile = new Stack<>();
        deckCounter = 1;
    }

    /**
     * Play the game.
     */
    public void play() {
    
        while(!gameWinnerExists) {
            deck.shuffle();
            dealHands();
            currentPlayedCard = verifyFirstCard(deck.getNextCard());
            discardPile.add(currentPlayedCard);

            int fakeGames = 0;
            while(!deckWinnerExists && fakeGames < FAKE_GAME_RUNS) {
                Main.out("Now playing deck number " + deckCounter + ".");

                // Let the players see how many cards are in each other's decks.
                player1.updateOtherPlayersHandCount(player2.showHandCount());
                player2.updateOtherPlayersHandCount(player1.showHandCount());

                if(isPlayerOnesTurn) {
                    playHand(player1);
                } else {
                    playHand(player2);
                }
                if(!deckWinnerExists) {

                    // Player 2 sometimes forgets to call out player 1 on not calling 'Cero!'
                    if (player1.getHand().getSize() == 1 && !player1.isCeroCalled() && Main.getRandomBoolean()) {
                        Main.out("Player 1 forgot to declare 'Cero!' with one card left - must draw two cards.");
                        for(int i = 0; i < 2; i++) {
                            draw(player1);
                        }
                    }

                    // boolean answer = Main.askUserYesOrNoQuestion("Would you like to declare that player 2 did not " +
                            // "declare 'Cero!' when it should have?");
                    boolean answer = false; // debug
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
                    }

                    // Reset each player's cero called value.
                    player1.resetCeroCalled();
                    player2.resetCeroCalled();

                    // Switch player's turn.
                    isPlayerOnesTurn = !isPlayerOnesTurn;
                }
                fakeGames++;
                deckCounter++;
            }
            if(player1.getScore() > 500 || player2.getScore() > 500) {
                gameWinnerExists = true;
                if(player1.getScore() > 500) {
                    Main.out("Player 1 is the winner!");
                }
                if(player2.getScore() > 500) {
                    Main.out("Player 2 is the winner!");
                }
            }
        }
    }

    /**
     * Update the current played card by asking a given player to make a move.
     *
     * @param player the player to move
     */
    public void playHand(Player player) {
        currentPlayedCard = move(player);
        discardPile.add(currentPlayedCard);
        player.callCero();
        if(player.getHand().getSize() == 0) {
            deckWinnerExists = true;
            if(isPlayerOnesTurn) {
                int playerOneScore = player2.getHand().getHandValue();
                Main.out("Player 1 wins deck # " + deckCounter + "!");
                Main.out("Current deck winnings: " + playerOneScore);
                player1.updateScore(playerOneScore);
                Main.out("Player 1's new score is " + player1.getScore());
            } else {
                int playerTwoScore = player1.getHand().getHandValue();
                Main.out("Player 2 wins deck # " + deckCounter + "!");
                Main.out("Current deck winnings: " + playerTwoScore);
                player2.updateScore(playerTwoScore);
                Main.out("Player 2's new score is " + player2.getScore());
            }
        }
    }

    @Nullable
    public Card move(Player player) {
        if(deck.getDeck().empty() && discardPile.isEmpty()) {
            Main.out("ERROR: both deck and discard pile sent to Game.move are empty. No action taken, returned null.");
            return null;
        }
        if (currentPlayedCard == null) {
            Main.out("ERROR: null card passed to Game.move. Cannot make any move.");
            return null;
        } else {
            if (player.isComputer()) {
                if (currentPlayedCard.isNumberCard()) {
                    Main.out("Handing move for numeric card.");
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

                // Draw four cards (for debug).
                for(int i = 0; i < 3; i++) {
                    draw(player);
                }

                // Just discard the first card (for debug).
                Card firstCard = player.getHand().getFirstCard();
                player.getHand().discard(firstCard);
                return firstCard;
            } else {
                // Main.out("Ask the user what to do.");
                Main.out("Just discarding player one's first card (for debug).");
                Card firstCard = player.getHand().getFirstCard();
                player.getHand().discard(firstCard);
                return firstCard;
            }
        }
    }

    public void draw(Player player) { // *** NEEDS TO BE TESTED ***
        if(deck == null || discardPile == null) {
            Main.out("WARN: Player.draw called with a null deck or a null discard pile, or both. " +
                    "No action taken.");
        } else {
            if(deck.getDeckSize() == 0 && discardPile.size() == 0) {
                Main.out("WARN: Player.draw called with empty deck and empty discard pile. " +
                        "At least one must be non-empty. No action taken.");
            } else {
                if(deck.getDeckSize() == 0) {
                    refreshDeck();
                }
                Card card = deck.getNextCard();
                player.getHand().addCard(card);
            }
        }
    }

    /**
     * Transfer the discard pile to the deck and shuffle.
     */
    public void refreshDeck() { // *** NEEDS TO BE TESTED ***
        if(deck.getDeckSize() == 0) {
            Main.out("Deck is empty! Replenishing with discard pile...");
            replaceDeckWithDiscardPile();
            Main.out("Shuffling deck...");
            deck.shuffle();
            discardPile.clear(); // useless - disappears after stack popped
        }
    }

    /**
     * Replace the current deck of cards with an injected discard pile.
     */
    public void replaceDeckWithDiscardPile() { // is covered by refreshDeckIfEmpty test
        if(discardPile == null || discardPile.isEmpty()) {
            Main.out("WARN: Game.replaceDeckWithDiscardPile received a null or empty discard pile. " +
                    "Deck not replaced.");
        } else {
            deck.clearDeck();
            Stack<Card> discardStack = discardPile;
            deck.replaceDeckWithAnotherDeck(discardStack);
        }
    }

    /**
     * Display the current played card.
     */
    private void displayCurrentPlayedCard() {
        Main.out("The current played card is " + currentPlayedCard.getPrintString());
    }

    /**
     * Deal 7 cards each from the top of the deck to each player.
     */
    private void dealHands() {
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        for(Player p : players) {
            List<Card> cards = new ArrayList<>();
            for(int i = 0; i < 7; i++) {
                cards.add(deck.getNextCard());
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
    @Nullable
    public Card verifyFirstCard(Card card) { // tested
        if (card != null) {
            while (card.equals(new Card(Card.COLORLESS, Card.WILD, cvm))
                    || card.equals(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm))) {
                deck.clearDeck();
                deck.populate();
                deck.shuffle();
                card = deck.getNextCard();
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
        deck.replaceDeckWithAnotherDeck(_deck.getDeck());
    }

    /**
     * @return the game's discard pile (for testing)
     */
    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(Stack<Card> injectedDiscardPile) { // *** NEEDS TO BE TESTED ***
        if(injectedDiscardPile.isEmpty()) {
            Main.out("WARN: Game.setDiscardPile called with empty discardPile. No action taken.");
        } else {
            this.discardPile.clear();
            Stack<Card> injectedDiscardStack = injectedDiscardPile;
            Iterator<Card> injectedDiscardCards = injectedDiscardStack.iterator();
            while(injectedDiscardCards.hasNext()) {
                this.discardPile.add(injectedDiscardCards.next());
            }
        }
    }

    /**
     * Clear the current deck (for testing)
     */
    public void clearDeck() {
        deck.clearDeck();
    }

}