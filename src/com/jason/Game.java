package com.jason;

import java.util.ArrayList;
import java.util.List;

public class Game {

    // Add null checks everywhere you can. Use if/else or try/catch blocks,
    // and have the exceptions ignore nulls and print warnings to the console.
    
    private Deck deck;
    private Player player0; // the computer
    private Player player1; // the user
    private boolean gameWinnerExists;
    private boolean deckWinnerExists;
    private boolean isPlayerOnesTurn;
    private Card currentCard;
    private String currentColor;
    private int score;
    private static final int FAKE_GAME_RUNS = 8;
    
    public Game(String userName) {
        this.deck = new Deck();
        deck.printDeck();
        Main.out("");
        this.player0 = new Player("Computer", true);
        this.player1 = new Player(userName, false);
        this.gameWinnerExists = false;
        this.deckWinnerExists = false;
        this.isPlayerOnesTurn = pickRandomPlayer();
        this.score = 0;
    }
    
    public void play() {
    
        // while(!gameWinnerExists) {
        
            deck.shuffle();
            
            dealHands();
            
            this.currentCard = deck.getNextCard();
            this.currentColor = this.currentCard.getColor(); 
        
            // while(!deckWinnerExists) {
            for(int i = 0; i < FAKE_GAME_RUNS; i++) {
                if(isPlayerOnesTurn) {
                    playHand(player1);
                } else {
                    playHand(player0);
                    
                // update winner status
                // if(checkForWinner) {
                    // gameWinnerExists = true;
                }
                isPlayerOnesTurn = !isPlayerOnesTurn;
                boolean x = isPlayerOnesTurn;
            }
        
            // }

        // }
        
    }
    
    public void playHand(Player player) {
        String playerName = player.getName();
        Main.out(playerName + "'s turn.");
        displayCurrentPlayedCard();

        Main.out(playerName + "'s hand:");
        // player.getHand().printHand();
        Hand hand = player.getHand();
        List<Card> handCards = hand.getAllCards();
        printCards(handCards);
        
        if(isNumberCard(this.currentCard)) {
            handleNumericCard();
        } else {
            handleNonNumericCard();
        }

        // Discard the first card no matter what for debug.
        int usersChoice = 0; // fake choice for debug.
        Card cardToDiscard = handCards.get(usersChoice);
        player.discard(cardToDiscard);
        currentCard = cardToDiscard;
        currentColor = currentCard.getColor();
        Main.out(playerName + " discards " + getCardPrintString(cardToDiscard) + ".");
        Main.out(playerName + " now holds " + player.getHand().getSize() + " cards.");
        Main.out("");
    }

    private void printCards(List<Card> cards) {
        int i = 1;
        for(Card c : cards) {
            Main.out(i + ": " + getCardPrintString(c));
            i++;
        }
    }
    
    public boolean isNumberCard(Card card) {
        String face = card.getFace();
        return !(face.equalsIgnoreCase(Deck.SKIP)
                || face.equalsIgnoreCase(Deck.REVERSE)
                || face.equalsIgnoreCase(Deck.DRAW_TWO)
                || face.equalsIgnoreCase(Deck.WILD)
                || face.equalsIgnoreCase(Deck.WILD_DRAW_FOUR));
    }
    
    public void handleNonNumericCard() {
        if(currentCard.getFace().equalsIgnoreCase(Deck.SKIP)) {
            Main.out("Handling move for " + Deck.SKIP + ".");
        } else if (currentCard.getFace().equalsIgnoreCase(Deck.REVERSE)) {
            Main.out("Handling move for " + Deck.REVERSE + ".");
        } else if (currentCard.getFace().equalsIgnoreCase(Deck.DRAW_TWO)) {
            Main.out("Handling move for " + Deck.DRAW_TWO + ".");
        } else if (currentCard.getFace().equalsIgnoreCase(Deck.WILD)) {
            Main.out("Handling move for " + Deck.WILD + ".");
        } else { // WILD_DRAW_FOUR
            Main.out("Handling move for " + Deck.WILD_DRAW_FOUR + ".");
        }
    }
    
    public void handleNumericCard() {
        Main.out("Handling move for numeric card.");
    }
    
    private void displayCurrentPlayedCard() {
        Main.out("The current played card is " + getCardPrintString(currentCard));
    }

    private String getCardPrintString(Card card) {
        return "(" + card.getColor() + ") " + card.getFace();
    }
    
    private void dealHands() {
        List<Player> players = new ArrayList<Player>();
        players.add(player0);
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
    
    public boolean pickRandomPlayer() {
        return Math.random() < 0.5; 
    }
        
}













