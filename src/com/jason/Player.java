package com.jason;

import java.util.List;

/**
 * A player. Has a hand, can manipulate that hand. Has a strategy.
 * Can play a card from its hand and react to previously played hand.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Player {

    private String name;
    private Hand hand;
    private boolean isComputer;
    public static final String STRATEGY_BOLD = "Bold";
    public static final String STRATEGY_CAUTIOUS = "Cautious";
    public static final String STRATEGY_NEUTRAL = "Neutral";
    private String strategy;
    private int score;
    private int otherPlayersHandCount;
    private boolean ceroCalled;

    public Player(String name, boolean isComputer) {
        this.name = name;
        this.isComputer = isComputer;
        strategy = "";
        score = 0;
        otherPlayersHandCount = 0;
        ceroCalled = false;
    }

    /**
     * Decide a move given the current played card.
     *
     * @param currentCard The current played card.
     */
    public Card move(Card currentCard) {
        if (currentCard == null) {
            Main.out("ERROR: null card passed to move. Cannot make any move.");
        } else {
            if (isComputer()) {
                if (currentCard.isNumberCard()) {
                    Main.out("Handing move for numeric card.");
                } else {
                    if (currentCard.getFace().equalsIgnoreCase(Card.SKIP)) {
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
                // Just discard the first card (for debug):
                Card firstCard = hand.getFirstCard();
                hand.discard(firstCard);
                return firstCard;
            } else {
                // Main.out("Ask the user what to do.");

                // Just discard the first card (for debug):
                Card firstCard = hand.getFirstCard();
                hand.discard(firstCard);
                return firstCard;
            }
        }
        return null;
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
     * Instruct the player to add a card to its hand.
     *
     * @param card The card to add.
     */
    public void draw(Card card) { // tested
        hand.addCard(card);
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
    public boolean isComputer() { // no test needed
        return this.isComputer;
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
    public void updateScore(int score) { // no test needed
        this.score += score;
    }

    /**
     * @return the player's score
     */
    public int getScore() { // no test needed
        return this.score;
    }

    /**
     * Update the value of the other player's hand cound.
     *
     * @param count the count to update.
     */
    public void updateOtherPlayersHandCount(int count) {
        otherPlayersHandCount = count;
    }

    /**
     * @return this player's hand size.
     */
    public int showHandCount() {
        return hand.getSize();
    }

    /**
     * Allow either player to call 'Cero!' on the other player if the other player has not already done so after
     * discarding penultimate card.
     */
    public void callCero() { // no test needed
        if (isComputer()) {
            if (hand.getSize() == 1 && Main.getRandomBoolean()) {
                ceroCalled = true;
                Main.out("Computer calls 'Cero!'");
            }
        } else {
            if(hand.getSize() == 1) {
                boolean validAnswerReceived = false;
                String answer = "";
                while(!validAnswerReceived) {
                    Main.outNoReturn("Would you like to declare 'Cero!' at this time? Please type 'y' or 'n': ");
                    answer = System.console().readLine();
                    if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
                        validAnswerReceived = true;
                    } else {
                        Main.out("Invalid answer received.");
                    }
                }
                if(answer.equalsIgnoreCase("y")) {
                    ceroCalled = true;
                    Main.out("Player one has just declared 'Cero!'.");
                }
            }
        }
    }

    /**
     * @return true if player has called 'Cero!' on the other player, false otherwise.
     */
    public boolean isCeroCalled() { // no test needed
        return this.ceroCalled;
    }

    /**
     * Reset this player's cero called value to false.
     */
    public void resetCeroCalled() { // no test needed
        ceroCalled = false;
    }
}