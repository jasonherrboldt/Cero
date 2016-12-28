package test.jason;

import com.jason.*;
import org.junit.*;

import java.util.ArrayList;
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

//    @Test
//    public void testPlayer_draw_nonEmptyDeck() {
//        player = new Player("Draw Non-Empty Deck Test", false);
//        cardList = new ArrayList<>();
//        cardList.add(new Card(Card.GREEN, Card.ZERO, cvm));
//        player.setHand(cardList);
//        assertEquals(player.getHand().getSize(), 1);
//
//        DiscardPile discardPile = new DiscardPile();
//        player.draw(deck, discardPile);
//
//        assertEquals(player.getHand().getSize(), 2);
//    }
//
//    @Test
//    public void testPlayer_draw_emptyDeck() {
//        player = new Player("Draw Empty Deck Test", false);
//        cardList = new ArrayList<>();
//        cardList.add(new Card(Card.GREEN, Card.ZERO, cvm));
//        player.setHand(cardList);
//        DiscardPile discardPile = new DiscardPile();
//        discardPile.add(new Card(Card.BLUE, Card.SEVEN, cvm));
//        discardPile.add(new Card(Card.RED, Card.EIGHT, cvm));
//        discardPile.add(new Card(Card.GREEN, Card.NINE, cvm));
//        deck.clearDeck();
//
//        // Try to draw from an empty deck.
//        player.draw(deck, discardPile);
//        assertEquals(player.getHand().getSize(), 2);
//
//        // Replace deck for other methods.
//        deck.populate();
//    }
}
















