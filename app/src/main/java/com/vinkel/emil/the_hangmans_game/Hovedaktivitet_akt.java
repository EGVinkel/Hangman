package com.vinkel.emil.the_hangmans_game;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vinkel.emil.the_hangmans_game.com.vinkel.emil.the_hangmans_game.playerdata.Sharedp;


public class Hovedaktivitet_akt extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer sound;
    private Button mm;
    private Button highscorebutton;
    private Button settings;
    private Button helpbutton;
    private android.app.FragmentManager fm = getFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedaktivitet_akt);
        if (findViewById(R.id.mainactfragment) != null) {
            if (savedInstanceState != null) {
                return;
            }

            fm.beginTransaction().add(R.id.mainactfragment, new HovedmenuFragment(), "Hovedmenufrag").commit();
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            helpbutton = findViewById(R.id.helpbutton);
            helpbutton.setOnClickListener(this);
            settings = findViewById(R.id.settings);
            settings.setOnClickListener(this);


            highscorebutton = findViewById(R.id.highscorebutton);
            highscorebutton.setOnClickListener(this);
            mm = findViewById(R.id.mainmenu);
            mm.setOnClickListener(this);
            if (Sharedp.prefs.getBoolean("pref_music", true)) {
                startTheme();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == helpbutton) {
            Intent i = new Intent(this, Helpactivity_akt.class);
            startActivity(i);

        }

        if (v == highscorebutton) {
            if (fm.findFragmentByTag("highscore") == null) {
                fm.beginTransaction().replace(R.id.mainactfragment, new HighscoreFragment(), "highscore")
                        .addToBackStack(null)
                        .commit();
            }
            if (fm.findFragmentByTag("highscore") != null) {
                fm.beginTransaction().replace(R.id.mainactfragment, fm.findFragmentByTag("highscore"), "highscore").addToBackStack(null).commit();
            }

        }
        if (v == mm) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }

            fm.beginTransaction().replace(R.id.mainactfragment, fm.findFragmentByTag("Hovedmenufrag"), "Hovedmenufrag")
                    .commit();

        }

        if (v == settings) {
            if (fm.findFragmentByTag("settings") == null) {
                fm.beginTransaction().replace(R.id.mainactfragment, new SettingsFragment(), "settings")
                        .addToBackStack(null)
                        .commit();
            }
            if (fm.findFragmentByTag("settings") != null) {
                fm.beginTransaction().replace(R.id.mainactfragment, fm.findFragmentByTag("settings"), "settings").addToBackStack(null).commit();
            }

        }


    }

    public void startTheme() {

        sound = MediaPlayer.create(this, R.raw.maintheme);
        sound.start();
        sound.setLooping(true);

    }

    public void stopTheme() {
        sound.stop();
    }


    @Override
    public void onBackPressed() {
        int count = fm.getBackStackEntryCount();

        if (count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton(R.string.exit, (dialog, id) -> {
                    if (sound != null && sound.isPlaying()) {
                        sound.stop();
                    }
                super.onBackPressed();
            });

            builder.setNegativeButton(R.string.dontexit, (dialog, id) -> {
                    return;
            });
            builder.setTitle(R.string.exittxt);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);

        }
        fm.popBackStackImmediate();


    }


}