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
    public void testHand_getColorList() {

    }

    private boolean mismatchFound(List<Card> cList1, List<Card> cList2) throws Exception {
        boolean mismatchFound = false;
        if(cList1.size() == cList2.size()) {
            for (int i = 0; i < cList1.size(); i++) {
                if (!(cList1.get(i).getColor().equalsIgnoreCase(cList2.get(i).getColor())
                        && cList1.get(i).getFace().equalsIgnoreCase(cList2.get(i).getFace()))) {
                    mismatchFound = true;
                }
            }
        } else {
            throw new Exception("cList1 must be the same size as cList2.");
        }
        return mismatchFound;
    }

}




























