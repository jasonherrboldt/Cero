package com.jason;

import com.sun.istack.internal.Nullable;

import java.util.*;

public class Hand {

    private List<List<Card>> hand;
    
    public Hand() {
        hand = new ArrayList<>();
    }

    public void addCard(Card card) { // tested
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
    public List<Card> getColorList(String color) { // tested
        for(List<Card> list : hand) {
            if(list.size() > 0) {
                if(list.get(0).getColor().equalsIgnoreCase(color)) {
                    return list;
                }
            }
        }
        return null;
    }

    public void discard(Card card) throws IllegalArgumentException { // tested
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

    public boolean hasCard(Card card) { // tested
        boolean cardFound = false;
        List<Card> colorList = getColorList(card.getColor());
        for(Card c : colorList) {
            if(card.equals(c)) {
                cardFound = true;
            }
        }
        return cardFound;
    }
    
    public int getSize() { // tested in testHand_discard()
        int count = 0;
        for(List<Card> list : hand) {
            count += list.size();
        }
        return count;
    }
    
    public List<Card> getAllCards() { // tested
        List<Card> cards = new ArrayList<>();
        for(List<Card> list : hand) {
            for(Card c : list) {
                cards.add(c);
            }
        }
        return cards;
    }

    /**
     * Get the highest value face card of a specified color.
     *
     * @param color     The color to search.
     * @param numeric   Whether or not the search should include non-numeric cards.
     * @return          The highest value card of the given color, or null if no card found.
     */
    @Nullable
    public Card getHighestFace(String color, boolean numeric) { // tested
        int highestValue = numeric ? 9 : 50;
        if(color != null) {
            List<Card> colorList = getColorList(color);
            Collections.sort(colorList, (Card c1, Card c2) -> c2.getValue() - (c1.getValue()));
            for(Card c : colorList) {
                if(c.getValue() <= highestValue) {
                    return c;
                }
            }
        }
        return null;
    }

    @Nullable
    public Card getNumberOfAnyColor(int number) {
        // List<Card> cards = getAllCards();
        List<List<Card>> cards = new ArrayList<>();
        // Collections.sort(colorList, (Card c1, Card c2) -> c1.getFace().compareTo(c2.getFace()));
        // do something like this:
        Collections.sort(cards, (List<Card> l1, List<Card> l2) -> l1.size() - l2.size());
        return null;
    }

    @Nullable
    public Card getWildOrWildDrawFour() {
        return null;
    }

    public int sumCards() { // tested
        int handSum = 0;
        for(List<Card> list : hand) {
            for(Card c : list) {
                handSum += c.getValue();
            }
        }
        return handSum;
    }
}



















