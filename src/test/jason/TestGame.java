package test.jason;

import com.jason.*;
import org.junit.*;
import java.util.Stack;

import static org.junit.Assert.*;

public class TestGame {

    private CardValueMap cvm;
    private Game game;

    @Before
    public void setup() {
        cvm = new CardValueMap();
        game = new Game("");
    }

    @Test
    public void testGame_verifyFirstCard() {
        Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        Card wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        assertEquals(game.getDeck().getDeckSize(), Deck.DECK_SIZE);

        Card notWild = game.verifyFirstCard(wild);
        assertFalse(wild.equals(notWild));
        assertEquals(game.getDeck().getDeckSize(), Deck.DECK_SIZE - 1);

        Card notWildDrawFour = game.verifyFirstCard(wildDrawFour);
        assertFalse(wildDrawFour.equals(notWildDrawFour));
        assertEquals(game.getDeck().getDeckSize(), Deck.DECK_SIZE - 1);
    }

    @Test
    public void testGame_refreshDeckIfEmpty() {

        // Empty the game's deck.
        game.clearDeck();

        // Make a small discard pile stack of three cards.
        DiscardPile mockDiscardPile = new DiscardPile();
        mockDiscardPile.add(new Card(Card.RED, Card.ZERO, cvm));
        mockDiscardPile.add(new Card(Card.BLUE, Card.ONE, cvm));
        mockDiscardPile.add(new Card(Card.GREEN, Card.TWO, cvm));

        // Call game's setDiscardPile, inject small stack.
        game.setDiscardPile(mockDiscardPile);
        assertTrue(game.getDiscardPile().size() == 3);

        // Call game's refreshDeckIfEmpty method.
        game.getDeck().refreshDeckIfEmpty(game.getDiscardPile());

        assertTrue(game.getDiscardPile().size() == 1);
        assertTrue(game.getDeck().getDeckSize() == 3);

        // Refresh deck for other tests.
        game.getDeck().populate();
    }
}
















