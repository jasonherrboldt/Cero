package test.jason;

import com.jason.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class TestGame {

    private CardValueMap cvm;
    private Player player1;
    private Player player2;
    private List<Card> hand;

    private Card numeric;
    private Card wild;
    private Card nonNumericAndNonWild;

    @Before
    public void setup() {
        player1 = new Player("Player One", false);
        player2 = new Player("Player Two", true);
        cvm = new CardValueMap();

        numeric = new Card(Card.RED, Card.SEVEN, cvm);
        wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        nonNumericAndNonWild = new Card(Card.GREEN, Card.SKIP, cvm);
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
        hand = new ArrayList<>();
        hand.add(new Card(Card.GREEN, Card.ZERO, cvm));
        player1.setHand(hand);
        assertEquals(player1.getHand().getSize(), 1);

        Stack<Card> discardPile = new Stack<>();
        game.setDiscardPile(discardPile);

        assertEquals(player1.getHand().getSize(), 1);
        game.draw(player1);
        assertEquals(player1.getHand().getSize(), 2);
    }

    @Test
    public void testGame_draw_emptyDeck() {
        Game game = new Game("");

        // Give the game a player with a hand of one card.
        hand = new ArrayList<>();
        hand.add(new Card(Card.GREEN, Card.ZERO, cvm));
        player1.setHand(hand);
        assertEquals(player1.getHand().getSize(), 1);

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
        game.draw(player1);

        // Player should now have two cards instead of one.
        assertEquals(player1.getHand().getSize(), 2);

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
        Game game = new Game("");

        // Make sure the isFirstMove indicator is getting updated.
        assertTrue(game.isFirstMove());
        game.startGame();
        assertFalse(game.isFirstMove());

        // Make sure the current played card is correctly reflected everywhere.
        Card currentPlayedCard = game.getCurrentPlayedCard();
        assertEquals(game.getCurrentColor(), currentPlayedCard.getColor());
        Stack<Card> discardPile = game.getDiscardPile();
        assertTrue(discardPile.get(0) != null);
        assertTrue(discardPile.get(discardPile.size() - 1).equals(currentPlayedCard));

        // Make sure both players can see 7 cards in the other's hand.
        List<Player> players = game.getPlayers();
        for (Player p : players) {
            assertEquals(p.otherPlayersHandCount, 7);
        }
    }

    @Test
    public void testGame_skipPlayersTurn_0() {
        // very first turn
        Game game = new Game("");
        game.setPlayerOnesTurn(true);
        game.setCurrentPlayedCard(numeric);
        assertFalse(game.skipPlayersTurn(player1));

        game.setCurrentPlayedCard(nonNumericAndNonWild);
        assertTrue(game.skipPlayersTurn(player1));
    }

    @Test
    public void testGame_skipPlayersTurn_1() {
        /*
        if(lastPlayedCardIsNumeric && currentPlayedCardIsNumeric) {
            return false; // do not skip player's turn
        }
        */
        Game game = new Game("");
        game.setIsFirstMove(false);
        game.setPlayerOnesTurn(false);
        player2.setMyLastPlayedCard(numeric);
        game.setPlayer2(player2);
        game.setCurrentPlayedCard(numeric);

        assertFalse(game.skipPlayersTurn(player2));

        /*
        if(lastPlayedCardIsWild && currentPlayedCardIsNumeric) {
            return false; // do not skip player's turn
        }
        */

        game.setPlayerOnesTurn(false);
        player2.setMyLastPlayedCard(wild);
        game.setPlayer2(player2);
        game.setCurrentPlayedCard(numeric);
        assertFalse(game.skipPlayersTurn(player2));

        /*
        if(lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsNonNumericAndNonWild) {
            return false; // do not skip player's turn
        }
        */
        game.setPlayerOnesTurn(false);
        player2.setMyLastPlayedCard(nonNumericAndNonWild);
        game.setPlayer2(player2);
        game.setCurrentPlayedCard(nonNumericAndNonWild);
        assertFalse(game.skipPlayersTurn(player2));

    }

    @Test
    public void testGame_playerTwoMove() {
        Game game = new Game("");

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
    public void testGame_getOtherPlayersChosenColor() {
        player1.setMyLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        player2.setMyLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        Game game = new Game("");
        game.startGame();
        List<Player> players = game.getPlayers();
        if(players.size() == 2) {
            Player playerBlue = players.get(0);
            Player playerRed = players.get(1);
            playerBlue.chosenColor = Card.BLUE;
            playerRed.chosenColor = Card.RED;
            assertEquals(game.getOtherPlayersChosenColor(playerBlue), Card.RED);
            assertEquals(game.getOtherPlayersChosenColor(playerRed), Card.BLUE);
        } else {
            fail("Test may have been close to throwing an out-of-bounds exception. " +
                    "List of players from game.getPlayers must have a size of exactly two.");
        }

    }
}