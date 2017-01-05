package test.jason;

import com.jason.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test the Main class methods.
 *
 * Created by jasonherrboldt on 12/28/2016.
 */
public class TestMain {

    private String nullQuestionWarning;
    private String nonNullQuestion;

    @Before
    public void setup() {
        nullQuestionWarning = "This is the null question warning message.";
        nonNullQuestion = "non-null question";
    }

    @Test
    // Happy path is only functionally testable.
    public void testMain_validateQuestion() {
        assertEquals(Main.isNullOrEmpty(null, nullQuestionWarning), true);
        assertEquals(Main.isNullOrEmpty("", nullQuestionWarning), true);
        assertEquals(Main.isNullOrEmpty(nonNullQuestion, nullQuestionWarning), false);
    }


    @Test
    // Happy path is only functionally testable.
    public void testMain_getUserResponse_yesNo() {
        assertEquals(Main.getUserResponse_yesNo(null), null);
    }

    @Test
    // Happy path is only functionally testable.
    public void testMain_getUserResponse_integer() {
        assertEquals(Main.getUserResponse_integer(null, 1, 2), null);
        assertEquals(Main.getUserResponse_integer(nonNullQuestion, 2, 1), null);
        assertEquals(Main.getUserResponse_integer(nonNullQuestion, -2, 1), null);
        assertEquals(Main.getUserResponse_integer(nonNullQuestion, 2, -1), null);
        assertEquals(Main.getUserResponse_integer(nonNullQuestion, -2, -1), null);
    }

    @Test
    // Happy path is only functionally testable.
    public void testMain_getUserResponse_string() {
        assertEquals(Main.getUserResponse_string(null), null);
    }
}