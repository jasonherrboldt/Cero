package test.jason;

import com.jason.*;
import org.junit.*;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Tests the Hand class methods.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class TestHand {

    private List<List<Card>> mockHand;
    Hand realHand;

    @Before
    public void setup() {
        // Create a mock hand and mimic the implementation of Hand.addCard.
        mockHand = new ArrayList<>();
        
        List<Card> blueCards = new ArrayList<>();
        blueCards.add(new Card(Deck.BLUE, Deck.ONE));
        blueCards.add(new Card(Deck.BLUE, Deck.NINE));
        blueCards.add(new Card(Deck.BLUE, Deck.WILD));
        mockHand.add(blueCards);

        List<Card> redCards = new ArrayList<>();
        redCards.add(new Card(Deck.RED, Deck.WILD_DRAW_FOUR));
        mockHand.add(redCards);

        List<Card> yellowCards = new ArrayList<>();
        yellowCards.add(new Card(Deck.YELLOW, Deck.SIX));
        yellowCards.add(new Card(Deck.YELLOW, Deck.REVERSE));
        mockHand.add(yellowCards);

        List<Card> greenCards = new ArrayList<>();
        greenCards.add(new Card(Deck.GREEN, Deck.TWO));
        mockHand.add(greenCards);

        // Adding same cards as mockHand, but in a random order.
        realHand = new Hand();
        realHand.addCard(new Card(Deck.BLUE, Deck.NINE));
        realHand.addCard(new Card(Deck.RED, Deck.WILD_DRAW_FOUR));
        realHand.addCard(new Card(Deck.YELLOW, Deck.SIX));
        realHand.addCard(new Card(Deck.GREEN, Deck.TWO));
        realHand.addCard(new Card(Deck.BLUE, Deck.ONE));
        realHand.addCard(new Card(Deck.YELLOW, Deck.REVERSE));
        realHand.addCard(new Card(Deck.BLUE, Deck.WILD));
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
        mockBlueCards.add(new Card(Deck.BLUE, Deck.ONE));
        mockBlueCards.add(new Card(Deck.BLUE, Deck.NINE));
        mockBlueCards.add(new Card(Deck.BLUE, Deck.WILD));
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
        Card inTempRealHand = new Card(Deck.BLUE, Deck.NINE);
        // Card inTempRealHand = new Card(Deck.BLUE, Deck.EIGHT); // test the test - should fail
        try {
            realHand.discard(inTempRealHand);
        } catch (IllegalArgumentException e) {
            fail();
        }
        int tempRealHandSizeAfter = realHand.getSize();
        assertEquals(tempRealHandSizeBefore, (tempRealHandSizeAfter + 1));
        // assertEquals(tempRealHandSizeBefore, (tempRealHandSizeAfter + 3)); // test the test - should fail

        /*
        Hand hand = new Hand();
        Card yellowNine = new Card(Deck.YELLOW, Deck.NINE);
        hand.addCard(yellowNine);
        hand.addCard(new Card(Deck.YELLOW, Deck.ONE));
        hand.addCard(new Card(Deck.RED, Deck.ZERO));
        try {
            hand.discard(yellowNine);
        } catch (IllegalArgumentException e) {
            fail();
        }
        */
    }

    @Test
    public void testHand_discard_exception() {
        Hand tempRealHand = realHand;
        Card notInTempRealHand = new Card(Deck.GREEN, Deck.WILD);
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

}




























