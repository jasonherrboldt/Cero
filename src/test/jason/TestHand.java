package test.jason;

import com.jason.*;
import org.junit.*;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

/**
 * Tests the Hand class methods.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class TestHand {

    private List<List<Card>> mockHand;
    private Hand realHand;
    private Hand getHighestFaceHand;
    private Hand getNumberOfMostColorsHand;
    private CardValueMap cvm;


    @Before
    public void setup() {
        cvm = new CardValueMap();

        // Create a mock hand and mimic the implementation of Hand.addCard.
        mockHand = new ArrayList<>();
        List<Card> blueCards = new ArrayList<>();
        blueCards.add(new Card(Card.BLUE, Card.ONE, cvm));
        blueCards.add(new Card(Card.BLUE, Card.EIGHT, cvm));
        blueCards.add(new Card(Card.BLUE, Card.NINE, cvm));
        mockHand.add(blueCards); // check

        List<Card> yellowCards = new ArrayList<>();
        yellowCards.add(new Card(Card.YELLOW, Card.SIX, cvm));
        yellowCards.add(new Card(Card.YELLOW, Card.REVERSE, cvm));
        mockHand.add(yellowCards); // check

        List<Card> redCards = new ArrayList<>();
        redCards.add(new Card(Card.RED, Card.ZERO, cvm));
        mockHand.add(redCards); // check

        List<Card> greenCards = new ArrayList<>();
        greenCards.add(new Card(Card.GREEN, Card.TWO, cvm));
        mockHand.add(greenCards); // check

        // Adding same cards as mockHand, but in a random order.
        realHand = new Hand();
        realHand.addCard(new Card(Card.BLUE, Card.NINE, cvm));
        realHand.addCard(new Card(Card.RED, Card.ZERO, cvm));
        realHand.addCard(new Card(Card.YELLOW, Card.SIX, cvm));
        realHand.addCard(new Card(Card.GREEN, Card.TWO, cvm));
        realHand.addCard(new Card(Card.BLUE, Card.ONE, cvm));
        realHand.addCard(new Card(Card.YELLOW, Card.REVERSE, cvm));
        realHand.addCard(new Card(Card.BLUE, Card.EIGHT, cvm));

        getHighestFaceHand = new Hand();
        getHighestFaceHand.addCard(new Card(Card.RED, Card.THREE, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.TWO, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.NINE, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.REVERSE, cvm));
        getHighestFaceHand.addCard(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));

        getNumberOfMostColorsHand = new Hand();
        getNumberOfMostColorsHand.addCard(new Card(Card.RED, Card.THREE, cvm));
        getNumberOfMostColorsHand.addCard(new Card(Card.RED, Card.NINE, cvm));
        getNumberOfMostColorsHand.addCard(new Card(Card.RED, Card.ZERO, cvm));
        getNumberOfMostColorsHand.addCard(new Card(Card.BLUE, Card.TWO, cvm));
        getNumberOfMostColorsHand.addCard(new Card(Card.BLUE, Card.NINE, cvm));
        getNumberOfMostColorsHand.addCard(new Card(Card.GREEN, Card.NINE, cvm));
    }
    
    @Test
    public void testHand_addCard() {
        List<Card> allMockHandCards = getAllCardsFromMockHand(mockHand);
        List<Card> allHandCards = realHand.getAllCards();
        try {
            assertFalse(mismatchFound(allHandCards, allMockHandCards));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHand_getColorList() {
        List<Card> realBlueCards = realHand.getColorList(Card.BLUE);
        List<Card> mockBlueCards = new ArrayList<>();
        mockBlueCards.add(new Card(Card.BLUE, Card.ONE, cvm));
        mockBlueCards.add(new Card(Card.BLUE, Card.EIGHT, cvm));
        mockBlueCards.add(new Card(Card.BLUE, Card.NINE, cvm));
        try {
            assertFalse(mismatchFound(realBlueCards, mockBlueCards));
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHand_discard() {
        Hand tempRealHand = realHand;
        int tempRealHandSizeBefore = tempRealHand.getSize();
        Card inTempRealHand = new Card(Card.BLUE, Card.NINE, cvm);
        // Card inTempRealHand = new Card(Card.BLUE, Card.EIGHT, cvm); // test the test - should fail
        try {
            realHand.discard(inTempRealHand);
        } catch (IllegalArgumentException e) {
            fail();
        }
        int tempRealHandSizeAfter = realHand.getSize();
        assertEquals(tempRealHandSizeBefore, (tempRealHandSizeAfter + 1));
    }

    @Test
    public void testHand_discard_exception() {
        Hand tempRealHand = realHand;
        Card notInTempRealHand = new Card(Card.BLUE, Card.TWO, cvm);
        try {
            tempRealHand.discard(notInTempRealHand);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Card not in hand.");
        }
    }

    @Test
    public void testHand_hasCard() {
        assertTrue(realHand.hasCard(new Card(Card.BLUE, Card.NINE, cvm)));
        assertFalse(realHand.hasCard(new Card(Card.BLUE, Card.ZERO, cvm)));
    }

    @Test
    public void testHand_getAllCards() {
        List<Card> realCards = realHand.getAllCards();
        assertEquals(realCards.size(), 7);
    }

    @Test
    public void testHand_sumCards() {
        assertEquals(realHand.sumCards(), 46);
    }

    @Test
    public void testHand_getHighestFace_numeric() {
        String color = Card.BLUE;
        Card inputCard = new Card(Card.BLUE, Card.NINE, cvm);
        Card outputCard = getHighestFaceHand.getHighestFace(color, true);
        assertTrue(inputCard.equals(outputCard));
    }

    @Ignore // ******** BROKEN ********
    public void testHand_getHighestFace_nonNumeric() {
        String color = Card.BLUE;
        Card inputCard = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
        Card outputCard = this.getHighestFaceHand.getHighestFace(color, false);
        assertTrue(inputCard.equals(outputCard));
    }

    @Test
    public void testHand_getHighestFace_noColor() {
        assertEquals(getHighestFaceHand.getHighestFace(Card.GREEN, true), null);
    }




    /*
        getHighestFaceHand.addCard(new Card(Card.RED, Card.THREE, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.TWO, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.FOUR, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.NINE, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.REVERSE, cvm));
        getHighestFaceHand.addCard(new Card(Card.BLUE, Card.WILD_DRAW_FOUR, cvm));
     */

    @Ignore
    public void testHand_getHighestNonNumericFace_nonWild() {
        // ditch the wd4
        // getHighestFaceHand.discard();
        // assertEquals(getHighestFaceHand.getHighestNonNumericFace(Card.BLUE), blueReverse);
    }

    @Ignore
    public void testHand_getHighestNonNumericFace_nonNumeric() {
        String color = Card.BLUE;
        Card inputCard = new Card(Card.BLUE, Card.WILD_DRAW_FOUR, cvm);
        Card outputCard = this.getHighestFaceHand.getHighestNonNumericFace(color);
        assertTrue(inputCard.equals(outputCard));
    }

    @Ignore
    public void testHand_getHighestNonNumericFace_noColor() {
        Card noCard = getHighestFaceHand.getHighestNonNumericFace(Card.GREEN);
        assertNull(noCard);
    }






    @Test
    public void testHand_getNumber() {
        Card correctCard = new Card(Card.RED, Card.NINE, cvm);
        Card testCard = getNumberOfMostColorsHand.getNumberFromLargestColorGroup(9);
        assertTrue(correctCard.equals(testCard));

        // Make sure it returns null if card not found.
        Card noCard = getNumberOfMostColorsHand.getNumberFromLargestColorGroup(4);
        assertNull(noCard);
    }

    @Test
    public void testHand_getHighestColor() {
        String highestColor = getNumberOfMostColorsHand.getHighestColor();
        assertTrue(highestColor.equalsIgnoreCase(Card.RED));
        assertFalse(highestColor.equalsIgnoreCase(Card.BLUE));
    }

    @Test
    public void testHand_getHandValue() {
        assertEquals(getHighestFaceHand.getHandValue(), 88);
    }

    @Test
    public void testHand_moveColorlessListToEnd() {
        Hand hand = new Hand();

        List<List<Card>> mockHand = new ArrayList<>();

        List<Card> colorList1 = new ArrayList<>();
        colorList1.add(new Card(Card.BLUE, Card.SEVEN, cvm));
        colorList1.add(new Card(Card.BLUE, Card.ONE, cvm));
        colorList1.add(new Card(Card.BLUE, Card.SIX, cvm));
        mockHand.add(colorList1);

        List<Card> colorList2 = new ArrayList<>();
        colorList2.add(new Card(Card.COLORLESS, Card.WILD, cvm));
        colorList2.add(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm));
        mockHand.add(colorList2);

        List<Card> colorList3 = new ArrayList<>();
        colorList3.add(new Card(Card.GREEN, Card.NINE, cvm));
        mockHand.add(colorList3);

        hand.setHand(mockHand);
        assertEquals(hand.getAllCards().get(hand.getSize() - 1).getColor(), Card.GREEN);

        hand.moveColorlessListToEnd();
        assertEquals(hand.getAllCards().get(hand.getSize() - 1).getColor(), Card.COLORLESS);
    }

    @Test
    public void testHand_getColorGroupSize() {
        assertEquals(getNumberOfMostColorsHand.getColorGroupSize(Card.RED), 3);
        assertEquals(getNumberOfMostColorsHand.getColorGroupSize(Card.BLUE), 2);
        assertEquals(getNumberOfMostColorsHand.getColorGroupSize(Card.GREEN), 1);
    }




    /*
     * Private helper methods:
     */
    private boolean mismatchFound(List<Card> cList1, List<Card> cList2) throws IllegalArgumentException {
        boolean mismatchFound = false;
        if(cList1.size() == cList2.size()) {
            for (int i = 0; i < cList1.size(); i++) {
                if (!(cList1.get(i).getColor().equalsIgnoreCase(cList2.get(i).getColor())
                        && cList1.get(i).getFace().equalsIgnoreCase(cList2.get(i).getFace()))) {
                    mismatchFound = true;
                }
            }
        } else {
            throw new IllegalArgumentException("cList1 must be the same size as cList2.");
        }
        return mismatchFound;
    }

    private List<Card> getAllCardsFromMockHand(List<List<Card>> hand) {
        List<Card> cards = new ArrayList<>();
        for(List<Card> list : hand) {
            for(Card c : list) {
                cards.add(c);
            }
        }
        return cards;
    }
}