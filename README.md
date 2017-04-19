# Cero

A Java coding exercise modeled after the card game Uno. See below for how to run. 

There are two players. The user is player one, and player two is the program. 

See [the Uno Wikipedia page](https://en.wikipedia.org/wiki/Uno_(card_game)) for a full list of game rules.

__HIGHLIGHTED FUNCTIONALITY__

Player two randomly selects one of three strategies at the beginning of the game: bold, cautious, or dumb. This decision is made behind the scenes, but the user can review the log after the game to see which strategy was chosen.

Bold
 * Saves non-numeric cards for when it gets backed into a corner.
 * First choice is a numeric card, second choice is a non-numeric color, third choice is a wild or wild draw four.
 * If a current played card can be matched by color and number from player two's hand, player two will play the card from the larger color group.
 * Will play zero value cards to keep deck color in its favor before playing higher cards of the same color.
 
Cautious
 * Discards non-numeric cards ASAP to prevent giving player one a higher score if player two loses.
 * If a current played card can be matched by color and number from player two's hand, player two will play the card with the higher value.
 * First choice is a wild or wild draw four, second choice is a non-numeric color, third choice is numeric.
 
Dumb
 * Will look blindly for first matching card in hand -- will not try to switch the deck color to its favor. ("Blindly" means player two's hand is shuffled before it marches through the cards, one by one, looking for a match.) 
 * Randomly decides to play a non-numeric card if any are present. Picks a color at random from its hand when discarding a wild or wild draw four.
 
Player one's hand is displayed with cards of the same color grouped together in ascending card value order. First numeric cards are shown, then non-numeric, then wild / wild draw four. 

Program will automatically add cards to either player's hand for draw two and wild draw four cards. 

Program will automatically skip turns as required. 

Program has very defensive user I/O rules -- illegal input is detected immediately, and the user is warned and given another chance to enter a legal value. Input code block will loop until legal input is detected. This applies to all user I/O throughout the game. 

Program has been rigorously tested, and most (if not all) corner cases have been detected and mitigated. Should something go wrong, the code is defensively structured to either warn the user via console that something has gone wrong and continue, or throw a fatal error. All program actions are logged (see below). 

__SAMPLE OUTPUT__

    Welcome to Cero! The rules are essentially the same as Uno.
    
    The first player to discard all cards wins the round,
    
    and the winner takes the value of the loser's cards.
    
    The first player to reach 100 points wins the game.
    
    Good luck!
    
    Please enter your name: Barbarella
    
    Hello, Barbarella! Let's begin.
    
    You will be playing against MASTER CONTROL PROGRAM.
    
    Starting a new game...
    
    The first played card is (Blue) 5.
    
    By toss of a coin, MASTER CONTROL PROGRAM has the first move.
    
    MASTER CONTROL PROGRAM discarded the card (Red) 5.
    
    
          === game status update ===
    
    Barbarella's score is 0, and MASTER CONTROL PROGRAM's score is 0.
    MASTER CONTROL PROGRAM has 6 cards left.
    
          === game status update === 
    
    
    Barbarella's hand:
     1: (Green) 1
     2: (Green) 3
     3: (Green) 7
     4: (Green) Skip
     5: (Red) 0
     6: (Red) Skip
     7: (Yellow) Draw Two
    
    The current played card is (Red) 5.
    
    Which card would you like to discard?
    
    Enter the number to the left of the card, or 0 to draw: 6
    
    You have successfully discarded the card (Red) Skip.
    
    MASTER CONTROL PROGRAM was forbidden from discarding.
    
    
          === game status update ===
    
    Barbarella's score is 0, and MASTER CONTROL PROGRAM's score is 0.
    MASTER CONTROL PROGRAM has 6 cards left.
    
          === game status update === 
    
    
    Barbarella's hand:
     1: (Green) 1
     2: (Green) 3
     3: (Green) 7
     4: (Green) Skip
     5: (Red) 0
     6: (Yellow) Draw Two
    
    The current played card is (Red) Skip.
    
    Which card would you like to discard?
    
    Enter the number to the left of the card, or 0 to draw: 4
    
    You have successfully discarded the card (Green) Skip.
    
    MASTER CONTROL PROGRAM was forbidden from discarding.
    
    
          === game status update ===
    
    Barbarella's score is 0, and MASTER CONTROL PROGRAM's score is 0.
    MASTER CONTROL PROGRAM has 6 cards left.
    
          === game status update === 
    
    
    Barbarella's hand:
     1: (Green) 1
     2: (Green) 3
     3: (Green) 7
     4: (Red) 0
     5: (Yellow) Draw Two
    
    The current played card is (Green) Skip.

__HOW TO RUN__

This program is meant to be run from the command line. Running it as a Java application from within an IDE will throw null pointer exceptions due to the command line-driven user I/O required to play the game.

Here's how to run it from a terminal window.

1. Download and install Java 1.8

 Download the Java 1.8 SDK from [Oracle] (http://www.oracle.com/technetwork/java/javase/downloads/index.html).

 Windows:
  * Open a terminal window and enter the following:
  * set PATH=[C:\path\to\jdk\bin];%PATH%
  * This will set your Java version to 1.8 for the current terminal session. (Will not compile with < 1.8.) 

 Mac:
  * Open a terminal window and type "vim" or "emacs" to see if either is already installed. If not, you can download and install Vim [here] (http://macvim-dev.github.io/macvim/) or Emacs [here] (https://emacsformacosx.com/). There are many guides available online for both. 
  * Use either Vim or Emacs to add the following to your .bash_profile document: 
  * export JAVA_HOME=$(/usr/path/to/java_home). 
  * For more information on what the .bash_profile document is and where to find (or create) it, click [here] (https://natelandau.com/my-mac-osx-bash_profile/).

2. Download this project to your computer.

 Click on the Release tab of this project and download the zip. 
 
 Unzip the directory to your local drive. 

3. Compile and run

 Create a new folder in the Cero directory. Name it "out". 

 Paste these commands into your terminal window:

 Windows:
  * cd C:\path\to\Cero_directory
  * javac src/com/jason/*.java -d C:\path\to\Cero_directory\out
  * java -cp ./out com.jason.Main  

 Mac:
  * cd /Users/yourname/path/to/Cero_directory
  * javac src/com/jason/*.java -d /Users/yourname/path/to/Cero_directory/out
  * java -cp ./out com.jason.Main
  * java -cp ./out com.jason.Main
  
  Optional program arguments: 
  
  Output delay seconds, user name. Skips right to the game, e.g.
  * java -cp ./out com.jason.Main 2 Barb
  
  Strategy log analysis -- lists the number of times the three program strategies have won and lost games. 
  Program argument is case-insensitive. 
  * java -cp ./out com.jason.Main strategylogs
  
  The program will terminate when one of the players has a score of 100 or greater. 

__LOGGING__

Every line sent to System.out is also sent to a log file, along with some additional information such as player two's hand, and the cards player two draws. This aids the reproduction of critical issues. I elected to roll my own logging infrastructure instead of using Log4J for the simple reason that there is not enough demand for the full utility of Log4J's features.

Logs are created in the /log directory and named with today's date, e.g. 2016-01-24.txt. Games that start before midnight and carry over will be logged to the same file. New games started after midnight will be logged to a new file. Each entry line has the following format: HH:MM:SS:SSS [log message]. Log uses military time. 

__UNIT TESTS__

The unit tests can be run locally in any IDE using junit-4.12.jar and hamcrest-core-1.3.jar dependencies. A directory named logs must exist in order for the tests to pass. This directory will be automatically generated by running the program, or it can be manually created. 
