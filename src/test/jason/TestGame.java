package test.jason;

import com.jason.*;
import org.junit.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

/**
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */

public class TestGame {

    private CardValueMap cvm;
    private List<Card> hand;
    private Card numeric;
    private Card wild;
    private Card wildDrawFour;
    private Card nonNumericAndNonWild;
    private Card drawTwo;
    private Card drawFour;
    private Card skip;
    private Card reverse;
    private int playerHandSizeBefore;
    private int playerHandSizeAfter;
    private Game game;

    @Before
    public void setup() {
        cvm = new CardValueMap();

        numeric = new Card(Card.RED, Card.SEVEN, cvm);
        wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        nonNumericAndNonWild = new Card(Card.GREEN, Card.SKIP, cvm);
        drawTwo = new Card(Card.YELLOW, Card.DRAW_TWO, cvm);
        drawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        skip = new Card(Card.BLUE, Card.SKIP, cvm);
        reverse = new Card(Card.BLUE, Card.REVERSE, cvm);
    }

    @Test
    public void testGame_startGame_gameObject() {
        game = new Game("Player One", "Player Two");
        game.startGame(null, true);

        // Make sure the current played card is correctly reflected everywhere.
        Card currentPlayedCard = game.getCurrentPlayedCard();
        // game.setTestStartGame(false);
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
    public void testGame_startGame_currentPlayedCard_1() {
        game = new Game("Player One", "Player Two");
        assertEquals(game.getCurrentPlayedCard(), null);
        game.startGame(null, true);
        assertNotEquals(game.getCurrentPlayedCard(), null);
    }

    @Test
    public void testGame_startGame_currentPlayedCard_2() {
        Game game = new Game("Player One", "Player Two");
        assertEquals(game.getCurrentPlayedCard(), null);
        game.startGame(numeric, true);
        assertNotEquals(game.getCurrentPlayedCard(), null);
    }

    @Test
    public void testGame_startGame_discardPile() {
        game = new Game("Player One", "Player Two");
        assertEquals(game.getDiscardPile().size(), 0);
        game.startGame(numeric, false);
        assertEquals(game.getDiscardPile().size(), 1);
    }

    @Test
    public void testGame_startGame_currentColor_1() {
        game = new Game("Player One", "Player Two");
        assertEquals(game.getCurrentColor(), "");
        game.startGame(numeric, true);
        assertNotEquals(game.getCurrentColor(), "");
    }

    @Test
    public void testGame_startGame_currentColor_2() {
        game = new Game("Player One", "Player Two");
        assertEquals(game.getCurrentColor(), "");
        game.startGame(null, true);
        assertNotEquals(game.getCurrentColor(), "");
    }

    @Test
    public void testGame_verifyFirstCard() {
        game = new Game("Player One", "Player Two");

        Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        assertEquals(game.getDeck().getSize(), Deck.DECK_SIZE);

        Card notWild = game.verifyFirstCard(wild);
        assertFalse(wild.equals(notWild));
        assertEquals(game.getDeck().getSize(), Deck.DECK_SIZE - 1);

        Card notWildDrawFour = game.verifyFirstCard(wildDrawFour);
        assertFalse(wildDrawFour.equals(notWildDrawFour));
        assertEquals(game.getDeck().getSize(), Deck.DECK_SIZE - 1);
    }

    @Test
    public void testGame_draw_nonEmptyDeck() {
        game = new Game("Player One", "Player Two");
        hand = new ArrayList<>();
        hand.add(new Card(Card.GREEN, Card.ZERO, cvm));
        game.getPlayer1().setHand(hand);
        assertEquals(game.getPlayer1().getHand().getSize(), 1);

        Stack<Card> discardPile = new Stack<>();
        game.setDiscardPile(discardPile);

        assertEquals(game.getPlayer1().getHand().getSize(), 1);
        game.draw(game.getPlayer1(), false);
        assertEquals(game.getPlayer1().getHand().getSize(), 2);
    }

    @Test
    public void testGame_draw_emptyDeck() {
        game = new Game("Player One", "Player Two");

        // Give the game a player with a hand of one card.
        hand = new ArrayList<>();
        hand.add(new Card(Card.GREEN, Card.ZERO, cvm));
        // player1.setHand(hand);
        game.getPlayer1().setHand(hand);
        assertEquals(game.getPlayer1().getHand().getSize(), 1);

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
        assertEquals(game.getDeck().getSize(), 0);

        // Try to draw from an empty deck.
        game.draw(game.getPlayer1(), false);

        // Player should now have two cards instead of one.
        assertEquals(game.getPlayer1().getHand().getSize(), 2);

        // Deck should now have 2 cards (3 from the discard pile minus one for the draw).
        assertEquals(game.getDeck().getSize(), 2);

        // Replace original deck for other methods.
        game.setDeck(new Deck());
    }

    @Test
    public void testGame_dealHands() {
        game = new Game("Player One", "Player Two");
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
    public void testGame_skipFirstTurn_numeric() {
        List<Card> cards = new ArrayList<>();
        game = new Game("Player One", "Player Two");
        game.getPlayer1().setHand(cards);
        game.setPlayer1(game.getPlayer1());
        game.setIsFirstMove(true);
        game.startGame(numeric, true);
        // should not skip player's first turn, should not increase player's hand size
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipFirstTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertTrue(playerHandSizeBefore == playerHandSizeAfter);
    }

    @Test
    public void testGame_skipFirstTurn_drawTwo() {
        List<Card> cards = new ArrayList<>();
        game = new Game("Player One", "Player Two");
        game.getPlayer1().setHand(cards);
        game.setPlayer1(game.getPlayer1());
        game.startGame(drawTwo, true);
        // should skip player's first turn, hand size should go up 2.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipFirstTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerHandSizeBefore + 2), playerHandSizeAfter);
    }

    @Test
    public void testGame_skipFirstTurn_skip() {
        game = new Game("Player One", "Player Two");
        List<Card> cards = new ArrayList<>();
        game.getPlayer1().setHand(cards);
        game.setPlayer1(game.getPlayer1());
        game.startGame(skip, true);
        // should skip player's first turn, hand size should not go up.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipFirstTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerHandSizeBefore), playerHandSizeAfter);
    }

    @Test
    public void testGame_skipFirstTurn_reverse() {
        game = new Game("Player One", "Player Two");
        game.startGame(reverse, true);
        // should skip player's first turn, hand size should not go up.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipFirstTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerHandSizeBefore), playerHandSizeAfter);
    }

    @Test
    public void testGame_skipFirstTurn_exception_notFirstTurn() {
        game = new Game("Player One", "Player Two");
        game.startGame(reverse, true);
        game.setIsFirstMove(false);
        try {
            game.skipFirstTurn(game.getPlayer1(), false);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "skipFirstTurn was called when isFirstMove == false.");
        }
    }

    @Test
    public void testGame_skipFirstTurn_exception_wildOrWD4() {
        // wild card
        game = new Game("Player One", "Player Two");
        game.startGame(wild, true);
        game.setIsFirstMove(true);
        try {
            game.skipFirstTurn(game.getPlayer1(), false);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "skipFirstTurn was called with wild or " +
                    "wild draw four card - not allowed for first turn.");
        }

        // wild draw four card
        game = new Game("Player One", "Player Two");
        game.startGame(wildDrawFour, true);
        game.setIsFirstMove(true);
        try {
            game.skipFirstTurn(game.getPlayer1(), false);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "skipFirstTurn was called with wild or " +
                    "wild draw four card - not allowed for first turn.");
        }
    }

    /**
     * There are no assertions in this test -- it simply makes sure the game can handle the scenario
     * where p2 discards a wd4, p1 skips another turn, and p2 is able to identify its own chosen color
     * to find a legal card to discard
     */
    @Test
    public void testGame_skipFirstTurn_p2_wd4() {
        game = new Game("Player One", "Player Two");
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));
        hand.add(new Card(Card.BLUE, Card.SIX, cvm));
        hand.add(new Card(Card.BLUE, Card.EIGHT, cvm));
        hand.add(new Card(Card.YELLOW, Card.NINE, cvm));
        hand.add(new Card(Card.RED, Card.ZERO, cvm));
        try {
            game.startGame(new Card(Card.GREEN, Card.DRAW_TWO, cvm), true);
            game.getPlayer2().setHand(hand);
            game.getPlayer2().setStrategy(Player.STRATEGY_DUMB);
            game.playFirstHand(false);
            game.playerTwosTurn(false);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGame_playFirstHand_correctTurn_p1() {
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);
        assertTrue(game.isPlayerOnesTurn());
        game.playFirstHand(false);
        assertTrue(game.isPlayerOnesTurn());
    }

    @Test
    public void testGame_playFirstHand_correctTurn_p2() {
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);
        assertFalse(game.isPlayerOnesTurn());
        game.playFirstHand(false);
        assertTrue(game.isPlayerOnesTurn());
    }

    @Test
    public void testGame_playFirstHand_exception() {
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);
        game.setIsFirstMove(false);
        try {
            game.playFirstHand(false);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Game.playFirstHand called after first move has already been played.");
        }
    }

    @Test
    public void testGame_playFirstHand_switchP1Move_drawTwo() {
        // Create a new game object
        game = new Game("Player One", "Player Two");

        // Start a new game, let player one have the first move
        game.startGame(drawTwo, true);
        assertTrue(game.isFirstMove());
        assertTrue(game.isPlayerOnesTurn());

        // assert it's no longer player one's turn, and that it's no longer the first move.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        game.playFirstHand(false);
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertFalse(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_playFirstHand_switchP1Move_skip() {
        // Create a new game object
        game = new Game("Player One", "Player Two");

        // Start a new game, let player one have the first move
        game.startGame(skip, true);
        assertTrue(game.isFirstMove());
        assertTrue(game.isPlayerOnesTurn());

        // assert it's no longer player one's turn, and that it's no longer the first move.
        game.playFirstHand(false);
        assertFalse(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_playFirstHand_switchP1Move_reverse() {

        // Create a new game object
        game = new Game("Player One", "Player Two");

        // Start a new game, let player one have the first move
        game.startGame(reverse, true);
        assertTrue(game.isFirstMove());
        assertTrue(game.isPlayerOnesTurn());

        // assert it's no longer player one's turn, and that it's no longer the first move.
        game.playFirstHand(false);
        assertFalse(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_playFirstHand_switchP2Move_drawTwo() {
        // Create a new game object
        game = new Game("Player One", "Player Two");

        // Start a new game, let player two have the first move.
        game.startGame(drawTwo, false);
        assertTrue(game.isFirstMove());
        assertFalse(game.isPlayerOnesTurn());

        // assert it's no longer player two's turn, and that it's no longer the first move.
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        game.playFirstHand(false);
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertTrue(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_playFirstHand_switchP2Move_skip() {
        // Create a new game object
        game = new Game("Player One", "Player Two");

        // Start a new game, let player two have the first move.
        game.startGame(skip, false);
        assertTrue(game.isFirstMove());
        assertFalse(game.isPlayerOnesTurn());

        // assert it's no longer player two's turn, and that it's no longer the first move.
        game.playFirstHand(false);
        assertTrue(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_playFirstHand_switchP2Move_reverse() {
        // Create a new game object
        game = new Game("Player One", "Player Two");

        // Start a new game, let player two have the first move.
        game.startGame(reverse, false);
        assertTrue(game.isFirstMove());
        assertFalse(game.isPlayerOnesTurn());

        // assert it's no longer player two's turn, and that it's no longer the first move.
        game.playFirstHand(false);
        assertTrue(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_skipTurn_exception1() {
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);
        // still first turn
        try {
            game.skipTurn(game.getPlayer2(), false);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Method called when isFirstTurn == true. Must be false.");
        }
    }

    @Test
    public void testGame_skipTurn_exception2() {
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(null);
        try {
            game.skipTurn(game.getPlayer2(), false);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Player's last played card is null in Game.skipTurn.");
        }
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNumeric_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);

        // player2.setLastPlayedCard(numeric);
        game.getPlayer2().setLastPlayedCard(numeric);

        playerHandSizeBefore = game.getPlayer2().getHand().getSize();

        // assertFalse(game.skipTurn(player2));
        assertFalse(game.skipTurn(game.getPlayer2(), false));

        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Ignore // player two can't be guaranteed to always discard a numeric card.
    public void testGame_skipTurn_lpcNumeric_cpcNumeric_p2() {

        // Start the game with a numeric first card. Player two has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player one should not draw or skip the 2nd move.
        game.playFirstHand(false);

        game.getPlayer1().setLastPlayedCard(numeric);

        playerHandSizeBefore = game.getPlayer1().getHand().getSize();

        assertFalse(game.skipTurn(game.getPlayer1(), false));

        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNumeric_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        // player2.setLastPlayedCard(wild);
        game.getPlayer2().setLastPlayedCard(wild);
        game.setCurrentPlayedCard(numeric);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNumeric_p2() {
        // Start the game with a numeric first card. Player two has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(wild);
        game.setCurrentPlayedCard(numeric);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpc_cpc_NonNumericNonWild_p1() {
        // Only need to test p1 against p2 -- the same is true wlog going the other way around.

        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(nonNumericAndNonWild, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(nonNumericAndNonWild);
        game.setCurrentPlayedCard(nonNumericAndNonWild);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpc_cpc_NonNumericNonWild_p2() {
        // Only need to test p1 against p2 -- the same is true wlog going the other way around.

        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(nonNumericAndNonWild, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(nonNumericAndNonWild);
        game.setCurrentPlayedCard(nonNumericAndNonWild);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcWild_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(numeric);
        game.setCurrentPlayedCard(wild);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcWild_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(numeric);
        game.setCurrentPlayedCard(wild);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcWild_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(wild);
        game.setCurrentPlayedCard(wild);

        game.getPlayer1().setChosenColor(Card.BLUE);
        game.getPlayer2().setChosenColor(Card.YELLOW);
        game.getPlayer2().setChosenColor(Card.YELLOW);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);

        // Assert color change transfer from player one to player two.
        assertEquals(game.getPlayer2().getChosenColor(), game.getPlayer1().getChosenColor());
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcWild_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(wild);
        game.setCurrentPlayedCard(wild);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_drawTwo_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(drawTwo);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_drawTwo_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(drawTwo);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_skip_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(skip);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_skip_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(skip);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_reverse_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(reverse);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_reverse_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(reverse);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_draw4_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(drawFour);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore + 4, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcNumeric_cpcNnnw_draw4_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(numeric);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(drawFour);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore + 4, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_drawTwo_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(drawTwo);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_drawTwo_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(drawTwo);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_skip_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(skip);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_skip_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(skip);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_reverse_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(reverse);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_reverse_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(reverse);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_draw4_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer2().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer2().setHand(hand);
        game.setCurrentPlayedCard(drawFour);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer2(), false));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore + 4, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipTurn_lpcWild_cpcNnnw_draw4_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One", "Player Two");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand(false);
        game.getPlayer1().setLastPlayedCard(wild);
        hand = new ArrayList<>();
        game.getPlayer1().setHand(hand);
        game.setCurrentPlayedCard(drawFour);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipTurn(game.getPlayer1(), false));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore + 4, playerHandSizeAfter);
    }

    @Test
    public void testGame_playerTwoMove() {
        game = new Game("Player One", "Player Two");

        game.startGame(null, false);
        game.setPlayerOnesTurn(false);

        // Make sure the happy path returns a card.
        assertTrue(game.playerTwoMove(false) != null);
    }

    @Test
    public void testGame_getOtherPlayersChosenColor() {
        game = new Game("Player One", "Player Two");
        game.getPlayer1().setLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        game.getPlayer2().setLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        game = new Game("Player One", "Player Two");
        game.startGame(null, false);
        List<Player> players = game.getPlayers();
        if(players.size() == 2) {
            Player playerBlue = players.get(0);
            Player playerRed = players.get(1);
            playerBlue.setChosenColor(Card.BLUE);
            playerRed.setChosenColor(Card.RED);
            // assertEquals(game.getOtherPlayersChosenColor(playerBlue), Card.RED);
            assertEquals(game.getOtherPlayer(playerBlue).getChosenColor(), Card.RED);
            // assertEquals(game.getOtherPlayersChosenColor(playerRed), Card.BLUE);
            assertEquals(game.getOtherPlayer(playerRed).getChosenColor(), Card.BLUE);
        } else {
            fail("Test may have been close to throwing an out-of-bounds exception. " +
                    "List of players from game.getPlayers must have a size of exactly two.");
        }
    }
}