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
    private Player player;

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
    }

    @Test
    public void testPlayer_setHand() {
        player = new Player("Set Hand Test", true);
        cardList = new ArrayList<>();
        cardList.add(new Card(Card.GREEN, Card.THREE, cvm));
        cardList.add(new Card(Card.RED, Card.DRAW_TWO, cvm));
        cardList.add(new Card(Card.RED, Card.EIGHT, cvm));
        cardList.add(new Card(Card.YELLOW, Card.ONE, cvm));
        cardList.add(new Card(Card.YELLOW, Card.ZERO, cvm));
        cardList.add(new Card(Card.YELLOW, Card.SIX, cvm));
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
    public void testPlayer_discard() {
        player = new Player("", false);
        cardList = new ArrayList<>();
        cardList.add(new Card(Card.RED, Card.EIGHT, cvm));
        Card greenFour = new Card(Card.GREEN, Card.FOUR, cvm);
        cardList.add(greenFour);
        player.setHand(cardList);
        assertEquals(player.getHand().getSize(), 2);

        player.discard(greenFour);
        assertEquals(player.getHand().getSize(), 1);
    }

    @Test
    public void testPlayer_setRandomStrategy() {
        player = new Player("", true);
        assertTrue(player.getStrategy().equals(""));
        assertTrue(player.setRandomStrategy());
    }

    @Test
    public void testPlayer_getPreferredColor() {
        // Can't test for a dumb player -- returns a random color.
        // Bold and cautious players always return color of highest count.
        player = new Player("", true);
        player.setHand(classHand.getAllCards());

        player.setStrategy(Player.STRATEGY_BOLD);
        assertEquals(player.getPreferredColor(), Card.YELLOW);

        player.setStrategy(Player.STRATEGY_CAUTIOUS);
        assertEquals(player.getPreferredColor(), Card.YELLOW);
    }
}
