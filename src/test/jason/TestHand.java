package test.jason;

import com.jason.*;
import com.sun.source.tree.AssertTree;
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


    @Before
    public void setup() {
        // Create a mock hand and mimic the implementation of Hand.addCard.
        mockHand = new ArrayList<>();
        
        List<Card> blueCards = new ArrayList<>();
        blueCards.add(new Card(Deck.BLUE, Deck.ONE, 1));
        blueCards.add(new Card(Deck.BLUE, Deck.NINE, 9));
        blueCards.add(new Card(Deck.BLUE, Deck.WILD, 50));
        mockHand.add(blueCards);

        List<Card> yellowCards = new ArrayList<>();
        yellowCards.add(new Card(Deck.YELLOW, Deck.SIX, 6));
        yellowCards.add(new Card(Deck.YELLOW, Deck.REVERSE, 20));
        mockHand.add(yellowCards);

        List<Card> redCards = new ArrayList<>();
        redCards.add(new Card(Deck.RED, Deck.WILD_DRAW_FOUR, 50));
        mockHand.add(redCards);

        List<Card> greenCards = new ArrayList<>();
        greenCards.add(new Card(Deck.GREEN, Deck.TWO, 2));
        mockHand.add(greenCards);

        // Adding same cards as mockHand, but in a random order.
        realHand = new Hand();
        realHand.addCard(new Card(Deck.BLUE, Deck.NINE, 9));
        realHand.addCard(new Card(Deck.RED, Deck.WILD_DRAW_FOUR, 50));
        realHand.addCard(new Card(Deck.YELLOW, Deck.SIX, 6));
        realHand.addCard(new Card(Deck.GREEN, Deck.TWO, 2));
        realHand.addCard(new Card(Deck.BLUE, Deck.ONE, 1));
        realHand.addCard(new Card(Deck.YELLOW, Deck.REVERSE, 20));
        realHand.addCard(new Card(Deck.BLUE, Deck.WILD, 50));

        getHighestFaceHand = new Hand();
        getHighestFaceHand.addCard(new Card(Deck.RED, Deck.THREE, 3));
        getHighestFaceHand.addCard(new Card(Deck.BLUE, Deck.TWO, 2));
        getHighestFaceHand.addCard(new Card(Deck.BLUE, Deck.FOUR, 4));
        getHighestFaceHand.addCard(new Card(Deck.BLUE, Deck.NINE, 9));
        getHighestFaceHand.addCard(new Card(Deck.BLUE, Deck.REVERSE, 20));
        getHighestFaceHand.addCard(new Card(Deck.BLUE, Deck.WILD_DRAW_FOUR, 50));

        getNumberOfMostColorsHand = new Hand();
        getNumberOfMostColorsHand.addCard(new Card(Deck.RED, Deck.THREE, 3));
        getNumberOfMostColorsHand.addCard(new Card(Deck.RED, Deck.NINE, 9));
        getNumberOfMostColorsHand.addCard(new Card(Deck.RED, Deck.ZERO, 0));
        getNumberOfMostColorsHand.addCard(new Card(Deck.BLUE, Deck.TWO, 2));
        getNumberOfMostColorsHand.addCard(new Card(Deck.BLUE, Deck.NINE, 9));
        getNumberOfMostColorsHand.addCard(new Card(Deck.GREEN, Deck.NINE, 9));
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
        List<Card> realBlueCards = realHand.getColorList(Deck.BLUE);
        List<Card> mockBlueCards = new ArrayList<>();
        mockBlueCards.add(new Card(Deck.BLUE, Deck.ONE, 1));
        mockBlueCards.add(new Card(Deck.BLUE, Deck.NINE, 9));
        mockBlueCards.add(new Card(Deck.BLUE, Deck.WILD, 50));
        try {
            assertFalse(mismatchFound(realBlueCards, mockBlueCards));
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHand_discard() {
        // List<List<Card>> tempMockHand = mockHand;
        Hand tempRealHand = realHand;
        int tempRealHandSizeBefore = tempRealHand.getSize();
        Card inTempRealHand = new Card(Deck.BLUE, Deck.NINE, 9);
        // Card inTempRealHand = new Card(Deck.BLUE, Deck.EIGHT); // test the test - should fail
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
        Card notInTempRealHand = new Card(Deck.GREEN, Deck.WILD, 50);
        try {
            tempRealHand.discard(notInTempRealHand);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Card not in hand.");
        }
    }

    // Private helper methods:

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

    @Test
    public void testHand_hasCard() {
        assertTrue(realHand.hasCard(new Card(Deck.BLUE, Deck.NINE, 9)));
        assertFalse(realHand.hasCard(new Card(Deck.BLUE, Deck.EIGHT, 8)));
    }

    @Test
    public void testHand_getAllCards() {
        List<Card> realCards = realHand.getAllCards();
        assertEquals(realCards.size(), 7);
    }

    @Test
    public void testHand_sumCards() {
        assertEquals(realHand.sumCards(), 138);
    }

    @Test
    public void testHand_getHighestFace_numeric() {
        String color = Deck.BLUE;
        Card inputCard = new Card(Deck.BLUE, Deck.NINE, 9);
        Card outputCard = getHighestFaceHand.getHighestFace(color, true);
        assertTrue(inputCard.equals(outputCard));
    }

    @Test
    public void testHand_getHighestFace_nonNumeric() {
        String color = Deck.BLUE;
        Card inputCard = new Card(Deck.BLUE, Deck.WILD_DRAW_FOUR, 50);
        Card outputCard = this.getHighestFaceHand.getHighestFace(color, false);
        assertTrue(inputCard.equals(outputCard));
    }

    @Test
    public void testHand_getHighestFace_noColor() {
        Card noCard = getHighestFaceHand.getHighestFace(Deck.GREEN, true);
        assertNull(noCard);
    }

    /*
     * Gets number of largest available color group. Checks all color groups descending by color group size
     * and returns best choice.
     */
    @Test
    public void testHand_getNumber() {
        Card correctCard = new Card(Deck.RED, Deck.NINE, 9);
        Card testCard = getNumberOfMostColorsHand.getNumber(9);
        assertTrue(correctCard.equals(testCard));

        // Make sure it returns null if card not found.
        Card noCard = getNumberOfMostColorsHand.getNumber(4);
        assertNull(noCard);
    }

}




























