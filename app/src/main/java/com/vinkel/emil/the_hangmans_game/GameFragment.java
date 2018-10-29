package com.vinkel.emil.the_hangmans_game;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vinkel.emil.the_hangmans_game.com.vinkel.emil.the_hangmans_game.playerdata.Sharedp;

import java.util.HashSet;
import java.util.Set;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.vinkel.emil.the_hangmans_game.R.color;
import static com.vinkel.emil.the_hangmans_game.R.drawable;
import static com.vinkel.emil.the_hangmans_game.R.id;
import static com.vinkel.emil.the_hangmans_game.R.layout;
import static com.vinkel.emil.the_hangmans_game.R.raw;
import static com.vinkel.emil.the_hangmans_game.R.string;


public class GameFragment extends android.app.Fragment implements View.OnClickListener {

    Galgelogik logik;
    private TextView infoomordet;
    private ImageView aniview;
    private TextView infobogstaver;
    private AnimationDrawable hovedAnimation;
    private boolean soundact;
    private Bundle restartgamebundle;
    private Chronometer cm;
    private int tid;
    private int score = 200;
    private String navn;
    private boolean vent;
    int[] idList = {id.a, id.b, id.c, id.d, id.e, id.f, id.g, id.h, id.i, id.j,
            id.k, id.l, id.m, id.n, id.o, id.p, id.q, id.r, id.s, id.t, id.u, id.v,
            id.w, id.x, id.y, id.z};

    Button a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;
    Button[] buttons = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z};
    private KonfettiView konfettiview;

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View gamefragment = inflater.inflate(layout.fragment_game, container, false);
        if (logik == null) {
            logik = new Galgelogik(getActivity());
        }


        Typeface creepster = Typeface.createFromAsset(getActivity().getAssets(), "creepsterregular.ttf");
        for (int iterator = 0; iterator < buttons.length; iterator++) {
            buttons[iterator] = gamefragment.findViewById(idList[iterator]);
            buttons[iterator].setOnClickListener(this);
        }
        for (Button b : buttons) {
            b.setTypeface(creepster);
        }
        konfettiview = getActivity().findViewById(id.viewKonfetti);
        cm = gamefragment.findViewById(id.count);

        final ProgressBar minBar = gamefragment.findViewById(id.progressBar);
        minBar.setVisibility(View.GONE);

        infoomordet = gamefragment.findViewById(id.gameinfo);

        if (getArguments().getSerializable("cat") == MyEnum.MyWord) {
            logik.myWord(getArguments().getString("myword"));
            infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
        }


        if (getArguments().getSerializable("cat") == MyEnum.WordsDR) {
            //Kontroller i prefs manager om ordene allerede findes, og henter derfra hvis de gør.
            if (!Sharedp.prefs.getStringSet("orddr", new HashSet<String>()).isEmpty()) {
                logik.categoriesAndDifficulty((Enum) getArguments().getSerializable("cat"), (Enum) getArguments().getSerializable("dif"));
                infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
            } else if (Sharedp.prefs.getStringSet("orddr", new HashSet<String>()).isEmpty()) {


                // Async skelet fra https://gist.github.com/sheharyarn/5f66a854ce43e0c90521
                new AsyncTask<Void, Void, Void>() {
                    protected void onPreExecute() {
                        infoomordet.setText("Loading\nWords\nPlease Wait");
                        minBar.setVisibility(View.VISIBLE);

                    }

                    protected Void doInBackground(Void... unused) {
                        try {
                            logik.hentOrdFraDr();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return null;
                    }

                    protected void onPostExecute(Void unused) {
                        minBar.setVisibility(View.GONE);
                        infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
                    }
                }.execute();

            }
        }

        if (getArguments().getSerializable("cat") != MyEnum.WordsDR && getArguments().getSerializable("cat") != MyEnum.MyWord) {
            logik.categoriesAndDifficulty((Enum) getArguments().getSerializable("cat"), (Enum) getArguments().getSerializable("dif"));
            infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
        }


        tid = 0;
        cm.setBase(SystemClock.elapsedRealtime());
        cm.start();
        cm.setTypeface(creepster);
        cm.setFormat("%s");
        cm.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override

            public void onChronometerTick(Chronometer chronometer) {
                tid++;

            }

        });


        //Hent lydeffecter til/fra
        soundact = Sharedp.prefs.getBoolean("pref_sound", true);
        restartgamebundle = new Bundle();
        //adds category to restart
        restartgamebundle.putSerializable("cat", getArguments().getSerializable("cat"));
        //adds difficulty to restart
        restartgamebundle.putSerializable("dif", getArguments().getSerializable("dif"));

        infobogstaver = gamefragment.findViewById(id.wrongletters);
        aniview = gamefragment.findViewById(id.animationwindow);
        aniview.setBackgroundResource(drawable.animationlisthoved);
        hovedAnimation = (AnimationDrawable) aniview.getBackground();

        return gamefragment;
    }

    @Override
    public void onClick(View view) {
        if (logik.getSynligtOrd() == null) {
            return;
        }

        for (int i = 0; i < buttons.length; i++) {
            if (view instanceof Button) {
                if (view.getId() == idList[i]) {
                    Button tempb = (Button) view;
                    String str = tempb.getText().toString();
                    infoomordet.setText(str);
                    logik.gætBogstav(str);
                    view.setClickable(false);
                    ((Button) view).setTextColor(getResources().getColor(color.colorPrimaryDark));
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 1) {
                        hovedAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 2) {
                        aniview.setBackgroundResource(drawable.animationlistkrop);
                        AnimationDrawable kropAnimation = (AnimationDrawable) aniview.getBackground();
                        kropAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 3) {
                        aniview.setBackgroundResource(drawable.animationlistvben);
                        AnimationDrawable vBenAnimation = (AnimationDrawable) aniview.getBackground();

                        vBenAnimation.start();

                    }

                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 4) {
                        aniview.setBackgroundResource(drawable.animationlisthben);
                        AnimationDrawable hBenAnimation = (AnimationDrawable) aniview.getBackground();

                        hBenAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 5) {
                        aniview.setBackgroundResource(drawable.animationlistvarm);
                        AnimationDrawable varmAnimation = (AnimationDrawable) aniview.getBackground();

                        varmAnimation.start();

                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver() == 6) {
                        aniview.setBackgroundResource(drawable.animationlistharm);
                        AnimationDrawable hArmAnimation = (AnimationDrawable) aniview.getBackground();

                        hArmAnimation.start();
                    }
                    if (!logik.erSidsteBogstavKorrekt() && logik.erSpilletTabt()) {
                        aniview.setBackgroundResource(drawable.animationlisttabt);
                        AnimationDrawable tabtAnimation = (AnimationDrawable) aniview.getBackground();
                        tabtAnimation.start();

                    }

                    opdaterSkærm();

                }
            }
        }

    }


    private void opdaterSkærm() {
        String infoord = "Guess:" + "\n" + logik.getSynligtOrd().substring(0, 1).toUpperCase() + logik.getSynligtOrd().substring(1).toLowerCase();
        infoomordet.setText(infoord);
        String infobogstave = "\nYou have " + logik.getAntalForkerteBogstaver() + " wrong guess(es):" + logik.getForkertebogstaver();
        infobogstaver.setText(infobogstave);


        if (logik.erSpilletVundet()) {
            cm.stop();

            konfettiview.getActiveSystems().clear();
            konfettiview.build()
                    .addColors(Color.WHITE, Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN)
                    .setDirection(6.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(3000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(3, 5))
                    .setPosition(-50f, konfettiview.getWidth() + 50f, -200f, -50f)
                    .streamFor(300, 5000L);


            if (soundact) {
                MediaPlayer winningsound1 = MediaPlayer.create(getActivity(), raw.cheering);
                MediaPlayer winningsound2 = MediaPlayer.create(getActivity(), raw.crowdapplause1);
                winningsound1.start();
                winningsound2.start();
            }

            highscore();


            String won = "Do you want to try again?";
            endOfGameDialog(won);


        }
        if (logik.erSpilletTabt()) {
            cm.stop();


            String lost = "You have lost, the word was:\n" + logik.getOrdet().substring(0, 1).toUpperCase() + logik.getOrdet().substring(1).toLowerCase();

            if (soundact) {

                MediaPlayer winningsound = MediaPlayer.create(getActivity(), raw.boo3);
                winningsound.start();
            }
            endOfGameDialog(lost);


        }
    }

    //Metode til at genstarte spillet
    private void restartGame() {
        logik.nulstil();
        GameFragment gamefragment = new GameFragment();
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        gamefragment.setArguments(restartgamebundle);
        ft.replace(id.mainactfragment, gamefragment)
                .addToBackStack(null)
                .commit();


    }

    public void endOfGameDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(string.playagain, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (MyEnum.MyWord == getArguments().getSerializable("cat")) {
                    logik.myWord(getArguments().getString("myword"));
                    infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
                    tid = 0;
                    score = 200;
                    cm.setBase(SystemClock.elapsedRealtime());
                    cm.start();

                    for (Button b : buttons) {
                        b.setClickable(true);
                        b.setPressed(false);
                        b.setTextColor(Color.WHITE);
                    }
                }
                if (MyEnum.MyWord != getArguments().getSerializable("cat")) {
                    restartGame();
                }
            }
        });

        builder.setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getFragmentManager().beginTransaction().replace(R.id.mainactfragment, getFragmentManager().findFragmentByTag("Hovedmenufrag"))
                        .addToBackStack(null)
                        .commit();
            }
        });
        builder.setTitle(message);

        AlertDialog dialog = builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
    }

    //Dialog til indtastning på highscore
    public boolean highscore() {

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        int thescore = (score - tid) + 5 * (logik.getOrdet().length()) - 5 * logik.getAntalForkerteBogstaver();
        System.out.println(thescore);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("You have won using: " + logik.getBrugteBogstaver().size() / 2 + " attempts");
        builder.setMessage("Your score is: " + thescore + " Whats your name?!");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navn = input.getText().toString();
                dialog.dismiss();
                if (navn == null || navn.equalsIgnoreCase("")) {
                    navn = "Emil";
                }
                Set navneSet = Sharedp.prefs.getStringSet("hscorenavne", new HashSet<>());
                navneSet.add(navn);
                Sharedp.prefs.edit().putInt(navn, thescore).commit();
                Sharedp.prefs.edit().putStringSet("hscorenavne", navneSet).commit();

                vent = true;
                handler.sendMessage(handler.obtainMessage());


            }
        });
        builder.show();
        try {
            Looper.loop();
        } catch (RuntimeException e) {
        }

        return vent;

    }

}