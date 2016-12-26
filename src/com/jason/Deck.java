package com.jason;

import java.util.*;

import static com.jason.Card.*;

public class Deck {

    /*

        From unorules.com:

        All number cards are the same value as the number on the card (e.g. a 9 is 9 points).
        “Draw Two” – 20 Points, “Reverse” – 20 Points, “Skip” – 20 Points, “Wild” – 50 Points,
        and “Wild Draw Four” – 50 Points. The first player to attain 500 points wins the game.
     */

    public static final int DECK_SIZE = 108;

    private Stack<Card> deck;
    private CardValueMap cvm;

    /**
     * Public constructor.
     */
    public Deck() {
        cvm = new CardValueMap();
        List<String> colors = new ArrayList<>();
        deck = new Stack<>();
        colors.add(Card.RED);
        colors.add(Card.YELLOW);
        colors.add(Card.GREEN);
        colors.add(Card.BLUE);
        
        // Make 0 - 9 of all four colors.
        for(String c : colors) {
            for(int i = 0; i < 10; i++) {
                Card card = new Card(c, Integer.toString(i), cvm);
                deck.push(card);
            }   
        }
        
        // Make 1 - 9 of all four colors.
        for(String c : colors) {
            for(int i = 1; i < 10; i++) {
                Card card = new Card(c, Integer.toString(i), cvm);
                deck.push(card);
            }   
        }
        
        // Make skip, reverse and draw two cards.
        for(int i = 0; i < 2; i++) {
            for(String c : colors) {
                Card card = new Card(c, Card.SKIP, cvm);
                deck.push(card);
                card = new Card(c, Card.REVERSE, cvm);
                deck.push(card);
                card = new Card(c, Card.DRAW_TWO, cvm);
                deck.push(card);
            }
        }
        
        // Make wild and draw four cards.
        for(String c : colors) {
            Card card = new Card (Card.COLORLESS, WILD, cvm);
            deck.push(card);
            card = new Card (Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
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