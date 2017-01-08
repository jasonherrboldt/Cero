package test.jason;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.jason.*;

import java.util.Iterator;

public class TestDeck {

    private Deck deck = new Deck();

    @Before
    public void setup() {
        deck = new Deck();
    }

    @Test
    public void testDeck_deckSize() {
        assertEquals(this.deck.getSize(), Deck.DECK_SIZE);
    }

    @Test
    public void testDeck_popCard() {
        int sizeBeforePop = deck.getSize();
        deck.popCard();
        int sizeAfterPop = deck.getSize();
        assertEquals(sizeBeforePop - 1, sizeAfterPop);
    }

    @Test
    public void testDeck_peekAtTopCard() {
        int sizeBeforePeek = deck.getSize();
        deck.peekAtTopCard();
        int sizeAfterPeek = deck.getSize();
        assertEquals(sizeBeforePeek, sizeAfterPeek);
    }

    @Test
    public void testDeck_spotCheck() {
        Iterator<Card> iter = deck.getDeckStack().iterator();
        // A correct deck should have eight sixes and thirteen greens.
        int sixCount = 0;
        int greenCount = 0;
        while(iter.hasNext()) {
            Card card = iter.next();
            if(card.getFace().equalsIgnoreCase(Card.SIX)) {
                sixCount++;
            }
            if(card.getColor().equalsIgnoreCase(Card.GREEN)) {
                greenCount++;
            }
        }
        assertEquals(sixCount, 8);
        assertEquals(greenCount, 25);
    }

    @Test
    public void testDeck_replaceWithAnotherDeck() {
        Deck cleanDeck = new Deck();
        assertEquals(cleanDeck.getSize(), Deck.DECK_SIZE);

        Deck replacementDeck = new Deck();
        replacementDeck.popCard();
        replacementDeck.popCard();
        replacementDeck.popCard();
        replacementDeck.popCard();
        assertEquals(replacementDeck.getSize(), (Deck.DECK_SIZE - 4));

        cleanDeck.replaceWithAnotherDeck(replacementDeck.getDeckStack());
        assertEquals(cleanDeck.getSize(), replacementDeck.getSize());
    }
}