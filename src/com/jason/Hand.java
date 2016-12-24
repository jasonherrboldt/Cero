package com.jason;

import java.util.*;

public class Hand {

    // Consider making hand a list of TreeMaps.
    // Might require special handling for non-numeric cards, since all card values are strings.

    private List<Card> hand;
    private List<Map<String, String>> handMap;
    
    public Hand() {
        hand = new ArrayList<>();
        Map<String, String> map = new TreeMap<String, String>();
        handMap = new ArrayList<>();
        handMap.add(map);
    }
    
    public void addCard(Card card) {
        hand.add(card);
        Map<String, String> m = findColorMap(card);
        if(m != null) {
            m.put(card.getFace(), card.getColor());
        } else {
            Map<String, String> newMap = new TreeMap<String, String>();
            newMap.put(card.getFace(), card.getColor());
            handMap.add(newMap);
        }
    }

    public Map<String, String> findColorMap(Card card) {
        if(handMap != null) {
            for(Map<String, String> m : handMap) {
                if(m.containsValue(card.getColor())) {
                    return m;
                }
            }
        }
        return null;
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
