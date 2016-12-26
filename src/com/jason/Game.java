package com.jason;

import java.util.ArrayList;
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
     *    Arbitrarily decide if the computer should declare "Cero!" when it reaches one card.
     *
     */
    
    private Deck deck;
    private Player player1; // the user
    private Player player2; // the computer
    private boolean gameWinnerExists;
    private boolean deckWinnerExists;
    private boolean isPlayerOnesTurn;
    private Card currentPlayedCard;
    private static final int FAKE_GAME_RUNS = 8;
    private CardValueMap cvm;
    private Stack<Card> discardPile;
    
    public Game(String userName) {

        cvm = new CardValueMap();
        deck = new Deck(null);
        Main.out("");
        player2 = new Player("Computer", true);
        if(getRandomBoolean()) {
            player2.setStrategy(Player.STRATEGY_BOLD);
        } else {
            player2.setStrategy(Player.STRATEGY_CAUTIOUS);
        }
        player1 = new Player(userName, false);
        player1.setStrategy(Player.STRATEGY_NEUTRAL);
        gameWinnerExists = false;
        deckWinnerExists = false;
        isPlayerOnesTurn = getRandomBoolean();
        discardPile = new Stack<>();
    }
    
    public void play() {
    
        while(!gameWinnerExists) {
            dealHands();
            deck.shuffle();
            currentPlayedCard = verifyFirstCard(deck.getNextCard());
            discardPile.push(currentPlayedCard);

            while(!deckWinnerExists) {
                refreshDeckIfEmpty(); // need to test!

            }
            // Call player one's UPDATEOTHERPLAYERSHANDCOUNT method (inject a call to player two's SHOWHANDCOUNT method)
            // Call player two's UPDATEOTHERPLAYERSHANDCOUNT method (inject a call to player one's SHOWHANDCOUNT method)

        }
        
    }

    public void playHand(Player player) {

        String playerName = player.getName();
        Main.out(playerName + "'s turn.");
        displayCurrentPlayedCard();

        // Wrap this block in if player1 conditional:
        Main.out(playerName + "'s hand:");
        player.showCards();

        player.move(currentPlayedCard);

        // Discard the first card no matter what (for debug).
        int usersChoice = 0; // fake choice for debug.
        List<Card> playerCards = player.getHand().getAllCards();
        Card cardToDiscard = playerCards.get(usersChoice);
        player.discard(cardToDiscard);
        currentPlayedCard = cardToDiscard;
        if (currentPlayedCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
            if(player.isComputer()) {
            } else {
                Main.out("Ask Player1 for color choice.");
            }
        } else {
        }

        Main.out(playerName + " discards " + cardToDiscard.getPrintString() + ".");
        Main.out(playerName + " now holds " + player.getHand().getSize() + " cards.");
        Main.out("");

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
     * @return random true or false
     */
    public boolean getRandomBoolean() { // no test needed
        return Math.random() < 0.5; 
    }

    /**
     * Reshuffle deck and pop the first card until a non-wild and non-wild draw four card appears.
     *
     * @param card  The card to analyze
     * @return      A verified card
     */
    public Card verifyFirstCard(Card card) { // tested
        while(card.equals(new Card(Card.COLORLESS, Card.WILD, cvm))
                || card.equals(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm))) {
            deck = new Deck(null);
            deck.shuffle();
            card = deck.getNextCard();
        }
        return card;
    }

    /**
     * @return The game's deck.
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Transfer the discard pile to the deck and shuffle.
     */
    public void refreshDeckIfEmpty() { // NEEDS TO BE TESTED
        if(deck.getDeckSize() == 0) {
            deck = new Deck(discardPile);
            deck.shuffle();
            discardPile = new Stack<>();
            discardPile.push(currentPlayedCard);
        }
    }

    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

}




























