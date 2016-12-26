package com.jason;

import java.util.HashMap;
import java.util.Map;

public class Card {
    
    private String color;
    private String face;
    private int value;

    public static final String RED = "Red";
    public static final String YELLOW = "Yellow";
    public static final String GREEN = "Green";
    public static final String BLUE = "Blue";
    public static final String COLORLESS = "Colorless";

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String FOUR = "4";
    public static final String FIVE = "5";
    public static final String SIX = "6";
    public static final String SEVEN = "7";
    public static final String EIGHT = "8";
    public static final String NINE = "9";

    public static final String SKIP = "Skip";
    public static final String REVERSE = "Reverse";
    public static final String DRAW_TWO = "Draw Two";
    public static final String WILD = "Wild";
    public static final String WILD_DRAW_FOUR = "Wild Draw Four";
    
    /**
     * Public constructor.
     * 
     * @param color The color of the card.
     * @param face  The face of the card.
     * @param cvm   The CardValueMap object used to find the numeric value of the card.
     */
    public Card(String color, String face, CardValueMap cvm) {
        this.color = color;
        this.face = face;
        try {
            this.value = cvm.getValue(face);
        } catch (IllegalArgumentException e) {
            Main.out("WARN: CardValueMap.getValue() called with illegal argument: " + face);
        }
    }
    
    /**
     * @return the color of the card.
     */
    public String getColor() {
        return this.color;
    }
    
    /** 
     * @return the face of the card.
     */
    public String getFace() {
        return this.face;
    }

    public int getValue() {
        return this.value;
    }

    public boolean equals(Card card) {
        return card.getColor().equalsIgnoreCase(this.color)
                && card.getFace().equalsIgnoreCase(this.face);
    }

    public boolean isNumberCard(CardValueMap cvm) {
        int faceVal = cvm.getValue(face);
        return faceVal >= 0 && faceVal < 10;
    }
}
