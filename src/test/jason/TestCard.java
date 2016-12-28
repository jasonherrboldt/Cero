package test.jason;

import com.jason.*;
import org.junit.*;

import static org.junit.Assert.*;

public class TestCard {

    Card colorlessCard;
    Card colorCard;
    private CardValueMap cvm;

    @Before
    public void setup() {
        cvm = new CardValueMap();
        colorlessCard = new Card(Card.COLORLESS, Card.WILD, cvm);
        colorCard = new Card(Card.RED, Card.TWO, cvm);
    }

    @Test
    public void testCard_equals() {
        Card blueNine1 = new Card(Card.BLUE, Card.NINE, cvm);
        Card blueNine2 = new Card(Card.BLUE, Card.NINE, cvm);
        Card redSix = new Card(Card.RED, Card.SIX, cvm);

        assertTrue(blueNine1.equals(blueNine2));
        assertFalse(blueNine1.equals(redSix));
    }

    @Test
    public void testCard_isNumberCard() {
        Card numericCard = new Card(Card.YELLOW, Card.FOUR, cvm);
        Card nonNumericCard = new Card(Card.YELLOW, Card.WILD, cvm);

        assertTrue(numericCard.isNumberCard());
        assertFalse(nonNumericCard.isNumberCard());
    }

    @Test
    public void testCard_isColorlessCard() {
        assertTrue(colorlessCard.isColorlessCard());
        assertFalse(colorCard.isColorlessCard());
    }

    @Test
    public void testCard_getPrintString() {
        assertTrue(colorlessCard.getPrintString().equals("Wild"));
        assertTrue(colorCard.getPrintString().equals("(Red) 2"));
    }
}