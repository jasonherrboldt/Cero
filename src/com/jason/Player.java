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

    /**
     * Wouldn't it be cool if player0 could keep track of what strategy it used for each deck, and whether or not
     * it won that deck? It could then pick future strategies by whichever strategy produced the most wins.
     */
    
    public Player(String name, boolean isComputer) {
        this.name = name;      
        this.isComputer = isComputer;
        hand = new Hand();
        strategy = "";
    }
    
    public Hand getHand() {
        return this.hand;
    }
    
    public void setHand(List<Card> hand) {
        for(Card c : hand) {
            this.hand.addCard(c);
        }
    }
    
    public void draw(Card card) {
        // hand.add(card);
    }
    
    public void discard(Card card) {
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
    
}
