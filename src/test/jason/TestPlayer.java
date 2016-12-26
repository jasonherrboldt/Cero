package test.jason;

import com.jason.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;

public class TestPlayer {

    private Player player;
    private CardValueMap cvm;
    private Hand classHand;

    @Before
    public void setup() {
        player = new Player("Hal 9000", true);
        cvm = new CardValueMap();
        classHand = new Hand();
        classHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        classHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        classHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
    }

    @Test
    public void testPlayer_setHand() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(new Card(Card.GREEN, Card.THREE, cvm));
        cardList.add(new Card(Card.RED, Card.DRAW_TWO, cvm));
        cardList.add(new Card(Card.RED, Card.EIGHT, cvm));
        cardList.add(new Card(Card.YELLOW, Card.ONE, cvm));
        cardList.add(new Card(Card.YELLOW, Card.ZERO, cvm));
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
    public void testPlayer_draw() {
        Player drawPlayer = new Player("", false);
        List<Card> drawList = new ArrayList<>();
        drawList.add(new Card(Card.GREEN, Card.ZERO, cvm));
        drawPlayer.setHand(drawList);
        assertEquals(drawPlayer.getHand().getSize(), 1);

        Card cardToDraw = new Card(Card.YELLOW, Card.REVERSE, cvm);
        drawPlayer.draw(cardToDraw);

        assertEquals(drawPlayer.getHand().getSize(), 2);
    }

    @Test
    public void testPlayer_discard() {
        Player discardPlayer = new Player("", false);
        List<Card> discardList = new ArrayList<>();
        discardList.add(new Card(Card.RED, Card.EIGHT, cvm));
        discardList.add(new Card(Card.GREEN, Card.FOUR, cvm));
        discardPlayer.setHand(discardList);
        assertEquals(discardPlayer.getHand().getSize(), 2);

        Card cardToDiscard = new Card(Card.GREEN, Card.FOUR, cvm);
        discardPlayer.discard(cardToDiscard);

        assertEquals(discardPlayer.getHand().getSize(), 1);
    }
    
}