package com.jason;

import java.util.*;

/**
 * Hand class. A list of a list of cards, sorted descending by color group.
 * Allows player many insights into cards, allowing it to decide a move.
 *
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */
public class Hand {

    private List<List<Card>> listOfListOfCards;

    /**
     * Public constructor.
     */
    public Hand() {
        listOfListOfCards = new ArrayList<>();
    }

    /**
     * Add a card.
     *
     * @param card the card to add
     */
    public void addCard(Card card) { // tested
        if(card != null) {
            List<Card> colorList = getColorList(card.getColor());
            if(colorList != null) {
                colorList.add(card);
                // Sort the color group descending by face value.
                Collections.sort(colorList, (Card c1, Card c2) -> c1.getFace().compareTo(c2.getFace()));
            } else {
                // Create a new color list and add it to the hand.
                List<Card> newList = new ArrayList<>();
                newList.add(card);
                listOfListOfCards.add(newList);
            }
            sortHand();
        }
    }

    /**
     * Sort the hand descending by color group size (NOT by face value sum size).
     * For example, if there are more blues than any other type of card, put the blues first. And so on.
     */
    public void sortHand() {
        Collections.sort(listOfListOfCards, (List<Card> l1, List<Card> l2) -> l2.size() - l1.size());
        moveColorlessListToEnd();
    }

    /**
     * Move the list of colorless (wild and wild draw four) cards to the end of the hand.
     */
    public void moveColorlessListToEnd() { // tested
        List<Card> colorlessCards = getColorList(Card.COLORLESS);
        if(colorlessCards != null) {
            if(colorlessCards.size() > 0) {
                listOfListOfCards.remove(colorlessCards);
                listOfListOfCards.add(colorlessCards);
            }
        }
    }

    /**
     * Get a list of cards of a specified color.
     *
     * @param color The specified color.
     * @return      The list of cards of the specified color, or null if none found.
     */
    public List<Card> getColorList(String color) { // tested
        for(List<Card> list : listOfListOfCards) {
            if(list.size() > 0) {
                if(list.get(0).getColor().equalsIgnoreCase(color)) {
                    return list;
                }
            }
        }
        return null;
    }

    /**
     * Discard a card.
     *
     * @param card The card to discard.
     */
    public void discard(Card card) { // tested
        if (!hasCard(card)) {
            throw new IllegalArgumentException("Card not in hand.");
        }
        if(card == null) {
            throw new IllegalArgumentException("Card is null.");
        } else {
            List<Card> colorList = getColorList(card.getColor());
            int index = -1;
            int i = 0;
            for(Card c : colorList) {
                if(c.equals(card)) {
                    index = i;
                }
                i++;
            }
            if(index == -1 || index > colorList.size() - 1) {
                throw new IllegalArgumentException("Card not in hand.");
            } else {
                colorList.remove(index);
                sortHand();
            }
        }
    }

    /**
     * Check to see if a specified card is in the hand.
     *
     * @param card  The card to check.
     * @return      True if the card is present in the deck, false otherwise.
     */
    public boolean hasCard(Card card) throws IllegalArgumentException { // tested
        if(card != null) {
            boolean cardFound = false;
            List<Card> colorList = getColorList(card.getColor());
            if(colorList != null) {
                for(Card c : colorList) {
                    if(card.equals(c)) {
                        cardFound = true;
                    }
                }
                return cardFound;
            }
        } else {
            throw new IllegalArgumentException("ERROR: null card handed to hasCard.");
        }
        return false;
    }

    /**
     * @return the number of cards in the hand.
     */
    public int getSize() { // tested in testHand_discard()
        int count = 0;
        for(List<Card> list : listOfListOfCards) {
            count += list.size();
        }
        return count;
    }

    /**
     * @return a flat list of all the cards in the deck.
     */
    public List<Card> getAllCards() { // tested
        List<Card> cards = new ArrayList<>();
        for(List<Card> list : listOfListOfCards) {
            for(Card c : list) {
                cards.add(c);
            }
        }
        return cards;
    }

    /**
     * Get the highest value face card of a specified color.
     *
     * @param color   The color to search.
     * @param numeric If the search should include only numeric cards.
     * @return        The highest value card of the given color, or null if no card found.
     */
    public Card getHighestFace(String color, boolean numeric) { // tested
        int highestValue = numeric ? 9 : 50;
        if(color == null) {
            throw new IllegalArgumentException("Hand.getHighestFace called with null color.");
        }
        List<Card> colorList = getColorList(color);
        if(colorList == null) {
            return null;
        }
        // Sort the color group descending by face value.
        Collections.sort(colorList, (Card c1, Card c2) -> c2.getValue() - (c1.getValue()));
        for(Card c : colorList) {
            if(c.getValue() <= highestValue) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get the highest non-numeric value face card of a specified color.
     * (Note: will not return wild or wd4 because they are colorless.)
     *
     * @param color The color to search.
     * @return      The highest value card of the given color, or null if no card found.
     */
    public Card getHighestNonNumericFace(String color) { // tested
        int lowestValue = 20;
        if(color == null) {
            throw new IllegalArgumentException("Hand.getHighestNonNumericFace called with null color.");
        }
        List<Card> colorList = getColorList(color);
        if(colorList == null) {
            return null;
        }
        // Sort the color group descending by face value.
        Collections.sort(colorList, (Card c1, Card c2) -> c2.getValue() - (c1.getValue()));
        for(Card c : colorList) {
            if(c.getValue() >= lowestValue) {
                return c;
            }
        }
        return null;
    }

    /**
     * Get number from largest available color group. Checks all color groups descending by color group size
     * and returns best choice.
     *
     * @param number    The number to search for.
     * @return          The card of the highest color group, or null if no card found.
     */
    public Card getNumberFromLargestColorGroup(int number, Card currentPlayedCard) { // tested
        // Hand is already sorted ascending by color group size.
        for(List<Card> list : listOfListOfCards) {
            for(Card c : list) {
                // need to differentiate non-numeric cards of the same value, e.g. skip, draw two, reverse,
                // wild, and WD4.
                if(c.getValue() == 20 || c.getValue() == 50) {
                    if(c.getValue() == number && c.getFace().equalsIgnoreCase(currentPlayedCard.getFace())) {
                        return c;
                    }
                } else {
                    if(c.getValue() == number) {
                        return c;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return the color group size.
     */
    public int getColorGroupSize(String color) { // tested
        for(List<Card> list : listOfListOfCards) {
            if(list.size() > 0) {
                if(list.get(0).getColor().equalsIgnoreCase(color)) {
                    return list.size();
                }
            }
        }
        return 0;
    }

    /**
     * @return the sum of all numeric card values in a hand.
     */
    public int sumCards() { // tested
        int handSum = 0;
        for(List<Card> list : listOfListOfCards) {
            for(Card c : list) {
                handSum += c.getValue();
            }
        }
        return handSum;
    }

    /**
     * @return the color of the highest color group.
     */
    public String getHighestColor() { // tested
        if(listOfListOfCards.size() > 0) {
            List<String> colors = getColors();
            if(colors.size() > 0) {
                if(!colors.get(0).equalsIgnoreCase(Card.COLORLESS)) {
                    return colors.get(0);
                } else {
                    if(colors.size() > 1) {
                        return colors.get(1);
                    }
                }
            }

            // pick a random color if the only color group the player has left is colorless.
            return getRandomColor();
        }
        Main.out("WARN: getHighestColor did not find a card. Returning null.");
        return null;
    }

    /**
     * @return a random color
     */
    public String getRandomColor() {
        List<String>colors = new ArrayList<>();
        colors.add(Card.BLUE);
        colors.add(Card.RED);
        colors.add(Card.YELLOW);
        colors.add(Card.GREEN);
        Collections.shuffle(colors);
        return colors.get(0);
    }

    /**
     * @return the value of the hand.
     */
    public int getHandValue() { // tested
        int value = 0;
        for(Card c : getAllCards()) {
            value += c.getValue();
        }
        return value;
    }

    /**
     * @return the first card of the hand (for testing).
     */
    public Card getFirstCard() { // no test needed
        if(getSize() > 0) {
            return getAllCards().get(0);
        } else {
            Main.out("WARN: Hand.getFirstCard called on an empty hand. No action taken, null returned.");
            return null;
        }
    }

    /**
     * @return a string list of all hands in the card
     */
    List<String> getHandPrintStringList() {
        List<String> allCards = new ArrayList<>();
        for(Card c : getAllCards()){
            allCards.add(c.getPrintString());
        }
        return allCards;
    }

    /**
     * Set the current hand (for testing).
     *
     * @param hand the hand to set
     */
    public void setHand(List<List<Card>> hand) { // no test needed
        listOfListOfCards = hand;
    }

    /**
     * @return a list of all existing colors in a hand
     */
    public List<String> getColors() { // tested
        List<String> colors = new ArrayList<>();
        for(List<Card> colorList : listOfListOfCards) {
            if(colorList.size() > 0) {
                if(!colorList.get(0).getColor().equalsIgnoreCase(Card.COLORLESS)) {
                    colors.add(colorList.get(0).getColor());
                }
            }
        }
        return colors;
    }
}