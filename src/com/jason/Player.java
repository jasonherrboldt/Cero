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
    
    public Player(String name, boolean isComputer) {
        this.name = name;      
        this.isComputer = isComputer;
        strategy = "";
    }

    public void move(Card currentCard, CardValueMap cvm) {
        if(isComputer()) {
            if(currentCard.isNumberCard(cvm)) {
                Main.out("Handing move for numeric card.");
            } else {
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
        } else {
            Main.out("Ask the user what to do.");
        }
    }
    
    public Hand getHand() {
        return this.hand;
    }
    
    public void setHand(List<Card> hand) { // tested
        this.hand = new Hand();
        for(Card c : hand) {
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
    
    public String getName() {
        return this.name;
    }
    
    public boolean isComputer() {
        return this.isComputer;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getStrategy() {
        return this.strategy;
    }

    public void showCards() {
        hand.showCards();
    }

    public String setCurrentColor() {
        return hand.getHighestColor();
    }
    
}
