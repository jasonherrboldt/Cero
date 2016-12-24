package com.jason;

import java.util.*;

public class Hand {

    private List<Map<String, String>> handMap;
    
    public Hand() {
        Map<String, String> map = new TreeMap<>();
        handMap = new ArrayList<>();
        handMap.add(map);
    }
    
    public void addCard(Card card) {
        Map<String, String> m = findColorMap(card);
        if(m != null) {
            m.put(card.getFace(), card.getColor());
        } else {
            Map<String, String> newMap = new TreeMap<>();
            newMap.put(card.getFace(), card.getColor());
            handMap.add(newMap);
        }
    }

    public Map<String, String> findColorMap(Card card) {
        for(Map<String, String> m : handMap) {
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
        for(Map<String, String> m : handMap) {
            if(m.containsKey(face) && m.containsValue(color)) {
                return new Card(color, face);
            }
        }
        return null;
    }
    
    public int getHandSize() {
        int count = 0;
        for(Map<String, String> m : handMap) {
            count += m.size();
        }
        return count;
    }
    
    public void printHand() {
        for(Map<String, String> m : handMap) {
            for(Map.Entry<String, String> entry : m.entrySet()) {
                Main.say("(" + entry.getValue() + ") " + entry.getKey());
            }
        }
    }
    
}