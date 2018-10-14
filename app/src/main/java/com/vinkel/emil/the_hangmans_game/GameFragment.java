package com.vinkel.emil.the_hangmans_game;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class GameFragment extends android.app.Fragment implements View.OnClickListener {

    Galgelogik logik = new Galgelogik();
    private TextView infoomordet;
    private ImageView aniview;
    private TextView infobogstaver;
    private AnimationDrawable hovedAnimation;
    private SharedPreferences prefs;
    private boolean soundact;
    private Bundle restartgamebundle;

    Button a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;
    Button[] buttons = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z};
    int[] idList = {R.id.a, R.id.b, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.h, R.id.i, R.id.j,
            R.id.k, R.id.l, R.id.m, R.id.n, R.id.o, R.id.p, R.id.q, R.id.r, R.id.s, R.id.t, R.id.u, R.id.v,
            R.id.w, R.id.x, R.id.y, R.id.z};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gamefragment = inflater.inflate(R.layout.fragment_game, container, false);
        setRetainInstance(true);
        for (int iterator = 0; iterator < buttons.length; iterator++) {
            buttons[iterator] = gamefragment.findViewById(idList[iterator]);
            buttons[iterator].setOnClickListener(this);

        }
        //Hent lydeffecter til/fra
        prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
        soundact=prefs.getBoolean("pref_sound",true);

        logik.categoriesAndDifficulty((Enum)getArguments().getSerializable("cat"),(Enum)getArguments().getSerializable("dif"),getActivity());
        restartgamebundle=new Bundle();
        //adds category to restart
        restartgamebundle.putSerializable("cat",getArguments().getSerializable("cat"));
        //adds difficulty to restart
        restartgamebundle.putSerializable("dif",getArguments().getSerializable("dif"));
        infoomordet = gamefragment.findViewById(R.id.gameinfo);
        infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
        infobogstaver = gamefragment.findViewById(R.id.wrongletters);
        aniview = gamefragment.findViewById(R.id.animationwindow);
        aniview.setBackgroundResource(R.drawable.animationlisthoved);
        hovedAnimation = (AnimationDrawable) aniview.getBackground();

        return gamefragment;
    }

    @Override
    public void onClick(View view) {


        for (int i = 0; i < buttons.length; i++) {
            if (view instanceof Button) {
                if (view.getId() == idList[i]) {
                    Button tempb = (Button) view;
                    String str = tempb.getText().toString();
                    infoomordet.setText(str);
                    logik.gætBogstav(str);
                    view.setClickable(false);
                    ((Button) view).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 1) {
                        hovedAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 2) {
                        aniview.setBackgroundResource(R.drawable.animationlistkrop);
                        AnimationDrawable kropAnimation = (AnimationDrawable) aniview.getBackground();
                        kropAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 3) {
                        aniview.setBackgroundResource(R.drawable.animationlistvben);
                        AnimationDrawable vBenAnimation = (AnimationDrawable) aniview.getBackground();

                        vBenAnimation.start();

                    }

                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 4) {
                        aniview.setBackgroundResource(R.drawable.animationlisthben);
                        AnimationDrawable hBenAnimation = (AnimationDrawable) aniview.getBackground();

                        hBenAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 5) {
                        aniview.setBackgroundResource(R.drawable.animationlistvarm);
                        AnimationDrawable varmAnimation = (AnimationDrawable) aniview.getBackground();

                        varmAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 6) {
                        aniview.setBackgroundResource(R.drawable.animationlistharm);
                        AnimationDrawable hArmAnimation = (AnimationDrawable) aniview.getBackground();

                        hArmAnimation.start();
                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.erSpilletTabt()) {
                        aniview.setBackgroundResource(R.drawable.animationlisttabt);
                        AnimationDrawable tabtAnimation = (AnimationDrawable) aniview.getBackground();
                        tabtAnimation.start();

                    }

                    opdaterSkærm();

                }
            }
        }

    }


    private void opdaterSkærm() {
        String infoord= "Guess:" + "\n" + logik.getSynligtOrd().substring(0,1).toUpperCase()+logik.getSynligtOrd().substring(1).toLowerCase();
        infoomordet.setText(infoord);
        String infobogstave= "\nYou have " + logik.getAntalForkerteBogstaver() + " wrong guesses:" + logik.getForkertebogstaver();
        infobogstaver.setText(infobogstave);


        if (logik.erSpilletVundet()) {
            String won = "You have won! the word was:\n" + logik.getOrdet().substring(0,1).toUpperCase()+ logik.getOrdet().substring(1).toLowerCase();

            if(soundact) {
                MediaPlayer winningsound1 = MediaPlayer.create(getActivity(), R.raw.cheering);
                MediaPlayer winningsound2 = MediaPlayer.create(getActivity(), R.raw.crowdapplause1);
                winningsound1.start();
                winningsound2.start();
            }
            endOfGameDialog(won);


        }
        if (logik.erSpilletTabt()) {


            String lost = "You have lost, the word was:\n" + logik.getOrdet().substring(0, 1).toUpperCase() + logik.getOrdet().substring(1).toLowerCase();

            if(soundact) {

                MediaPlayer winningsound = MediaPlayer.create(getActivity(), R.raw.boo3);
                winningsound.start();
            }
            endOfGameDialog(lost);


        }
    }

    private void restartGame() {
        logik.nulstil();

        GameFragment gamefragment = new GameFragment();
        android.app.FragmentManager fm=getFragmentManager();
        android.app.FragmentTransaction ft=fm.beginTransaction();
        gamefragment.setArguments(restartgamebundle);
        ft.replace(R.id.mainactfragment, gamefragment)
                .addToBackStack(null)
                .commit();


    }

    public void endOfGameDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(R.string.playagain, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                restartGame();

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getFragmentManager().popBackStackImmediate();
            }
        });
        builder.setTitle(message);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }
}























