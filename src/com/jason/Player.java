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
 *     BOLD:
 *           Saves non-numeric cards for when it gets backed into a corner.
 *           1st choice is a numeric card, 2nd choice is a non-numeric color, 3rd choice is a wild or wd4.
 *           If a current played card can be matched by color and number from p2's hand, p2 will play the card from
 *               the larger card group.
 *           Will play zero value cards to keep deck color in its favor before playing higher cards of the same color.
 *           Will switch to CAUTIOUS if player1 has < 4 cards. Will switch back to BOLD if player1 has > 3 cards.
 *
 *     CAUTIOUS:
 *           Discards non-numeric cards ASAP to prevent giving player one a higher score if p2 loses.
 *           If a current played card can be matched by color and number from p2's hand, p2 will play the card with
 *               the higher value.
 *           1st choice is a wild or wd4, 2nd choice is a non-numeric color, 3rd choice is numeric.
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
    public boolean isLegalDiscard(Card cardToDiscard, Card currentPlayedCard, String currentColor) { // *** NEEDS RE-TESTING ***
        // Main.out("oh hai from Player.isLegalDiscard.");
        // Main.out("cardToDiscard: " + cardToDiscard.getPrintString() + ", currentPlayedCard: " + currentPlayedCard.getPrintString()
               //  + ", currentColor:" + currentColor);
        if(cardToDiscard == null || currentPlayedCard == null || currentColor == null) {
            throw new IllegalArgumentException("One or more args was null.");
        } else {

            // if the other player discarded a wild, then cardToDiscard must match the currentColor
            // (the other player's chosen color)
//            if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
//                // Main.out("oh hai from conditional if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)).");
////                Main.out("Player.isLegalDiscard has decided that the cpc is wild, and to return true if " +
////                        "cardToDiscard.getColor().equalsIgnoreCase(currentColor), which evaluates to "
////                        + cardToDiscard.getColor().equalsIgnoreCase(currentColor));
//                return cardToDiscard.getColor().equalsIgnoreCase(currentColor);
//
//            // if this player discarded a WD4 last time, this player only has to match its own chosen color
//            } else if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
//                // Main.out("oh hai from conditional else if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)).");
//                return cardToDiscard.getColor().equalsIgnoreCase(chosenColor);

//            if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)
//                    || currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
//                return true;
//            } else {

//                Main.out("oh hai from conditional } else {.");
//
//                String cardToDiscardColor = cardToDiscard.getColor();
//                String currentPlayedCardColor = currentPlayedCard.getColor();
//                String cardToDiscardFace = cardToDiscard.getFace();
//                String currentPlayedCardFace = currentPlayedCard.getFace();
//
//                Main.out("cardToDiscardColor: " + cardToDiscardColor);
//                Main.out("cardToDiscardFace: " + cardToDiscardFace);
//                Main.out("currentPlayedCardColor: " + currentPlayedCardColor);
//                Main.out("currentPlayedCardFace: " + currentPlayedCardFace);
//
//                Main.out("cardToDiscard.getFace().equalsIgnoreCase(Card.WILD) = "
//                        + cardToDiscard.getFace().equalsIgnoreCase(Card.WILD));
//                Main.out("cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR) = "
//                        + cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR));
//                Main.out("cardToDiscard.getColor().equalsIgnoreCase(currentPlayedCard.getColor() = "
//                        + cardToDiscard.getColor().equalsIgnoreCase(currentPlayedCard.getColor()));
//                Main.out("cardToDiscard.getFace().equalsIgnoreCase(currentPlayedCard.getFace() = "
//                        + cardToDiscard.getFace().equalsIgnoreCase(currentPlayedCard.getFace()));


                // currentPlayedCard will be any card other than a wild or a wd4
                // any of the below will be legal
                return cardToDiscard.getFace().equalsIgnoreCase(Card.WILD)
                        || cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)
                        || cardToDiscard.getColor().equalsIgnoreCase(currentPlayedCard.getColor())
                        || cardToDiscard.getFace().equalsIgnoreCase(currentPlayedCard.getFace());
            // }
        }
        // throw new IllegalStateException("Logic fell through all conditionals.");
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
            throw new IllegalStateException("Player.pickStrategyColor called for player 1.");
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
        switch(strategy) {
            case Player.STRATEGY_BOLD:
                return getBoldStrategyCard(currentPlayedCard, currentColor);
            case Player.STRATEGY_CAUTIOUS:
                return getCautiousStrategyCard(currentPlayedCard, currentColor);
            case Player.STRATEGY_DUMB:
                return getDumbStrategyCard(currentPlayedCard, currentColor);
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
    public Card getBoldStrategyCard(Card currentPlayedCard, String currentColor) { // tested
        if(currentPlayedCard == null || currentColor == null) {
            throw new IllegalArgumentException("One or both args were null.");
        }
//        Main.out("oh hai from Player.getBoldStrategyCard. currentPlayedCard is "
//                + currentPlayedCard.getPrintString() + ", and currentColor is "+ currentColor);
        String currentPlayedCardColor = "";
        if(currentPlayedCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
//            Main.out("oh hai from Player.getBoldStrategyCard. currentPlayedCard.getColor() = "
//                    + currentPlayedCard.getColor());
            currentPlayedCardColor = currentColor;
            // Main.out("currentPlayedCardColor is now " + currentPlayedCardColor);
        } else {
            currentPlayedCardColor = currentPlayedCard.getColor();
        }
        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
            // return the highest numeric face of currentColor
            // Card returnCard = hand.getHighestFace(currentColor, true);
            // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card " +
                    // "hand.getHighestFace(currentColor, true): " + returnCard.getPrintString());
            return hand.getHighestFace(currentColor, true); // tested
        } else {

            // Will play zero value cards to keep deck color in its favor before playing higher cards of the same color.
            int matchingColorGroupSize = hand.getColorGroupSize(currentColor);
            if(matchingColorGroupSize > 2) {
                CardValueMap cvm = new CardValueMap();
                Card zeroColorMatch = new Card(currentColor, Card.ZERO, cvm);
                if(hand.hasCard(zeroColorMatch)) {
                    // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card zeroColorMatch: " + zeroColorMatch.getPrintString());
                    return zeroColorMatch; // tested
                }
            }

            // numeric cards go first

            // try to match the number
            Card numberMatch = hand.getNumberFromLargestColorGroup(currentPlayedCard.getValue(), currentPlayedCard);

            // try to match the color
            Card colorMatch = hand.getHighestFace(currentPlayedCardColor, true);

            // compare the two, return the one with the highest card group
            if(numberMatch != null && colorMatch != null) {
                if(hand.getColorGroupSize(numberMatch.getColor()) > hand.getColorGroupSize(colorMatch.getColor())) {
                    // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card numberMatch (1):" + numberMatch.getPrintString());
                    return numberMatch; // tested for non-zero
                } else {
                    // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card colorMatch (1): " + colorMatch.getPrintString());
                    return colorMatch; // tested
                }
            }

            // if only one is non-null, return it.
            if(numberMatch != null || colorMatch != null) {
                if(numberMatch != null) {
                    // Will play zero value cards to keep deck color in its favor before playing higher cards of the same color.
                    // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card numberMatch (1): " + numberMatch.getPrintString());
                    return numberMatch; // tested for non-zero
                } else {
                    // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card colorMatch (1): " + colorMatch.getPrintString());
                    return colorMatch; // tested
                }
            }

            // 2nd approach is non-numeric color cards (draw two, rev, skip)

            // try to return a non-numeric card from the cpc color group
            Card highestNonNumericColorFace = hand.getHighestNonNumericFace(currentPlayedCardColor);
            if(highestNonNumericColorFace != null) {
                // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card highestNonNumericColorFace: "
                        // + highestNonNumericColorFace.getPrintString());
                return highestNonNumericColorFace; // tested
            }

            // finally try returning a wild card if nothing else was found.

            // return a wild card if present
            if(hand.hasCard(wild)) {
                // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card wild: " + wild.getPrintString());
                return wild; // tested
            }
            // return a wild draw four card if present
            if(hand.hasCard(wildDrawFour)) {
                // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card wildDrawFour: " + wildDrawFour.getPrintString());
                return wildDrawFour; // tested
            }
        }

        // found nothing - return null and let Game.playerTwoMove draw a card from the deck
        // Main.out("oh hai from Player.getCautiousStrategyCard. About to return null.");
        return null; // tested
    }

    /**
     * Pick a card from hand based on the cautious strategy.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the current chosen color
     * @return                  the preferred card, or null if no legal card found
     */
    public Card getCautiousStrategyCard(Card currentPlayedCard, String currentColor) { // tested
        if(currentPlayedCard == null || currentColor == null) {
            throw new IllegalArgumentException("One or both args were null.");
        }
//        Main.out("oh hai from Player.getCautiousStrategyCard. currentPlayedCard is "
//                + currentPlayedCard.getPrintString() + ", and currentColor is "+ currentColor);
        String currentPlayedCardColor = "";
        if(currentPlayedCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
//            Main.out("oh hai from Player.getCautiousStrategyCard. currentPlayedCard.getColor() = "
//                    + currentPlayedCard.getColor());
            currentPlayedCardColor = currentColor;
            // Main.out("currentPlayedCardColor is now " + currentPlayedCardColor);
        } else {
            currentPlayedCardColor = currentPlayedCard.getColor();
        }

        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
            // return the highest non-numeric face of currentColor
            // Card returnCard = hand.getHighestFace(currentColor, false);
            // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card hand.getHighestFace(currentColor, false): "
                    // + returnCard.getPrintString());
            return hand.getHighestFace(currentColor, false); // tested
        } else {

            // highest-value cards go first

            if(hand.hasCard(wild)) {
//                Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card wild: "
//                        + wild.getPrintString());
                return wild; // tested
            }

            if(hand.hasCard(wildDrawFour)) {
//                Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card wildDrawFour: "
//                        + wildDrawFour.getPrintString());
                return wildDrawFour; // tested
            }

            // 2nd highest-value cards go next - draw two, skip, reverse

            Card highestNonNumericFace = hand.getHighestNonNumericFace(currentPlayedCardColor);
            if(highestNonNumericFace != null) {
//                Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card highestNonNumericColorFace: "
//                        + highestNonNumericFace.getPrintString());
                return highestNonNumericFace; // tested
            }

            // 3rd option is a numeric card

            // try to match the number
            Card numberMatch = hand.getNumberFromLargestColorGroup(currentPlayedCard.getValue(), currentPlayedCard);

            // try to match the color
            Card colorMatch = hand.getHighestFace(currentPlayedCardColor, true);

            // compare the two, return the one with the higher value
            if(numberMatch != null && colorMatch != null) {
                if(numberMatch.getValue() > colorMatch.getValue()) {
                    // Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card numberMatch (1): "
                           // + numberMatch.getPrintString());
                    return numberMatch; // tested
                } else {
//                    Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card colorMatch (1): "
//                            + colorMatch.getPrintString());
                    return colorMatch; // tested
                }
            }

            // if only one is non-null, return it.
            if(numberMatch != null || colorMatch != null) {
                if(numberMatch != null) {
//                    Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card numberMatch (2): "
//                            + numberMatch.getPrintString());
                    return numberMatch; // tested
                } else {
//                    Main.out("oh hai from Player.getCautiousStrategyCard. About to return the card colorMatch (2): "
//                            + colorMatch.getPrintString());
                    return colorMatch; // tested
                }
            }
        }

        // found nothing - return null and let Game.playerTwoMove draw a card from the deck
        // Main.out("oh hai from Player.getCautiousStrategyCard. About to return null.");
        return null; // tested
    }

    /**
     * Pick a card from hand based on the dumb strategy.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the current chosen color
     * @return                  the preferred card, or null if no legal card found
     */
    public Card getDumbStrategyCard(Card currentPlayedCard, String currentColor) { // tested
        if(currentPlayedCard == null || currentColor == null) {
            throw new IllegalArgumentException("One or both args were null.");
        }

        // shuffle hand
        List<Card> shuffledHand = hand.getAllCards();
        Collections.shuffle(shuffledHand);

        // step through shuffled hand one card at a time and return the first legal card found
        for(Card c : shuffledHand) {
            if(isLegalDiscard(c, currentPlayedCard, currentColor)) {
                // Main.out("oh hai from Player.getDumbStrategyCard. isLegalDiscard just returned true.");
                return c; // tested
            }
        }
        return null; // tested
    }

    /**
     * @return the next current color for wild and wd2 discards
     */
    public String selectNewColor() { // tested
        if(!isPlayer2()) {
            throw new IllegalStateException("Can only be called by player 2.");
        }
        if(!lastPlayedCard.isColorlessCard()) {
            throw new IllegalStateException("Can only be called when player 2 discards a wild or wd4.");
        }

        // pick a random color if dumb, otherwise pick the color of the largest color group
        if(strategy.equalsIgnoreCase(Player.STRATEGY_DUMB)) {
            Collections.shuffle(colors);
            return colors.get(0); // tested
        } else { // must be bold or cautious
            return hand.getHighestColor(); // tested
        }
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