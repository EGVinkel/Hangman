package com.vinkel.emil.the_hangmans_game;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.vinkel.emil.the_hangmans_game.com.vinkel.emil.the_hangmans_game.playerdata.Sharedp;

import java.util.HashSet;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference deletedr;
    private Preference deletehighscore;
    private Preference music;
    private Preference sound;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        music = findPreference("pref_music");
        sound = findPreference("pref_sound");
        sound.setOnPreferenceClickListener(this);
        music.setOnPreferenceClickListener(this);
        deletedr = findPreference("deletedr");
        deletedr.setOnPreferenceClickListener(this);
        deletehighscore = findPreference("deletehs");
        deletehighscore.setOnPreferenceClickListener(this);

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference == deletedr) {
            Sharedp.prefs.edit().putStringSet("orddr", new HashSet<>()).commit();
        }

        if (preference == deletehighscore) {
            Sharedp.prefs.edit().putStringSet("hscorenavne", new HashSet<>()).commit();
        }

        if (preference == music) {
            if (Sharedp.prefs.getBoolean("pref_music", true)) {
                ((Hovedaktivitet_akt) getActivity()).startTheme();
            }
            if (!Sharedp.prefs.getBoolean("pref_music", true)) {
                ((Hovedaktivitet_akt) getActivity()).stopTheme();
            }

        }
        if (preference == sound) {


            if (Sharedp.prefs.getBoolean("pref_sound", true)) {
                Sharedp.prefs.edit().putBoolean("pref_sound", true).apply();

            }
            if (!Sharedp.prefs.getBoolean("pref_sound", true)) {
                Sharedp.prefs.edit().putBoolean("pref_sound", false).apply();


            }

        }


        return true;
    }


}

