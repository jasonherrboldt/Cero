package com.jason;

/**
 * A card.
 *
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */
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

    private CardValueMap cvm;
    
    /**
     * Public constructor.
     * 
     * @param color The color of the card.
     * @param face  The face of the card.
     * @param cvm   The CardValueMap object used to find the numeric value of the card.
     */
    public Card(String color, String face, CardValueMap cvm) {
        if(cvm == null) {
            Main.out("WARN: Card constructor received a null CardValueMap object.");
        }
        if(color.equalsIgnoreCase(Card.COLORLESS) && (!face.equalsIgnoreCase(Card.WILD) && !face.equalsIgnoreCase(Card.WILD_DRAW_FOUR))) {
            throw new IllegalArgumentException("Card constructor incorrectly called with colorless color and face " + face);
        }
        if(!color.equalsIgnoreCase(Card.COLORLESS) && (face.equalsIgnoreCase(Card.WILD) || face.equalsIgnoreCase(Card.WILD_DRAW_FOUR))) {
            throw new IllegalArgumentException("Card constructor incorrectly called with non-colorless color and face " + face);
        }
        this.cvm = cvm;
        this.color = color;
        this.face = face;
        try {
            if(cvm == null) {
                throw new IllegalArgumentException("ERROR: Card constructor called with a null cvm object.");
            } else {
                this.value = cvm.getValue(face);
            }
        } catch (IllegalArgumentException e) {
            Main.out("ERROR: CardValueMap.getValue() called with illegal argument: " + face);
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

    /**
     * @return the value of the card.
     */
    int getValue() {
        return this.value;
    }

    /**
     * Determines if another card is equal to this card.
     *
     * @param card  The card to compare.
     * @return      True if the cards have the same color and face value, false otherwise.
     */
    public boolean equals(Card card) { // tested
        if(card == null) {
            Main.out("ERROR: Card.equals was given a null card. Returning false without comparison.");
            return false;
        } else {
            return card.getColor().equalsIgnoreCase(this.color)
                    && card.getFace().equalsIgnoreCase(this.face);
        }
    }

    /**
     * @return true if this card has a numeric face value, false otherwise.
     */
    public boolean isNumeric() { // tested
        int faceVal = cvm.getValue(face);
        return faceVal >= 0 && faceVal < 10;
    }

    /**
     * @return true if the card is colorless (i.e wild or wild draw four), false otherwise.
     */
    public boolean isColorlessCard() { // tested
        return face.equalsIgnoreCase(WILD) || face.equalsIgnoreCase(WILD_DRAW_FOUR);
    }

    /**
     * @return the string needed to print card properties.
     */
    public String getPrintString() { // tested
        if(isColorlessCard()) {
            return getFace();
        } else {
            return "(" + getColor() + ") " + getFace();
        }
    }

    /**
     * @return true if a card is non-numeric (i.e. wild, skip, rev, d2, or d4)
     */
    public boolean isNonNumeric() {
        return isColorlessCard() || !isNumeric();
    }

    /**
     * @return true if the card is non-numeric and non-wild, e.g. skip, rev, draw two.
     */
    public boolean isNonNumericNonWild() {
        return isNonNumeric() && !this.face.equalsIgnoreCase(WILD);
    }
}