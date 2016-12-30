package test.jason;

import com.jason.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;

public class TestPlayer {

    private CardValueMap cvm;
    private Hand classHand;
    // private Player player;

    // Not instantiated in setup:
    private List<Card> cardList;

    @Before
    public void setup() {
        cvm = new CardValueMap();
        classHand = new Hand();
        classHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        classHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        classHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));

        cardList = new ArrayList<>();
        cardList.add(new Card(Card.GREEN, Card.THREE, cvm));
        cardList.add(new Card(Card.RED, Card.DRAW_TWO, cvm));
        cardList.add(new Card(Card.RED, Card.EIGHT, cvm));
        cardList.add(new Card(Card.YELLOW, Card.ONE, cvm));
        cardList.add(new Card(Card.YELLOW, Card.ZERO, cvm));
        cardList.add(new Card(Card.YELLOW, Card.SIX, cvm));
    }

    @Test
    public void testPlayer_setHand() {
        Player player = new Player("Set Hand Test", true);
        player.setHand(cardList);
        Hand mockHand = player.getHand();
        List<Card> mockList = mockHand.getAllCards();
        List<Card> classList = classHand.getAllCards();

        boolean mismatchFound = false;
        if(cardList.size() != classHand.getSize()) {
            fail("ERROR: classHand must be the same size as mockHand");
        } else {
            for(int i = 0; i < mockList.size(); i++) {
                if(!mockList.get(i).equals(classList.get(i))) {
                    mismatchFound = true;
                }
            }
            assertFalse(mismatchFound);
        }
    }

    @Test
    public void testPlayer_discard_number() {
        Player player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());

        int handSize = player2.getHand().getSize();
        int cardsDiscarded = 0;

        // legal - match number but not color
        Card greenThree = new Card(Card.GREEN, Card.THREE, cvm);
        Card cpcYellowThree = new Card(Card.YELLOW, Card.THREE, cvm);

        assertTrue(player2.discard(greenThree, cpcYellowThree, false, null));
        cardsDiscarded++;
        assertEquals(player2.getHand().getSize(), (handSize - cardsDiscarded));

        // legal - match color but not number
        Card yellowSix = new Card(Card.YELLOW, Card.SIX, cvm);
        assertTrue(player2.discard(yellowSix, cpcYellowThree, false, null));
        cardsDiscarded++;
        assertEquals(player2.getHand().getSize(), (handSize - cardsDiscarded));

        // illegal - match neither color nor number
        Card redEight = new Card(Card.RED, Card.EIGHT, cvm);
        assertFalse(player2.discard(redEight, cpcYellowThree, false, null));
        // no card discarded, hand stays same size.
        assertEquals(player2.getHand().getSize(), (handSize - cardsDiscarded));
    }

    @Test
    public void testPlayer_discard_colorNonNumeric() {
        Player player2 = new Player("", true);
        player2.setHand(cardList);

        // only need to match color
        Card cpcRedThree = new Card(Card.RED, Card.THREE, cvm);
        Card redDrawTwo = new Card(Card.RED, Card.DRAW_TWO, cvm);
        assertTrue(player2.discard(redDrawTwo, cpcRedThree, false, null));

    }

    @Test
    public void testPlayer_discard_colorless() {
        Player player2 = new Player("", true);
        Card colorlessWild = new Card(Card.COLORLESS, Card.WILD, cvm);
        List<Card> hand = new ArrayList<>();
        hand.add(colorlessWild);
        player2.setHand(hand);

        Card cpcBlueOne = new Card(Card.BLUE, Card.ONE, cvm);
        assertTrue(player2.discard(colorlessWild, cpcBlueOne, false, null));

        Card colorlessWildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        hand.add(colorlessWildDrawFour);
        player2.setHand(hand);

        assertTrue(player2.discard(colorlessWildDrawFour, cpcBlueOne, false, null));
    }

    @Test
    public void testPlayer_discard_nullArgs() {
        Player player2 = new Player("", true);
        player2.setHand(cardList);
        Card realCard = new Card(Card.YELLOW, Card.FIVE, cvm);
        assertFalse(player2.discard(null, null, false, null));
        assertFalse(player2.discard(realCard, null, false, null));
        assertFalse(player2.discard(null, realCard, false, null));
    }

    @Test
    public void testPlayer_discard_callCero() {

    }

    @Test
    public void testPlayer_setRandomStrategy() {
        Player player2 = new Player("", true);
        assertTrue(player2.getStrategy().equals(""));
        assertTrue(player2.setRandomStrategy());
    }

    @Test
    public void testPlayer_getPlayerTwosChosenColor() {
        // Can't test for a dumb player -- returns a random color.
        // Bold and cautious players always return color of highest count.
        Player player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());

        player2.setStrategy(Player.STRATEGY_BOLD);
        assertEquals(player2.getPlayerTwosChosenColor(), Card.YELLOW);

        player2.setStrategy(Player.STRATEGY_CAUTIOUS);
        assertEquals(player2.getPlayerTwosChosenColor(), Card.YELLOW);

        // Test the unhappy path.
        Player player1 = new Player("", false);
        assertEquals(player1.getPlayerTwosChosenColor(), null);
    }

    @Test
    public void testPlayer_otherPlayerForgotToCallCero_player1() {
        Player player2 = new Player("", true);
        List<Card> hand = new ArrayList<>();
        Card blueFour = new Card(Card.BLUE, Card.FOUR, cvm);
        Card yellowSix = new Card(Card.YELLOW, Card.SIX, cvm);
        hand.add(blueFour);
        hand.add(yellowSix);
        player2.setHand(hand);

        // Player two legally discards penultimate card, but forgets to declare 'Cero plus one!'.
        Card currentPlayedCard = new Card(Card.YELLOW, Card.NINE, cvm);
        assertTrue(player2.discard(yellowSix, currentPlayedCard, false, null));

        // player one has to call out player 2's mistake
        Player player1 = new Player("", false);
        assertTrue(player1.otherPlayerForgotToCallCero(player2));
    }

    @Test
    public void testPlayer_otherPlayerForgotToCallCero_player2() {
        Player player1 = new Player("", false);
        List<Card> hand = new ArrayList<>();
        Card blueFour = new Card(Card.BLUE, Card.FOUR, cvm);
        Card yellowSix = new Card(Card.YELLOW, Card.SIX, cvm);
        hand.add(blueFour);
        hand.add(yellowSix);
        player1.setHand(hand);

        // Player one legally discards penultimate card, but forgets to declare 'Cero plus one!'.
        Card currentPlayedCard = new Card(Card.YELLOW, Card.NINE, cvm);
        assertTrue(player1.discard(yellowSix, currentPlayedCard, false, null)); // ultimately happens in Game.playerTwoMove

        Player player2_dumb = new Player("", true);
        player2_dumb.setStrategy(Player.STRATEGY_DUMB);
        assertFalse(player2_dumb.otherPlayerForgotToCallCero(player1));

        // (Can't test for player2 when strategy is bold or cautious -- result is always random.)
    }

    @Test
    public void testPlayer_decidePlayerTwoDiscard() {

    }
}





















