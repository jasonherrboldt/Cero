package test.jason;

import com.jason.Main;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * Tests the Hand class methods.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class TestHand {



    @Before
    public void setup() {

    }

    @Test
    public void test() {
        Map<String, String> map = new TreeMap<>();
        map.put("a", "0");
        printMap(map);
        map.put("b", "1");
        printMap(map);
        map.put("a", "1");
        printMap(map);
    }

    private void printMap(Map<String, String> map) {
        for(Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        System.out.println("");
    }
}
























