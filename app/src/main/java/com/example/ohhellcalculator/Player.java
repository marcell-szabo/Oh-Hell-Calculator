package com.example.ohhellcalculator;

public class Player {
    String name;
    int guess, actual, points = 0;

    Player(String name) {
        this.name = name;
    }
    public String getName() { return name; }
}
