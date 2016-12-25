package com.jason;

public class Card {
    
    private String color;
    private String face;
    
    /**
     * Public constructor.
     * 
     * @param color The color of the card.
     * @param face  The face of the card.
     */
    public Card(String color, String face) {
        this.color = color;
        this.face = face;
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

    public boolean equals(Card card) {
        return card.getColor().equalsIgnoreCase(this.color)
                && card.getFace().equalsIgnoreCase(this.face);
    }
}
