package com.jason;

import java.util.Stack;

/**
 * The game's discard pile.
 *
 * Created by jasonherrboldt on 12/27/2016.
 */
public class DiscardPile {

    private Stack<Card> stack;

    public DiscardPile() {
        stack = new Stack<>();
    }

    public void add(Card card) {
        if(card == null) {
            Main.out("WARN: tried and failed to add a null card to DiscardPile.add.");
        } else {
            stack.push(card);
        }
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public void clear() {
        stack.clear();
    }

    public Stack<Card> getStack() {
        return this.stack;
    }
}
