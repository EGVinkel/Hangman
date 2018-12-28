package com.vinkel.emil.the_hangmans_game;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Galgelogik {
    /**
     * AHT afprøvning er muligeOrd synlig på pakkeniveau
     */
    private ArrayList<String> muligeOrd = new ArrayList<String>();
    private String ordet;
    private ArrayList<String> brugteBogstaver = new ArrayList<String>();
    private ArrayList<String> forkerteBogstaver = new ArrayList<String>();
    private String synligtOrd;
    private int antalForkerteBogstaver;
    private boolean sidsteBogstavVarKorrekt;
    private boolean spilletErVundet;
    private boolean spilletErTabt;
    private boolean gameinprogress=false;
    private int minlength = 3;
    private int maxlength = 20;

    public boolean isGameinprogress() {
        return gameinprogress;
    }

    public void setGameinprogress(boolean gameinprogress) {
        this.gameinprogress = gameinprogress;
    }







    public ArrayList<String> getBrugteBogstaver() {return brugteBogstaver;}

    public String getSynligtOrd() {
        return synligtOrd;
    }

    public String getOrdet() {
        return ordet;
    }

    public int getAntalForkerteBogstaver() {
        return antalForkerteBogstaver;
    }

    public boolean erSidsteBogstavKorrekt() {
        return sidsteBogstavVarKorrekt;
    }

    public boolean erSpilletVundet() {
        return spilletErVundet;
    }

    public boolean erSpilletTabt() {
        return spilletErTabt;
    }

    public ArrayList<String> getForkertebogstaver() {
        return forkerteBogstaver;
    }

    public Galgelogik() {

    }

    //tilføjer mulige ord til arrayet fra text fil ud fra en valgt kategori. @author emil_
    public void categoriesAndDifficulty(Enum cat, Enum difficulty,Context con) {

        try {
            if (difficulty == MyEnum.EASY) {
                maxlength = 6;
            }
            if (difficulty == MyEnum.NORMAL) {
                minlength = 6;
                maxlength = 12;
            }
            if (difficulty == MyEnum.NORMAL) {
                minlength = 10;
                maxlength = 30;
            }
            if (!MyEnum.WordsDR.equals(cat)) {
                InputStream is = null;
                if (MyEnum.DEFAULT.equals(cat)) {
                    is = (con.getAssets().open("def.txt"));

                } else if (MyEnum.STARWARS.equals(cat)) {
                    is = (con.getAssets().open("starwars.txt"));

                } else if (MyEnum.HARRYPOTTER.equals(cat)) {
                    is = (con.getAssets().open("harrypotter.txt"));

                } else if (MyEnum.FOOD.equals(cat)) {
                    is = (con.getAssets().open("food.txt"));

                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();

                while (line != null) {
                    String[] wordsLine = line.split(",");
                    for (String word : wordsLine) {
                        muligeOrd.add(word);
                    }
                    line = reader.readLine();
                }

            }


            if (MyEnum.WordsDR.equals(cat)) {
                Set<String> myset = TheGameState.prefs.getStringSet("orddr", new HashSet<String>());
                for (String word : myset) {
                    muligeOrd.add(word);
                }

            }


            setWords();

            nulstil();


        } catch (Exception e) {
            throw new RuntimeException(
                    "This should never happen, I know this file exists", e);
        }
    }

    public void myWord(String myword) {
        muligeOrd.clear();
        muligeOrd.add(myword);
        nulstil();
    }


    private void setWords() {
        ArrayList<String> temp = new ArrayList<String>();
        for (String word : muligeOrd) {
            if (minlength <= word.length() && word.length() <= maxlength) {
                temp.add(word);

            }
        }
        muligeOrd.retainAll(temp);

        if (muligeOrd.isEmpty()) {
            muligeOrd.add("ingenord");
        }

    }

    public void nulstil() {
        brugteBogstaver.clear();
        forkerteBogstaver.clear();
        antalForkerteBogstaver = 0;
        spilletErVundet = false;
        spilletErTabt = false;
        ordet = muligeOrd.get(new Random().nextInt(muligeOrd.size()));
        opdaterSynligtOrd();
    }


    private void opdaterSynligtOrd() {
        synligtOrd = "";
        spilletErVundet = true;
        brugteBogstaver.add("-");


        for (int n = 0; n < ordet.length(); n++) {
            String bogstav = ordet.substring(n, n + 1);

            if (brugteBogstaver.contains(bogstav)) {
                synligtOrd = synligtOrd + bogstav;
            } else {
                synligtOrd = synligtOrd + " _ ";
                spilletErVundet = false;
            }
        }
        brugteBogstaver.remove("-");
    }

    public void gætBogstav(String bogstav) {
        if (ordet == null)
            return;

        if (bogstav.length() != 1) return;

        if (brugteBogstaver.contains(bogstav)) return;
        if (spilletErVundet || spilletErTabt) return;

        brugteBogstaver.add(bogstav);

        if (ordet.contains(bogstav)) {
            sidsteBogstavVarKorrekt = true;

        } else {
            // Vi gættede på et bogstav der ikke var i ordet.
            sidsteBogstavVarKorrekt = false;
            antalForkerteBogstaver = antalForkerteBogstaver + 1;
            forkerteBogstaver.add(bogstav);
            if (antalForkerteBogstaver > 6) {
                spilletErTabt = true;
            }
        }
        opdaterSynligtOrd();
    }

    public void logStatus() {
        System.out.println("---------- ");
        System.out.println("- ordet (skult) = " + ordet);
        System.out.println("- synligtOrd = " + synligtOrd);
        System.out.println("- forkerteBogstaver = " + antalForkerteBogstaver);
        System.out.println("- brugeBogstaver = " + brugteBogstaver);
        if (spilletErTabt) System.out.println("- SPILLET ER TABT");
        if (spilletErVundet) System.out.println("- SPILLET ER VUNDET");
        System.out.println("---------- ");
    }


    public static String hentUrl(String url) throws IOException {
        System.out.println("Henter data fra " + url);
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }


    public void hentOrdFraDr() throws Exception {
        String data = hentUrl("https://dr.dk");
        //System.out.println("data = " + data);

        data = data.substring(data.indexOf("<body")). // fjern headere
                replaceAll("<.+?>", " ").toLowerCase(). // fjern tags
                replaceAll("&#198;", "æ"). // erstat HTML-tegn
                replaceAll("&#230;", "æ"). // erstat HTML-tegn
                replaceAll("&#216;", "ø"). // erstat HTML-tegn
                replaceAll("&#248;", "ø"). // erstat HTML-tegn
                replaceAll("&oslash;", "ø"). // erstat HTML-tegn
                replaceAll("&#229;", "å"). // erstat HTML-tegn
                replaceAll("[^a-zæøå]", " "). // fjern tegn der ikke er bogstaver
                replaceAll("this", " "). //Fjerner nogle engelske ord der bruges ifl til html.
                replaceAll("adspaces", " ").
                replaceAll("push", " ").
                replaceAll(" [a-zæøå] ", " "). // fjern 1-bogstavsord
                replaceAll(" [a-zæøå][a-zæøå] ", " ").trim(); // fjern 2-bogstavsord

        System.out.println("data = " + data);
        System.out.println("data = " + Arrays.asList(data.split("\\s+")));
        muligeOrd.clear();
        muligeOrd.addAll(new HashSet<>(Arrays.asList(data.split(" "))));
        ArrayList temp = new ArrayList();
        for (String word : muligeOrd) {
            if (!(word.contains("å") || word.contains("ø") || word.contains("æ")) && word.length() > 3) {
                temp.add(word);

            }
        }
        muligeOrd = temp;

        Collections.sort(muligeOrd);
        Set mySet = new HashSet(muligeOrd);
        TheGameState.prefs.edit().putStringSet("orddr", mySet).commit();
        nulstil();

    }
}
