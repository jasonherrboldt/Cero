package com.jason;

import java.util.regex.Pattern;

/**
 * Main class. Create a new game and run it.
 *
 * Created by jasonherrboldt on 12/24/16.
 */
public class Main {

    public static void main(String[] args) {

        /*
         * To run via command line:
         *
         *     cd [root]
         *     javac src/com/jason/*.java
         *     java -cp ./src com.jason.Main
         */

        // Uncomment out this block to run interactive game.
        /*
        sayNoReturn("Greetings player one!");
        out("Please enter your name (no special characters): ");
        String name = System.console().readLine();
        while(!isValid(name)) {
            sayNoReturn("Please try again.");
            out("Allowed characters are a-z, A-Z, and space: ");
            name = System.console().readLine();
        }
        System.out.println("Welcome, " + name + "! Let's begin.");

        Game game = new Game(name);
        game.play();
        */

        // Shortcut to game (debug).
        Game game = new Game("David Lightman");
        game.play();

    }

    /**
     * Checks for invalid characters in submitted name.
     *
     * @param name  The user's name.
     * @return      True if name contains only alphanumeric characters, false otherwise.
     */
    private static boolean isValid(String name) {
        Pattern p = Pattern.compile("[^A-Za-z\\s]");
        return !p.matcher(name).find();
    }

    /**
     * Shortcut to System.out.println().
     *
     * @param s The string to getPrintString.
     */
    protected static void out(String s) {
        System.out.println(s);
    }

    /**
     * Shortcut to System.out.getPrintString().
     *
     * @param s The string to getPrintString.
     */
    protected static void outNoReturn(String s) {
        System.out.print(s);
    }


    /**
     * @return random true or false
     */
    public static boolean getRandomBoolean() { // no test needed
        return Math.random() < 0.5;
    }

    /**
     * Ask the user a yes or no question.
     *
     * @param question  The question to ask.
     * @return          True if the user said 'yes', false otherwise.
     */
    public static boolean askUserYesOrNoQuestion(String question) {
        String answer = "";
        while(true) {
            Main.out(question);
            Main.outNoReturn("Please type 'y' or 'n': ");
            answer = System.console().readLine();
            if (answer.equalsIgnoreCase("y")) {
                return true;
            } else if(answer.equalsIgnoreCase("n")) {
                return false;
            } else {
                Main.out("Invalid answer received.");
            }
        }
    }

}












