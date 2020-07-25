package com.example.ohhellcalculator;

import java.util.ArrayList;
import java.util.List;

/*
* Player class
* represents the players playing the game
* */
public class Player {
    private String name;
    private List<Integer> guesses = new ArrayList<>(), actuals = new ArrayList<>();
    private int points = 0;
    //scoring attributes
    static int base = 10, guessMultiplier = 2;

    Player(String name) {
        this.name = name;
    }
    //getter methods
    public String getName() { return name; }
    public int getPoints() { return points; }
    public int getGuesses() { return guesses.get(guesses.size() - 1); }

    //setter methods
    public void addGuess(int guess, int round) {
        if(guesses.size() < round)
            guesses.add(guess);
        else
            guesses.set(round - 1, guess);
    }
    public void addActual(int actual, int round) {
        if(actuals.size() < round)
            actuals.add( actual);
        else
            actuals.set(round - 1, actual);
    }
    /*
    * Calculates the point of the Player based on the guess and the actual score
    * */
    public void calculatePoint(int round) {
        int guess = guesses.get(round - 1), actual = actuals.get(round - 1);
        if(guess == actual)
            points += base + guess * guessMultiplier;
        else
            points -= Math.abs(actual - guess) * guessMultiplier;
    }
    /*
    * Recalculates the point when an Undo event is dipatched. Sets point to the value which it had before*/
    public void reCalculatePoint(int round) {
        int pointsNow = points;
        calculatePoint(round);
        points -= 2 * (points - pointsNow);
    }

}
