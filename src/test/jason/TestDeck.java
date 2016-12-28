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

//    @Test
//    public void testGame_refreshDeckIfEmpty() {
//
//        // Empty the game's deck.
//        game.clearDeck();
//
//        // Make a small discard pile stack of three cards.
//        DiscardPile mockDiscardPile = new DiscardPile();
//        mockDiscardPile.add(new Card(Card.RED, Card.ZERO, cvm));
//        mockDiscardPile.add(new Card(Card.BLUE, Card.ONE, cvm));
//        mockDiscardPile.add(new Card(Card.GREEN, Card.TWO, cvm));
//
//        // Call game's setDiscardPile, inject small stack.
//        game.setDiscardPile(mockDiscardPile);
//        assertTrue(game.getDiscardPile().size() == 3);
//
//        // Call game's refreshDeckIfEmpty method.
//        game.getDeck().refreshDeckIfEmpty(game.getDiscardPile());
//
//        assertTrue(game.getDiscardPile().size() == 1);
//        assertTrue(game.getDeck().getDeckSize() == 3);
//
//        // Refresh deck for other tests.
//        game.getDeck().populate();
//    }
}












