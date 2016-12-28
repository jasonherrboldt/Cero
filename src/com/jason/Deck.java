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
    public void shuffle() { // no test needed
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
    public Card getNextCard() { // tested
        if(!deckStack.empty()) {
            return deckStack.pop();
        }
        Main.out("WARN: getNextCard called on an empty deck. No card returned.");
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

    public Stack<Card> getDeck() { // no test necessary
        return deckStack;
    }

    // for debug
    public void printDeck() { // no test necessary
        Iterator<Card> iter = getDeck().iterator();
        while(iter.hasNext()) {
            Card card = iter.next();
            Main.out(card.getColor() + " " + card.getFace());
        }
    }

    /**
     * Make a fresh deck with all new cards.
     */
    public void populate() {
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

    public void clearDeck() { // no test needed
        deckStack.clear();
    }

    /**
     * Transfer the discard pile to the deck and shuffle.
     */
    public void refreshDeckIfEmpty(DiscardPile discardPile) { // *** NEEDS TO BE TESTED ***
        if(getDeckSize() == 0) {
            Main.out("Replenishing deck with shuffled discard pile.");
            replaceDeckWithShuffledDiscardPile(discardPile);
            shuffle();
            // return discardPile.pop?
            discardPile.clear();
        }
    }

    /**
     * Replace the current deck of cards with an injected discard pile.
     *
     * @param discardPile the replacement cards
     */
    public void replaceDeckWithShuffledDiscardPile(DiscardPile discardPile) { // *** NEEDS TO BE TESTED ***
        if(discardPile == null || discardPile.isEmpty()) {
            Main.out("WARN: someone tried to replace the deck with a null or empty discard pile. Deck not replaced.");
        } else {
            deckStack.clear();
            Stack<Card> discardStack = discardPile.getStack();
            Iterator<Card> discardCards = discardStack.iterator();
            while(discardCards.hasNext()) {
                deckStack.add(discardCards.next());
            }
        }
    }

    /**
     * Replace this deck with another deck (for testing).
     *
     * @param injectedDeck the replacement deck
     */
    public void replaceDeckWithAnotherDeck(Deck injectedDeck) {
        if(injectedDeck == null || injectedDeck.getDeck().isEmpty()) {
            Main.out("WARN: Deck.replaceDeckWithAnotherDeck called with an empty or null deck. No action taken.");
        } else {
            for(Card c : injectedDeck.getDeck()) {
                deckStack.push(c);
            }
        }
    }
    
}















