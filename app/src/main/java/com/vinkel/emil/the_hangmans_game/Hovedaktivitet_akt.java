package com.vinkel.emil.the_hangmans_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Hovedaktivitet_akt extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer sound;
    private Button mm;
    private Button highscorebutton;
    private Button settings;
    private Button helpbutton;
    private int count;
    private android.app.FragmentManager fm = getFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedaktivitet_akt);
        if (findViewById(R.id.mainactfragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            android.app.FragmentTransaction ft = fm.beginTransaction();
            HovedmenuFragment firsthovedmenufragment = new HovedmenuFragment();
            ft.add(R.id.mainactfragment, firsthovedmenufragment, "Hovedmenufrag").commit();
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            helpbutton = findViewById(R.id.helpbutton);
            helpbutton.setOnClickListener(this);
            settings = findViewById(R.id.settings);
            settings.setOnClickListener(this);

            highscorebutton = findViewById(R.id.highscorebutton);
            highscorebutton.setOnClickListener(this);
            mm = findViewById(R.id.mainmenu);
            mm.setOnClickListener(this);
            count = 0;
            if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_music", true)) {
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


        }
        if (v == mm) {
            fm.popBackStackImmediate();

        }

        if (v == settings) {

            if (fm.findFragmentByTag("settings") == null) {
                android.app.Fragment fragset = new SettingsFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainactfragment, fragset, "settings")
                        .addToBackStack(null)
                        .commit();
                return;
            }
            return;
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

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (sound != null && sound.isPlaying()) {
                        sound.stop();
                    }
                    finish();
                }
            });

            builder.setNegativeButton(R.string.dontexit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
            builder.setTitle(R.string.exittxt);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);

        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        }

    }
}