package test.jason;

import com.jason.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
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
    public void testMain_getUserResponse_string() {
        assertEquals(Main.getUserResponse_string(null), null);
    }

    @Test
    public void testMain_getRandomPlayerTwoComment_grumbles() {

        // make sure the grumbles list is getting populated to the correct size
        Main.initializeGlobalVariables(false);
        assertEquals(Main.getPlayerTwoGrumbles().size(), Main.NUMBER_OF_GRUMBLES);

        // drain the grumbles list
        boolean nonNullGrumbleFound = false;
        String thisGrumble = "";
        for(int i = 0; i < Main.NUMBER_OF_GRUMBLES; i++) {
            thisGrumble = Main.getRandomPlayerTwoComment(Main.getPlayerTwoGrumbles(), Main.GRUMBLES);
            if(!thisGrumble.equals("")) {
                nonNullGrumbleFound = true;
            }
        }
        assertEquals(Main.getPlayerTwoGrumbles().size(), 0);
        assertTrue(nonNullGrumbleFound);

        // call it one more time
        Main.getRandomPlayerTwoComment(Main.getPlayerTwoGrumbles(), Main.GRUMBLES);

        // assert it has been refilled (minus the one taken just above)
        assertEquals(Main.getPlayerTwoGrumbles().size(), Main.NUMBER_OF_GRUMBLES - 1);
    }

    @Test
    public void testMain_getRandomPlayerTwoComment_taunts() {

        // make sure the grumbles list is getting populated to the correct size
        Main.initializeGlobalVariables(false);
        assertEquals(Main.getPlayerTwoTaunts().size(), Main.NUMBER_OF_TAUNTS);

        // drain the grumbles list
        boolean nonNullTauntFound = false;
        String thisTaunt = "";
        for(int i = 0; i < Main.NUMBER_OF_TAUNTS; i++) {
            thisTaunt = Main.getRandomPlayerTwoComment(Main.getPlayerTwoTaunts(), Main.TAUNTS);
            if(!thisTaunt.equals("")) {
                nonNullTauntFound = true;
            }
        }
        assertEquals(Main.getPlayerTwoTaunts().size(), 0);
        assertTrue(nonNullTauntFound);

        // call it one more time
        Main.getRandomPlayerTwoComment(Main.getPlayerTwoTaunts(), Main.TAUNTS);

        // assert it has been refilled (minus the one taken just above)
        assertEquals(Main.getPlayerTwoTaunts().size(), Main.NUMBER_OF_TAUNTS - 1);
    }
}