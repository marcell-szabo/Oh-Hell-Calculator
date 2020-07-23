package com.example.ohhellcalculator;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance = new Game();
    private List<Player> players = new ArrayList<>();
    private int actualPlayer = 0, noOfRounds, actualRound = 1;
    private boolean addedguesses = false;

    private Game(){}
    public static Game getInstance() {
        return instance;
    }
    public void addPlayer(Player newplayer) { players.add( newplayer); }
    public void setNoOfRounds(int noOfRounds) { this.noOfRounds = noOfRounds; }
    public int getNoOfRounds() { return noOfRounds; }
    public int getPlayernumber() { return players.size(); }
    public String getPlayerName(int i) { return players.get(i).getName(); }
    public int getActualRound() { return actualRound; }
    public int getActualPlayer() { return actualPlayer; }
    public Player getPlayer(int i) { return players.get(i); }

    public RoundResult manageRound(int guessOrActual) {
        if (!addedguesses) {
            registerGuess(guessOrActual);
            return RoundResult.ADDGUESS;
        } else {
            registerActual(guessOrActual);
            if(!addedguesses)
                return RoundResult.ENDROUND;
            else
                return RoundResult.ADDACTUAL;
        }
    }
    private void registerGuess(int guess) {
        players.get(actualPlayer).addGuess(guess);
        if(actualPlayer == (players.size() - 1)) {
            actualPlayer = 0;
            addedguesses = true;
        }
        else
            actualPlayer++;
    }
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

    public UndoResult undo() {
        if(!addedguesses) {
            if(actualPlayer != 0) {
                actualPlayer--;
                return UndoResult.FIRST;
            }
            else if (actualPlayer == 0 && actualRound != 1) {
                actualPlayer = players.size() - 1;
                actualRound--;
                addedguesses = true;
                players.get(actualPlayer).reCalculatePoint();
                return UndoResult.CHANGE_ENDROUND;
            }
        } else {
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
