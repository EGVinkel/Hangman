package com.vinkel.emil.the_hangmans_game;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Hovedaktivitet_akt extends AppCompatActivity implements View.OnClickListener {

    private  MediaPlayer sound;
    private Button mm;
    // TODO private Button highscorebutton;
    private Button settings;

    private Button helpbutton;
    private Fragment firsthovedmenufragment = new HovedmenuFragment();

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedaktivitet_akt);

        if (savedInstanceState == null) {


            getSupportFragmentManager().beginTransaction().add(R.id.mainactfragment, firsthovedmenufragment)
                    .commit();


        }
        startTheme();
        helpbutton = findViewById(R.id.helpbutton);
        helpbutton.setOnClickListener(this);
        settings=findViewById(R.id.settings);
        settings.setOnClickListener(this);

        // TODO highscorebutton= findViewById(R.id.highscorebutton);
        mm=findViewById(R.id.mainmenu);
        mm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == helpbutton) {
            Intent i = new Intent(this, Helpactivity_akt.class);
            startActivity(i);

        }
         if(v==mm){
            getSupportFragmentManager().popBackStackImmediate();
             getSupportFragmentManager().beginTransaction().replace(R.id.mainactfragment, firsthovedmenufragment)
                     .commit();


        }
        if(v==settings){
            if(sound.isPlaying()){
                sound.stop();
            }
            else if(!sound.isPlaying()){

                startTheme();
            }

        }

    }
    public void startTheme(){
        sound= MediaPlayer.create(this,R.raw.maintheme);
        sound.start();
        sound.setLooping(true);
    }
    public void onOrientationchange

}
