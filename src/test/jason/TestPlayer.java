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
    private Deck deck;

    // Not instantiated in setup:
    private List<Card> cardList;
    private Player player;

    @Before
    public void setup() {
        cvm = new CardValueMap();
        deck = new Deck();
        classHand = new Hand();
        classHand.addCard(new Card(Card.GREEN, Card.THREE, cvm));
        classHand.addCard(new Card(Card.RED, Card.DRAW_TWO, cvm));
        classHand.addCard(new Card(Card.RED, Card.EIGHT, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.ONE, cvm));
        classHand.addCard(new Card(Card.YELLOW, Card.ZERO, cvm));
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
    public void testPlayer_draw_nonEmptyDeck() {
        player = new Player("Draw Non-Empty Deck Test", false);
        cardList = new ArrayList<>();
        cardList.add(new Card(Card.GREEN, Card.ZERO, cvm));
        player.setHand(cardList);
        assertEquals(player.getHand().getSize(), 1);

        DiscardPile discardPile = new DiscardPile();
        player.draw(deck, discardPile);

        assertEquals(player.getHand().getSize(), 2);
    }

    @Test
    public void testPlayer_draw_emptyDeck() {
        player = new Player("Draw Empty Deck Test", false);
        cardList = new ArrayList<>();
        cardList.add(new Card(Card.GREEN, Card.ZERO, cvm));
        player.setHand(cardList);
        DiscardPile discardPile = new DiscardPile();
        discardPile.add(new Card(Card.BLUE, Card.SEVEN, cvm));
        discardPile.add(new Card(Card.RED, Card.EIGHT, cvm));
        discardPile.add(new Card(Card.GREEN, Card.NINE, cvm));
        deck.clearDeck();

        // Try to draw from an empty deck.
        player.draw(deck, discardPile);
        assertEquals(player.getHand().getSize(), 2);

        // Replace deck for other methods.
        deck.populate();
    }

    @Test
    public void testPlayer_discard() {
        Player discardPlayer = new Player("", false);
        cardList = new ArrayList<>();
        cardList.add(new Card(Card.RED, Card.EIGHT, cvm));
        Card greenFour = new Card(Card.GREEN, Card.FOUR, cvm);
        cardList.add(greenFour);
        discardPlayer.setHand(cardList);
        assertEquals(discardPlayer.getHand().getSize(), 2);

        discardPlayer.discard(greenFour);
        assertEquals(discardPlayer.getHand().getSize(), 1);
    }
}




















