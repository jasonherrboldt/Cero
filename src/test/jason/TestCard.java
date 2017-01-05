package test.jason;

import com.jason.*;
import org.junit.*;

import static org.junit.Assert.*;

public class TestCard {

    private Card colorlessCard;
    private Card colorCard;
    private CardValueMap cvm;
    private Card skip;
    private Card reverse;
    private Card drawTwo;
    private Card wild;
    private Card wildDrawFour;

    @Before
    public void setup() {
        cvm = new CardValueMap();
        colorlessCard = new Card(Card.COLORLESS, Card.WILD, cvm);
        colorCard = new Card(Card.RED, Card.TWO, cvm);
        skip = new Card(Card.RED, Card.SKIP, cvm);
        reverse = new Card(Card.GREEN, Card.REVERSE, cvm);
        drawTwo = new Card(Card.YELLOW, Card.DRAW_TWO, cvm);
        wild = new Card(Card.COLORLESS, Card.WILD, cvm);
        wildDrawFour = new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm);
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
        Card nonNumericCard = new Card(Card.COLORLESS, Card.WILD, cvm);

        assertTrue(numericCard.isNumeric());
        assertFalse(nonNumericCard.isNumeric());
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

    @Test
    public void testCard_isNonNumeric() {
        assertTrue(wild.isNonNumeric());
        assertTrue(wildDrawFour.isNonNumeric());
        assertTrue(drawTwo.isNonNumeric());
        assertTrue(reverse.isNonNumeric());
        assertTrue(skip.isNonNumeric());
    }

    @Test
    public void testCard_isNonNumericNonWild() {
        assertTrue(drawTwo.isNonNumericNonWild());
        assertTrue(reverse.isNonNumericNonWild());
        assertTrue(skip.isNonNumericNonWild());
    }


}























