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
    public int otherPlayersHandCount;
    private boolean ceroCalled;
    public String chosenColor;

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
     * Discard a card.
     *
     * @param card                  The card to discard.
     * @param currentPlayedCard     The current played card.
     * @param callCero              Whether or not to declare 'Cero!'
     * @param playerOnesChosenColor Player one's chosen color (for wild and wild draw four cards)
     * @return                      True if the discarded card was deemed legal, false otherwise.
     */
    public boolean discard(Card card, Card currentPlayedCard, boolean callCero, String playerOnesChosenColor) { // *** NEEDS TESTING ***
        if(card == null || currentPlayedCard == null) {
            Main.out("WARN: Player.discard called with a null card, a null currentPlayedCard, or both. " +
                    "No action taken, returned false.");
            return false;
        } else {
            ceroCalled = callCero; // incumbent on the accuracy of Main.main
            if(card.getFace().equalsIgnoreCase(Card.WILD) || card.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                if(isPlayer2) {
                    chosenColor = getPlayerTwosChosenColor();
                } else {
                    if(playerOnesChosenColor != null) {
                        chosenColor = playerOnesChosenColor;
                    }
                }
                hand.discard(card);
                return true;
            } else {
                if(card.isNumberCard()) {
                    if(card.getColor().equalsIgnoreCase(currentPlayedCard.getColor()) ||
                            card.getFace().equalsIgnoreCase(currentPlayedCard.getFace())) {
                        hand.discard(card);
                        return true;
                    }
                } else { // guaranteed to be skip, reverse, or draw two.
                    if(card.getColor().equalsIgnoreCase(currentPlayedCard.getColor())) {
                        hand.discard(card);
                        return true;
                    }
                }
            }
            Main.out("WARN: Player.discard received an illegal discard choice. No action taken.");
            return false;
        }
    }

    /**
     * Allow either player to call 'Cero!' on the other player if the other player has not already done so after
     * discarding penultimate card.
     *
     * @param otherPlayer the player to inspect
     * @return            true if the other player incorrectly forgot to declare 'Cero plus one!', false otherwise.
     */
    public boolean otherPlayerIncorrectlyForgotToCallCero(Player otherPlayer) { // *** NEEDS TO BE TESTED ***
        if(isPlayer2()) {
            // dumb players never check
            if(strategy.equalsIgnoreCase(Player.STRATEGY_DUMB)) {
                return false;
            } else {
                // even bold and cautious players sometimes forget (NOT TESTABLE)
                if (otherPlayer.getHand().getSize() == 1 && !otherPlayer.ceroCalled && Main.getRandomBoolean()) {
                    return true;
                }
            }
        } else { // player1
            if (otherPlayer.getHand().getSize() == 1 && !otherPlayer.ceroCalled) {
                return true;
            }
        }
        return false;
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
     * @return true if player has called 'Cero!' on the other player, false otherwise.
     */
    boolean isCeroCalled() { // no test needed
        return this.ceroCalled;
    }

    /**
     * Reset this player's cero called value to false.
     */
    void setCeroCalled(boolean ceroCalled) { // no test needed
        this.ceroCalled = ceroCalled;
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

    public String getPlayerTwosChosenColor() { // *** DOUBLE CHECK TESTS ***
        if (!isPlayer2()) {
            Main.out("WARN: Player.getPlayerTwosChosenColor called for player 1. No action taken, returned null.");
            return null;
        } else {
            if (strategy.equalsIgnoreCase(Player.STRATEGY_DUMB)) {
                // return a random color
                List<String> colors = new ArrayList<>();
                colors.add(Card.BLUE);
                colors.add(Card.RED);
                colors.add(Card.YELLOW);
                colors.add(Card.GREEN);
                Collections.shuffle(colors);
                return colors.get(0);
            } else {
                return (hand.getHighestColor());
            }
        }
    }

    /**
     * Let the program decide the best card to discard based on its strategy.
     * Guaranteed by Game.playerTwosTurn not to be called unless the current played card
     * is numeric or wild.
     *
     * Returned card does not have to be exact card found in hand -- discard happens elsewhere.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the selected color of the last move
     * @return                  the card to discard, or null if no playable card found.
     */
    public Card decidePlayerTwoDiscard(Card currentPlayedCard, String currentColor) { // switch cases can be functionally tested

        // Debug:
        Main.out("Player Two is playing with a " + strategy + " strategy.");

        switch(strategy) {
            case Player.STRATEGY_BOLD:
                return getBoldStrategyCard(currentPlayedCard, currentColor);
            case Player.STRATEGY_CAUTIOUS:
                // another difference from bold: play the card with the higher face value - don't look for higher color group sizes
                // and of course, try to discard high non-numeric face cards first
                // todo
                return null;
            case Player.STRATEGY_DUMB:
                // todo
                return null;
        }
        return null;
    }

    /**
     * Pick a card from hand based on bold strategy.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the current chosen color
     * @return                  the preferred card, or null if no legal card found
     */
    public Card getBoldStrategyCard(Card currentPlayedCard, String currentColor) { // *** TESTING IN PROGRESS ***
        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
            // return the highest numeric face of currentColor
            return hand.getHighestFace(currentColor, true); // tested

        } else {
            // try to match the number
            Card card1 = hand.getNumberFromLargestColorGroup(currentPlayedCard.getValue());

            // try to match the color
            Card card2 = hand.getHighestFace(currentPlayedCard.getColor(), true);

            // compare the two, return the one with the highest card group
            if(card1 != null && card2 != null) {
                if(hand.getColorGroupSize(card1.getColor()) > hand.getColorGroupSize(card2.getColor())) {
                    return card1; // tested
                } else {
                    return card2; // tested
                }
            }
            if(card1 != null || card2 != null) {
                if(card1 != null) {
                    return card1; // tested
                } else {
                    return card2; // tested
                }
            }
            CardValueMap cvm = new CardValueMap();

            // try to return a non-numeric card from the cpc color group
            Card highestNonNumericFace = hand.getHighestFace(currentPlayedCard.getColor(), false);
            if(highestNonNumericFace != null) {
                return highestNonNumericFace; // tested
            }

            // return a wild card if present
            Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);
            if(hand.hasCard(wild)) {
                return wild; // tested
            }
            // return a wild draw four card if present
            Card wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
            if(hand.hasCard(wildDrawFour)) {
                return wildDrawFour; // tested
            }
        }
        return null;
    }
}

























