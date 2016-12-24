package com.jason;

import java.util.regex.Pattern;

public class Start {

    public static void main(String[] args) {

        /* uncomment out below for real game.
        sayLine("Greetings player one!");
        say("Please enter your name (no special characters): ");
        String name = System.console().readLine();
        while(!isValid(name)) {
            sayLine("Please try again.");
            say("Allowed characters are a-z, A-Z, and space: ");
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

    protected static void say(String s) {
        System.out.println(s);
    }

    protected static void sayNoReturn(String s) {
        System.out.print(s);
    }

}
