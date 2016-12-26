package test.jason;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.jason.*;

import java.util.Iterator;

public class TestDeck {

    private Deck deck = new Deck(null);

    @Before
    public void setup() {
        deck = new Deck(null);
    }

    @Test
    public void testDeck_deckSize() {
        assertEquals(this.deck.getDeckSize(), Deck.DECK_SIZE);
    }

    @Test
    public void testDeck_getNextCard() {
        int sizeBeforePop = deck.getDeckSize();
        deck.getNextCard();
        int sizeAfterPop = deck.getDeckSize();
        assertEquals(sizeBeforePop - 1, sizeAfterPop);
    }

    @Test
    public void testDeck_peekAtTopCard() {
        int sizeBeforePeek = deck.getDeckSize();
        deck.peekAtTopCard();
        int sizeAfterPeek = deck.getDeckSize();
        assertEquals(sizeBeforePeek, sizeAfterPeek);
    }

    @Test
    public void testDeck_spotCheck() {
        Iterator<Card> iter = deck.getDeck().iterator();
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
}