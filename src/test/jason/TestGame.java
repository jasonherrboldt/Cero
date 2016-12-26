package test.jason;

import com.jason.*;
import org.junit.*;

import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class TestGame {

    // private Deck deck;
    CardValueMap cvm;
    Game game;
    Stack<Card> discardPile;

    @Before
    public void setup() {
        // deck = new Deck(null);
        cvm = new CardValueMap();
        game = new Game("");
        discardPile = new Stack<>();
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

    }
}
















