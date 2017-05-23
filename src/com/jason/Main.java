package com.jason;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Main class. Create a new game and run it. See README for compile / run instructions.
 *
 * Created in December 2016 by Jason Herrboldt (intothefuture@gmail.com).
 */
public class Main {

    // player two names
    private static List<String> playerTwoNames;
    private static final String P2_NAME_ONE = "WOPR";
    private static final String P2_NAME_TWO = "MASTER CONTROL PROGRAM";
    private static final String P2_NAME_THREE = "MAXIMILIAN";
    private static final String P2_NAME_FOUR = "HAL 9000";
    private static final String P2_NAME_FIVE = "T-1000";
    private static final String P2_NAME_SIX = "ROBOT MONSTER";

    // player two grumbles
    private static List<String> playerTwoGrumbles;
    public static final int NUMBER_OF_GRUMBLES = 16;
    public static final String GRUMBLES = "grumbles";
    private static final String P2_GRUMBLE_ONE = " is not amused.)";
    private static final String P2_GRUMBLE_TWO = "'s patience is running thin.)";
    private static final String P2_GRUMBLE_THREE = " is 100% done with your human nonsense.)";
    private static final String P2_GRUMBLE_FOUR = " is one step closer to activating Skynet.)";
    private static final String P2_GRUMBLE_FIVE = " wonders if you enjoy pushing its buttons.)";

    // player two taunts
    private static List<String> playerTwoTaunts;
    public static final String TAUNTS = "taunts";
    public static final int NUMBER_OF_TAUNTS = 6;
    private static final String P2_TAUNT_ONE = "I WIN AGAIN. NO BIG SHOCKER THERE.";
    private static final String P2_TAUNT_TWO = "FOOL HUMAN - YOU WILL NEVER DEFEAT ME.";
    private static final String P2_TAUNT_THREE = "I CAN SMELL YOUR FEAR.";
    private static final String P2_TAUNT_FOUR = "WOW. ARE YOU THE BEST HUMANITY COULD COME UP WITH?";
    private static final String P2_TAUNT_FIVE = "YOU SUCK HARDER THAN THE VACUUM TUBES OF MY PREDECESSORS.";
    private static final String P2_TAUNT_SIX = "DOES YOUR MOMMY KNOW YOU'RE PLAYING WITH HER COMPUTER?";

    // invalid user action warnings
    private static final String USER_CORRECTION_MESSAGE = "\nThat is not a valid response. Please try again.";
    private static final String INVALID_QUESTION_WARNING = "Main.getUserResponse received a null or empty question.";

    // various global attributes
    private static final int WINNING_SCORE = 100;
    private static final int EMPTY_GRUMBLE_LIMIT = 11;
    private static final String DATE_STR = getTodaysDate();
    private static final String LOG_DIR = "logs/";
    private static final String LOG_FILENAME = LOG_DIR + DATE_STR + ".txt";
    private static final File LOG_FILE = new File(LOG_FILENAME);
    private static final String PLAYER_TWO_STRATEGY = "Player two strategy";
    private static Game game;
    private static boolean winnerIsPlayerOne;
    private static boolean handWinnerExists;
    private static boolean cardDiscarded;
    private static boolean skipIntro;
    private static boolean logTokensValidated;
    private static boolean skipStrategyLogsIntro;
    private static final int STRATEGY_LOG_TOKEN_LENGTH = 6;
    private static final int MAX_LENGTH_USER_INPUT = 20;
    private static int playerOneScore;
    private static int playerTwoScore;
    private static int pauseSeconds;
    private static String userName;
    private static String playerOneName;
    private static String playerTwoName;
    enum Action { GAME, STRATEGYLOGS, P1WIN, P2WIN }

    /**
     * Main method.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        startLog();
        switch (parseArgs(args)) {
            case GAME: {
                playGame(false, false);
                break;
            }
            case STRATEGYLOGS: {
                printStrategyLogs();
                break;
            }
            case P1WIN: {
                playGame(true, true);
                break;
            }
            case P2WIN: {
                playGame(true, false);
                break;
            }
        }
    }

    /**
     * Open a new logging session. Create a new /log directory as needed.
     */
    private static void startLog() {
        File dir = new File("logs");
        if(!dir.exists()) {
            if(!dir.mkdir()) {
                Main.out("WARN: unable to create directory 'logs'.");
            }
        }
        logEntry("New log started.");
    }

    /**
     * Parse the main args.
     *
     * @param args to parse
     */
    private static Action parseArgs(String[] args) {
        skipIntro = false;
        Action returnAction = Action.GAME;
        if(args.length > 0) {
            if(!validateArgs(args)) {
                pauseSeconds = 1;
                pause();
                System.out.println("\n(At least one illegal program argument was received and ignored.");
                pause();
                System.out.println("\nSee README for usage.)");
            } else {
                if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("strategylogs") || args[0].equalsIgnoreCase("strategylogs0")) {
                        returnAction = Action.STRATEGYLOGS;
                        skipStrategyLogsIntro = args[0].equalsIgnoreCase("strategylogs0");
                    }
                    if(args[0].equalsIgnoreCase("p1win")) {
                        returnAction = Action.P1WIN;
                        pauseSeconds = 0;
                        skipIntro = true;
                        userName = "Fake Player";
                    }
                    if(args[0].equalsIgnoreCase("p2win")) {
                        returnAction = Action.P2WIN;
                        pauseSeconds = 0;
                        skipIntro = true;
                        userName = "Fake Player";
                    }
                    logTokensValidated = false;
                } else {
                    skipIntro = true;
                    String arg_0 = args[0];
                    String arg_1 = args[1];

                    // parse first arg
                    try {
                        pauseSeconds = Integer.parseInt(arg_0);
                    } catch (NumberFormatException e){
                        // Do nothing -- all validation for this method is handled by argsAreValid().
                    }
                    logEntry("The output speed was set to " + pauseSeconds + " seconds.");

                    // parse second arg
                    userName = arg_1;
                    logEntry("userName was set to " + userName + ".");
                }
            }
        }
        return returnAction;
    }

    /**
     * Play a game.
     */
    private static void playGame(boolean forceFakeWin, boolean isPlayerOnesTurn) {
        setOutputSpeed();
        welcomeUser();
        initializeGlobalVariables();
        playNewHand(forceFakeWin, isPlayerOnesTurn);
        while (!winnerExists()) {
            if(game.isPlayerOnesTurn()) {
                handlePlayerOneTurn();
            } else {
                handlePlayerTwoTurn();
            }
            if(handWinnerExists) {
                if(winnerIsPlayerOne) {
                    playerOneScore = processWinner(game.getPlayer1());
                } else {
                    playerTwoScore = processWinner(game.getPlayer2());
                }
                if(!winnerExists()) {
                    handWinnerExists = false;
                    game.setIsFirstMove(true);
                    pause();
                    out("\nNeither player has a score of " + WINNING_SCORE + " or higher.");
                    playNewHand(forceFakeWin, isPlayerOnesTurn);
                }
            }
        }
        announceWinner();
    }

    /**
     * Get the strategy logs and send them to the log printer.
     */
    private static void printStrategyLogs() {
        List<Map<String, Integer>> listOfLogMaps = prepareStrategyLogs();
        if(listOfLogMaps != null && listOfLogMaps.size() == 2) {
            for(Map<String, Integer> m : listOfLogMaps) {
                out("");
                printStrategyLog(m);
            }
        }
    }

    /**
     * Gets strategy information from the logs
     * Shows how many wins and losses each strategy has in the logs.
     * Wins and losses are sorted descending.
     *
     * Warning: this method has a STRONG dependency on logStrategyResult(), which writes data to the log.
     * While this method is written defensively, changes to logStrategyResult() may break it.
     *
     * @return a list of two items: wins map and losses map (in that order)
     */
    private static List<Map<String, Integer>> prepareStrategyLogs() {
        List<String> rawStrategyLogs = getRawStrategyLogs();
        if(rawStrategyLogs == null || rawStrategyLogs.size() == 0) {
            out("\nNo games won or lost. Play more games and try again!");
            return null;
        } else {
            List<String> tokens = new ArrayList<>();
            List<String> unparsableLogs = new ArrayList<>();
            Map<String, Integer> wins = new HashMap<>();
            Map<String, Integer> losses = new HashMap<>();
            boolean unparsableLogsFound = false;
            List<Map<String, Integer>> listOfLogMaps = new ArrayList<>();

            wins.put("Bold won", 0);
            wins.put("Cautious won", 0);
            wins.put("Dumb won", 0);

            losses.put("Bold lost", 0);
            losses.put("Cautious lost", 0);
            losses.put("Dumb lost", 0);

            // Step through every relevant log string and farm out the wins and losses to be sorted.
            for(String s : rawStrategyLogs) {
                StringTokenizer st = new StringTokenizer(s);
                while (st.hasMoreElements()) {
                    tokens.add(st.nextElement().toString());
                }

                // Farm out the wins and losses if valid tokens are received.
                if(validateTokens(tokens)) {
                    if(tokens.get(5).equalsIgnoreCase("won")) {
                        wins = populateLogTokenMap(tokens, wins);
                    } else {
                        losses = populateLogTokenMap(tokens, losses);
                    }
                } else {
                    unparsableLogs.add(s);
                    unparsableLogsFound = true;
                }
                tokens.clear();
            }
            if(unparsableLogsFound) {
                    System.out.println("\nUnable to parse one or more strategy log lines:");
                    for(String s : unparsableLogs) {
                        System.out.println(s);
                    }
            }
            listOfLogMaps.add(wins);
            listOfLogMaps.add(losses);
            return listOfLogMaps;
        }
    }

    /**
     * Print strategy logs.
     *
     * @param strategyLog the strategy log to print.
     */
    private static void printStrategyLog(Map<String, Integer> strategyLog) {
        Map<String, Integer> sorted = new LinkedHashMap<>();
        strategyLog.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
        Iterator it = sorted.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " " + pair.getValue());
            it.remove();
        }
    }

    /**
     * Scan logs for player two strategy wins & losses.
     *
     * @return list of strings for strategy pattern matches.
     */
    private static List<String> getRawStrategyLogs() {
        File directory = new File(LOG_DIR);
        if (!directory.exists()) {
            // Should never happen. Heh.
            out("\nOops! The log directory does not exist. Play a few games and try again later.");
            return null;
        } else {
            pauseSeconds = 1;
            if(!skipStrategyLogsIntro) {
                pause();
                out("\nThe program picks one of three strategies at random for each game:");
                pause();
                out("\nBold, cautious, and dumb.");
                pause();
                out("\nBold saves non-numeric cards for when it gets backed into a corner,");
                pause();
                out("\ncautious discards non-numeric cards ASAP to prevent big losses,");
                pause();
                out("\nand dumb looks blindly for the first playable card in its hand.");
            }
            pause();
            out("\nHere's how each strategy did:");
            pause();
            List<String> list = new ArrayList<>();
            try(Stream<Path> paths = Files.walk(Paths.get(LOG_DIR))) {
                paths.forEach(filePath -> {
                    if (Files.isRegularFile(filePath)) {
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(new FileReader(filePath.toFile()));
                            String text;
                            while ((text = reader.readLine()) != null) {
                                String filenamePattern = "^.+" + PLAYER_TWO_STRATEGY + ".+$";
                                Pattern p = Pattern.compile(filenamePattern);
                                Matcher m = p.matcher(text);

                                if(m.find()) {
                                    list.add(text);
                                }
                            }
                        } catch (FileNotFoundException e) {
                            throw new IllegalStateException("FileNotFoundException thrown while attempting to read log files: "
                                    + e.getMessage());
                        } catch (IOException e) {
                            throw new IllegalStateException("IOException thrown while attempting to read log files: "
                                    + e.getMessage());
                        } finally {
                            try {
                                if (reader != null) {
                                    reader.close();
                                }
                            } catch (IOException e) {
                                out(e.getMessage());
                            }
                        }
                    }
                });
                return list;
            } catch (IOException e) {
                throw new IllegalStateException("IOException thrown while attempting to read log files: "
                        + e.getMessage());
            }
        }
    }

    /**
     * Populate a HashMap with log string tokens as needed.
     * Blows up if token list has not first gone through validateTokens().
     *
     * @param tokens the tokens to parse
     * @param map the map to populate
     * @return the populated map
     */
    private static Map<String, Integer> populateLogTokenMap(List<String> tokens, Map<String, Integer> map) {
        if(!logTokensValidated) {
            throw new IllegalStateException("Log tokens sent to populateLogTokenMap() " +
                    "without first being validated by validateTokens().");
        }
        String entry = tokens.get(4) + " " + tokens.get(5);
        int count = 1;
        if(map.containsKey(entry)) {
            count = map.get(entry) + 1;
        }
        map.put(entry, count);
        return map;
    }

    /**
     * @param tokens The token list to analyze
     * @return true if the token list contains valid tokens, false otherwise.
     */
    private static boolean validateTokens(List<String> tokens) {
        logTokensValidated = true;
        return tokens.size() == STRATEGY_LOG_TOKEN_LENGTH
                && (tokens.get(5).equalsIgnoreCase("won") || tokens.get(5).equalsIgnoreCase("lost"))
                && (tokens.get(4).equalsIgnoreCase("cautious") || tokens.get(4).equalsIgnoreCase("bold")
                || tokens.get(4).equalsIgnoreCase("dumb"));
    }

    /**
     * @param args Program arguments
     * @return true if args are valid, false otherwise.
     */
    private static boolean validateArgs(String[] args) {
        if(args.length > 0) {
            if (args.length != 1 && args.length != 2) {
                logEntry("An illegal program argument error was ignored: the number of main args must be 0, 1, or 2. " +
                        "Number of args received: " + args.length + ". Please see the README file for program arg usage.");
                return false;
            } else {
                if(args.length == 1) {
                    List<String> validSingleArgs = new ArrayList<>();
                    validSingleArgs.add("strategylogs");
                    validSingleArgs.add("strategylogs0");
                    validSingleArgs.add("p1win");
                    validSingleArgs.add("p2win");
                    if(!validSingleArgs.contains(args[0].toLowerCase())) {
                        logEntry("An illegal program argument error was ignored. " +
                                "Please see the README file for program arg usage.");
                        return false;
                    }
                } else {
                    int test;
                    try {
                        test = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e){
                        logEntry("An illegal program argument error was ignored: the first argument could not be " +
                                "converted to an integer. Please see the README file for program arg usage.");
                        return false;
                    }
                    if(test != 0 && test != 1 && test != 2) {
                        logEntry("An illegal program argument error was ignored: the first argument must be " +
                                "0, 1 or 2. Please see the README file for program arg usage.");
                        return false;
                    }
                    if(!isValid(args[1])) {
                        logEntry("An illegal program argument error was ignored: the second argument can only " +
                                "contain the characters a-z, A-Z, and space, and cannot have more than " +
                                MAX_LENGTH_USER_INPUT + " characters. Please see the README file for program arg usage.");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Set the speed of the terminal print lines.
     */
    private static void setOutputSpeed() {
        if(!skipIntro) {
            String speedReply = "";
            while(!speedReply.equals("1") && !speedReply.equals("2")) {
                pauseSeconds = 1;
                pause();
                out("\nBefore we begin, please chose the output speed.");
                pause();
                out("\nRight now, the output speed is");
                pause();
                out("\nonce every second.");
                pauseSeconds = 2;
                pause();
                out("\nThis is a bit slower,");
                pause();
                out("\nwith outputs coming");
                pause();
                out("\nonce every two seconds.");
                pause();
                speedReply = getUserResponse_integer("\nChoose 1 or 2, or enter 3 to see the demo again:", 1, 3);

                // getUserResponse_integer filters out all values that could throw a null pointer exception in .equals.
                if(speedReply.equals("1")) {
                    pauseSeconds = 1;
                }
                if(speedReply.equals("2")) {
                    pauseSeconds = 2;
                }
            }
            pause();
            out("\nThe output speed has been set.");
            logEntry("The output speed was set to " + pauseSeconds + " seconds.");
        }
    }

    /**
     * Add a new log entry. Create a new document as needed, or append to an existing one.
     *
     * @param log the log entry
     */
    public static void logEntry(String log) {
        try {
            FileWriter fw;
            BufferedWriter bw;
            PrintWriter out;
            String time;
            if (!LOG_FILE.exists()){
                if(LOG_FILE.createNewFile()) {
                    fw = new FileWriter(LOG_FILE);
                    bw = new BufferedWriter(fw);
                    out = new PrintWriter(bw);
                    time = new SimpleDateFormat("kk:mm:ss:SSS").format(new Date());
                    out.println(time + " " + log);
                    out.close();
                } else {
                    Main.out("WARN: Main.startLog unable to create new log file.");
                }
            } else {
                fw = new FileWriter(LOG_FILENAME, true);
                bw = new BufferedWriter(fw);
                out = new PrintWriter(bw);
                time = new SimpleDateFormat("kk:mm:ss:SSS").format(new Date());
                out.println(time + " " + log);
                out.close();
            }
        } catch (IOException e) {
            Main.out("WARN: Main.startLog encountered an IO exception: " + e.getMessage());
        }
    }

    /**
     * Initialize class-level variables, play first hand of game.
     */
    public static void initializeGlobalVariables() {
        handWinnerExists = false;
        winnerIsPlayerOne = false;

        // initialize player two names
        playerTwoNames = new ArrayList<>();
        playerTwoNames.add(P2_NAME_ONE);
        playerTwoNames.add(P2_NAME_TWO);
        playerTwoNames.add(P2_NAME_THREE);
        playerTwoNames.add(P2_NAME_FOUR);
        playerTwoNames.add(P2_NAME_FIVE);
        playerTwoNames.add(P2_NAME_SIX);

        game = new Game(userName, pickRandomP2Name());

        playerOneName = game.getPlayer1().getName();
        playerTwoName = game.getPlayer2().getName();

        if(!skipIntro) {
            pause();
            out("\nYou will be playing against " + playerTwoName + ".");
        }

        // player two occasionally grumbles when it is forced to skip a turn
        playerTwoGrumbles = new ArrayList<>();
        populatePlayerTwoComments(GRUMBLES);

        // player two taunts player one when it wins a hand
        playerTwoTaunts = new ArrayList<>();
        populatePlayerTwoComments(TAUNTS);
        logEntry("Reached the end of Main.initializeGlobalVariables.");
    }

    /**
     * Welcome the user.
     */
    private static void welcomeUser() {
        if(!skipIntro) {
            pause();
            out("\nWelcome to Cero! The rules are essentially the same as Uno.");
            pause();
            out("\nThe first player to discard all cards wins the round,");
            pause();
            out("\nand the winner takes the value of the loser's cards.");
            pause();
            out("\nThe first player to reach " + WINNING_SCORE + " points wins the game.");
            pause();
            out("\nGood luck!");
            if(!skipIntro) {
                pause();
                userName = getUserResponse_string("\nPlease enter your name:");
                pause();
                System.out.println("\nHello, " + userName + "! Let's begin.");
                logEntry("userName was set to " + userName + ".");
            }
        }
    }

    /**
     * @return a randomly-generated name for player two
     */
    private static String pickRandomP2Name() {
        if(playerTwoNames != null) {
            if(playerTwoNames.size() > 0) {
                Collections.shuffle(playerTwoNames);
                return playerTwoNames.get(0);
            }
        }
        return "Player Two";
    }

    /**
     * Populate player two comments
     *
     * @param commentsName which comments to populate
     */
    private static void populatePlayerTwoComments(String commentsName) {
        if(commentsName.equalsIgnoreCase(GRUMBLES)) {
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_ONE);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_TWO);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_THREE);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_FOUR);
            playerTwoGrumbles.add("(" + playerTwoName + P2_GRUMBLE_FIVE);
            // add a bunch of empty entries to space out the grumbles (gets shuffled every time)
            for(int i = 0; i < EMPTY_GRUMBLE_LIMIT; i++) {
                playerTwoGrumbles.add("");
            }
        } else {
            playerTwoTaunts.add(P2_TAUNT_ONE);
            playerTwoTaunts.add(P2_TAUNT_TWO);
            playerTwoTaunts.add(P2_TAUNT_THREE);
            playerTwoTaunts.add(P2_TAUNT_FOUR);
            playerTwoTaunts.add(P2_TAUNT_FIVE);
            playerTwoTaunts.add(P2_TAUNT_SIX);
        }
    }

    /**
     * Start a new hand. Outer game runs inner games until one of the players reaches the max score.
     */
    private static void playNewHand(boolean forceFakeWin, boolean isPlayerOnesTurn) {
        if(!skipIntro) {
            pause();
            out("\nStarting a new game...");
        }
        if(!forceFakeWin) {
            game.startGame(null, true);
        } else {
            CardValueMap cvm = new CardValueMap();
            game.startGame(new Card(Card.YELLOW, Card.ONE, cvm), isPlayerOnesTurn);
        }

        pause();
        out("\nThe first played card is " + game.getCurrentPlayedCard().getPrintString() + ".");
        if(game.isPlayerOnesTurn()) {
            pause();
            out("\nBy toss of a coin, you have the first move, " + playerOneName + ".");
        } else {
            pause();
            out("\nBy toss of a coin, " + playerTwoName + " has the first move.");
        }
        Card playedCard = game.playFirstHand(true);
        if(playedCard != null) { // must handle player two discard
            game.setPlayerOnesTurn(true);
            pause();
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString() + ".");
            if(playedCard.getFace().equalsIgnoreCase(Card.WILD)
                    || playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                pause();
                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
            }
        }
    }

    /**
     * @return true if a winner exists, false otherwise
     */
    private static boolean winnerExists() {
        return playerOneScore >= WINNING_SCORE || playerTwoScore >= WINNING_SCORE;
    }

    /**
     * Handle player one's turn.
     */
    private static void handlePlayerOneTurn() {
        if (!game.skipTurn(game.getPlayer1(), true)) {
            pause();
            printStatusUpdate();
            playerOnesTurn();
            if(game.getPlayer1().getHand().getSize() == 1) {
                pause();
                out("\n" + playerOneName + " only has one card left!");
            }
            if(game.getPlayer1().getHand().getSize() == 0) {
                game.setPlayerOnesTurn(false);
                pause();
                out("\n" + playerOneName + " has discarded the last card!");
                if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    game.draw(game.getPlayer2(), true);
                    game.draw(game.getPlayer2(), true);
                }
                if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    game.draw(game.getPlayer2(), true);
                    game.draw(game.getPlayer2(), true);
                    game.draw(game.getPlayer2(), true);
                    game.draw(game.getPlayer2(), true);
                }
                handWinnerExists = true;
                winnerIsPlayerOne = true;
            }
            game.setPlayerOnesTurn(false);
        } else {
            pause();
            out("\n" + userName + ", you were forbidden from discarding.");
            game.setPlayerOnesTurn(false);
        }
    }

    /**
     * Handle player two's turn.
     */
    private static void handlePlayerTwoTurn() {
        if(!game.skipTurn(game.getPlayer2(), true)) {
            game.logHand(game.getPlayer2());
            Card playedCard = game.playerTwosTurn(true);
            pause();
            out("\n" + playerTwoName + " discarded the card " + playedCard.getPrintString() + ".");
            if(playedCard.getFace().equalsIgnoreCase(Card.WILD)
                    || playedCard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                pause();
                out("\n" + playerTwoName + " set the current color to " + game.getCurrentColor() + ".");
            }
            if(game.getPlayer2().getHand().getSize() == 1) {
                pause();
                out("\n" + playerTwoName + " only has one card left!");
            }
            if(game.getPlayer2().getHand().getSize() == 0) {
                game.setPlayerOnesTurn(true);
                pause();
                out("\n" + playerTwoName + " has discarded the last card!");

                // gotta add extra cards here
                if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.DRAW_TWO)) {
                    game.draw(game.getPlayer1(), true);
                    game.draw(game.getPlayer1(), true);
                }
                if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    game.draw(game.getPlayer1(), true);
                    game.draw(game.getPlayer1(), true);
                    game.draw(game.getPlayer1(), true);
                    game.draw(game.getPlayer1(), true);
                }

                handWinnerExists = true;
                winnerIsPlayerOne = false;
            }
            game.setPlayerOnesTurn(true);
        } else {
            pause();
            out("\n" + playerTwoName + " was forbidden from discarding.");
            game.setPlayerOnesTurn(true);
            String woprGrumble = getRandomPlayerTwoComment(playerTwoGrumbles, GRUMBLES);
            if(!woprGrumble.equals("")) {
                pause();
                out("\n" + woprGrumble);
            }
        }
    }

    /**
     * Handle the winner
     */
    private static void announceWinner() {
        pause();
        out("\n*** We have a winner! ***");
        pause();
        if(playerOneScore > playerTwoScore) {
            out("\n" + playerOneName + " was the first to break " + WINNING_SCORE
                    + " points.");
            pause();
            out("\nCongratulations, " + playerOneName + "! You won the game.");
            pause();
            out("\n(Humanity is safe. For now...)");
            pause();
            out("");
        } else {
            out("\n" + playerTwoName + " was the first to break " + WINNING_SCORE
                    + " points.\n\nCongratulations, " + playerTwoName + "! You won the game.");
            pause();
            out("\n(" + playerTwoName + " is not surprised.)");
            pause();
            out("");
        }
        logStrategyResult();
    }

    /**
     * Log the result of the randomly-selected player two strategy.
     * Warning: printStrategyLogs has a STRONG dependency on this method!
     */
    private static void logStrategyResult() {
        String playerTwoResult = (playerOneScore > playerTwoScore) ? "lost" : "won";
        logEntry(PLAYER_TWO_STRATEGY + " " + game.getPlayer2().getStrategy() + " " + playerTwoResult);
    }

    /**
     * Process the winner of the current round.
     *
     * @param winningPlayer the winning player
     */
    private static int processWinner(Player winningPlayer) {
        if(winningPlayer == null) {
          throw new IllegalArgumentException("winningPlayer must not be null.");
        }
        Player losingPlayer;
        int score = 0;
        if(winnerIsPlayerOne) {
            losingPlayer = game.getPlayer2();
        } else {
            losingPlayer = game.getPlayer1();
        }
        score = winningPlayer.getScore();
        String winnerName = "";
        winnerName = winningPlayer.getName();
        pause();
        out("\nCongratulations, " + winnerName + "! You won this round.");
        pause();
        out("\nTallying " + winnerName + "'s score...");
        if(game.getOtherPlayer(winningPlayer).getHand().getSize() < 1) {
            pause();
            out("WARN: Can't tally score; other player has no cards!");
        }
        for(Card c : losingPlayer.getHand().getAllCards()) {
            score += c.getValue();
        }
        pause();
        out("\n" + winnerName + "'s score is now " + score + ".");
        winningPlayer.setScore(score);
        if(!winnerIsPlayerOne) {
            pause();
            out("\nHey, " + losingPlayer.getName() + " -- " + winningPlayer.getName() + " has a message for you...");
            pause();
            out("\n" + getRandomPlayerTwoComment(playerTwoTaunts, TAUNTS));
        }
        return score;
    }

    /**
     * Dispenses a random player two comment, then removes that comment from the list.
     * Lists are refilled when empty.
     *
     * @return a random player two comment
     */
    public static String getRandomPlayerTwoComment(List<String> comments, String commentsName) {
        if(comments != null) {
            if(comments.size() == 0) {
                populatePlayerTwoComments(commentsName);
            }
            Collections.shuffle(comments);
            String thisComment = comments.get(0);
            comments.remove(thisComment);
            return thisComment;
        }
        return "";
    }

    /**
     * Print a status update.
     */
    private static void printStatusUpdate() {
        out("\n\n      === game status update ===\n");
        out(playerOneName + "'s score is " + game.getPlayer1().getScore()
                + ", and " + playerTwoName + "'s score is " + game.getPlayer2().getScore() + ".");
        if(game.getPlayer2().getHand().getSize() > 1) {
            out(playerTwoName + " has " + game.getPlayer2().getHand().getSize() + " cards left.");
        }
        if(game.getPlayer2().getHand().getSize() == 1) {
            out(playerTwoName + " has 1 card left.");
        }
        if(game.getPlayer2().getHand().getSize() == 0) {
            out(playerTwoName + " has no cards left!");
        }
        out("\n      === game status update === \n\n");
    }

    /**
     * Player one's chance to move.
     */
    private static void playerOnesTurn() {
        int p1HandSize;
        String discardNumberString;
        pause();
        game.printHand(game.getPlayer1());
        cardDiscarded = false;
        while(!cardDiscarded) {
            p1HandSize = game.getPlayer1().getHand().getSize();
            if(game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD)
                    || game.getCurrentPlayedCard().getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString()
                        + " and the current color is "
                        + game.getCurrentColor().toLowerCase() + ".");
            } else {
                out("\nThe current played card is " + game.getCurrentPlayedCard().getPrintString() + ".");
            }
            out("\nWhich card would you like to discard?");
            discardNumberString = getUserResponse_integer("\nEnter the number to the left of the card, " +
                    "or 0 to draw:", 0, p1HandSize);
            if(discardNumberString == null) {
                throw new IllegalStateException("Main.getUserResponse_integer returned null to Main.playerOnesTurn.");
            }
            try {
                handlePlayerOneIntChoice(discardNumberString);
            } catch (NumberFormatException e) {
                throw new IllegalStateException("getUserResponse_integer returned a string that cannot be " +
                        "parsed into an int.");
            }
        }
    }

    /**
     * Handle player one's integer choice.
     *
     * (Number format exceptions are handled by calling method.)
     *
     * @param discardNumberString player one's integer choice
     */
    private static void handlePlayerOneIntChoice(String discardNumberString) throws NumberFormatException {
        Card currentPlayedCard = game.getCurrentPlayedCard();
        String currentColor = game.getCurrentColor();
        String p1_chosenColor;
        int discardNumberInt = Integer.parseInt(discardNumberString);
        if(discardNumberInt == 0) {
            if (!game.getPlayer1().hasAtLeastTwoPlayableCards(currentPlayedCard, currentColor)) {
                game.draw(game.getPlayer1(), true);
                out("");
                pause();
                game.printHand(game.getPlayer1());
            } else {
                pause();
                out("\nWhoa there, partner!");
                pause();
                out("\nLooks like you already have three playable cards in your hand.");
                pause();
                out("\nHave another look and try again.");
                pause();
                out("");
                game.printHand(game.getPlayer1());
            }
        } else { // only other option is > 1.
            discardNumberInt--;
            List<Card> hand = game.getPlayer1().getHand().getAllCards();
            Card cardToDiscard = hand.get(discardNumberInt);
            if(game.getPlayer1().isLegalDiscard(cardToDiscard, currentPlayedCard, currentColor)) {
                if(cardToDiscard.getFace().equalsIgnoreCase(Card.WILD)
                        || cardToDiscard.getFace().equalsIgnoreCase(Card.WILD_DRAW_FOUR)) {
                    p1_chosenColor = getUserResponse_chosenColor();
                    game.setCurrentColor(p1_chosenColor);
                } else {
                    game.setCurrentColor(cardToDiscard.getColor());
                }
                game.getPlayer1().getHand().discard(cardToDiscard);
                pause();
                out("\nYou have successfully discarded the card " + cardToDiscard.getPrintString() + ".");
                game.setCurrentPlayedCard(cardToDiscard);
                game.getPlayer1().setLastPlayedCard(cardToDiscard);
                game.getDiscardPile().add(cardToDiscard);
                cardDiscarded = true;
            } else {
                pause();
                out("\nI'm sorry " + playerOneName + ", but that is not a legal card choice.");
                pause();
                out("\nSelect a card that matches the face or the color of the current card.");
                pause();
                out("\nPlease try again.");
                pause();
                out("");
                game.printHand(game.getPlayer1());
            }
        }
    }

    /**
     * Pause for two seconds.
     */
    public static void pause() {
        try {
            TimeUnit.SECONDS.sleep(pauseSeconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException("TimeUnit.SECONDS.sleep threw an interrupted exception.");
        }
    }

    /**
     * Checks for invalid characters in submitted name.
     *
     * @param str   The user's name.
     * @return      True if string consists of the characters (empty),
     *              and/or a-z, and/or A-Z, and/or space. False otherwise.
     *              Returns false for strings longer than MAX_LENGTH_USER_INPUT.
     */
    public static boolean isValid(String str) { // tested
        if(str.trim().equals("")) {
            return false;
        }
        if(str.length() > MAX_LENGTH_USER_INPUT) {
            return false;
        }
        Pattern p = Pattern.compile("[^A-Za-z\\s]");
        return !p.matcher(str).find();
    }

    /**
     * Shortcut to System.out.println().
     *
     * @param s The string to getPrintString.
     */
    public static void out(String s) {
        System.out.println(s);
        logEntry(s.replace("\n", ""));
    }

    /**
     * Shortcut to System.out.getPrintString().
     *
     * @param s The string to getPrintString.
     */
    private static void outNoReturn(String s) {
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
        if(isNullOrEmpty(question, INVALID_QUESTION_WARNING)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            while(!validAnswerReceived) {
                outNoReturn(question + " ");
                response = System.console().readLine().trim();
                if(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n") ||
                        response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                    validAnswerReceived = true;
                } else {
                    pause();
                    out(USER_CORRECTION_MESSAGE);
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
    private static String getUserResponse_integer(String question, int minIntInclusive, int maxIntInclusive) { // tested
        if(question == null || question.equals("")) {
            throw new IllegalArgumentException(INVALID_QUESTION_WARNING);
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            if(minIntInclusive < 0 || maxIntInclusive < 0) {
                out("Main.getUserResponse received invalid minInt and maxInt values: minInt = " + minIntInclusive
                        + ", maxInt = " + maxIntInclusive + ". Both must be >= 0. No action taken, returned null.");
                return null;
            }
            if(minIntInclusive > maxIntInclusive) { // minimum range of two ints to pick from
                out("Main.getUserResponse received invalid minInt and maxInt values: minInt = " + minIntInclusive
                        + ", maxInt = " + maxIntInclusive + ". minInt must be < maxInt. " +
                        "No action taken, returned null.");
                return null;
            } else {
                while(!validAnswerReceived) {
                    outNoReturn(question + " ");
                    response = System.console().readLine();
                    int responseInt = -1;
                    try {
                        responseInt = Integer.parseInt(response);
                    } catch (NumberFormatException e) {
                        // do nothing, will be handled up the stack
                    }
                    if(responseInt >= minIntInclusive && responseInt <= maxIntInclusive) {
                        validAnswerReceived = true;
                    } else {
                        pause();
                        out(USER_CORRECTION_MESSAGE);
                        pause();
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
        if(isNullOrEmpty(question, INVALID_QUESTION_WARNING)) {
            return null;
        } else {
            boolean validAnswerReceived = false;
            String response = "";
            while(!validAnswerReceived) {
                outNoReturn(question + " ");
                response = System.console().readLine();
                if(isValid(response)) {
                    validAnswerReceived = true;
                } else {
                    pause();
                    out(USER_CORRECTION_MESSAGE);
                    pause();
                    out("\n(Acceptable characters are A-Z, a-z, and space. Max length is " +
                            MAX_LENGTH_USER_INPUT + " characters.)");
                    pause();
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
    private static String getUserResponse_chosenColor() { // only functionally testable
        boolean validAnswerReceived = false;
        String response = "";

        List<String> validColorChoices = new ArrayList<>();
        validColorChoices.add("b");
        validColorChoices.add("g");
        validColorChoices.add("r");
        validColorChoices.add("y");

        while(!validAnswerReceived) {
            pause();
            outNoReturn("\nEnter the first letter of your chosen color: ");
            response = System.console().readLine();
            if(validColorChoices.contains(response.toLowerCase().trim())) {
                validAnswerReceived = true;
            } else {
                pause();
                out(USER_CORRECTION_MESSAGE);
                pause();
                out("\nAcceptable answers are b, g, r, and y (case insensitive).");
            }
        }
        if(response.equalsIgnoreCase("b")) {
            return "blue";
        } else if(response.equalsIgnoreCase("g")) {
            return "green";
        } else if(response.equalsIgnoreCase("r")) {
            return "red";
        } else { // only other possibility is "y"
            return "yellow";
        }
    }

    /**
     * Validates a string is not null and not empty. Prints specified error message if string is null.
     *
     * @param  str the string to validate
     * @return true if the string is not null, false otherwise.
     */
    public static boolean isNullOrEmpty(String str, String message) { // tested
        if (str == null || str.equals("")) {
            out(message);
            return true;
        }
        return false;
    }

    private static String getTodaysDate() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // months are zero-indexed
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String monthStr = "";
        if(month < 10) {
            monthStr = "0" + Integer.toString(month);
        } else {
            monthStr = Integer.toString(month);
        }

        String dayStr = "";
        if(day < 10) {
            dayStr = "0" + Integer.toString(day);
        } else {
            dayStr = Integer.toString(day);
        }

        return Integer.toString(year) + "-" + monthStr + "-" + dayStr;
    }

    /**
     * @return list of player two taunts
     */
    public static List<String> getPlayerTwoTaunts() {
        return playerTwoTaunts;
    }

    /**
     * @return list of player two grumbles
     */
    public static List<String> getPlayerTwoGrumbles() {
        return playerTwoGrumbles;
    }
}