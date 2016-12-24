package test.jason;

import com.jason.*;
import org.junit.*;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Hand class methods.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class TestHand {

    private List<List<Card>> mockHand;

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
    }
    
    @Test
    public void testHand_addCard() {
        Hand hand = new Hand();
        hand.addCard(new Card(Deck.BLUE, Deck.NINE));
        hand.addCard(new Card(Deck.RED, Deck.WILD_DRAW_FOUR));
        hand.addCard(new Card(Deck.YELLOW, Deck.SIX));
        hand.addCard(new Card(Deck.GREEN, Deck.TWO));
        hand.addCard(new Card(Deck.BLUE, Deck.ONE));
        hand.addCard(new Card(Deck.YELLOW, Deck.REVERSE));
        hand.addCard(new Card(Deck.BLUE, Deck.WILD));

        List<Card> allMockHandCards = getAllCardsFromMockHand(mockHand);
        List<Card> allHandCards = hand.getAllCards();

        if(allMockHandCards.size() == allHandCards.size()) {
            for (int i = 0; i < allMockHandCards.size(); i++) {
                assertEquals(allHandCards.get(i).getColor(), allMockHandCards.get(i).getColor());
                assertEquals(allHandCards.get(i).getFace(), allMockHandCards.get(i).getFace());
            }
        } else {
            fail("allMockHandCards.size() must be the same as allHandCards.size() to compare.");
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

    private List<Card> getAllCardsForTest2(Hand hand) {
        return null;
    }

}




























