package test.jason;

import java.util.ArrayList;
import java.util.List;
import com.jason.*;


import org.junit.Before;
import org.junit.Test;

public class TestPlayer {

    Player player;
    List<Card> hand;

    @Before
    public void setup() {
        player = new Player("Michael Jackson", false);
        hand = new ArrayList<>();
    }
    
    @Test
    public void testPlayer_draw() {
        
    }
    
}
