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
            Collections.sort(colorList, (Card c1, Card c2) -> c1.getFace().compareTo(c2.getFace()));
        } else {
            List<Card> newList = new ArrayList<>();
            newList.add(card);
            hand.add(newList);
        }
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

    public void discard(Card card) throws IllegalArgumentException {
        if(hasCard(card)) {
            List<Card> colorList = getColorList(card.getColor());
            colorList.remove(card);
        } else {
            throw new IllegalArgumentException("Card not in hand.");
        }
    }

    public boolean hasCard(Card card) {
        boolean cardFound = false;
        List<Card> colorList = getColorList(card.getColor());
        for(Card c : colorList) {
            if(card.equals(c)) {
                cardFound = true;
            }
        }
        return cardFound;
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



















