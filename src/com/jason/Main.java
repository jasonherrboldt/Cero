package com.jason;

import java.util.regex.Pattern;

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
    protected static void sayNoReturn(String s) {
        System.out.print(s);
    }

}
