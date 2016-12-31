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
    private List<Card> cardList;
    private Hand getBoldStrategyCard_numeric_hand;
    private Card blueNine;
    private Card yellowThree;
    private Card greenThree;
    private Card blueFour;
    private Card getBoldStrategyCard_currentPlayedCard;

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


        getBoldStrategyCard_numeric_hand = new Hand();

        // all legal cards that can match the cpc:
        blueNine = new Card(Card.BLUE, Card.NINE, cvm);
        yellowThree = new Card(Card.YELLOW, Card.THREE, cvm);
        greenThree = new Card(Card.GREEN, Card.THREE, cvm);
        blueFour = new Card(Card.BLUE, Card.FOUR, cvm);

        getBoldStrategyCard_currentPlayedCard = new Card(Card.BLUE, Card.THREE, cvm);

        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.BLUE, Card.NINE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.THREE, cvm));
    }

    @Test
    public void testPlayer_setHand() {
        Player player2 = new Player("Set Hand Test", true);
        player2.setHand(cardList);
        Hand mockHand = player2.getHand();
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
        assertTrue(player1.otherPlayerIncorrectlyForgotToCallCero(player2));
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
        assertFalse(player2_dumb.otherPlayerIncorrectlyForgotToCallCero(player1));

        // (Can't test for player2 when strategy is bold or cautious -- result is always random.)
    }

    @Test
    public void testPlayer_getBoldStrategyCard_wild() {
        Player player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        Card currentPlayedCard = new Card(Card.COLORLESS, Card.WILD, cvm);
        String currentColor = Card.YELLOW;
        Card yellowSix = new Card(Card.YELLOW, Card.SIX, cvm);
        assertTrue(player2.getBoldStrategyCard(currentPlayedCard, currentColor).equals(yellowSix));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_numeric_matchNumber() {
        Player player2 = new Player("", true);
        // the best card to pick at the moment is the yellow three, since it has the largest matching color group
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());
        String currentColor = Card.BLUE;
        // make sure no actual discarding happens during call to getBoldStrategyCard (handled elsewhere)
        int handSizeBefore = player2.getHand().getSize();
        assertTrue(player2.getBoldStrategyCard(getBoldStrategyCard_currentPlayedCard, currentColor).equals(yellowThree));
        int handSizeAfter = player2.getHand().getSize();
        assertEquals(handSizeBefore, handSizeAfter);
    }

    @Test
    public void testPlayer_getBoldStrategyCard_numeric_matchColor() {
        Player player2 = new Player("", true);
        String currentColor = Card.BLUE;

        // ditch the yellow three from getBoldStrategyCard_numeric_hand
        // now it should favor the blue nine, because it's the next biggest matching color group
        getBoldStrategyCard_numeric_hand.discard(yellowThree);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());
        int handSizeBefore = player2.getHand().getSize();
        assertTrue(player2.getBoldStrategyCard(getBoldStrategyCard_currentPlayedCard, currentColor).equals(blueNine));
        int handSizeAfter = player2.getHand().getSize();
        assertEquals(handSizeBefore, handSizeAfter);

        // now ditch the green three and the blue nine, leaving no other legal choice but the blue four
        getBoldStrategyCard_numeric_hand.discard(greenThree);
        getBoldStrategyCard_numeric_hand.discard(blueNine);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());
        assertTrue(player2.getBoldStrategyCard(getBoldStrategyCard_currentPlayedCard, currentColor).equals(blueFour));
    }

    /*
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.GREEN, Card.THREE, cvm)); // legal (ditched)
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.BLUE, Card.NINE, cvm)); // legal (ditched)
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.THREE, cvm)); // legal (ditched)
     */

    @Test
    public void testPlayer_getBoldStrategyCard_nonNumeric() {
        Player player2 = new Player("", true);
        String currentColor = Card.BLUE;

        // ditch the same cards as in testPlayer_getBoldStrategyCard_numeric_matchColor
        getBoldStrategyCard_numeric_hand.discard(yellowThree);
        getBoldStrategyCard_numeric_hand.discard(greenThree);
        getBoldStrategyCard_numeric_hand.discard(blueNine);

        // ditch the blue four as well
        getBoldStrategyCard_numeric_hand.discard(new Card(Card.BLUE, Card.FOUR, cvm));

        // add a blue reverse to the hand, making it the only viable option at this point.
        Card blueReverse = new Card(Card.BLUE, Card.REVERSE, cvm);

        // set the new hand
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());

        // should return the blue reverse
        getBoldStrategyCard_numeric_hand.addCard(blueReverse);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());
        int handSizeBefore = player2.getHand().getSize();
        assertTrue(player2.getBoldStrategyCard(getBoldStrategyCard_currentPlayedCard, currentColor).equals(blueReverse));
        int handSizeAfter = player2.getHand().getSize();
        assertEquals(handSizeBefore, handSizeAfter);

        // ditch the blue reverse
        getBoldStrategyCard_numeric_hand.discard(blueReverse);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());

        // add a wild card to the hand, assert that it comes back
        Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        getBoldStrategyCard_numeric_hand.addCard(wild);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());
        handSizeBefore = player2.getHand().getSize();
        assertTrue(player2.getBoldStrategyCard(getBoldStrategyCard_currentPlayedCard, currentColor).equals(wild));
        handSizeAfter = player2.getHand().getSize();
        assertEquals(handSizeBefore, handSizeAfter);

        // ditch the wild
        getBoldStrategyCard_numeric_hand.discard(wild);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());

        // add a wild draw four card to the hand
        Card wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        getBoldStrategyCard_numeric_hand.addCard(wildDrawFour);
        player2.setHand(getBoldStrategyCard_numeric_hand.getAllCards());
        handSizeBefore = player2.getHand().getSize();
        assertTrue(player2.getBoldStrategyCard(getBoldStrategyCard_currentPlayedCard, currentColor).equals(wildDrawFour));
        handSizeAfter = player2.getHand().getSize();
        assertEquals(handSizeBefore, handSizeAfter);
    }
}





















