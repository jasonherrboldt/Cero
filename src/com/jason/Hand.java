package com.jason;

import com.sun.istack.internal.Nullable;

import java.util.*;

public class Hand {

    private List<List<Card>> hand;
    
    public Hand() {
        hand = new ArrayList<>();
    }

    // tested
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

    // tested
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

    // tested
    public void discard(Card card) throws IllegalArgumentException {
        if (!hasCard(card)) {
            throw new IllegalArgumentException("Card not in hand.");
        } else {
            List<Card> colorList = getColorList(card.getColor());
            int index = -1;
            int i = 0;
            for(Card c : colorList) {
                if(c.equals(card)) {
                    index = i;
                }
            }
            if(index == -1 || index > colorList.size() - 1) {
                throw new IllegalArgumentException("Card not in hand.");
            } else {
                colorList.remove(index);
            }
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



















