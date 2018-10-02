package com.vinkel.emil.the_hangmans_game;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Hovedmenu_akt extends AppCompatActivity implements View.OnClickListener {
    private Button playbutton;
    private Button helpbutton;
    private Button highscorebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedmenu_akt);
        playbutton = findViewById(R.id.playbutton);
        helpbutton= findViewById(R.id.helpbutton);
        helpbutton.setOnClickListener(this);
        highscorebutton= findViewById(R.id.highscorebutton);



    }

    @Override
    public void onClick(View v) {
        if(v ==helpbutton){
            Intent i = new Intent(this, Helpactivity_akt.class);
            startActivity(i);

        }

    }
}
