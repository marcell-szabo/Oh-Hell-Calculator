package com.example.ohhellcalculator;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance = new Game();
    private List<Player> players = new ArrayList<>();
    private int actualPlayer, noOfRounds;

    private Game(){}
    public static Game getInstance() {
        return instance;
    }
    public void addPlayer(Player newplayer) { players.add( newplayer); }
    public void setNoOfRounds(int noOfRounds) { this.noOfRounds = noOfRounds; }
    public int getPlayernumber() { return players.size(); }
    public String getPlayerName(int i) { return players.get(i).getName(); }
}
