package com.jason;

import com.sun.istack.internal.Nullable;
import java.util.*;

/**
 * Hand class. A list of a list of cards, sorted descending by color group.
 * Allows player many insights into cards, allowing it to decide a move.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Hand {

    private List<List<Card>> hand;
    
    public Hand() {
        hand = new ArrayList<>();
    }

    public void addCard(Card card) { // tested
        if(card == null) {
            Main.out("WARN: addCard handed a null card.");
        }
        List<Card> colorList = getColorList(card.getColor());
        if(colorList != null) {
            colorList.add(card);
            // Sort the color group descending by face value.
            Collections.sort(colorList, (Card c1, Card c2) -> c1.getFace().compareTo(c2.getFace()));
        } else {
            List<Card> newList = new ArrayList<>();
            newList.add(card);
            hand.add(newList);
        }
        // Sort the hand descending by color group size (NOT by face value sum size).
        Collections.sort(hand, (List<Card> l1, List<Card> l2) -> l2.size() - l1.size());
    }

    /**
     * Get a list of cards of a specified color.
     *
     * @param color The specified color.
     * @return      The list of cards of the specified color.
     */
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

    /**
     * Discard a card.
     *
     * @param card                          The card to discard.
     * @throws IllegalArgumentException     If card not found in hand.
     */
    public void discard(Card card) throws IllegalArgumentException { // tested
        if(card == null) {
            Main.out("WARN: discard received a null card.");
        }
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

    /**
     * Check to see if a specified card is in the hand.
     *
     * @param card  The card to check.
     * @return      True if the card is present in the deck, false otherwise.
     */
    public boolean hasCard(Card card) throws IllegalArgumentException { // tested
        if(card != null) {
            boolean cardFound = false;
            List<Card> colorList = getColorList(card.getColor());
            for(Card c : colorList) {
                if(card.equals(c)) {
                    cardFound = true;
                }
            }
            return cardFound;
        } else {
            throw new IllegalArgumentException("ERROR: null card handed to hasCard.");
        }
    }

    /**
     * @return the number of cards in the deck.
     */
    public int getSize() { // tested in testHand_discard()
        int count = 0;
        for(List<Card> list : hand) {
            count += list.size();
        }
        return count;
    }

    /**
     * @return a flat list of all the cards in the deck.
     */
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
            if(colorList == null) {
                return null;
            }
            // Sort the color group descending by face value.
            Collections.sort(colorList, (Card c1, Card c2) -> c2.getValue() - (c1.getValue()));
            for(Card c : colorList) {
                if(c.getValue() <= highestValue) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Get number of largest available color group. Checks all color groups descending by color group size
     * and returns best choice.
     *
     * @param number    The number to search for.
     * @return          The card of the highest color group, or null if no card found.
     */
    @Nullable
    public Card getNumber(int number) { // tested
        // Hand is already sorted ascending by color group.
        for(List<Card> list : hand) {
            for(Card c : list) {
                if(c.getValue() == number) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * @return the sum of all numeric card values in a hand.
     */
    public int sumCards() { // tested
        int handSum = 0;
        for(List<Card> list : hand) {
            for(Card c : list) {
                handSum += c.getValue();
            }
        }
        return handSum;
    }

    /**
     * Print each card in hand to console.
     */
    public void showCards() { // don't need to test
        int i = 1;
        for(Card c : getAllCards()) {
            Main.out(i + ": " + c.getPrintString());
            i++;
        }
    }

    /**
     * @return the color of the highest color group.
     */
    @Nullable
    public String getHighestColor() {
        if(hand.size() > 0) {
            // Hand is already sorted ascending by color group.
            if(hand.get(0).size() > 0) {
                return hand.get(0).get(0).getColor();
            }
        }
        Main.out("WARN: getHighestColor did not find a card.");
        return null;
    }

    /**
     * @return the value of the hand.
     */
    public int getHandValue() { // tested
        int value = 0;
        for(Card c : getAllCards()) {
            value += c.getValue();
        }
        return value;
    }

}






















