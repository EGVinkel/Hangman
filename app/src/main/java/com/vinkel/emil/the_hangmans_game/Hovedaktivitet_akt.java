package com.vinkel.emil.the_hangmans_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Hovedaktivitet_akt extends AppCompatActivity implements View.OnClickListener {

    private  MediaPlayer sound;
    private Button mm;
    // TODO private Button highscorebutton;
    private Button settings;
    private Button helpbutton;
    private int count;
    private HovedmenuFragment firsthovedmenufragment = new HovedmenuFragment();
    private android.app.FragmentManager fm=getFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedaktivitet_akt);

        if (savedInstanceState == null) {
            android.app.FragmentTransaction ft=fm.beginTransaction();

           ft.replace(R.id.mainactfragment, firsthovedmenufragment,"Hovedmenufrag")
                   .addToBackStack(null)
                    .commit();


        }
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false);
        helpbutton = findViewById(R.id.helpbutton);
        helpbutton.setOnClickListener(this);
        settings=findViewById(R.id.settings);
        settings.setOnClickListener(this);
        // TODO highscorebutton= findViewById(R.id.highscorebutton);
        mm=findViewById(R.id.mainmenu);
        mm.setOnClickListener(this);
        count=0;
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_music",true)) {
            startTheme();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == helpbutton) {
            Intent i = new Intent(this, Helpactivity_akt.class);
            startActivity(i);

        }
        if (v == mm) {
            fm.popBackStackImmediate();

        }

            if (v == settings) {
            if (count == 0) {
                    count++;

                    android.app.Fragment setfrag = new SettingsFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mainactfragment, setfrag, "settings")
                            .addToBackStack(null)
                            .commit();
                } else {

                    android.app.Fragment fragset = fm.findFragmentByTag("settings");
                    if (fragset.isVisible() && fragset != null) {
                        android.app.FragmentTransaction ft = fm.beginTransaction();
                        ft.detach(fragset);
                        ft.commit();
                    } else {
                        android.app.FragmentTransaction ft = fm.beginTransaction();
                        ft.attach(fragset);
                        ft.commit();
                    }

                }
            }

    }
    public void startTheme(){

        sound= MediaPlayer.create(this,R.raw.maintheme);
        sound.start();
        sound.setLooping(true);

    }
    public void stopTheme(){
            sound.stop();
        }


    @Override
    public void onBackPressed() {
        count=0;
        if(getFragmentManager().getBackStackEntryCount()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(sound.isPlaying()){
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
        if(getFragmentManager().getBackStackEntryCount()>0){
            super.onBackPressed();
        }

    }
}