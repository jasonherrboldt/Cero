package com.jason;

import java.util.List;

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
