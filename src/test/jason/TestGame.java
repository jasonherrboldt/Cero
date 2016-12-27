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
        // make an empty deck.
        Stack<Card> mockDeck = new Stack<>();
        // call game's setDeck method, inject empty deck.
        game.setDeck(mockDeck);
        assertTrue(game.getDeck().getDeckSize() == 0);

        // make a small discard pile stack of three cards
        Stack<Card> mockDiscardPile = new Stack<>();
        mockDiscardPile.push(new Card(Card.RED, Card.ZERO, cvm));
        mockDiscardPile.push(new Card(Card.BLUE, Card.ONE, cvm));
        mockDiscardPile.push(new Card(Card.GREEN, Card.TWO, cvm));
        // call game's setDiscardPile, inject small stack.
        game.setDiscardPile(mockDiscardPile);
        assertTrue(game.getDiscardPile().size() == 3);

        // call game's refreshDeckIfEmpty method.
        game.refreshDeckIfEmpty();

        assertTrue(game.getDiscardPile().size() == 1);
        assertTrue(game.getDeck().getDeckSize() == 3);
    }
}
















