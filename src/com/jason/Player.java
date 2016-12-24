package com.jason;

import java.util.List;

public class Player {

    private String name;
    private Hand hand;
    private boolean isComputer;
    
    public Player(String name, boolean isComputer) {
        this.name = name;      
        this.isComputer = isComputer;
        hand = new Hand();
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
    
    public Card discard(String face, String color) {
        // return hand.discard(face, color);
        return null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isComputer() {
        return this.isComputer;
    }
    
}
