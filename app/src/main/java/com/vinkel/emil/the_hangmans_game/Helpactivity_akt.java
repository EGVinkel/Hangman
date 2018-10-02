package com.vinkel.emil.the_hangmans_game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Helpactivity_akt extends AppCompatActivity implements View.OnClickListener {
    private TextView helptext;
    private ToggleButton languagetoggle;
    private WebView helpview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpakt);

        helpview= findViewById(R.id.helpview);
        helpview.loadUrl("file:///android_asset/help.html");

        helptext=findViewById(R.id.helpscreentitle);


        languagetoggle=findViewById(R.id.toggleButton);
        languagetoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                helptext.setText(getString(R.string.hjælpeskærm));
                helpview.loadUrl("file:///android_asset/hjælp.html");

            }else if(!isChecked){
                helptext.setText(getString(R.string.helpscreen));
                helpview.loadUrl("file:///android_asset/help.html");




            }
            }
        });




    }

    @Override
    public void onClick(View v) {


    }
}
