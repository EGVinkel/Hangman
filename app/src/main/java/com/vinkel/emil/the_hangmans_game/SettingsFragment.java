package com.vinkel.emil.the_hangmans_game;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.HashSet;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        Preference music = findPreference("pref_music");
        Preference sound = findPreference("pref_sound");
        sound.setOnPreferenceClickListener(this);
        music.setOnPreferenceClickListener(this);
        Preference delete = findPreference(getString(R.string.deleteDR));
        delete.setOnPreferenceClickListener(this);

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        SharedPreferences sharedp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (preference == findPreference(getString(R.string.deleteDR))) {
            sharedp.edit().putStringSet("orddr", new HashSet<String>()).commit();

        }



        if (preference == findPreference("pref_music")) {
            if (sharedp.getBoolean("pref_music", true)) {
                ((Hovedaktivitet_akt) getActivity()).startTheme();
            }
            if (!sharedp.getBoolean("pref_music", true)) {
                ((Hovedaktivitet_akt) getActivity()).stopTheme();
            }

        }
        if (preference == findPreference("pref_sound")) {


            if (sharedp.getBoolean("pref_sound", true)) {
                sharedp.edit().putBoolean("pref_sound", true).apply();

            }
            if (!sharedp.getBoolean("pref_sound", true)) {
                sharedp.edit().putBoolean("pref_sound", false).apply();


            }

        }


        return true;
    }


}

