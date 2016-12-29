package test.jason;

import com.jason.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class TestGame {

    private CardValueMap cvm;
    private Player player;
    private List<Card> hand;

    @Before
    public void setup() {
        cvm = new CardValueMap();

    }

    @Test
    public void testGame_verifyFirstCard() {
        Game game = new Game("");

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
    public void testGame_draw_nonEmptyDeck() {
        Game game = new Game("");
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
    public void testGame_draw_emptyDeck() {
        Game game = new Game("");

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

    @Test
    public void testGame_dealHands() {
        Game game = new Game("");
        List<Player> players = game.getPlayers();
        for(Player p : players) {
            assertEquals(p.getHand(), null);
        }
        game.dealHands();
        for(Player p : players) {
            assertEquals(p.getHand().getSize(), 7);
        }
    }

    @Test
    public void testGame_startGame_gameObject() {
        Game game = new Game("Test Player");

        // Make sure the isFirstMove indicator is getting updated.
        assertTrue(game.isFirstMove());
        GameState gameState = game.startGame();
        assertFalse(game.isFirstMove());

        // Make sure the current played card is correctly reflected everywhere.
        Card currentPlayedCard = game.getCurrentPlayedCard();
        assertEquals(game.getCurrentColor(), currentPlayedCard.getColor());
        assertTrue(game.getDiscardPile().get(0) != null);
        assertTrue(game.getDiscardPile().get(0).equals(currentPlayedCard));

        // Make sure both players can see 7 cards in the other's hand.
        List<Player> players = game.getPlayers();
        for (Player p : players) {
            assertEquals(p.getOtherPlayersHandCount(), 7);
        }
    }

    @Test
    public void testGame_startGame_gameStateObject() {
        Game game = new Game("Test Player");
        GameState gameState = game.startGame();

        // Make sure player one has a hand of 7 cards.
        assertEquals(gameState.getPlayer().getHand().getSize(), 7);

        // Make sure the player in the GameState object is player one.
        assertFalse(gameState.getPlayer().isPlayer2());

        // Make sure game's current color made it to the gameState object.
        assertEquals(gameState.getCurrentColor(), game.getCurrentColor());

        // Make sure the game's current played card made it to the gameState object.
        assertTrue(gameState.getCurrentPlayedCard().equals(game.getCurrentPlayedCard()));

        assertEquals(gameState.getCurrentColor(), game.getCurrentPlayedCard().getColor());

        // There should be at least a "Your turn" message.
        assertNotEquals(gameState.getMessage(), "");

        // Make sure the current played card is non-null
        assertNotEquals(gameState.getCurrentPlayedCard().getColor(), null);
        assertNotEquals(gameState.getCurrentPlayedCard().getFace(), null);
    }

    @Test
    public void testGame_nonNumericCardReceived() {
        Game game = new Game("Test Player");
        player = new Player("Non Numeric Card Received Test", false);
        Card numericCard = new Card(Card.GREEN, Card.ONE, cvm);
        List<Card> hand = new ArrayList<>();
        hand.add(numericCard);
        player.setHand(hand);

        game.setCurrentPlayedCard(numericCard);
        assertFalse(game.nonNumericCardReceived(player));

        Card nonNumericCard = new Card(Card.RED, Card.DRAW_TWO, cvm);
        game.setCurrentPlayedCard(nonNumericCard);
        assertTrue(game.nonNumericCardReceived(player));

    }
}






























