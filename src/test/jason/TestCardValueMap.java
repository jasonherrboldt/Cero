package test.jason;

import com.jason.Card;
import com.jason.CardValueMap;
import org.junit.*;
import static org.junit.Assert.*;
import static junit.framework.TestCase.fail;

/**
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */
public class TestCardValueMap {

    @Test
    public void testCardValueMap_getValue_exception() {
        CardValueMap cvm = new CardValueMap();
        String badFace = "no such card";
        try {
            cvm.getValue(badFace);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Invalid face value: " + badFace);
        }
    }

    @Test
    public void testCardValueMap_getValue_numeric() {
        CardValueMap cvm = new CardValueMap();
        assertEquals(cvm.getValue(Card.NINE), 9);
    }

    @Test
    public void testCardValueMap_getValue_nonNumeric() {
        CardValueMap cvm = new CardValueMap();
        assertEquals(cvm.getValue(Card.DRAW_TWO), 20);
        assertEquals(cvm.getValue(Card.WILD), 50);
    }
}