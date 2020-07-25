package com.example.ohhellcalculator;

import java.util.ArrayList;
import java.util.List;

/*
* Game class. Represents the Controller in the MVC architecture.
* Controls the request coming from the UI, and instructs the model (Player class).
* Singleton design pattern.
* */
public class Game {
    private static Game instance = new Game();
    private List<Player> players = new ArrayList<>();
    private int actualPlayer = 0, noOfRounds, actualRound = 1;
    private boolean addedguesses = false;

    private Game(){}
    public static Game getInstance() {
        return instance;
    }
    /*setter and getter functions*/
    public void addPlayer(Player newplayer) { players.add( newplayer); }
    public void setNoOfRounds(int noOfRounds) { this.noOfRounds = noOfRounds; }
    public int getNoOfRounds() { return noOfRounds; }
    public int getPlayernumber() { return players.size(); }
    public String getPlayerName(int i) { return players.get(i).getName(); }
    public int getActualRound() { return actualRound; }
    public int getActualPlayer() { return actualPlayer; }
    public Player getPlayer(int i) { return players.get(i); }

    /*
    * Returns the real round number. The difference from actualround is that rounds go from '1' to 'n' and then to '1'.
    * The function gives the real one which follows this characteristic.
    * @return real round number
    * */
    public int getRealActualRound() {
        if(noOfRounds >= actualRound)
            return actualRound;
        else
            return noOfRounds - (actualRound - noOfRounds);
    }

    /*
    * Gives the result of guessing, meaning: the function sums the guesses of all the players and then decides,
    * whether it exceeds or equals or subseeds the rounds number.
    * @return result of guessing
    * */
    public GuessesNo resultOfGuessing() {
        int sum = 0;
        for (Player i: players)
            sum += i.getGuess();
        if(sum > getRealActualRound())
            return GuessesNo.FIGHT;
        else if (sum == getRealActualRound())
            return GuessesNo.EQUAL;
        else
            return GuessesNo.SCATTER;
    }

    /*
    * Manages the round, if a guessbutton is pressed on the UI, the represented guess or actual comes
    * and it is registered.
    * @return result of Round
    * */
    public RoundResult manageRound(int guessOrActual) {
        if (!addedguesses) {
            registerGuess(guessOrActual);
            if(addedguesses)
                return RoundResult.LASTADDEDGUESS;
            return RoundResult.ADDGUESS;
        } else {
            registerActual(guessOrActual);
            if(!addedguesses)
                // return ENDRESULT if a new round has to come because the all the players have been registered
                return RoundResult.ENDROUND;
            return RoundResult.ADDACTUAL;
        }
    }

    /*
    * Registers a guess. Adds the actualplayer the incoming guess parameter.
    * @parameter guess - guess to be registered
    * */
    private void registerGuess(int guess) {
        players.get(actualPlayer).addGuess(guess);
        //if the actualplayer is the last one, then the next actualplayer becomes the first one,
        // and the addedguesses flag becomes true
        if(actualPlayer == (players.size() - 1)) {
            actualPlayer = 0;
            addedguesses = true;
        } else
            actualPlayer++;
    }

    /*
    * Registers actual score. Adds these actuals to the actualplayer and then calculates the points accordingly.
    * @parameter actual - actual score of the player
    * */
    private void registerActual(int actual) {
        players.get(actualPlayer).addActual(actual);
        players.get(actualPlayer).calculatePoint();
        if(actualPlayer == players.size() - 1) {
            actualPlayer = 0;
            addedguesses = false;
            actualRound++;
        }
        else
            actualPlayer++;
    }

    /*
    * Undoes the last actions.
    * */
    public UndoResult undo() {
        //if the addedguesses flag is false, so all the guesses have not been added yet the function returns
        //FIRST or ENDROUND
        if(!addedguesses) {
            if(actualPlayer != 0) {
                actualPlayer--;
                return UndoResult.FIRST;
            }
            //if we want to change the previos actual score of the last player
            else if (actualPlayer == 0 && actualRound != 1) {
                actualPlayer = players.size() - 1;
                actualRound--;
                addedguesses = true;
                players.get(actualPlayer).reCalculatePoint();
                return UndoResult.CHANGE_ENDROUND;
            }
        }
        //if guesses have been added, than we would want to change the actuals.
        //the function could return FIRST or SECOND
        else {
            //change last players guess
            if(actualPlayer == 0) {
                actualPlayer = players.size() - 1;
                addedguesses = false;
                return UndoResult.FIRST;
            } else {
                actualPlayer--;
                players.get(actualPlayer).reCalculatePoint();
                return UndoResult.SECOND;
            }
        }
        return null;
    }
}
