package test.jason;

import com.jason.*;
import org.junit.*;
import java.util.*;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

/**
 * Tests for Card methods.
 *
 * Created by jasonherrboldt on 12/25/16.
 */
public class TestCard {

    private CardValueMap cvm;

    @Before
    public void setup() {
        cvm = new CardValueMap();
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

        assertTrue(numericCard.isNumberCard(cvm));
        assertFalse(nonNumericCard.isNumberCard(cvm));
    }
}



