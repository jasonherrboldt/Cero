package com.jason;

import java.util.HashMap;
import java.util.Map;

/**
 * A map that connects card face values with numeric values.
 *
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */
public class CardValueMap {

    private Map<String, Integer> valueMap;

    /**
     * Public constructor.
     */
    public CardValueMap() {
        valueMap = new HashMap<>();
        valueMap.put(Card.ZERO, 0);
        valueMap.put(Card.ONE, 1);
        valueMap.put(Card.TWO, 2);
        valueMap.put(Card.THREE, 3);
        valueMap.put(Card.FOUR, 4);
        valueMap.put(Card.FIVE, 5);
        valueMap.put(Card.SIX, 6);
        valueMap.put(Card.SEVEN, 7);
        valueMap.put(Card.EIGHT, 8);
        valueMap.put(Card.NINE, 9);

        valueMap.put(Card.SKIP, 20);
        valueMap.put(Card.REVERSE, 20);
        valueMap.put(Card.DRAW_TWO, 20);
        valueMap.put(Card.WILD, 50);
        valueMap.put(Card.WILD_DRAW_FOUR, 50);
    }

    /**
     * Get the numeric value associated with a face card. "Nine" = 9, "Reverse" = 20, etc.
     *
     * @param face                          The face to analyze.
     * @return                              The numeric value of the face.
     * @throws IllegalArgumentException     Throw up if submitted face does not exist in map.
     */
    public int getValue(String face) throws IllegalArgumentException {
        if(face == null || face.equals("")) {
            Main.out("WARN: CardValueMap.getValue called with a null or empty string.");
        }
        if(!valueMap.containsKey(face)) {
            throw new IllegalArgumentException ("Invalid face value: " + face);
        }
        return valueMap.get(face);
    }
}