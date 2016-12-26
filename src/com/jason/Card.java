package com.jason;

/**
 * A card.
 */
public class Card {

    /*

        From unorules.com:

        All number cards are the same value as the number on the card (e.g. a 9 is 9 points).
        “Draw Two” – 20 Points, “Reverse” – 20 Points, “Skip” – 20 Points, “Wild” – 50 Points,
        and “Wild Draw Four” – 50 Points. The first player to attain 500 points wins the game.

     */
    
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

    CardValueMap cvm;
    
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
            Main.out("WARN: Card constructor incorrectly called with colorless color and face " + face);
        }
        this.cvm = cvm;
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

    /**
     * Determines if another card is equal to this card.
     *
     * @param card  The card to compare.
     * @return      True if the cards have the same color and face value, false otherwise.
     */
    public boolean equals(Card card) { // tested
        if(card == null) {
            Main.out("WARN: trying to compare a null card to this card.");
        }
        return card.getColor().equalsIgnoreCase(this.color)
                && card.getFace().equalsIgnoreCase(this.face);
    }

    /**
     * Determines if this card has a numeric face value.
     *
     * @return      True if this card has a numeric face value, false otherwise.
     */
    public boolean isNumberCard() { // tested
        int faceVal = cvm.getValue(face);
        return faceVal >= 0 && faceVal < 10;
    }

    /**
     * @return true if the card is colorless (e.g. wild), false otherwise.
     */
    public boolean isColorlessCard() { // tested
        return face.equalsIgnoreCase(WILD) || face.equalsIgnoreCase(WILD_DRAW_FOUR);
    }

    /**
     * Get the string needed to print card properties to the console.
     *
     * @return The string to print.
     */
    public String getPrintString() { // tested
        if(isColorlessCard()) {
            return getFace();
        } else {
            return "(" + getColor() + ") " + getFace();
        }
    }
}