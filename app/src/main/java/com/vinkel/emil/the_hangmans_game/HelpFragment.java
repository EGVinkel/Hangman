package com.vinkel.emil.the_hangmans_game;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HelpFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView helptext;
    private ToggleButton languagetoggle;
    private TextView helpview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View helpfrag = inflater.inflate(R.layout.help_frag, container, false);
        Typeface creepster = Typeface.createFromAsset(getActivity().getAssets(), "creepsterregular.ttf");
        helpview = helpfrag.findViewById(R.id.helpview);
        helpview.setTypeface(creepster);

        helptext = helpfrag.findViewById(R.id.helpscreentitle);
        helptext.setText(getString(R.string.helpscreen));
        helpview.setText(R.string.helpinfo);

        languagetoggle = helpfrag.findViewById(R.id.toggleButton);
        languagetoggle.setOnCheckedChangeListener(this);

        languagetoggle.setTypeface(creepster);
        return helpfrag;
    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            helptext.setText(getString(R.string.hjælpeskærm));
            helpview.setText(R.string.hjælpinfo);


        } else if (!isChecked) {
            helptext.setText(getString(R.string.helpscreen));
            helpview.setText(R.string.helpinfo);


        }
    }
    
}
