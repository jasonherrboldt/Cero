package com.jason;

import java.util.*;

public class Deck {

    @SuppressWarnings("rawtypes")
    Stack deck;
    public static final String RED = "Red";
    public static final String YELLOW = "Yellow";
    public static final String GREEN = "Green";
    public static final String BLUE = "Blue";
    
    public static final String SKIP = "Skip";
    public static final String REVERSE = "Reverse";
    public static final String DRAW_TWO = "Draw Two";
    public static final String WILD = "Wild";
    public static final String WILD_DRAW_FOUR = "Wild Draw Four";
    public static final String DRAW_FOUR = "Draw Four";

    /**
     * Public constructor.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Deck() {
        List<String> colors = new ArrayList<String>();
        deck = new Stack();
        colors.add(RED);
        colors.add(YELLOW);
        colors.add(GREEN);
        colors.add(BLUE);
        
        // Make 0 - 9 of all four colors.
        for(String c : colors) {
            for(int i = 0; i < 10; i++) {
                Card card = new Card(c, Integer.toString(i));
                deck.push(card);
            }   
        }
        
        // Make 1 - 9 of all four colors.
        for(String c : colors) {
            for(int i = 1; i < 10; i++) {
                Card card = new Card(c, Integer.toString(i));
                deck.push(card);
            }   
        }
        
        // Make skip, reverse and draw two cards.
        for(int i = 0; i < 2; i++) {
            for(String c : colors) {
                Card card = new Card(c, SKIP);
                deck.push(card);
                card = new Card(c, REVERSE);
                deck.push(card);
                card = new Card(c, DRAW_TWO);
                deck.push(card);
            }
        }
        
        // Make wild and draw four cards.
        for(String c : colors) {
            Card card = new Card (c, WILD_DRAW_FOUR);
            deck.push(card);
            card = new Card (c, DRAW_FOUR);
            deck.push(card);
        }
    }

    /**
     * Shuffle the deck.
     */
    public void shuffle() {
        Collections.shuffle(deck);
    }
    
    /**
     * Get the current deck size.
     * 
     * @return  The size of the deck.
     */
    public int getDeckSize() {
        return deck.size(); 
    }
    
    /**
     * Print the card faces to the console (for debug). 
     */
    public void printCards() {
        @SuppressWarnings("unchecked")
        Iterator<Card> iter = deck.iterator();
        while(iter.hasNext()) {
            Card c = iter.next();
            Main.say(c.getColor() + " " + c.getFace());
        }
    }
    
    /**
     * Pop a card off the top of the deck.
     * 
     * @return the card on the top of the deck. 
     */
    public Card getNextCard() {
        return (Card)deck.pop();
    }
    
    /**
     * Peek at the top card on the deck.
     * 
     * @return a copy of the top card on the deck. 
     */
    public Card peekAtTopCard() {
        return (Card)deck.peek();
    }
    
}
