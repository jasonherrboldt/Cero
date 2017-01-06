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
 *
 *     CAUTIOUS: will discard non-numeric cards ASAP.
 *
 *     NEUTRAL: (for player1)
 *
 *     DUMB: will look blindly for first matching card in hand - will not try to switch the deck color to its favor.
 *             Randomly decides to play a non-numeric card if any are present.
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
    private String chosenColor;
    private Card lastPlayedCard;
    List<String> colors;

    // for strategy getters:
    private Card wild;
    private Card wildDrawFour;

    public Player(String name, boolean isComputer) {
        this.name = name;
        this.isPlayer2 = isComputer;
        strategy = "";
        score = 0;
        otherPlayersHandCount = 0;
        lastPlayedCard = null;

        // for strategy getters:
        CardValueMap cvm = new CardValueMap();
        wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);

        // for when a dumb player two needs to choose a random color
        colors = new ArrayList<>();
        colors.add(Card.BLUE);
        colors.add(Card.RED);
        colors.add(Card.YELLOW);
        colors.add(Card.GREEN);
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
     * @param cardToDiscard     The card to discard.
     * @param currentPlayedCard The current played card.
     * @return                  True if the discarded card was deemed legal, false otherwise.
     */
    public boolean isLegalDiscard(Card cardToDiscard, Card currentPlayedCard) { // tested
        if(cardToDiscard == null || currentPlayedCard == null) {
            throw new IllegalStateException("Player.discard called with a null card, a null currentPlayedCard, or both.");
        } else {
            if(cardToDiscard.getFace().equalsIgnoreCase(Card.WILD) || cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                return true;
            } else {
                if(cardToDiscard.isNumeric()) {
                    if(cardToDiscard.getColor().equalsIgnoreCase(currentPlayedCard.getColor()) ||
                            cardToDiscard.getFace().equalsIgnoreCase(currentPlayedCard.getFace())) {
                        return true;
                    } else {
                        Main.out("\nWARN: Player.discard decided that " + cardToDiscard.getPrintString() + " is not " +
                                "a valid discard. Returning false.");
                        return false; // illegal card choice
                    }
                } else { // guaranteed to be skip, reverse, or draw two.
                    if(cardToDiscard.getColor().equalsIgnoreCase(currentPlayedCard.getColor())) {
                        return true;
                    } else {
                        Main.out("\nWARN: Player.discard decided that " + cardToDiscard.getPrintString() + " is not " +
                                "a valid discard. Returning false.");
                        return false;
                    }
                }
            }
        }
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
     * @return true if a random strategy has been selected, false otherwise.
     */
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

    /**
     * Returns a chosen color based on the player's strategy. Bold and cautious strategies pick the color
     * with the most cards left; dumb picks a color completely at random.
     *
     * @return the chosen color
     */
    public String pickStrategyColor() { // tested
        if (!isPlayer2()) {
            Main.out("WARN: Player.pickStrategyColor called for player 1. No action taken, returned null.");
            return null;
        } else {
            if (strategy.equalsIgnoreCase(Player.STRATEGY_DUMB)) {
                // return a random color
                Collections.shuffle(colors);
                return colors.get(0);
            } else {
                return (hand.getHighestColor());
            }
        }
    }

    /**
     * Let the program decide the best card to discard based on its strategy.
     *
     * Returned card does not have to be exact card found in hand -- discard happens elsewhere.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the selected color of the last move
     * @return                  the card to discard, or null if no playable card found.
     */
    public Card decidePlayerTwoDiscard(Card currentPlayedCard, String currentColor) { // switch cases can be functionally tested

        if(currentPlayedCard.isNonNumericNonWild()) {
            throw new IllegalStateException("Player.decidePlayerTwoDiscard was called with a non-numeric, " +
                    "non-wild current played card.");
        }

        // Debug:
        Main.out("Player Two is playing with a " + strategy + " strategy.");

        switch(strategy) {
            case Player.STRATEGY_BOLD:
                return getBoldStrategyCard(currentPlayedCard, currentColor);
            case Player.STRATEGY_CAUTIOUS:
                // todo
                return null;
            case Player.STRATEGY_DUMB:
                // todo
                return null;
        }
        return null;
    }

    /**
     * Pick a card from hand based on the bold strategy.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the current chosen color
     * @return                  the preferred card, or null if no legal card found
     */
    public Card getBoldStrategyCard(Card currentPlayedCard, String currentColor) { // ******** NEEDS TESTING! ********
        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
            // return the highest numeric face of currentColor
            return hand.getHighestFace(currentColor, true);
        } else {
            // try to match the number
            Card card1 = hand.getNumberFromLargestColorGroup(currentPlayedCard.getValue());

            // try to match the color
            Card card2 = hand.getHighestFace(currentPlayedCard.getColor(), true);

            // compare the two, return the one with the highest card group
            if(card1 != null && card2 != null) {
                if(hand.getColorGroupSize(card1.getColor()) > hand.getColorGroupSize(card2.getColor())) {
                    return card1;
                } else {
                    return card2;
                }
            }
            if(card1 != null || card2 != null) {
                if(card1 != null) {
                    return card1;
                } else {
                    return card2;
                }
            }

            // try to return a non-numeric card from the cpc color group
            Card highestNonNumericFace = hand.getHighestFace(currentPlayedCard.getColor(), false);
            if(highestNonNumericFace != null) {
                return highestNonNumericFace;
            }

            // return a wild card if present
            if(hand.hasCard(wild)) {
                return wild;
            }
            // return a wild draw four card if present
            if(hand.hasCard(wildDrawFour)) {
                return wildDrawFour;
            }
        }
        // no legal card found
        return null;
    }

    /**
     * Pick a card from hand based on the cautious strategy.
     *
     * Differences from bold strategy:
     *
     *     try to discard high non-numeric face cards before anything else (either colorless or not)
     *
     *     if a cpc can be matched both by color and by number, play the card with the higher numeric value first -
     *     don't look for higher color group sizes
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the current chosen color
     * @return                  the preferred card, or null if no legal card found
     */
    public Card getCautiousStrategyCard(Card currentPlayedCard, String currentColor) { // tested
        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
            // return the highest non-numeric face of currentColor
            return hand.getHighestFace(currentColor, false); // tested
        } else {

            // highest-value cards go first
            if(hand.hasCard(wild)) {
                return wild; // tested
            }

            if(hand.hasCard(wildDrawFour)) {
                return wildDrawFour; // tested
            }

            // 2nd highest-value cards go next - draw two, skip, reverse
            Card highestNonNumericFace = hand.getHighestNonNumericFace(currentPlayedCard.getColor());
            if(highestNonNumericFace != null) {
                return highestNonNumericFace; // tested
            }

            // 3rd option is a numeric card

            // try to match the number
            Card numberMatch = hand.getNumberFromLargestColorGroup(currentPlayedCard.getValue());

            // try to match the color
            Card colorMatch = hand.getHighestFace(currentPlayedCard.getColor(), true);

            // compare the two, return the one with the highest value
            if(numberMatch != null && colorMatch != null) {
                if(numberMatch.getValue() > colorMatch.getValue()) {
                    return numberMatch; // tested
                } else {
                    return colorMatch; // tested
                }
            }
            if(numberMatch != null || colorMatch != null) {
                if(numberMatch != null) {
                    return numberMatch; // tested
                } else {
                    return colorMatch; // tested
                }
            }
        }
        return null; // tested
    }

    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

    public void setLastPlayedCard(Card lastPlayedCard) {
        this.lastPlayedCard = lastPlayedCard;
    }


    public String getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(String chosenColor) {
        this.chosenColor = chosenColor;
    }

}
