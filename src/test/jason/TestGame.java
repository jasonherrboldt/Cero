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
    private Card drawTwo;
    private Card drawFour;
    private Card skip;
    private Card reverse;

    @Before
    public void setup() {
        player1 = new Player("Player One", false);
        player2 = new Player("Player Two", true);
        cvm = new CardValueMap();

        numeric = new Card(Card.RED, Card.SEVEN, cvm);
        wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        nonNumericAndNonWild = new Card(Card.GREEN, Card.SKIP, cvm);
        drawTwo = new Card(Card.YELLOW, Card.DRAW_TWO, cvm);
        drawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        skip = new Card(Card.BLUE, Card.SKIP, cvm);
        reverse = new Card(Card.BLUE, Card.REVERSE, cvm);
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
    public void testGame_skipPlayersTurn_firstTurn_numeric() {
        Game game = new Game("");
        game.setIsFirstMove(true);
        game.startGame(numeric, true); // inject card here
//
//        // game.setCurrentPlayedCard(numeric); // ******** REPLACE THIS **********
        assertFalse(game.skipPlayersTurn(player1));

//        game.setIsFirstMove(true); // get rid of this
//
//        // make this a new test:
//        game.setCurrentPlayedCard(nonNumericAndNonWild); // ******** REPLACE THIS **********
//        assertTrue(game.skipPlayersTurn(player1));
    }

    /**
     * I'm going to have to write a whole new suite of tests that handle the 2nd move. Shit.
     */

    @Test
    public void testGame_skipPlayersTurn_firstTurn_drawTwo() {
        List<Card> cards = new ArrayList<>();
        player1.setHand(cards);
        Game game = new Game("");
        game.setPlayer1(player1);
        game.startGame(drawTwo, true); // inject card here
        int playerOneHandSizeBefore = game.getPlayer1().getHand().getSize();
        game.setIsFirstMove(true); // necessary?

        assertTrue(game.skipPlayersTurn(player1));
        int playerOneHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerOneHandSizeBefore + 2), playerOneHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersTurn_firstTurn_skip() {
        Game game = new Game("");
        List<Card> cards = new ArrayList<>();
        player1.setHand(cards);
        game.setPlayer1(player1);
        game.startGame(skip, true);
        int playerOneHandSizeBefore = game.getPlayer1().getHand().getSize();
        assertTrue(game.skipPlayersTurn(player1));
        int playerOneHandSizeAfter = game.getPlayer1().getHand().getSize();
        assertEquals((playerOneHandSizeBefore), playerOneHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersTurn_firstTurn_reverse() {
        // also need to test that cards are not being drawn to player one's hand.
        Game game = new Game("");
        game.startGame(reverse, true);
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
    public void testGame_skipPlayersTurn_2() {

        Game game = new Game("");
        game.setIsFirstMove(false);
        game.setPlayerOnesTurn(false);
        player2.setMyLastPlayedCard(numeric);
        game.setPlayer2(player2);
        game.setCurrentPlayedCard(drawTwo);

        List<Card> cards = new ArrayList<>();
        player2.setHand(cards);
        game.setPlayer2(player2);

        // if (lastPlayedCardIsNumeric && currentPlayedCardIsNonNumericAndNonWild) {
        // Make sure player two draws two cards
        int playerTwoHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipPlayersTurn(player2));
        int playerTwoHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals((playerTwoHandSizeBefore + 2), playerTwoHandSizeAfter);

        // Make sure player two draws four cards
        game.setPlayerOnesTurn(false);
        game.setCurrentPlayedCard(drawFour);
        playerTwoHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipPlayersTurn(player2));
        playerTwoHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals((playerTwoHandSizeBefore + 4), playerTwoHandSizeAfter);

        // if (lastPlayedCardIsWild && currentPlayedCardIsNonNumericAndNonWild) {
        // Make sure player two draws two cards
        player2.setMyLastPlayedCard(wild);
        game.setCurrentPlayedCard(drawTwo);
        playerTwoHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipPlayersTurn(player2));
        playerTwoHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals((playerTwoHandSizeBefore + 2), playerTwoHandSizeAfter);

        // Make sure player two draws four cards
        game.setPlayerOnesTurn(false);
        game.setCurrentPlayedCard(drawFour);
        playerTwoHandSizeBefore = game.getPlayer2().getHand().getSize();
        assertTrue(game.skipPlayersTurn(player2));
        playerTwoHandSizeAfter = game.getPlayer2().getHand().getSize();
        assertEquals((playerTwoHandSizeBefore + 4), playerTwoHandSizeAfter);
    }

    @Test
    public void testGame_skipPlayersTurn_3() {
        /*
        if (lastPlayedCardIsNumeric && currentPlayedCardIsWild) {
            player.chosenColor = getOtherPlayersChosenColor(player);
            return false; // do not skip player's turn
        }
        */
        Game game = new Game("");
        game.setIsFirstMove(false);
        game.setPlayerOnesTurn(false);
        player2.setMyLastPlayedCard(numeric);
        player1.chosenColor = Card.GREEN;
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setCurrentPlayedCard(wild);

        assertFalse(game.skipPlayersTurn(player2));
        assertTrue(player2.chosenColor.equalsIgnoreCase(Card.GREEN));

        /*
        if (lastPlayedCardIsWild && currentPlayedCardIsWild) {
            player.chosenColor = getOtherPlayersChosenColor(player);
            return false; // do not skip player's turn
        }
         */
        player2.setMyLastPlayedCard(wild);
        player1.chosenColor = Card.RED;
        assertFalse(game.skipPlayersTurn(player2));
        assertTrue(player2.chosenColor.equalsIgnoreCase(Card.RED));
    }

    @Test
    public void testGame_skipPlayersTurn_4() {
    /*
                if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsNumeric) {
                    Main.out("WARN: Game.skipPlayersTurn encountered a state that should never occur. " +
                            "No action taken, returned true.");
                    return true;
                }
                if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsWild) {
                    Main.out("WARN: Game.skipPlayersTurn encountered a state that should never occur. " +
                            "No action taken, returned true.");
                    return true;
                }
     */

        Game game = new Game("");
        game.setIsFirstMove(false);
        game.setPlayerOnesTurn(false);
        player2.setMyLastPlayedCard(nonNumericAndNonWild);
        game.setCurrentPlayedCard(numeric);
        game.setPlayer2(player2);
        assertTrue(game.skipPlayersTurn(player2));

        game.setCurrentPlayedCard(wild);
        assertTrue(game.skipPlayersTurn(player2));

    }

    @Test
    public void testGame_playerTwoMove() {
        Game game = new Game("");

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
        player1.setMyLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        player2.setMyLastPlayedCard(new Card(Card.GREEN, Card.ZERO, cvm));
        Game game = new Game("");
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

    @Test
    public void testGame_playFirstHand_notFirstMove() {
        // Create a new game object.
        Game game = new Game("");
        assertTrue(game.isFirstMove());

        // Start a new game.
        game.startGame(null, true);
        assertTrue(game.isFirstMove());

        // Play the first hand.
        game.playFirstHand();
        assertFalse(game.isFirstMove());
    }

}























