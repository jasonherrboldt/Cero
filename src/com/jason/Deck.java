package com.jason;

import java.util.*;

public class Deck {

    /*

        From unorules.com:

        All number cards are the same value as the number on the card (e.g. a 9 is 9 points).
        “Draw Two” – 20 Points, “Reverse” – 20 Points, “Skip” – 20 Points, “Wild” – 50 Points,
        and “Wild Draw Four” – 50 Points. The first player to attain 500 points wins the game.
     */

    public static final int DECK_SIZE = 108;

    private Stack<Card> deck;

    public static final String RED = "Red";
    public static final String YELLOW = "Yellow";
    public static final String GREEN = "Green";
    public static final String BLUE = "Blue";
    public static final String COLORLESS = "Colorless";
    
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String FOUR = "4";
    public static final String FIVE = "5";
    public static final String SIX = "6";
    public static final String SEVEN = "7";
    public static final String EIGHT = "8";
    public static final String NINE = "9";

    public static final String SKIP = "Skip";
    public static final String REVERSE = "Reverse";
    public static final String DRAW_TWO = "Draw Two";
    public static final String WILD = "Wild";
    public static final String WILD_DRAW_FOUR = "Wild Draw Four";

    /**
     * Public constructor.
     */
    public Deck() {
        List<String> colors = new ArrayList<>();
        deck = new Stack<>();
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
            Card card = new Card (COLORLESS, WILD);
            deck.push(card);
            card = new Card (COLORLESS, WILD_DRAW_FOUR);
            deck.push(card);
        }
    }

    /**
     * Shuffle the deck.
     */
    public void shuffle() { // tested
        Collections.shuffle(deck);
    }
    
    /**
     * Get the current deck size.
     * 
     * @return  The size of the deck.
     */
    public int getDeckSize() { // tested
        return deck.size(); 
    }
    
    /**
     * Pop a card off the top of the deck.
     * 
     * @return the card on the top of the deck. 
     */
    public Card getNextCard() { // tested
        return (Card)deck.pop();
    }
    
    /**
     * Peek at the top card on the deck.
     * 
     * @return a copy of the top card on the deck. 
     */
    public Card peekAtTopCard() { // tested
        return (Card)deck.peek();
    }

    public Stack<Card> getDeck() { // no test necessary
        return deck;
    }

    public void printDeck() { // no test necessary
        Iterator<Card> iter = getDeck().iterator();
        while(iter.hasNext()) {
            Card card = iter.next();
            Main.out(card.getColor() + " " + card.getFace());
        }
    }
    
}