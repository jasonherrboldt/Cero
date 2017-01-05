package test.jason;

import com.jason.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;

public class TestPlayer {

    private Card blueNine;
    private Card yellowThree;
    private Card greenThree;
    private Card blueFour;
    private Card yellowSix;
    private Card blueDrawTwo;private Card wild;
    private Card wildDrawFour;
    private Card redDrawTwo;
    private Card yellowReverse;
    private CardValueMap cvm;
    private Hand classHand;
    private Hand cautiousHand;
    private List<Card> cardList;
    private Hand getBoldStrategyCard_numeric_hand;
    private Player player2;

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

        // all legal cards that can match the cpc:
        blueNine = new Card(Card.BLUE, Card.NINE, cvm);
        yellowThree = new Card(Card.YELLOW, Card.THREE, cvm);
        greenThree = new Card(Card.GREEN, Card.THREE, cvm);
        blueFour = new Card(Card.BLUE, Card.FOUR, cvm);
        yellowSix = new Card(Card.YELLOW, Card.SIX, cvm);
        blueDrawTwo = new Card(Card.BLUE, Card.DRAW_TWO, cvm);
        wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        redDrawTwo = new Card(Card.RED, Card.DRAW_TWO, cvm);
        yellowReverse = new Card(Card.YELLOW, Card.REVERSE, cvm);

        getBoldStrategyCard_numeric_hand = new Hand();
        getBoldStrategyCard_numeric_hand.addCard(greenThree);
        getBoldStrategyCard_numeric_hand.addCard(redDrawTwo);
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        getBoldStrategyCard_numeric_hand.addCard(blueNine);
        getBoldStrategyCard_numeric_hand.addCard(blueFour);
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        getBoldStrategyCard_numeric_hand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        getBoldStrategyCard_numeric_hand.addCard(yellowThree);


        cautiousHand = new Hand();
        cautiousHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        cautiousHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        cautiousHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        cautiousHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.REVERSE, cvm));
        cautiousHand.addCard(new Card(Card.COLORLESS, Card.WILD, cvm));
        cautiousHand.addCard(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));
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
    public void testPlayer_isLegalDiscard_legal_numberNotColor() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        assertTrue(player2.isLegalDiscard(greenThree, yellowThree));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_colorNotNumber() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        assertTrue(player2.isLegalDiscard(yellowSix, yellowThree));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_colorAndNumber() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        assertTrue(player2.isLegalDiscard(yellowSix, yellowSix));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_nonNumericNonWild() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        player2.getHand().addCard(blueDrawTwo);
        assertTrue(player2.isLegalDiscard(blueDrawTwo, blueFour));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_wild() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        player2.getHand().addCard(wild);
        assertTrue(player2.isLegalDiscard(wild, blueFour));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_wildDrawFour() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        player2.getHand().addCard(wildDrawFour);

        int handSize = player2.getHand().getSize();
        assertTrue(player2.isLegalDiscard(wildDrawFour, blueFour));
    }

    @Test
    public void testPlayer_isLegalDiscard_illegal() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());

        int handSize = player2.getHand().getSize();
        int cardsDiscarded = 0;

        assertFalse(player2.isLegalDiscard(yellowSix, blueNine));
        assertEquals(player2.getHand().getSize(), (handSize - cardsDiscarded));
    }

    @Test
    public void testPlayer_setRandomStrategy_1() {
        player2 = new Player("", true);
        assertTrue(player2.getStrategy().equals(""));
        assertTrue(player2.setRandomStrategy());
        assertFalse(player2.getStrategy().equals(""));
    }

    @Test
    public void testPlayer_setRandomStrategy_2() {
        player2 = new Player("", true);
        player2.setRandomStrategy();
        String firstStrategy = player2.getStrategy();
        boolean strategyReplaced = false;
        // generate 20 random strategies and assert at least one of them comes back different.
        for(int i = 0; i < 20; i++) {
            player2.setRandomStrategy();
            if (!player2.getStrategy().equalsIgnoreCase(firstStrategy)) {
                strategyReplaced = true;
            }
        }
        assertTrue(strategyReplaced);
    }

    @Test
    public void testPlayer_pickStrategyColor() {
        // Can't test for a dumb player -- returns a random color.
        // Bold and cautious players always return color of highest count.
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());

        player2.setStrategy(Player.STRATEGY_BOLD);
        assertEquals(player2.pickStrategyColor(), Card.YELLOW);

        player2.setStrategy(Player.STRATEGY_CAUTIOUS);
        assertEquals(player2.pickStrategyColor(), Card.YELLOW);

        // Test the unhappy path.
        Player player1 = new Player("", false);
        assertEquals(player1.pickStrategyColor(), null);
    }

    @Test
    public void testPlayer_getCautiousStrategyCard_null() {
        player2 = new Player("", true);
        List<Card> cards = new ArrayList<>();
        Card redOne = new Card(Card.RED, Card.ONE, cvm);
        Card blueTwo = new Card(Card.BLUE, Card.TWO, cvm);
        Card greenThree = new Card(Card.GREEN, Card.THREE, cvm);
        cards.add(redOne);
        cards.add(blueTwo);
        cards.add(greenThree);
        player2.setHand(cards);
        Card currentPlayedCard = new Card(Card.YELLOW, Card.SIX, cvm);
        String currentColor = Card.YELLOW;
        assertEquals(player2.getCautiousStrategyCard(currentPlayedCard, currentColor), null);
    }

    @Test
    public void testPlayer_getCautiousStrategyCard_wild() {
        player2 = new Player("", true);
        player2.setHand(cautiousHand.getAllCards());
        Card currentPlayedCard = new Card(Card.COLORLESS, Card.WILD, cvm);
        String currentColor = Card.YELLOW;
        assertTrue(player2.getCautiousStrategyCard(currentPlayedCard, currentColor).equals(yellowReverse));
    }

    @Test
    public void testPlayer_getCautiousStrategyCard_higherFirst_wd4() {
        // make sure the cautious strategy gets rid of higher value cards first
        // when there are other legal lower value cards in the hand.
        player2 = new Player("Player Two", true);
        player2.setHand(cautiousHand.getAllCards());

        // get rid of the wild so the wd4 is the most attractive option
        player2.getHand().discard(wild);
        assertTrue(player2.getCautiousStrategyCard(yellowThree, yellowThree.getColor()).equals(wildDrawFour));
    }

    @Ignore // *** UNDER CONSTRUCTION *************************************************************************************************
    public void testPlayer_getCautiousStrategyCard_higherFirst_drawTwo() {
        // make sure the cautious strategy gets rid of higher value cards first
        // when there are other legal lower value cards in the hand.
        player2 = new Player("Player Two", true);
        player2.setHand(cautiousHand.getAllCards());

        // let the cpc be a red nine
        // get rid of the wild and wd4 cards so the red draw two is the most attractive option
        player2.getHand().discard(wild);
        player2.getHand().discard(wildDrawFour);
        Card redNine = new Card(Card.RED, Card.NINE, cvm);
        assertTrue(player2.getCautiousStrategyCard(redNine, redNine.getColor()).equals(redDrawTwo));
    }

    @Test
    public void testPlayer_getCautiousStrategyCard_numeric_matchColor() {
        // assert that, given no non-numeric cards, a color match is possible without a numeric match
        player2 = new Player("Player Two", true);
        player2.setHand(cautiousHand.getAllCards());

        // let the cpc be a yellow three
        // get rid of all non-numeric cards and the green three so the yellow six is the most attractive option
        player2.getHand().discard(wild);
        player2.getHand().discard(wildDrawFour);
        player2.getHand().discard(yellowReverse);
        player2.getHand().discard(redDrawTwo);
        player2.getHand().discard(greenThree);

        assertTrue(player2.getCautiousStrategyCard(yellowThree, yellowThree.getColor()).equals(yellowSix));
    }

    @Test
    public void testPlayer_getCautiousStrategyCard_numeric_matchNumber() {
        // assert that, given no non-numeric cards, a numeric match is possible without a color match
        player2 = new Player("Player Two", true);
        player2.setHand(cautiousHand.getAllCards());

        // let the cpc be a blue eight
        Card blueEight = new Card(Card.BLUE, Card.EIGHT, cvm);

        // get rid of all non-numeric cards and the blue four so the red eight is the most attractive option
        player2.getHand().discard(wild);
        player2.getHand().discard(wildDrawFour);
        player2.getHand().discard(yellowReverse);
        player2.getHand().discard(redDrawTwo);
        player2.getHand().discard(blueFour);

        Card redEight = new Card(Card.RED, Card.EIGHT, cvm);

        assertTrue(player2.getCautiousStrategyCard(blueEight, yellowThree.getColor()).equals(redEight));
    }

    /*
        cautiousHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        cautiousHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        cautiousHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        cautiousHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));
        cautiousHand.addCard(new Card(Card.YELLOW, Card.REVERSE, cvm));
        cautiousHand.addCard(new Card(Card.COLORLESS, Card.WILD, cvm));
        cautiousHand.addCard(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));
     */

}























