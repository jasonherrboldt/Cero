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
  * Open a terminal window and enter the following:
  * set PATH=[C:\path\to\jdk\bin];%PATH%
  * (must be >= java 1.8)

 Mac:
  * Open a terminal window and type "vim" or "emacs" to see if either is already installed. If not, you can download and install Vim [here] (http://macvim-dev.github.io/macvim/) or Emacs [here] (https://emacsformacosx.com/). There are many guides available online for both. 
  * Use either Vim or Emacs to add the following to your .bash_profile document: export JAVA_HOME=$(/usr/path/to/java_home). For more information on what the .bash_profile document is and where to find (or create) it, click [here] (https://natelandau.com/my-mac-osx-bash_profile/).

2. Download and install Git

 Use [this guide] (https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

3. Download this project to your computer.

 Click the green Clone or Download button.
 
 Copy the URL.
 
 Open a terminal window and navigate to the desired parent directory.
 
 Enter "clone [URL]" in the terminal window.

4. Compile and run

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

__UNIT TESTS__

The unit tests can be run locally in any IDE using junit-4.12.jar and hamcrest-core-1.3.jar dependencies. 
