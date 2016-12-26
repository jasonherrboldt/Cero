package com.jason;

import java.util.ArrayList;
import java.util.List;

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
     * 1) Add null checks everywhere you can. Use if/else or try/catch blocks,
     *    and have the exceptions ignore nulls and getPrintString warnings to the console.
     *
     *  2) Space out the console output for computer steps; make it look like the
     *     computer is thinking about what its doing, instead of just showing a dump
     *     of steps to the user. Might be fun to have it complain if it has to draw more
     *     than three cards.
     *
     *  3) Add javadocs to every method.
     *
     *  4) Make sure every method is public and tested.
     *
     *  5) Wouldn't it be cool if player2 could keep track of what strategy it used for each deck,
     *     and whether or not it won that deck? It could then pick future strategies by whichever
     *     strategy produced the most wins.
     *
     */
    
    private Deck deck;
    private Player player1; // the user
    private Player player2; // the computer
    private boolean gameWinnerExists;
    private boolean deckWinnerExists;
    private boolean isPlayerOnesTurn;
    private Card currentCard;
    private String currentColor;
    private int score;
    private static final int FAKE_GAME_RUNS = 8;
    private CardValueMap cvm;
    
    public Game(String userName) {

        cvm = new CardValueMap();
        deck = new Deck();
        Main.out("");
        player2 = new Player("Computer", true);
        if(pickRandomBoolean()) {
            player2.setStrategy(Player.STRATEGY_BOLD);
        } else {
            player2.setStrategy(Player.STRATEGY_CAUTIOUS);
        }
        player1 = new Player(userName, false);
        player1.setStrategy(Player.STRATEGY_NEUTRAL);
        gameWinnerExists = false;
        deckWinnerExists = false;
        isPlayerOnesTurn = pickRandomBoolean();
        score = 0;
    }
    
    public void play() {
    
        // while(!gameWinnerExists) {
        
            deck.shuffle();
            // deck.printDeck(); // for debug
            
            dealHands();

            currentCard = deck.getNextCard();
            currentColor = currentCard.getColor();
        
            // while(!deckWinnerExists) {
            for(int i = 0; i < FAKE_GAME_RUNS; i++) {
                if(isPlayerOnesTurn) {
                    playHand(player1);
                } else {
                    playHand(player2);
                    
                // update winner status
                // if(checkForWinner) {
                    // gameWinnerExists = true;
                }
                isPlayerOnesTurn = !isPlayerOnesTurn;
            }
        
            // }

        // }
        
    }
    
    public void playHand(Player player) {

        String playerName = player.getName();
        Main.out(playerName + "'s turn.");
        displayCurrentPlayedCard();

        // Wrap this block in if player1 conditional:
        Main.out(playerName + "'s hand:");
        player.showCards();

        player.move(currentCard);

        // Discard the first card no matter what (for debug).
        int usersChoice = 0; // fake choice for debug.
        List<Card> playerCards = player.getHand().getAllCards();
        Card cardToDiscard = playerCards.get(usersChoice);
        player.discard(cardToDiscard);
        currentCard = cardToDiscard;
        if (currentCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
            if(player.isComputer()) {
                currentColor = player.setCurrentColor();
                if(currentColor == null) {
                    Main.out("WARN: Game just set current color to null");
                }
            } else {
                Main.out("Ask Player1 for color choice.");
            }
        } else {
            currentColor = currentCard.getColor();
        }

        Main.out(playerName + " discards " + cardToDiscard.getPrintString() + ".");
        Main.out(playerName + " now holds " + player.getHand().getSize() + " cards.");
        Main.out("");

    }
    
    private void displayCurrentPlayedCard() {
        Main.out("The current played card is " + currentCard.getPrintString());
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
    public boolean pickRandomBoolean() {
        return Math.random() < 0.5; 
    }
        
}













