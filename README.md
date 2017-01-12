# Cero

A Java coding exercise modeled after the card game Uno.

There are two players. The user is player one, and player two is the program. 

See [the Uno Wikipedia page](https://en.wikipedia.org/wiki/Uno_(card_game)) for a full list of game rules.

__HOW TO RUN__

This program is meant to be run from the command line. Running it as a Java application from within an IDE will throw null pointer exceptions due to the command line-driven user I/O required to play the game.

Here's how to run it from a terminal window.

1. Download and install Java 1.8

 Download the Java 1.8 SDK from [Oracle] (http://www.oracle.com/technetwork/java/javase/downloads/index.html).

 Windows:
  * set PATH=[path\to\jdk\bin];%PATH%
  * (must be >= java 1.8)

 Mac:
  * vim .bash_profile
  * export JAVA_HOME=$(/usr/path/to/java_home)

2. Compile and run

 Create a new folder in the Cero directory. Name it "out". 

 Paste these commands into your terminal window:

 Windows:
  * cd C:\path\to\Cero_directory
  * javac src/com/jason/*.java -d C:\path\to\Cero_directory\out
  * java -cp ./out com.jason.Main

 Mac:
  * cd /Users/yourname/path/to/Cero_directory
  * javac src/com/jason/*.java -d /Users/yourname/path/to/out
  * java -cp ./out com.jason.Main

__UNIT TESTS__

The unit tests can be run locally in any IDE using junit-4.12.jar and hamcrest-core-1.3.jar dependencies. 
