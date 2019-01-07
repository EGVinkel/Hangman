package com.vinkel.emil.the_hangmans_game;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class TheGameState {
    public static Galgelogik GameLogic = new Galgelogik();
    public static SharedPreferences prefs;
    public static long timewhendestroyed;
    public static ArrayList<Player> generateHighscore() {
        //Henter navne set, og kobler navne og værdier samme i hashmap
        Set myset = prefs.getStringSet("hscorenavne", new HashSet<>());
        HashMap<String, Integer> scoremap = new HashMap<>();

        for (Object s : myset) {
            scoremap.put(s.toString(), prefs.getInt(s.toString() + "s", 0));
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
           // Player(String name, String word, int time, int score)
            thelist.add(new Player(prefs.getString(s.toString(), "def"),prefs.getString(s.toString()+"o","def"),prefs.getInt(s.toString()+"t",0), sorted.get(s.toString())));
        }
        return thelist;

    }

}
