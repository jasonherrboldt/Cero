package com.jason;

import com.sun.istack.internal.Nullable;

import java.util.*;
import static com.jason.Card.*;

/**
 * A stack of cards.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Deck {

    public static final int DECK_SIZE = 108;

    private Stack<Card> deckStack;
    private CardValueMap cvm;

    /**
     * Public constructor.
     */
    public Deck() {
        cvm = new CardValueMap();
        deckStack = new Stack<>();
        populate();
    }

    /**
     * Shuffle the deck.
     */
    void shuffle() { // no test needed
        Collections.shuffle(deckStack);
    }
    
    /**
     * Get the current deck size.
     * 
     * @return  The size of the deck.
     */
    public int getDeckSize() { // tested
        return deckStack.size();
    }
    
    /**
     * Pop a card off the top of the deck.
     * 
     * @return the card on the top of the deck. 
     */
    @Nullable
    public Card popCard() { // tested
        if(!deckStack.empty()) {
            return deckStack.pop();
        }
        Main.out("WARN: Deck.getNextCard called on an empty deck. No action taken. Returned null.");
        return null;
    }
    
    /**
     * Peek at the top card on the deck.
     * 
     * @return a copy of the top card on the deck. 
     */
    public Card peekAtTopCard() { // tested
        return deckStack.peek();
    }

    public Stack<Card> getDeckStack() { // no test necessary
        return deckStack;
    }

    // for debug
    public void printDeck() { // no test necessary
        Iterator<Card> iter = getDeckStack().iterator();
        while(iter.hasNext()) {
            Card card = iter.next();
            Main.out(card.getColor() + " " + card.getFace());
        }
    }

    /**
     * Make a fresh deck with all new cards.
     */
    void populate() { // partially tested by testDeck_spotCheck
        if(deckStack.empty()) {
            List<String> colors = new ArrayList<>();
            colors.add(Card.RED);
            colors.add(Card.YELLOW);
            colors.add(Card.GREEN);
            colors.add(Card.BLUE);

            // Make 0 - 9 of all four colors.
            for(String c : colors) {
                for(int i = 0; i < 10; i++) {
                    Card card = new Card(c, Integer.toString(i), cvm);
                    deckStack.push(card);
                }
            }

            // Make 1 - 9 of all four colors.
            for(String c : colors) {
                for(int i = 1; i < 10; i++) {
                    Card card = new Card(c, Integer.toString(i), cvm);
                    deckStack.push(card);
                }
            }

            // Make skip, reverse and draw two cards.
            for(int i = 0; i < 2; i++) {
                for(String c : colors) {
                    Card card = new Card(c, Card.SKIP, cvm);
                    deckStack.push(card);
                    card = new Card(c, Card.REVERSE, cvm);
                    deckStack.push(card);
                    card = new Card(c, Card.DRAW_TWO, cvm);
                    deckStack.push(card);
                }
            }

            // Make wild and draw four cards.
            for(String c : colors) {
                Card card = new Card (Card.COLORLESS, WILD, cvm);
                deckStack.push(card);
                card = new Card (Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
                deckStack.push(card);
            }
        } else {
            Main.out("WARN: someone tried to populate a non-empty deck in Deck's constructor. " +
                    "Deck was not populated.");
        }
    }

    /**
     * Clear this deck.
     */
    public void clearDeck() { // no test needed
        deckStack.clear();
    }

    /**
     * Replace this deck with another deck (for testing).
     *
     * @param injectedCards the replacement deck
     */
    public void replaceWithAnotherDeck(Stack<Card> injectedCards) { // tested
        deckStack.clear();
        if(injectedCards == null) {
            Main.out("WARN: Deck.replaceWithAnotherDeck called with a null deck. No action taken.");
        } else {
            for(Card c : injectedCards) {
                deckStack.push(c);
            }
        }
    }
}