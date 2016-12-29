package com.jason;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by jasonherrboldt on 12/24/16.
 *
 * A player. Has a hand, can manipulate that hand. Has a randomly-selected strategy.
 * Can play a card from its hand and react to previously played hand.
 *
 * Strategy descriptions for player2 (computer):
 *
 *     BOLD: Will play zero value cards to keep deck color in its favor before playing higher cards of the same color.
 *           Will switch to CAUTIOUS if player1 has < 4 cards. Will switch back to BOLD if player1 has > 3 cards.
 *     CAUTIOUS: will discard non-numeric cards ASAP.
 *     NEUTRAL: (for player1)
 *     DUMB: will look blindly for first matching card in hand - don't try to switch the deck color to your favor.
 *             Decide randomly to play a non-numeric card (if any are present).
 *
 *    "A defensive strategy would advise playing a high card in order to reduce the point value of the hand.
 *    However, an offensive strategy would suggest playing a 0 when the player wants to continue on the current
 *    color, because it is less likely to be matched by another 0 of a different color (there is only one 0 of
 *    each color, but two of each 1–9)." - uno wikipedia page
 */
public class Player {

    private String name;
    private Hand hand;
    private boolean isPlayer2;
    public static final String STRATEGY_BOLD = "Bold";
    public static final String STRATEGY_CAUTIOUS = "Cautious";
    public static final String STRATEGY_NEUTRAL = "Neutral";
    public static final String STRATEGY_DUMB = "Dumb";
    private String strategy;
    private int score;
    private int otherPlayersHandCount;
    private boolean ceroCalled;
    private String chosenColor;

    public Player(String name, boolean isComputer) {
        this.name = name;
        this.isPlayer2 = isComputer;
        strategy = "";
        score = 0;
        otherPlayersHandCount = 0;
        ceroCalled = false;
    }

    /**
     * @return the player's current hand.
     */
    public Hand getHand() { // no test necessary.
        return this.hand;
    }

    /**
     * Set the current player's hand with a given list of cards.
     *
     * @param hand The hand to set.
     */
    public void setHand(List<Card> hand) { // tested
        this.hand = new Hand();
        for (Card c : hand) {
            this.hand.addCard(c);
        }
    }

    /**
     * Instruct the player to discard a card.
     *
     * @param card The card to discard.
     */
    public void discard(Card card) { // tested
        hand.discard(card);
    }

    /**
     * @return the player's name
     */
    public String getName() { // no test needed
        return this.name;
    }

    /**
     * @return whether the current player is the computer.
     */
    public boolean isPlayer2() { // no test needed
        return this.isPlayer2;
    }

    /**
     * Set the player's strategy. Can be cautious or bold.
     *
     * @param strategy The strategy to set.
     */
    public void setStrategy(String strategy) { // no test needed
        this.strategy = strategy;
    }

    /**
     * @return the player's strategy.
     */
    public String getStrategy() { // no test needed
        return this.strategy;
    }

    /**
     * Display the current hand, allowing user to remote-control player 1.
     */
    public void showCards() { // no test needed
        hand.showCards();
    }

    /**
     * Update the player's score
     *
     * @param score The score to update.
     */
    void updateScore(int score) { // no test needed
        this.score += score;
    }

    /**
     * @return the player's score
     */
    int getScore() { // no test needed
        return this.score;
    }

    /**
     * Update the value of the other player's hand cound.
     *
     * @param count the count to update.
     */
    void updateOtherPlayersHandCount(int count) {
        otherPlayersHandCount = count;
    }

    /**
     * @return this player's hand size.
     */
    int showHandCount() {
        return hand.getSize();
    }

    /**
     * Allow either player to call 'Cero!' on the other player if the other player has not already done so after
     * discarding penultimate card.
     */
    void callCero() { // no test needed
        Main.out("\n(This is where the player will be given the chance to call 'Cero!'.)\n");
        if (isPlayer2()) {
            if (hand.getSize() == 1 && Main.getRandomBoolean()) {
                ceroCalled = true;
                Main.out("Computer calls 'Cero!'");
            }
//        } else { // un-comment out this block to make the game interactive.
//            if(hand.getSize() == 1) {
//                boolean validAnswerReceived = false;
//                boolean answer = Main.askUserYesOrNoQuestion("Would you like to declare 'Cero!' at this time?");
//                if(answer) {
//                    ceroCalled = true;
//                    Main.out("Player one has just declared 'Cero!'.");
//                }
//            }
        }
    }

    /**
     * @return true if player has called 'Cero!' on the other player, false otherwise.
     */
    boolean isCeroCalled() { // no test needed
        return this.ceroCalled;
    }

    /**
     * Reset this player's cero called value to false.
     */
    void resetCeroCalled() { // no test needed
        ceroCalled = false;
    }

    public boolean setRandomStrategy() { // tested
        int randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        switch(randomNum) {
            case 1:
                strategy = STRATEGY_BOLD;
                return true;
            case 2:
                strategy = STRATEGY_CAUTIOUS;
                return true;
            case 3:
                strategy = STRATEGY_DUMB;
                return true;
        }
        return false;
    }

    public String getPreferredColor() { // tested
        if(isPlayer2()) {
            if(strategy.equalsIgnoreCase(Player.STRATEGY_DUMB)) {
                // return a random number
                List<String> colors = new ArrayList<>();
                colors.add(Card.BLUE);
                colors.add(Card.RED);
                colors.add(Card.YELLOW);
                colors.add(Card.GREEN);
                Collections.shuffle(colors);
                return colors.get(0);
            } else {
                return(hand.getHighestColor());
            }
        } else {
            // ask the user to provide preferred color
            // for now just return blue
            return Card.BLUE;
        }
    }

    public String getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(String chosenColor) {
        this.chosenColor = chosenColor;
    }

    public int getOtherPlayersHandCount() {
        return otherPlayersHandCount;
    }
}