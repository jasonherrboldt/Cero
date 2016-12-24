package com.jason;

import com.sun.istack.internal.Nullable;

import java.util.*;

public class Hand {

    private List<List<Card>> hand;
    
    public Hand() {
        hand = new ArrayList<>();
    }
    
    public void addCard(Card card) {
        List<Card> colorList = getColorList(card.getColor());
        if(colorList != null) {
            colorList.add(card);
            sortColorList(colorList);
        } else {
            List<Card> newList = new ArrayList<>();
            newList.add(card);
            hand.add(newList);
        }
    }

    public void sortColorList(List<Card> list) {
        Collections.sort(list, (Card c1, Card c2) -> c1.getFace().compareTo(c2.getFace()));

    }

    @Nullable
    public List<Card> getColorList(String color) {
        for(List<Card> list : hand) {
            if(list.size() > 0) {
                if(list.get(0).getColor().equalsIgnoreCase(color)) {
                    return list;
                }
            }
        }
        return null;
    }

    public Card discard(Card card) {
        return null;
    }
    
    public int getSize() {
        int count = 0;
        for(List<Card> list : hand) {
            count += list.size();
        }
        return count;
    }
    
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        for(List<Card> list : hand) {
            for(Card c : list) {
                cards.add(c);
            }
        }
        return cards;
    }

    @Nullable
    public Card getHighestFaceOfColor(String color) {
        return null;
    }

    @Nullable
    public Card getNumberOfAnyColor(String number) {
        return null;
    }
}



















