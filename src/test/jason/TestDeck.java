package test.jason;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.jason.*;

import java.util.Iterator;

public class TestDeck {

    private Deck deck = new Deck();
    private static final int DECK_SIZE = 108;

    @Before
    public void setup() {
        deck = new Deck();
    }

    @Test
    public void testDeck_deckSize() {
        assertEquals(this.deck.getDeckSize(), DECK_SIZE);
    }

    @Test
    public void testDeck_shuffle() {
        Card firstCardBeforeShuffle = (Card)deck.peekAtTopCard();
        deck.shuffle();
        Card firstCardAfterShuffle = (Card)deck.peekAtTopCard();
        assertNotSame(firstCardBeforeShuffle, firstCardAfterShuffle);
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
            if(card.getFace().equalsIgnoreCase(Deck.SIX)) {
                sixCount++;
            }
            if(card.getColor().equalsIgnoreCase(Deck.GREEN)) {
                greenCount++;
            }
        }
        assertEquals(sixCount, 8);
        assertEquals(greenCount, 25);

        // assertEquals(sixCount, 1); // test the test - should fail
        // assertEquals(greenCount, 1); // test the test - should fail
    }
}
















