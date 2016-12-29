package com.jason;

/**
 * A pojo created to deliver browser payload information from a game action.
 *
 * Created by jasonherrboldt on 12/29/2016.
 */
public class GameState {

    private Card currentPlayedCard;
    private String currentColor;
    private String message;
    private Player player;

    public GameState() {
    }

    public Card getCurrentPlayedCard() {
        return currentPlayedCard;
    }

    public void setCurrentPlayedCard(Card currentPlayedCard) {
        this.currentPlayedCard = currentPlayedCard;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(String currentColor) {
        this.currentColor = currentColor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        if(player.isPlayer2()) {
            Main.out("WARN: GameState.setPlayer sent Player2 instead of Player1. this.player set to null.");
            this.player = null;
        } else {
            this.player = player;
        }
    }
}