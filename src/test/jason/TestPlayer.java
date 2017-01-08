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
    private Card yellowNine;
    private Card yellowZero;
    private Card yellowOne;
    private CardValueMap cvm;
    private Hand classHand;
    private Hand strategyHand;
    private List<Card> cardList;
    private Player player2;
    private String blue;

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

        // various testing cards
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
        yellowNine = new Card(Card.YELLOW, Card.NINE, cvm);
        yellowZero = new Card(Card.YELLOW, Card.ZERO, cvm);
        yellowOne = new Card(Card.YELLOW, Card.ONE, cvm);

        strategyHand = new Hand();
        strategyHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        strategyHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        strategyHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        strategyHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.REVERSE, cvm));
        strategyHand.addCard(new Card(Card.COLORLESS, Card.WILD, cvm));
        strategyHand.addCard(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));

        blue = Card.BLUE;
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
        assertTrue(player2.isLegalDiscard(greenThree, yellowThree, blue));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_colorNotNumber() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        assertTrue(player2.isLegalDiscard(yellowSix, yellowThree, blue));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_colorAndNumber() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        assertTrue(player2.isLegalDiscard(yellowSix, yellowSix, blue));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_nonNumericNonWild() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        player2.getHand().addCard(blueDrawTwo);
        assertTrue(player2.isLegalDiscard(blueDrawTwo, blueFour, blue));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_wild() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        player2.getHand().addCard(wild);
        assertTrue(player2.isLegalDiscard(wild, blueFour, blue));
    }

    @Test
    public void testPlayer_isLegalDiscard_legal_wildDrawFour() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());
        player2.getHand().addCard(wildDrawFour);

        int handSize = player2.getHand().getSize();
        assertTrue(player2.isLegalDiscard(wildDrawFour, blueFour, blue));
    }

    @Test
    public void testPlayer_isLegalDiscard_illegal() {
        player2 = new Player("", true);
        player2.setHand(classHand.getAllCards());

        int handSize = player2.getHand().getSize();
        int cardsDiscarded = 0;

        assertFalse(player2.isLegalDiscard(yellowSix, blueNine, blue));
        assertEquals(player2.getHand().getSize(), (handSize - cardsDiscarded));
    }

    // need more isLegalDiscard tests here for when cpc is non-numeric! ****************************************

    @Test
    public void testPlayer_setRandomStrategy_1() {
        player2 = new Player("", true);
        assertTrue(player2.getStrategy().equals(""));
        assertTrue(player2.setRandomStrategy());
        assertFalse(player2.getStrategy().equals(""));
    }

    /**
     * This test depends on looping many times to determine that a random selection has changed.
     * If this test fails, simply run it again - it should not fail more than once in a blue moon.
     */
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
    }

    @Test
    public void testPlayer_changeStrategy() {
        Game game = new Game("Player One", true);
        game.getPlayer2().setStrategy(Player.STRATEGY_BOLD);
        game.startGame(null, true);
        // game.playFirstHand();
        assertEquals(game.getPlayer2().getStrategy(), Player.STRATEGY_BOLD);
        for(int i = 0; i < 5; i++) {
            game.getPlayer1().getHand().discard(game.getPlayer1().getHand().getFirstCard());
        }
        assertTrue(game.getPlayer1().getHand().getSize() < 4);
        game.setPlayerOnesTurn(false);
        game.playerTwosTurn();
        assertEquals(game.getPlayer2().getStrategy(), Player.STRATEGY_CAUTIOUS);
    }

    @Test
    public void testPlayer_getBoldStrategy_exception() {
        player2 = new Player("", true);

        // only 1st arg is null
        try {
            player2.getBoldStrategyCard(null, Card.BLUE);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }

        // only 2nd arg is null
        try {
            player2.getBoldStrategyCard(yellowThree, null);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }

        // both args are null
        try {
            player2.getBoldStrategyCard(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }
    }

    @Test
    public void testPlayer_getBoldStrategyCard_null() {
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
        assertEquals(player2.getBoldStrategyCard(currentPlayedCard, currentPlayedCard.getColor()), null);
    }

    @Test
    public void testPlayer_getBoldStrategyCard_wild() {
        player2 = new Player("", true);
        player2.setHand(strategyHand.getAllCards());
        Card currentPlayedCard = new Card(Card.COLORLESS, Card.WILD, cvm);
        String currentColor = Card.YELLOW;

        // I want the highest numeric yellow in the hand - that's a yellow six.
        assertTrue(player2.getBoldStrategyCard(currentPlayedCard, currentColor).equals(yellowSix));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_numeric_colorBeatsNumber() {

        // tempt algorithm with higher cards (strategy hand has both wild and wd4)

        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());

        // pick a cpc that could return two possible options
        // and assert that the higher value card comes back

        // make a cpc that might return a yellow six or a red eight
        // assert the yellow comes back, even though the red eight has a higher value
        Card redSix = new Card(Card.RED, Card.SIX, cvm);
        assertTrue(player2.getBoldStrategyCard(redSix, redSix.getColor()).equals(yellowSix));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_numeric_numberBeatsColor() {
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());
        player2.getHand().discard(yellowZero);
        Card yellowEight = new Card(Card.YELLOW, Card.EIGHT, cvm);
        assertTrue(player2.getBoldStrategyCard(yellowEight, yellowEight.getColor()).equals(yellowSix));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_numeric_matchColorNotNumber() {
        // assert that, given no non-numeric cards, a color match is possible without a numeric match
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());
        player2.getHand().discard(greenThree);
        player2.getHand().discard(yellowZero);
        assertTrue(player2.getBoldStrategyCard(yellowThree, yellowThree.getColor()).equals(yellowSix));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_numeric_matchNumberNotColor() {
        // assert that, given no non-numeric cards, a numeric match is possible without a color match
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());
        Card blueEight = new Card(Card.BLUE, Card.EIGHT, cvm);
        player2.getHand().discard(blueFour);
        player2.getHand().discard(yellowZero);
        Card redEight = new Card(Card.RED, Card.EIGHT, cvm);
        assertTrue(player2.getBoldStrategyCard(blueEight, yellowThree.getColor()).equals(redEight));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_higherLast_drawTwo() {
        // make sure the cautious strategy gets rid of higher value cards last
        // when there are no other legal lower value cards in the hand.
        // should pick non-numeric colors (skip, reverse, draw two) before wilds.
        player2 = new Player("Player Two", true);
        Hand hand = new Hand();
        player2.setHand(hand.getAllCards());
        player2.getHand().addCard(blueDrawTwo);
        player2.getHand().addCard(wild);
        player2.getHand().addCard(wildDrawFour);
        assertTrue(player2.getBoldStrategyCard(blueFour, blueFour.getColor()).equals(blueDrawTwo));
    }

    @Test
    public void testPlayer_getBoldStrategyCard_higherLast_wild_wd4() {
        // this is a bit of a trivial test, asserting the strategy will return
        // a wild or a wd4 if nothing else is present
        player2 = new Player("Player Two", true);
        Hand hand = new Hand();
        player2.setHand(hand.getAllCards());
        player2.getHand().addCard(wild);
        assertTrue(player2.getBoldStrategyCard(yellowThree, yellowThree.getColor()).equals(wild));

        player2.getHand().discard(wild);
        player2.getHand().addCard(wildDrawFour);
        assertTrue(player2.getBoldStrategyCard(yellowThree, yellowThree.getColor()).equals(wildDrawFour));
    }

    /*
        strategyHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        strategyHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        strategyHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        strategyHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.REVERSE, cvm));
        strategyHand.addCard(new Card(Card.COLORLESS, Card.WILD, cvm));
        strategyHand.addCard(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));
     */

    @Test
    public void testPlayer_getBoldStrategyCard_zeroColor() {
        // let the cpc be a yellow six, tempting the bold strategy with a perfect match
        // but see if bold returns a yellow zero instead for color groups with size > 2
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());
        assertTrue(player2.getBoldStrategyCard(yellowSix, yellowSix.getColor()).equals(yellowZero));

        // try again, but reduce the yellow color group to <= 2
        player2.getHand().discard(yellowSix);
        player2.getHand().discard(yellowReverse);

        assertTrue(player2.getBoldStrategyCard(yellowOne, yellowSix.getColor()).equals(yellowOne));
    }

    @Test
    public void testPlayer_getCautiousStrategyCard_exception() {
        player2 = new Player("", true);

        // only 1st arg is null
        try {
            player2.getCautiousStrategyCard(null, Card.BLUE);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }

        // only 2nd arg is null
        try {
            player2.getCautiousStrategyCard(yellowThree, null);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }

        // both args are null
        try {
            player2.getCautiousStrategyCard(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }
    }

    @Test
    public void testPlayer_getCautiousStrategyCardCard_null() {
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
        assertEquals(player2.getCautiousStrategyCard(currentPlayedCard, currentPlayedCard.getColor()), null);
    }

    @Test
    public void testPlayer_getCautiousStrategyCardCard_wild() {
        player2 = new Player("", true);
        player2.setHand(strategyHand.getAllCards());
        Card currentPlayedCard = new Card(Card.COLORLESS, Card.WILD, cvm);
        String currentColor = Card.YELLOW;
        assertTrue(player2.getCautiousStrategyCard(currentPlayedCard, currentColor).equals(yellowReverse));
    }

    @Test
    public void testPlayer_getCautiousStrategyCardCard_higherFirst_wd4() {
        // make sure the cautious strategy gets rid of higher value cards first
        // when there are other legal lower value cards in the hand.
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());

        // get rid of the wild so the wd4 is the most attractive option
        player2.getHand().discard(wild);
        assertTrue(player2.getCautiousStrategyCard(yellowThree, yellowThree.getColor()).equals(wildDrawFour));
    }

    @Test
    public void testPlayer_getCautiousStrategyCardCard_higherFirst_drawTwo() {
        // make sure the cautious strategy gets rid of higher value cards first
        // when there are other legal lower value cards in the hand.
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());

        // let the cpc be a red nine
        // get rid of the wild and wd4 cards so the red draw two is the most attractive option
        player2.getHand().discard(wild);
        player2.getHand().discard(wildDrawFour);
        Card redNine = new Card(Card.RED, Card.NINE, cvm);
        assertTrue(player2.getCautiousStrategyCard(redNine, redNine.getColor()).equals(redDrawTwo));
    }

    @Test
    public void testPlayer_getCautiousStrategyCardCard_numeric_matchColorNotNumber() {
        // assert that, given no non-numeric cards, a color match is possible without a numeric match
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());

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
    public void testPlayer_getCautiousStrategyCardCard_numeric_matchNumberNotColor() {
        // assert that, given no non-numeric cards, a numeric match is possible without a color match
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());

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

    @Test
    public void testPlayer_getCautiousStrategyCardCard_numeric_colorBeatsNumber() {
        player2 = new Player("Player Two", true);
        player2.setHand(strategyHand.getAllCards());

        // pick a cpc that could return two possible options
        // and assert that the higher value card comes back

        // drop the yellow reverse, the red draw two, and both the wild cards
        // only want to pit numeric cards against each other
        player2.getHand().discard(yellowReverse);
        player2.getHand().discard(redDrawTwo);
        player2.getHand().discard(wild);
        player2.getHand().discard(wildDrawFour);

        // make a cpc that might return a yellow six or a red eight
        // assert the red eight comes back, even though there are more sixes (i.e. bold strategy)
        Card redSix = new Card(Card.RED, Card.SIX, cvm);
        Card redEight = new Card(Card.RED, Card.EIGHT, cvm);
        assertTrue(player2.getCautiousStrategyCard(redSix, redSix.getColor()).equals(redEight));
    }

    @Test
    public void testPlayer_getCautiousStrategyCardCard_numeric_numberBeatsColor() {
        player2 = new Player("Player Two", true);

        // make a temp hand for custom scenario
        List<Card> tempHand = new ArrayList<>();
        Card redFive = new Card(Card.RED, Card.FIVE, cvm);
        tempHand.add(redFive);
        tempHand.add(yellowNine);
        player2.setHand(tempHand);

        // let the current played card be a red 9
        Card redNine = new Card(Card.RED, Card.NINE, cvm);
        assertTrue(player2.getCautiousStrategyCard(redNine, redNine.getColor()).equals(yellowNine));
    }

    @Test
    public void testPlayer_getDumbStrategyCard_null() {
        // make sure getDumbStrategyCard returns null if no legal card found
        player2 = new Player("", true);
        Hand hand = new Hand();
        player2.setHand(hand.getAllCards());
        assertEquals(player2.getDumbStrategyCard(yellowOne, yellowOne.getColor()), null);
    }

    @Test
    public void testPlayer_getDumbStrategy_exception() {
        player2 = new Player("", true);

        // only 1st arg is null
        try {
            player2.getDumbStrategyCard(null, Card.BLUE);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }

        // only 2nd arg is null
        try {
            player2.getDumbStrategyCard(yellowThree, null);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }

        // both args are null
        try {
            player2.getDumbStrategyCard(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "One or both args were null.");
        }
    }

    /*
        strategyHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        strategyHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        strategyHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        strategyHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));
        strategyHand.addCard(new Card(Card.YELLOW, Card.REVERSE, cvm));
        strategyHand.addCard(new Card(Card.COLORLESS, Card.WILD, cvm));
        strategyHand.addCard(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));
     */

    @Test
    public void testPlayer_getDumbStrategyCard_oneWinner_color() {
        // verify that the dumb strategy returns the one winning card in its hand.
        player2 = new Player("", true);
        player2.setHand(strategyHand.getAllCards());
        player2.getHand().discard(wild);
        player2.getHand().discard(wildDrawFour);
        assertTrue(player2.getDumbStrategyCard(blueNine, blueNine.getColor()).equals(blueFour));
    }

    /**
     * This test depends on looping many times to determine that a random selection has changed.
     * If this test fails, simply run it again - it should not fail more than once in a blue moon.
     */
    @Test
    public void testPlayer_getDumbStrategyCard_differentWinners() {
        player2 = new Player("", true);
        player2.setHand(strategyHand.getAllCards());
        boolean differentLegalCardFound = false;
        Card firstDiscard = player2.getDumbStrategyCard(yellowSix, yellowSix.getColor());
        for(int i = 0; i < 25; i++) {
            Card loopCard = player2.getDumbStrategyCard(yellowSix, yellowSix.getColor());
            if(!loopCard.equals(firstDiscard)) {
                differentLegalCardFound = true;
            }
        }
        assertTrue(differentLegalCardFound);
    }

    @Test
    public void testPlayer_selectNewColor_exceptions() {
        Player player1 = new Player("", false);
        player2 = new Player("", true);

        // 1st illegal state
        try {
            player1.selectNewColor();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Can only be called by player 2.");
        }

        // 2nd illegal state
        player2.setLastPlayedCard(yellowNine);
        try {
            player2.selectNewColor();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "Can only be called when player 2 discards a wild or wd4.");
        }
    }

    /**
     * This test depends on looping many times to determine that a random selection has changed.
     * If this test fails, simply run it again - it should not fail more than once in a blue moon.
     */
    @Test
    public void testPlayer_selectNewColor_dumb() {
        player2 = new Player("", true);
        player2.setStrategy(Player.STRATEGY_DUMB);
        player2.setLastPlayedCard(wild);
        String firstRandomColor = player2.selectNewColor();
        boolean differentColorFound = false;
        for(int i = 0; i < 25; i++) {
            String nextRandomColor = player2.selectNewColor();
            if(!nextRandomColor.equalsIgnoreCase(firstRandomColor)) {
                differentColorFound = true;
            }
        }
        assertTrue(differentColorFound);
    }

    @Test
    public void testPlayer_selectNewColor_notDumb() {
        player2 = new Player("", true);
        player2.setStrategy(Player.STRATEGY_DUMB);
        player2.setLastPlayedCard(wild);

        // strategyHand has more yellow cards than any other color
        player2.setHand(strategyHand.getAllCards());

        // bold
        player2.setStrategy(Player.STRATEGY_BOLD);
        assertEquals(player2.selectNewColor(), Card.YELLOW);

        // cautious
        player2.setStrategy(Player.STRATEGY_CAUTIOUS);
        assertEquals(player2.selectNewColor(), Card.YELLOW);
    }
}
























