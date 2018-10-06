package com.vinkel.emil.the_hangmans_game;
import android.support.v4.app.Fragment;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Hovedaktivitet_akt extends AppCompatActivity implements View.OnClickListener {

    private Button helpbutton;
    private Button mm;
    //private Button highscorebutton; TODO

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedaktivitet_akt);

        if(savedInstanceState==null){
            Fragment fragment = new HovedmenuFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainactfragment,fragment)
                    .commit();

        }
        helpbutton= findViewById(R.id.helpbutton);
        helpbutton.setOnClickListener(this);
        mm=findViewById(R.id.mainmenu);
        mm.setOnClickListener(this);

       // highscorebutton= findViewById(R.id.highscorebutton); TODO
    }

    @Override
    public void onClick(View v) {
        if(v ==helpbutton){
            Intent i = new Intent(this, Helpactivity_akt.class);
            startActivity(i);

        }
     /*   else if(v==mm){
            Fragment f = new HovedmenuFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.mainactfragment,f)
                    .addToBackStack(null)
                    .commit();

        }

*/
    }
}
