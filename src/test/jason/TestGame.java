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
        player1 = new Player("Player One", false);
        player2 = new Player("Player Two", true);
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
    public void testGame_verifyFirstCard() {
        game = new Game("Player One");

        Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        // Card wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
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
        game = new Game("Player One");
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
        game = new Game("Player One");

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
        game = new Game("Player One");
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
        game = new Game("Player One");
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
    public void testGame_skipPlayersFirstTurn_numeric() {
        List<Card> cards = new ArrayList<>();
        player1.setHand(cards);
        game = new Game("Player One");
        game.setPlayer1(player1);
        game.setIsFirstMove(true);
        game.startGame(numeric, true);
        // should not skip player's first turn, should not increase player's hand size
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipPlayersFirstTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertTrue(playerHandSizeBefore == playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersFirstTurn_drawTwo() {
        List<Card> cards = new ArrayList<>();
        player1.setHand(cards);
        game = new Game("Player One");
        game.setPlayer1(player1);
        game.startGame(drawTwo, true);
        // should skip player's first turn, hand size should go up 2.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipPlayersFirstTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerHandSizeBefore + 2), playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersFirstTurn_skip() {
        game = new Game("Player One");
        List<Card> cards = new ArrayList<>();
        player1.setHand(cards);
        game.setPlayer1(player1);
        game.startGame(skip, true);
        // should skip player's first turn, hand size should not go up.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipPlayersFirstTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerHandSizeBefore), playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersFirstTurn_reverse() {
        game = new Game("Player One");
        game.startGame(reverse, true);
        // should skip player's first turn, hand size should not go up.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipPlayersFirstTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerHandSizeBefore), playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersFirstTurn_exception_notFirstTurn() {
        game = new Game("Player One");
        game.startGame(reverse, true);
        game.setIsFirstMove(false);
        try {
            game.skipPlayersFirstTurn(player1);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "skipPlayersFirstTurn was called when isFirstMove == false.");
        }
    }

    @Test
    public void testGame_skipPlayersFirstTurn_exception_wildOrWD4() {
        // wild card
        game = new Game("Player One");
        game.startGame(wild, true);
        game.setIsFirstMove(true);
        try {
            game.skipPlayersFirstTurn(player1);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "skipPlayersFirstTurn was called with wild or " +
                    "wild draw four card - not allowed for first turn.");
        }

        // wild draw four card
        game = new Game("Player One");
        game.startGame(wildDrawFour, true);
        game.setIsFirstMove(true);
        try {
            game.skipPlayersFirstTurn(player1);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "skipPlayersFirstTurn was called with wild or " +
                    "wild draw four card - not allowed for first turn.");
        }
    }

    @Test
    public void testGame_playFirstHand_exception() {
        game = new Game("Player One");
        game.startGame(numeric, true);
        game.setIsFirstMove(false);
        try {
            game.playFirstHand();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Game.playFirstHand called after first move has already been played.");
        }
    }

    @Test
    public void testGame_playFirstHand_switchP1Move_drawTwo() {

        // draw two

        // Create a new game object
        game = new Game("Player One");

        // Start a new game, let player one have the first move
        game.startGame(drawTwo, true);
        assertTrue(game.isFirstMove());
        assertTrue(game.isPlayerOnesTurn());

        // assert it's no longer player one's turn, and that it's no longer the first move.
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        game.playFirstHand();
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertFalse(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_playFirstHand_switchP1Move_skip() {
        // Create a new game object
        game = new Game("Player One");

        // Start a new game, let player one have the first move
        game.startGame(skip, true);
        assertTrue(game.isFirstMove());
        assertTrue(game.isPlayerOnesTurn());

        // assert it's no longer player one's turn, and that it's no longer the first move.
        game.playFirstHand();
        assertFalse(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_playFirstHand_switchP1Move_reverse() {

        // Create a new game object
        game = new Game("Player One");

        // Start a new game, let player one have the first move
        game.startGame(reverse, true);
        assertTrue(game.isFirstMove());
        assertTrue(game.isPlayerOnesTurn());

        // assert it's no longer player one's turn, and that it's no longer the first move.
        game.playFirstHand();
        assertFalse(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_playFirstHand_switchP2Move_drawTwo() {
        // Create a new game object
        game = new Game("Player One");

        // Start a new game, let player two have the first move.
        game.startGame(drawTwo, false);
        assertTrue(game.isFirstMove());
        assertFalse(game.isPlayerOnesTurn());

        // assert it's no longer player two's turn, and that it's no longer the first move.
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        game.playFirstHand();
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertTrue(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
        assertEquals(playerHandSizeBefore + 2, playerHandSizeAfter);
    }

    @Test
    public void testGame_playFirstHand_switchP2Move_skip() {
        // Create a new game object
        game = new Game("Player One");

        // Start a new game, let player two have the first move.
        game.startGame(skip, false);
        assertTrue(game.isFirstMove());
        assertFalse(game.isPlayerOnesTurn());

        // assert it's no longer player two's turn, and that it's no longer the first move.
        game.playFirstHand();
        assertTrue(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_playFirstHand_switchP2Move_reverse() {
        // Create a new game object
        game = new Game("Player One");

        // Start a new game, let player two have the first move.
        game.startGame(reverse, false);
        assertTrue(game.isFirstMove());
        assertFalse(game.isPlayerOnesTurn());

        // assert it's no longer player two's turn, and that it's no longer the first move.
        game.playFirstHand();
        assertTrue(game.isPlayerOnesTurn());
        assertFalse(game.isFirstMove());
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_exception() {
        game = new Game("Player One");
        game.setIsFirstMove(true);
        try {
            game.skipPlayersSubsequentTurn(player2);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Game.skipPlayersSubsequentTurn called when isFirstMove == true.");
        }
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcNumeric_cpcNumeric_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player2.setLastPlayedCard(numeric);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player2));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcNumeric_cpcNumeric_p2() {
        // Start the game with a numeric first card. Player two has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player1.setLastPlayedCard(numeric);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    /*
        Do not skip my turn (return false):
        Lpc n, Cpc n: draw nothing, do not skip my turn (done for p1 and p2)
        Lpc w, Cpc n: draw nothing, do not skip my turn (done for p1 and p2)
        Lpc nn-nw, Cpc nn-nw: (same card bouncing back) draw nothing, do not skip my turn (done for p1 and [wlog] p2)
        Lpc n, Cpc w: set my color to other player's preferred color, draw nothing, do not skip my turn (done for p1 and p2)
        Lpc w, Cpc w: set my color to other player's preferred color, draw nothing, do not skip my turn (done for p1 and [wlog] p2)

        Skip my turn (return true)
        Lpc n, Cpc nn-nw: skip my turn and potentially draw
        Lpc w, Cpc nn-nw: skip my turn and potentially draw

        Blow up — throw an illegal state exception (put user warning in the exception:):
        Lpc nn-nw, Cpc n: (should never happen - other user should have skipped a turn)
        Lpc nn-nw, Cpc w: (should never happen - other user should have skipped a turn)
     */

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcWild_cpcNumeric_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player2.setLastPlayedCard(wild);
        game.setCurrentPlayedCard(numeric);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player2));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }


    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcWild_cpcNumeric_p2() {
        // Start the game with a numeric first card. Player two has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player1.setLastPlayedCard(wild);
        game.setCurrentPlayedCard(numeric);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpc_cpc_NonNumericNonWild() {
        // Only need to test p1 against p2 -- the same is true wlog going the other way around.

        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(nonNumericAndNonWild, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player2.setLastPlayedCard(nonNumericAndNonWild);
        game.setCurrentPlayedCard(nonNumericAndNonWild);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player2));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpc_cpc_nnnw_exception() {
        // Only need to test p1 against p2 -- the same is true wlog going the other way around.

        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(nonNumericAndNonWild, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player2.setLastPlayedCard(nonNumericAndNonWild);
        game.setCurrentPlayedCard(new Card(Card.RED, Card.REVERSE, cvm));
        try {
            game.skipPlayersSubsequentTurn(player2);
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "A non-numeric, non-wild card can not come back to the " +
                    "player that dealt it unless it is exactly the same card.");
        }
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcNumeric_cpcWild_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player2.setLastPlayedCard(numeric);
        game.setCurrentPlayedCard(wild);
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player2));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcNumeric_cpcWild_p2() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player1.setLastPlayedCard(numeric);
        game.setCurrentPlayedCard(wild);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }

    // Lpc w, Cpc w: set my color to other player's preferred color, draw nothing, do not skip my turn

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcWild_cpcWild_p1() {
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, true);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player2.setLastPlayedCard(wild);
        game.setCurrentPlayedCard(wild);

        game.getPlayer1().chosenColor = Card.BLUE;
        player2.chosenColor = Card.YELLOW;
        game.getPlayer2().chosenColor = Card.YELLOW;
        playerHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player2));
        playerHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);

        // Assert color change transfer from player one to player two.
        assertEquals(player2.chosenColor, game.getPlayer1().chosenColor);
    }

    @Test
    public void testGame_skipPlayersSubsequentTurn_lpcWild_cpcWild_p2() {
        // *** REQUIRES UPDATING ***
        // Start the game with a numeric first card. Player one has the first turn.
        game = new Game("Player One");
        game.startGame(numeric, false);

        // Assert player two should not draw or skip the 2nd move.
        game.playFirstHand();
        player1.setLastPlayedCard(wild);
        game.setCurrentPlayedCard(wild);
        playerHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertFalse(game.skipPlayersSubsequentTurn(player1));
        playerHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals(playerHandSizeBefore, playerHandSizeAfter);
    }


    @Test
    public void testGame_playerTwoMove() {
        game = new Game("Player One");

        game.startGame(null, false);
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
        player1.setLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        player2.setLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        game = new Game("Player One");
        game.startGame(null, false);
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























