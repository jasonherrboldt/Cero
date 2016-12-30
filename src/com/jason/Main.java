package com.jason;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Main class. Create a new game and run it.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Main {

    /*
        set PATH=[path\to\jdk\bin];%PATH%, e.g.
        set PATH=C:\Program Files\Java\jdk1.8.0_111\bin;%PATH%
        (must be > java 1.6)
        javac src/com/jason/*.java
        java -cp ./src com.jason.Main
     */

    private static final String userCorrectionMessage = "That is not a valid response. Please try again.";
    private static final String invalidQuestionWarning =
            "WARN: Main.getUserResponse received a null or empty question. No action taken, returned null.";

    public static void main(String[] args) {
//        Game game = new Game("");
//        GameState gameState = game.startGame();
//        String currentPlayedCardString = gameState.getCurrentPlayedCard().getPrintString();
//        Main.out("Current card: " + currentPlayedCardString);

//        outNoReturn("Please enter your name (no special characters): ");
//        String name = System.console().readLine();
//        while(!isValid(name)) {
//            outNoReturn("Please try again.");
//            out("Allowed characters are a-z, A-Z, and space: ");
//            name = System.console().readLine();
//        }
//        System.out.println("Welcome, " + name + "! Let's begin.");

//        String answer = getUserResponse_yesNo("Do you believe in love?");
//        out("Your answer: " + answer);
//
//        answer = getUserResponse_integer("Please enter an integer between 0 and 5: ", 0, 5);
//        out("Your answer: " + answer);
//
//        answer = getUserResponse_string("Enter any string: ");
//        out("Your answer: " + answer);
//
//        answer = getUserResponse_chosenColor();
//        out("Your answer: " + answer);

        Game game = new Game("David Lightman");
        game.startGame();
        out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString() + "\n");
        game.printHand(game.player1);

//
//        boolean done = false;
//        while(!done) {
//            String answer = getUserResponse_string("Please enter the secret message: ");
//            gameState.setMessage(answer);
//            if(gameState.getMessage().equalsIgnoreCase("password")) {
//                done = true;
//            }
//        }

//        List<Player> players = game.getPlayers();
//        for(Player p : players) {
//            if(p.getScore() > 500) {
//                // we have a winner
//            }
//        }
    }

    /**
     * Checks for invalid characters in submitted name.
     *
     * @param str  The user's name.
     * @return      True if name contains only alphanumeric characters, false otherwise.
     */
    private static boolean isValid(String str) {
        Pattern p = Pattern.compile("[^A-Za-z\\s]");
        return !p.matcher(str).find();
    }

    /**
     * Shortcut to System.out.println().
     *
     * @param s The string to getPrintString.
     */
    static void out(String s) {
        System.out.println(s);
    }

    /**
     * Shortcut to System.out.getPrintString().
     *
     * @param s The string to getPrintString.
     */
    static void outNoReturn(String s) {
        System.out.print(s);
    }


    /**
     * @return random true or false
     */
    static boolean getRandomBoolean() { // no test needed
        return Math.random() < 0.5;
    }

    /**
     * Ask the user a yes or no question.
     *
     * @param question the question to ask
     * @return         the user's answer
     */
    public static String getUserResponse_yesNo(String question) { // tested
        if(!validateQuestion(question, invalidQuestionWarning)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            while(!validAnswerReceived) {
                outNoReturn(question + " ");
                response = System.console().readLine();
                if(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n")) {
                    validAnswerReceived = true;
                } else {
                    out(userCorrectionMessage);
                }
            }
            return response;
        }
    }

    /**
     * Ask the user to provide an integer within a specified range.
     *
     * @param question        the question to ask
     * @param minIntInclusive the minimum inclusive integer the user must input
     * @param maxIntInclusive the maximum inclusive integer the user must input
     * @return                the user's answer - guaranteed to be integer parsable
     */
    public static String getUserResponse_integer(String question, int minIntInclusive, int maxIntInclusive) { // tested
        if(!validateQuestion(question, invalidQuestionWarning)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            if(minIntInclusive < 0 || maxIntInclusive < 0) {
                out("Main.getUserResponse received invalid minInt and maxInt values: minInt = " + minIntInclusive
                        + ", maxInt = " + maxIntInclusive + ". Both must be >= 0. No action taken, returned null.");
                return null;
            }
            if(minIntInclusive >= maxIntInclusive - 1) { // minimum range of two ints to pick from
                out("Main.getUserResponse received invalid minInt and maxInt values: minInt = " + minIntInclusive
                        + ", maxInt = " + maxIntInclusive + ". minInt must be < maxInt. " +
                        "No action taken, returned null.");
                return null;
            } else {
                while(!validAnswerReceived) {
                    outNoReturn(question);
                    response = System.console().readLine();
                    int responseInt = -1;
                    try {
                        responseInt = Integer.parseInt(response);
                    } catch (NumberFormatException e) {
                        out("Your response " + response + " could not be converted to an integer.");
                    }
                    if(responseInt >= minIntInclusive && responseInt <= maxIntInclusive) {
                        validAnswerReceived = true;
                    } else {
                        out(userCorrectionMessage);
                    }
                }
            }
            return response;
        }
    }

    /**
     * Ask the user to provide the answer to a general question.
     *
     * @param question the question to ask
     * @return         the user's response
     */
    public static String getUserResponse_string(String question) { // tested
        if(!validateQuestion(question, invalidQuestionWarning)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            while(!validAnswerReceived) {
                outNoReturn(question);
                response = System.console().readLine();
                if(isValid(response)) {
                    validAnswerReceived = true;
                } else {
                    out(userCorrectionMessage + " (Acceptable characters are A-Z, a-z, and space.)");
                }
            }
            return response;
        }
    }


    /**
     * Ask the user to pick a color for discarded wild cards.
     * Force user to pick one of four pre-set colors.
     *
     * @return the chosen color.
     */
    public static String getUserResponse_chosenColor() { // only functionally testable
        boolean validAnswerReceived = false;
        String response = "";

        List<String> validColorChoices = new ArrayList<>();
        validColorChoices.add(Card.BLUE.toLowerCase());
        validColorChoices.add(Card.GREEN.toLowerCase());
        validColorChoices.add(Card.RED.toLowerCase());
        validColorChoices.add(Card.YELLOW.toLowerCase());

        while(!validAnswerReceived) {
            outNoReturn("What is your chosen color for the next move? ");
            response = System.console().readLine();
            if(validColorChoices.contains(response.toLowerCase())) {
                validAnswerReceived = true;
            } else {
                out(userCorrectionMessage + " Acceptable answers are " + validColorChoices + ".");
            }
        }
        return response;
    }

    /**
     * Validates a string is not null and not empty. Prints specified error message if string is null.
     *
     * @param  str the string to validate
     * @return true if the string is not null, false otherwise.
     */
    public static boolean validateQuestion(String str, String message) { // tested
        if (str == null || str.equals("")) {
            out(message);
            return false;
        }
        return true;
    }
}