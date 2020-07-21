package com.example.ohhellcalculator;

public class Player {
    private String name;
    private int guess, actual, points = 0;
    static int base = 10, guessMultiplier = 2;

    Player(String name) {
        this.name = name;
    }
    public String getName() { return name; }
    public int getPoints() { return points; }

    public void addGuess(int guess) {
        this.guess = guess;
    }
    public void addActual(int actual) {
        this.actual = actual;
    }
    public void calculatePoint() {
        if(guess == actual)
            points += base + guess * guessMultiplier;
        else
            points -= guess * guessMultiplier;
    }
}
