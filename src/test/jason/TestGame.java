package test.jason;

import com.jason.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class TestGame {

    private CardValueMap cvm;
    private Game game;
    private Player player;
    private List<Card> hand;

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
    public void testPlayer_draw_nonEmptyDeck() {
        player = new Player("Draw Non-Empty Deck Test", false);
        hand = new ArrayList<>();
        hand.add(new Card(Card.GREEN, Card.ZERO, cvm));
        player.setHand(hand);
        assertEquals(player.getHand().getSize(), 1);

        Stack<Card> discardPile = new Stack<>();
        game.setDiscardPile(discardPile);

        assertEquals(player.getHand().getSize(), 1);
        game.draw(player);
        assertEquals(player.getHand().getSize(), 2);
    }

    @Test
    public void testPlayer_draw_emptyDeck() {

        // Give the game a player with a hand of one card.
        player = new Player("Draw Empty Deck Test", false);
        hand = new ArrayList<>();
        hand.add(new Card(Card.GREEN, Card.ZERO, cvm));
        player.setHand(hand);
        assertEquals(player.getHand().getSize(), 1);

        // Give the game a discard pile of three cards.
        Stack<Card> discardPile = new Stack<>();
        discardPile.add(new Card(Card.BLUE, Card.SEVEN, cvm));
        discardPile.add(new Card(Card.RED, Card.EIGHT, cvm));
        discardPile.add(new Card(Card.GREEN, Card.NINE, cvm));
        game.setDiscardPile(discardPile);
        assertEquals(game.getDiscardPile().size(), 3);

        // Give the game an empty deck.
        Deck emptyDeck = new Deck();
        emptyDeck.clearDeck();
        game.setDeck(emptyDeck);
        assertEquals(game.getDeck().getDeckSize(), 0);

        // Try to draw from an empty deck.
        game.draw(player);

        // Player should now have two cards instead of one.
        assertEquals(player.getHand().getSize(), 2);

        // Deck should now have 2 cards (3 from the discard pile minus one for the draw).
        assertEquals(game.getDeck().getDeckSize(), 2);

        // Replace original deck for other methods.
        game.setDeck(new Deck());
    }
}
















