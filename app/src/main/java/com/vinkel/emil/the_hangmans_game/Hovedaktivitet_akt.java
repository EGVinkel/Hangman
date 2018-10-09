package com.vinkel.emil.the_hangmans_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class Hovedaktivitet_akt extends AppCompatActivity implements View.OnClickListener {
    private static final String COMMON_TAG = "OrintationChange";

    // TODO private Button mm;
    // TODO private Button highscorebutton;
    private Button helpbutton;
    private Fragment firsthovedmenufragment = new HovedmenuFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedaktivitet_akt);

        if (savedInstanceState == null) {


            getSupportFragmentManager().beginTransaction().add(R.id.mainactfragment, firsthovedmenufragment)
                    .commit();


        }
        helpbutton = findViewById(R.id.helpbutton);
        helpbutton.setOnClickListener(this);


        /* TODO highscorebutton= findViewById(R.id.highscorebutton);
        mm=findViewById(R.id.mainmenu);
        mm.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        if (v == helpbutton) {
            Intent i = new Intent(this, Helpactivity_akt.class);
            startActivity(i);

        }
      /* TODO  if(v==mm&&!firsthovedmenufragment.isVisible()){

            getSupportFragmentManager().beginTransaction().replace(R.id.mainactfragment,firsthovedmenufragment).commit();


        }*/


    }


}
