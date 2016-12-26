package com.jason;

public class Card {
    
    private String color;
    private String face;
    private int value;
    
    /**
     * Public constructor.
     * 
     * @param color The color of the card.
     * @param face  The face of the card.
     */
    public Card(String color, String face, int value) { // Gosh it sure would be nice if we didn't have to give
                                                        // the fucking int value every time.
        this.color = color;
        this.face = face;
        this.value = value;
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
}
