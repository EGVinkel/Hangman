package com.vinkel.emil.the_hangmans_game;

public class Player {
    private String name;
    private String word;
    private int score;



    private int time;


    public Player(String name, String word, int time, int score) {
        this.name = name;
        this.score = score;
        this.word= word;
        this.time= time;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getWord() {
        return word;
    }

    public int getHowfast() {
        return time;
    }


}