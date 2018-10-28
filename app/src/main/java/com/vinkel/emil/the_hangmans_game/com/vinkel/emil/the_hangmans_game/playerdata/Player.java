package com.vinkel.emil.the_hangmans_game.com.vinkel.emil.the_hangmans_game.playerdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class Player {
    private String name;
    private int score;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public static ArrayList<Player> getPlayers() {
        //Henter navne set, og kobler navne og værdier samme i hashmap
        Set myset = Sharedp.prefs.getStringSet("hscorenavne", new HashSet<>());
        HashMap<String, Integer> scoremap = new HashMap<>();
        for (Object s : myset) {
            scoremap.put(s.toString(), Sharedp.prefs.getInt(s.toString(), 0));
        }
        //Sortere Hashmap, så højeste score er øverst.

        Map<String, Integer> sorted = scoremap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));


        //Laver et nyt Player array og tilføjer players ud fra den sortede highscore
        ArrayList<Player> thelist = new ArrayList<>();
        for (Object s : sorted.keySet()) {
            thelist.add(new Player(s.toString(), sorted.get(s.toString())));
        }
        return thelist;

    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


}