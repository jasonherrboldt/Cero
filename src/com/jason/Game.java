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
    private String currentColor;
    private static final int FAKE_GAME_RUNS = 8;
    private CardValueMap cvm;
    
    public Game(String userName) {

        cvm = new CardValueMap();
        deck = new Deck();
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
    }
    
    public void play() {
    
        // while(!gameWinnerExists) {
        
            deck.shuffle();
            // deck.printDeck(); // for debug
            
            dealHands();

            /*
             * Note: If the first card turned up from the Draw Pile (to form the Discard Pile) is an Action card,
             * the Action from that card applies and must be carried out. The exceptions are if the Wild or Wild
             * Draw Four cards are turned up, in which case – Return them to the Draw Pile, shuffle them, and turn
             * over a new card. - unorules.com
             */

            currentPlayedCard = deck.getNextCard();
            currentColor = currentPlayedCard.getColor();

            /*
             * At any time, if the Draw Pile becomes depleted and no one has yet won the round, take the Discard Pile,
             * shuffle it, and turn it over to regenerate a new Draw Pile. - unorules.com
             */
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

        player.move(currentPlayedCard);

        // Discard the first card no matter what (for debug).
        int usersChoice = 0; // fake choice for debug.
        List<Card> playerCards = player.getHand().getAllCards();
        Card cardToDiscard = playerCards.get(usersChoice);
        player.discard(cardToDiscard);
        currentPlayedCard = cardToDiscard;
        if (currentPlayedCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
            if(player.isComputer()) {
                // currentColor = player.setCurrentColor();
                if(currentColor == null) {
                    Main.out("WARN: Game just set current color to null");
                }
            } else {
                Main.out("Ask Player1 for color choice.");
            }
        } else {
            currentColor = currentPlayedCard.getColor();
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
        
}