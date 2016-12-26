package com.jason;

import java.util.ArrayList;
import java.util.List;

public class Game {

    /*
     * TODO:
     *
     * 1) Add null checks everywhere you can. Use if/else or try/catch blocks,
     *    and have the exceptions ignore nulls and getPrintString warnings to the console.
     *
     *  2) Space out the console output for computer steps; make it look like the
     *     computer is thinking about what its doing, instead of just showing a dump
     *     of steps. Might be fun to have it complain if it has to draw more than three cards.
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
        // Hand hand = player.getHand();
        // List<Card> handCards = hand.getAllCards();
        // printCards(handCards);
        player.showCards();

        if(currentCard.isNumberCard(cvm)) {
            handleNumericCard();
        } else {
            handleNonNumericCard();
        }

        // Discard the first card no matter what for debug.
        /*
        int usersChoice = 0; // fake choice for debug.
        Card cardToDiscard = handCards.get(usersChoice); // I don't like this - should go through player object.
        player.discard(cardToDiscard);
        currentCard = cardToDiscard;
        currentColor = currentCard.getColor();
        Main.out(playerName + " discards " + getCardPrintString(cardToDiscard) + ".");
        Main.out(playerName + " now holds " + player.getHand().getSize() + " cards.");
        */
        Main.out("");

    }
    
    public void handleNonNumericCard() {
        if(currentCard.getFace().equalsIgnoreCase(Card.SKIP)) {
            Main.out("Handling move for " + Card.SKIP + ".");
        } else if (currentCard.getFace().equalsIgnoreCase(Card.REVERSE)) {
            Main.out("Handling move for " + Card.REVERSE + ".");
        } else if (currentCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
            Main.out("Handling move for " + Card.DRAW_TWO + ".");
        } else if (currentCard.getFace().equalsIgnoreCase(Card.WILD)) {
            Main.out("Handling move for " + Card.WILD + ".");
        } else { // WILD_DRAW_FOUR
            Main.out("Handling move for " + Card.WILD_DRAW_FOUR + ".");
        }
    }
    
    public void handleNumericCard() {
        Main.out("Handling move for numeric card.");
    }
    
    private void displayCurrentPlayedCard() {
        Main.out("The current played card is " + currentCard.getPrintString());
    }
    
    private void dealHands() {
        List<Player> players = new ArrayList<Player>();
        players.add(player2);
        players.add(player1);
        for(Player p : players) {
            List<Card> cards = new ArrayList<Card>();
            for(int i = 0; i < 7; i++) {
                cards.add(deck.getNextCard());
            }
            p.setHand(cards);
        }
    }
    
    public void setCurrentColor(String color) {
        this.currentColor = color;
    }
    
    public boolean pickRandomBoolean() {
        return Math.random() < 0.5; 
    }
        
}













