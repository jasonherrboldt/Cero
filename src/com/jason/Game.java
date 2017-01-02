package com.jason;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Game class. Create players, alternate turns, keep track of the score,
 * and declare a winner. Allows user to remote-control Player 1.
 * (Player 2 is computer.)
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Game {

    /*
     * TODO:
     *
     *    MUST-HAVES:
     *
     *    Automatically draw cards for player one when it receives a draw two or draw four card.
     *
     *    Must call player1.setMyLastPlayedCard(currentPlayedCard); for player one's turn to prevent unnecessary
     *    automatic draws and turn skips for non-numeric cards. (I think this would happen in game.discard.)
     *
     *    Required Game methods:
     *
     *        game.startGame: Shuffles deck, deals first hand, pops first card off of deck.
     *        Picks a random first player. Allows player2 to discard if its turn first. Returns
     *        object to servlet that contains player1's hand, the current played card, and a
     *        message ("Your turn first," or "Player2 went first," etc). Would also handle
     *        automatic draws for draw two and turn reversal for skip or reverse cards dealt
     *        to player1. (Does NOT call the Game constructor; game object must keep track of
     *        overall score as each new inner game is created.)
     *
     *        game.discard: user picks a card from the UI and clicks either Discard or Discard
     *        and Declare 'Cero Plus One!' button. Game handles discard, allows the computer to
     *        have its turn, takes care of any non-numeric cards discarded by computer, and
     *        returns gameState object to servlet. (Must handle incorrectly discarded cards.)
     *
     *        game.draw: ads new card to hand, returns gameState object to servlet.
     *
     *
     *
     *    NICE-TO-HAVES:
     *
     *    Space out the console output for computer steps; make it look like the
     *    computer is thinking about what its doing, instead of just showing a dump
     *    of steps to the user. Might be fun to have it complain if it has to draw more
     *    than three cards.
     *
     *    Once the user can play the computer, set up another game so that the computer
     *    can play itself. Let each player pick a random strategy each time, and let them
     *    play like 10k games. Each time a game is won, the game.play method will return
     *    an object containing the winning player and the strategy it used to win. Whatever
     *    class calls game.play will take that information, collate it, and display it to the
     *    user after the 10k games have finished.
     *
     */

    private Deck deck;
    public Player player1; // the user
    public Player player2; // the computer
    private boolean isPlayerOnesTurn;
    public Card currentPlayedCard;
    private CardValueMap cvm;
    private Stack<Card> discardPile;
    private String currentColor;
    private boolean isFirstMove;
    private static final int MAX_P2_DRAW_LOOP = 100;

    // *************** get this garbage out of here. ***************
    // private boolean testStartGame;
    // *************** get this garbage out of here. ***************

    public Game(String playerOneName) {
        cvm = new CardValueMap();
        deck = new Deck();
        player1 = new Player(playerOneName, false);
        player1.setStrategy(Player.STRATEGY_NEUTRAL);
        player2 = new Player("Computer", true);
        player2.setRandomStrategy();
        isPlayerOnesTurn = Main.getRandomBoolean();
        discardPile = new Stack<>();
        currentColor = "";
        isFirstMove = true;

        // *************** get this garbage out of here. ***************
        // testStartGame = false;
        // *************** get this garbage out of here. ***************
    }

    /**
     * Start the game. (Can only be called once.)
     *
     * @param firstPlayedCard for testing
     */
    public void startGame(Card firstPlayedCard) { // tested
        if(!isFirstMove) {
            Main.out("ERROR: Game.startGame called after first move has already been played. No action taken.");
        } else {
            deck.shuffle();
            dealHands();

            // for testing:
            if(firstPlayedCard == null) {
                currentPlayedCard = verifyFirstCard(deck.popCard());
            } else {
                currentPlayedCard = new Card(Card.YELLOW, Card.DRAW_TWO, cvm);
                setPlayerOnesTurn(true);
            }

            discardPile.add(currentPlayedCard);
            currentColor = currentPlayedCard.getColor();

            // Let the players see how many cards are in each other's decks.
            player1.updateOtherPlayersHandCount(player2.showHandCount());
            player2.updateOtherPlayersHandCount(player1.showHandCount());
        }
    }

    /**
     * Play the first hand.
     */
    public void playFirstHand() {
        if(!isFirstMove) { // tested
            Main.out("ERROR: Game.playFirstHand called after first move has already been played. No action taken.");
        } else {
            if (!isPlayerOnesTurn) {
                Main.out("Player two had the first move.");


                // ************* this entire block will have to be repeated for all future moves *************
                if (skipPlayersTurn(player2) && !currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) {
                    Main.out("Player two was forbidden from discarding.");
                } else {
                    playerTwosTurn();
                }
                isPlayerOnesTurn = !isPlayerOnesTurn; // only happens here!
                // ************* this entire block will have to be repeated for all future moves *************


            } else { // player one's turn
                if (skipPlayersTurn(player1)) { // && !currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) { // NO! handled by skipPlayersTurn
                    Main.out("Player one, you were forbidden from discarding. " +
                            "The first move switches to player two.");
                    isPlayerOnesTurn = false;
                    isFirstMove = false;
                    playerTwosTurn();
                }
                // don't flip turns here because it's still player one's turn.
            }
            Main.out("OK it's your turn, " + player1.getName() + "!");
            isFirstMove = false;
        }
    }

    /**
     * Determines if the player has to react to a non-numeric card move. Will return false if the current
     * played card is the same as the last card the player discarded - this is to prevent endless merry-go-rounds
     * of non-numeric discards.
     *
     * ***** THIS METHOD MUST ALWAYS BE CALLED BEFORE Game.playerTwosTurn TO HANDLE NON-NUMERIC CARDS. *****
     *
     * @param  player the player who has the current move
     * @return true if a non-numeric card has been received, false otherwise. Also return false if (see above).
     */
    public boolean skipPlayersTurn(Player player) { // *** TESTING IN PROGRESS ***
            if(isFirstMove) {
                if(!currentPlayedCard.isNumberCard()) {
                    if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                        draw(player);
                        draw(player);
                        return true; // tested
                    }
                    return true; // tested
                } else {
                    return false; // tested
                }
            } else {
                // this is a problem when player one is skipped on the first move and it switches over to player two

                // Card lastPlayedCard = player.getMyLastPlayedCard();
                Card lastPlayedCard = currentPlayedCard;
                if(player.getMyLastPlayedCard() != null) {
                    lastPlayedCard = player.getMyLastPlayedCard();
                }
                Card wild = new Card(Card.COLORLESS, Card.WILD, cvm);

                boolean lastPlayedCardIsNumeric = lastPlayedCard.isNumberCard();
                boolean lastPlayedCardIsWild = lastPlayedCard.equals(wild);
                boolean lastPlayedCardIsNonNumericAndNonWild = !lastPlayedCard.isNumberCard() && !lastPlayedCard.equals(wild);

                boolean currentPlayedCardIsNumeric = currentPlayedCard.isNumberCard();
                boolean currentPlayedCardIsWild = currentPlayedCard.equals(wild);
                boolean currentPlayedCardIsNonNumericAndNonWild = !currentPlayedCard.isNumberCard()
                        && !currentPlayedCard.equals(wild);

                // todo: come back later and fold the below conditionals together so as to prettify them a bit:
                // (Leaving them alone for now because I nearly lost my mind trying to design the algorithm.)

                if (lastPlayedCardIsNumeric && currentPlayedCardIsNumeric) {
                    // do not skip player's turn
                    return false; // tested
                }
                if (lastPlayedCardIsWild && currentPlayedCardIsNumeric) {
                    // do not skip player's turn
                    return false; // tested
                }
                if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsNonNumericAndNonWild) {
                    // do not skip player's turn
                    return false; // tested
                }



                if (lastPlayedCardIsNumeric && currentPlayedCardIsNonNumericAndNonWild) {
                    // draw as needed and skip player's turn
                    if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                        draw(player);
                        draw(player);
                        return true; // tested
                    }
                    if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                        draw(player);
                        draw(player);
                        draw(player);
                        draw(player);
                        return true; // tested
                    }
                }
                if (lastPlayedCardIsWild && currentPlayedCardIsNonNumericAndNonWild) {
                    // draw as needed and skip player's turn
                    if (currentPlayedCard.getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                        draw(player);
                        draw(player);
                        return true; // tested
                    }
                    if (currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                        draw(player);
                        draw(player);
                        draw(player);
                        draw(player);
                        return true; // tested
                    }
                }




                if (lastPlayedCardIsNumeric && currentPlayedCardIsWild) {
                    player.chosenColor = getOtherPlayersChosenColor(player);
                    // do not skip player's turn
                    return false; // tested
                }
                if (lastPlayedCardIsWild && currentPlayedCardIsWild) {
                    player.chosenColor = getOtherPlayersChosenColor(player);
                    // do not skip player's turn
                    return false; // tested
                }




                if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsNumeric) {
                    Main.out("WARN: Game.skipPlayersTurn encountered a state that should never occur. " +
                            "No action taken, returned true.");
                    return true; // tested
                }
                if (lastPlayedCardIsNonNumericAndNonWild && currentPlayedCardIsWild) {
                    Main.out("WARN: Game.skipPlayersTurn encountered a state that should never occur. " +
                            "No action taken, returned true.");
                    return true; // tested
                }




                Main.out("WARN: logic in Game.skipPlayersTurn fell through to the bottom. No action taken, returning true.");
                return true;
            }
    }

    /**
     * Player two's turn.
     */
    public void playerTwosTurn() { // not unit tested - might only be functionally testable.
        if(!skipPlayersTurn(player2) || currentPlayedCard.getFace().equalsIgnoreCase(Card.WILD)) { // 2nd conditional unnecessary? ****************
            if(isPlayerOnesTurn) {
                Main.out("ERROR: Game.playerTwosTurn called during player one's turn. No action taken.");
            } else {
                if(player2.otherPlayerIncorrectlyForgotToCallCero(player1)) {
                    for(int i = 0; i < 2; i++) {
                        draw(player1);
                    }
                }
                currentPlayedCard = playerTwoMove();
                player2.setMyLastPlayedCard(currentPlayedCard);
                if(currentPlayedCard == null) {
                    Main.out("ERROR: Game.playerTwoMove returned a null card to Game.playerTwosFirstMove." +
                            "No action taken (showstopper).");
                } else {
                    if(!isFirstMove) {
                        currentColor = player2.chosenColor; // could be a wild card if not first move
                    } else {
                        currentColor = currentPlayedCard.getColor();
                    }
                    discardPile.add(currentPlayedCard);
                    Main.out("Player two has discarded a card. ");
                }
            }
        } else {
            // Must be a reverse, skip, or draw two.
            Main.out("Player Two was forbidden from discarding.");
        }
    }

    /**
     * A player's chance to move.
     *
     * @return the card player two has chosen to discard
     */
    public Card playerTwoMove() { // *** MORE TESTING NEEDED - possibly functional only ***
        if(deck.getDeckStack().empty() && discardPile.isEmpty()) {
            Main.out("ERROR: Game.playerTwoMove - both deck and discard pile are empty. " +
                    "No action taken, returned null.");
            return null;
        }
        if (isPlayerOnesTurn) {
            Main.out("ERROR: Game.playerTwoMove called during player one's turn. No action taken, returned null.");
            return null;
        }
        Card cardToDiscard = null;
        // just discard first card for debug
        return player2.getHand().getFirstCard();

        // Currently under construction: *********************************************************************************************************************
//        boolean playerTwoHasDiscarded = false;
//        int i = 0;
//        while(!playerTwoHasDiscarded && i < MAX_P2_DRAW_LOOP) {  // prevent infinite looping
//            cardToDiscard = player2.decidePlayerTwoDiscard(currentPlayedCard, currentColor);
//            if (cardToDiscard == null) {
//                draw(player2);
//            } else {
//                playerTwoHasDiscarded = true;
//            }
//            i++;
//            if(i == (MAX_P2_DRAW_LOOP - 1)) {
//                Main.out("WARN: Game.playerTwoMove was stopped from falling into an infinite loop " +
//                        "while trying to find a playable card in the deck. " +
//                        "Loop aborted at iteration " + (MAX_P2_DRAW_LOOP - 1) + ", null returned.");
//                return null;
//            }
//        }
//        if(player2.discard(cardToDiscard, currentPlayedCard, false, null)) { // *** needs to decide callCero value! ***
//            return cardToDiscard;
//        } else {
//            Main.out("Player two attempted an illegal move. This is a showstopper. Returning null.");
//            return null;
//        }
    }

    public String getOtherPlayersChosenColor(Player player) {  // tested
        if(player.isPlayer2()) {
            return player1.chosenColor;
        } else {
            return player2.chosenColor;
        }
    }

    /**
     * Draw a card for a player.
     *
     * @param player the player to draw
     */
    public void draw(Player player) { // tested
        if(deck == null || discardPile == null) {
            Main.out("WARN: Game.draw called with a null deck or a null discard pile, or both. " +
                    "No action taken.");
        } else {
            if(deck.getDeckSize() == 0 && discardPile.size() > 0) {
                refreshDeck();
            }
            Card card = deck.popCard();
            player.getHand().addCard(card);
        }
    }

    /**
     * Transfer the discard pile to the deck and shuffle.
     */
    private void refreshDeck() { // tested by testGame_draw_emptyDeck
        if(deck.getDeckSize() == 0) {
            // Main.out("Deck is empty! Replenishing with discard pile...");
            replaceDeckWithDiscardPile();
            // Main.out("Shuffling deck...");
            deck.shuffle();
            discardPile.clear();
            // Main.out("Deck size is now " + deck.getDeckSize());
        }
    }

    /**
     * Replace the current deck of cards with an injected discard pile.
     */
    private void replaceDeckWithDiscardPile() { // tested by testGame_draw_emptyDeck
        if(discardPile == null || discardPile.isEmpty()) {
            Main.out("WARN: Game.replaceDeckWithDiscardPile received a null or empty discard pile. " +
                    "Deck not replaced.");
        } else {
            deck.clearDeck();
            Stack<Card> discardStack = discardPile;
            deck.replaceWithAnotherDeck(discardStack);
        }
    }

    /**
     * Deal 7 cards each from the top of the deck to each player.
     */
    public void dealHands() { // tested
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        for(Player p : players) {
            List<Card> cards = new ArrayList<>();
            for(int i = 0; i < 7; i++) {
                cards.add(deck.popCard());
            }
            p.setHand(cards);
        }
    }

    /**
     * Reshuffle deck and pop the first card until a non-wild and non-wild draw four card appears.
     *
     * @param card  The card to analyze
     * @return      A verified card
     */
    public Card verifyFirstCard(Card card) { // tested
        if (card != null) {
            while (card.equals(new Card(Card.COLORLESS, Card.WILD, cvm))
                    || card.equals(new Card(Card.COLORLESS, Card.WILD_DRAW_FOUR, cvm))) {
                deck.clearDeck();
                deck.populate();
                deck.shuffle();
                card = deck.popCard();
            }
            return card;
        } else {
            Main.out("WARN: Game.verifyFirstCard called with a null card. No action taken, null returned.");
            return null;
        }
    }

    /**
     * @return The game's deck (for testing).
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Set the current game's deck (for testing).
     *
     * @param _deck the deck to set
     */
    public void setDeck(Deck _deck) {
        deck.replaceWithAnotherDeck(_deck.getDeckStack());
    }

    /**
     * @return the game's discard pile (for testing)
     */
    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * Set the discard pile.
     *
     * @param injectedDiscardPile the discard pile to set
     */
    public void setDiscardPile(Stack<Card> injectedDiscardPile) { // tested by testGame_draw_emptyDeck
        if(injectedDiscardPile.isEmpty()) {
            Main.out("WARN: Game.setDiscardPile called with empty discardPile. No action taken.");
        }
        this.discardPile.clear();
        Iterator<Card> injectedDiscardCards = injectedDiscardPile.iterator();
        while(injectedDiscardCards.hasNext()) {
            this.discardPile.add(injectedDiscardCards.next());
        }
    }

    /**
     * Clear the current deck (for testing)
     */
    public void clearDeck() { // no test needed
        deck.clearDeck();
    }

    /**
     * @return a list of both game players (for testing)
     */
    public List<Player> getPlayers() { // no test needed
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        return players;
    }

    /**
     * Print the hand for the given player.
     *
     * @param player the given player
     */
    public void printHand(Player player) { // no test needed
        if(player.getHand() == null) {
            Main.out("WARN: Game.printHand called with a null hand. No action taken.");
        } else {
            int count = 1;
            Main.out(player.getName() + "'s hand:");
            List<String> allCards = player.getHand().getHandPrintStringList();
            for(String s : allCards) {
                if(count < 15) { // debug
                    if(count < 10) {
                        Main.out("0" + count + ": " + s);
                        count++;
                    } else {
                        Main.out(count + ": " + s);
                        count++;
                    }
                }
            }
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }

    public void setIsFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public Card getCurrentPlayedCard() {
        return currentPlayedCard;
    }

    public void setCurrentPlayedCard(Card card) {
        this.currentPlayedCard = card;
    }

    public void setPlayerOnesTurn(boolean playerOnesTurn) {
        isPlayerOnesTurn = playerOnesTurn;
    }

    public boolean isPlayerOnesTurn() {
        return isPlayerOnesTurn;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

//    public void setTestStartGame(boolean testStartGame) {
//        this.testStartGame = testStartGame;
//    }


}