package com.jason;

import java.util.*;

public class Hand {

    // Consider making hand a list of TreeMaps.
    // Might require special handling for non-numeric cards, since all card values are strings.

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
        // Collections.sort(hand, (Card c1, Card c2) -> c1.getFace().toLowerCase().compareTo(c2.getFace().toLowerCase()));
        for(Card c : this.hand) {
            Main.say("(" + c.getColor() + ") " + c.getFace());
        }
    }
    
}
