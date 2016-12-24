package com.jason;

import java.util.*;

public class Hand {

    private List<Map<String, String>> hand;
    
    public Hand() {
        Map<String, String> map = new TreeMap<>();
        hand = new ArrayList<>();
        hand.add(map);
    }
    
    public void addCard(Card card) {
        Map<String, String> m = getColorMap(card);
        if(m != null) {
            m.put(card.getFace(), card.getColor());
        } else {
            Map<String, String> newMap = new TreeMap<>();
            newMap.put(card.getFace(), card.getColor());
            hand.add(newMap);
        }
    }

    public Map<String, String> getColorMap(Card card) {
        for(Map<String, String> m : hand) {
            if(m.containsValue(card.getColor())) {
                return m;
            }
        }
        return null;
    }

    public Card discard(String face, String color) {
        return findCard(face, color);
    }

    public Card findCard(String face, String color) {
        for(Map<String, String> m : hand) {
            if(m.containsKey(face) && m.containsValue(color)) {
                return new Card(color, face);
            }
        }
        return null;
    }
    
    public int getHandSize() {
        int count = 0;
        for(Map<String, String> m : hand) {
            count += m.size();
        }
        return count;
    }
    
    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        for(Map<String, String> m : hand) {
            for(Map.Entry<String, String> entry : m.entrySet()) {
                // Main.say("(" + entry.getValue() + ") " + entry.getKey());
                cards.add(new Card(entry.getKey(), entry.getValue()));
            }
        }
        return cards;
    }

    public boolean hasColor() {
        return true;
    }

    public boolean hasNumber() {
        return true;
    }
}



















