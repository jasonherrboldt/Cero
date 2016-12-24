package com.jason;

import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        /*
            javac src/com/jason/*.java
            java -cp ./src com.jason.Main
         */

        // comment out below for real game.
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

        // shortcut to game.
        Game game = new Game("Michael Jackson");
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

    protected static void out(String s) {
        System.out.println(s);
    }

    protected static void sayNoReturn(String s) {
        System.out.print(s);
    }

}
