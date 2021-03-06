package com.jason;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 *
 * A player. Has a hand, can manipulate that hand. Has a randomly-selected strategy if it is player 2 (computer).
 * (See README for Player Two strategy descriptions.)
 * Can play a card from its hand and react to previously played hand.
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

    /**
     * Public constructor.
     *
     * @param name       the player's name
     * @param isComputer if the player is the computer
     */
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
    public boolean isLegalDiscard(Card cardToDiscard, Card currentPlayedCard, String currentColor) {
        if(cardToDiscard == null || currentPlayedCard == null || currentColor == null) {
            throw new IllegalArgumentException("One or more args was null.");
        } else {

            // always allow player to discard a wild or a wild draw four card.
            if(cardToDiscard.getFace().equalsIgnoreCase(Card.WILD)
                    || cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                return true;
            }

            // if the LAST played card is a wild or a wild draw four, only need to match current color.
            // (current color can be set by either player, depending on which wild card was last played.)
            if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)
                    || currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                return cardToDiscard.getColor().equalsIgnoreCase(currentColor);
            } else {

                // the last card played was numeric or non-numeric color - card to discard must match
                // either color or face of last played card.
                return cardToDiscard.getFace().equalsIgnoreCase(currentPlayedCard.getFace())
                        || cardToDiscard.getColor().equalsIgnoreCase(currentPlayedCard.getColor());
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
     * Set the player's name.
     *
     * @param _name the name to set.
     */
    public void setName(String _name) {
        this.name = _name;
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
     * @return the player's score
     */
    int getScore() { // no test needed
        return this.score;
    }

    /**
     * Update the value of the other player's hand count.
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
     * Set a player's random strategy.
     *
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
     * Pick a card from the hand based on the bold strategy.
     *
     * @param currentPlayedCard the current played card
     * @param currentColor      the current chosen color
     * @return                  the preferred card, or null if no legal card found
     */
    public Card getBoldStrategyCard(Card currentPlayedCard, String currentColor) { // tested
        if(currentPlayedCard == null || currentColor == null) {
            throw new IllegalArgumentException("One or both args were null.");
        }
        String currentPlayedCardColor = "";
        if(currentPlayedCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
            currentPlayedCardColor = currentColor;
        } else {
            currentPlayedCardColor = currentPlayedCard.getColor();
        }
        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)
                || currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
            // return the highest non-numeric face of currentColor
            Card highestFace = hand.getHighestFace(currentColor, false); // tested
            if(highestFace != null) {
               return highestFace;
            } else {
                // don't go looking for a new card if a wild or wd4 is already present
                if(hand.hasCard(wild)) {
                    return wild;
                }
                if(hand.hasCard(wildDrawFour)) {
                    return wildDrawFour;
                }
            }
        } else {
            // Will play zero value cards to keep deck color in its favor before playing higher cards of the same color.
            int matchingColorGroupSize = hand.getColorGroupSize(currentColor);
            if(matchingColorGroupSize > 2) {
                CardValueMap cvm = new CardValueMap();
                Card zeroColorMatch = new Card(currentColor, Card.ZERO, cvm);
                if(hand.hasCard(zeroColorMatch)) {
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
                    return numberMatch; // tested for non-zero
                } else {
                    return colorMatch; // tested
                }
            }

            // if only one is non-null, return it.
            if(numberMatch != null || colorMatch != null) {
                if(numberMatch != null) {
                    return numberMatch; // tested for non-zero
                } else {
                    return colorMatch; // tested
                }
            }

            // 2nd approach is non-numeric color cards (draw two, rev, skip)

            // try to return a non-numeric card from the cpc color group
            Card highestNonNumericColorFace = hand.getHighestNonNumericFace(currentPlayedCardColor);
            if(highestNonNumericColorFace != null) {
                return highestNonNumericColorFace; // tested
            }

            // finally try returning a wild card if nothing else was found.

            // return a wild card if present
            if(hand.hasCard(wild)) {
                return wild; // tested
            }
            // return a wild draw four card if present
            if(hand.hasCard(wildDrawFour)) {
                return wildDrawFour; // tested
            }
        }

        // found nothing - return null and let Game.playerTwoMove draw a card from the deck
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
        String currentPlayedCardColor = "";
        if(currentPlayedCard.getColor().equalsIgnoreCase(Card.COLORLESS)) {
            currentPlayedCardColor = currentColor;
        } else {
            currentPlayedCardColor = currentPlayedCard.getColor();
        }

        if(currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)
                || currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
            // return the highest non-numeric face of currentColor
            Card highestFace = hand.getHighestFace(currentColor, false); // tested
            if(highestFace != null) {
                return highestFace;
            } else {
                // don't go looking for a new card if a wild or wd4 is already present
                if(hand.hasCard(wild)) {
                    return wild;
                }
                if(hand.hasCard(wildDrawFour)) {
                    return wildDrawFour;
                }
            }
        } else {

            // highest-value cards go first

            if(hand.hasCard(wild)) {
                return wild; // tested
            }

            if(hand.hasCard(wildDrawFour)) {
                return wildDrawFour; // tested
            }

            // 2nd highest-value cards go next - draw two, skip, reverse

            Card highestNonNumericFace = hand.getHighestNonNumericFace(currentPlayedCardColor);
            if(highestNonNumericFace != null) {
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
                    return numberMatch; // tested
                } else {
                    return colorMatch; // tested
                }
            }

            // if only one is non-null, return it.
            if(numberMatch != null || colorMatch != null) {
                if(numberMatch != null) {
                    return numberMatch; // tested
                } else {
                    return colorMatch; // tested
                }
            }
        }

        // found nothing - return null and let Game.playerTwoMove draw a card from the deck
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
            List<String> existingColors = getHand().getColors();
            Collections.shuffle(existingColors);
            if(existingColors.size() == 0) {
                // here the player has a wild or a WD4 as the last card and is asked to pick a color
                // any color will do, since the round is now effectively over
                return getHand().getRandomColor();
            }
            return(existingColors.get(0));
        } else { // must be bold or cautious
            return hand.getHighestColor(); // tested
        }
    }

    /**
     * @return true if hand contains at least three legally playable cards, false otherwise.
     */
    public boolean hasAtLeastTwoPlayableCards(Card currentPlayedCard, String currentColor) { // tested
        int numberOfPlayableCardsFound = 0;
        for(Card c : hand.getAllCards()) {
            if(isLegalDiscard(c, currentPlayedCard, currentColor)) {
                numberOfPlayableCardsFound++;
            }
        }
        return numberOfPlayableCardsFound >= 2;
    }

    /**
     * @return the player's last played card
     */
    public Card getLastPlayedCard() {
        return lastPlayedCard;
    }

    /**
     * Set the player's last played card.
     *
     * @param lastPlayedCard the last played card
     */
    public void setLastPlayedCard(Card lastPlayedCard) {
        this.lastPlayedCard = lastPlayedCard;
    }

    /**
     * @return the player's chosen color
     */
    public String getChosenColor() {
        return chosenColor;
    }

    /**
     * Set the player's chosen color
     *
     * @param chosenColor the chosen color
     */
    public void setChosenColor(String chosenColor) {
        this.chosenColor = chosenColor;
    }

    /**
     * Set the player's score (for testing)
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }
}