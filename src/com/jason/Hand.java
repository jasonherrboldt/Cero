package com.jason;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Card> hand;
    
    public Hand() {
        hand = new ArrayList<Card>();
    }
    
    public void addCard(Card card) {
        hand.add(card);
    }
    
    public Card discard(int cardPosition) {
        Card card = hand.get(cardPosition);
        hand.remove(cardPosition);
        return card;
    }
    
    public int getCardCount() {
        return hand.size();
    }
    
    public void printHand() {
        for(Card c : this.hand) {
            Main.say("(" + c.getColor() + ") " + c.getFace());
        }
    }
    
}
