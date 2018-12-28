package com.vinkel.emil.the_hangmans_game;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.HashSet;

public class SettingsFragment extends PreferenceFragmentCompat implements android.support.v7.preference.Preference.OnPreferenceClickListener {
    private android.support.v7.preference.Preference deletedr;
    private android.support.v7.preference.Preference  deletehighscore;
    private android.support.v7.preference.Preference  music;
    private android.support.v7.preference.Preference  sound;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_frag);
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
    public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {

        if (preference == deletedr) {
            TheGameState.prefs.edit().putStringSet("orddr", new HashSet<>()).commit();
        }

        if (preference == deletehighscore) {
            TheGameState.prefs.edit().putStringSet("hscorenavne", new HashSet<>()).commit();
        }

        if (preference == music) {
            if (TheGameState.prefs.getBoolean("pref_music", true)) {
                ((MainActivity) getActivity()).startTheme();
            }
            if (!TheGameState.prefs.getBoolean("pref_music", true)) {
                ((MainActivity) getActivity()).stopTheme();
            }

        }
        if (preference == sound) {


            if (TheGameState.prefs.getBoolean("pref_sound", true)) {
                TheGameState.prefs.edit().putBoolean("pref_sound", true).apply();

            }
            if (!TheGameState.prefs.getBoolean("pref_sound", true)) {
                TheGameState.prefs.edit().putBoolean("pref_sound", false).apply();


            }

        }


        return true;
    }
}

