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
        game.startGame();
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

    @Test
    public void testGame_playerTwoMove() {
        Game game = new Game("Test Player");

        game.startGame();
        game.setPlayerOnesTurn(false);

        // Make sure the happy path returns a card.
        assertTrue(game.playerTwoMove() != null);

        // Make sure it returns null when it's supposed to.
        game.clearDeck();
        Stack<Card> emptyStack = new Stack<>();
        game.setDiscardPile(emptyStack);
        assertEquals(game.playerTwoMove(), null);
        game.setPlayerOnesTurn(true);
        assertEquals(game.playerTwoMove(), null);
    }

    @Test
    public void testGame_playerTwosTurn() {
        Game game = new Game("Test Player");
        game.setPlayerOnesTurn(true);
        String message = game.playerTwosTurn("");
        assertTrue(message.contains("ERROR: Game.playerTwosTurn called during player one's turn. No action taken."));

        game.startGame();
        game.setPlayerOnesTurn(false);
        message = game.playerTwosTurn("");
        assertTrue(message.contains("Player two has discarded a card."));
    }

    @Test
    public void testGame_getOtherPlayersChosenColor() {
        Game game = new Game("Test Player");
        game.startGame();
        List<Player> players = game.getPlayers();
        if(players.size() == 2) {
            Player playerBlue = players.get(0);
            Player playerRed = players.get(1);
            playerBlue.setChosenColor(Card.BLUE);
            playerRed.setChosenColor(Card.RED);
            assertEquals(game.getOtherPlayersChosenColor(playerBlue), Card.RED);
            assertEquals(game.getOtherPlayersChosenColor(playerRed), Card.BLUE);
        } else {
            fail("Test may have been close to throwing an out-of-bounds exception. " +
                    "List of players from game.getPlayers must have a size of exactly two.");
        }

    }
}