package com.vinkel.emil.the_hangmans_game;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vinkel.emil.the_hangmans_game.com.vinkel.emil.the_hangmans_game.playerdata.Sharedp;

import java.lang.ref.WeakReference;
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

    private final int score = 200;
    private TextView infoomordet;
    private ImageView aniview;
    private TextView infobogstaver;
    private Galgelogik logik;
    private boolean soundact;
    private Bundle restartgamebundle;
    private Chronometer cm;
    private int tid;
    private AnimationDrawable Animation;
    private Animation ryst;
    private String navn;
    private boolean vent;
    private ProgressBar minBar;
    private AsyncTask taske;
    private Vibrator myvib;
    int[] idList = {id.a, id.b, id.c, id.d, id.e, id.f, id.g, id.h, id.i, id.j,
            id.k, id.l, id.m, id.n, id.o, id.p, id.q, id.r, id.s, id.t, id.u, id.v,
            id.w, id.x, id.y, id.z};

    Button a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;
    Button[] buttons = {a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z};
    private KonfettiView konfettiview;
    private Typeface creepster;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View gamefragment = inflater.inflate(layout.fragment_game, container, false);
        if (logik == null) {
            logik = new Galgelogik(getActivity());
        }
        //Diverse oprettelser
        konfettiview = getActivity().findViewById(id.viewKonfetti);
        cm = gamefragment.findViewById(id.count);
        cm.setTypeface(creepster);
        minBar = gamefragment.findViewById(id.progressBar);
        minBar.setVisibility(View.GONE);
        //Hent lydeffecter til/fra
        soundact = Sharedp.prefs.getBoolean("pref_sound", true);
        infoomordet = gamefragment.findViewById(id.gameinfo);
        infobogstaver = gamefragment.findViewById(id.wrongletters);
        aniview = gamefragment.findViewById(id.animationwindow);
        aniview.setBackgroundResource(R.drawable.gallowshoved0);
        creepster = Typeface.createFromAsset(getActivity().getAssets(), "creepsterregular.ttf");
        ryst = AnimationUtils.loadAnimation(getActivity(), R.anim.anima);
        myvib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        //Opretter knapper
        for (int iterator = 0; iterator < buttons.length; iterator++) {
            buttons[iterator] = gamefragment.findViewById(idList[iterator]);
            buttons[iterator].setOnClickListener(this);
        }
        for (Button b : buttons) {
            b.setTypeface(creepster);
        }


        //Kategori selvskrevet ord:
        if (getArguments().getSerializable("cat") == MyEnum.MyWord) {
            logik.myWord(getArguments().getString("myword"));
            infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
        }
        //Kategori DR ord:
        if (getArguments().getSerializable("cat") == MyEnum.WordsDR) {
            getWords();
        }
        //Andre Kategorier:
        if (getArguments().getSerializable("cat") != MyEnum.WordsDR && getArguments().getSerializable("cat") != MyEnum.MyWord) {
            logik.categoriesAndDifficulty((Enum) getArguments().getSerializable("cat"), (Enum) getArguments().getSerializable("dif"));
            infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
        }
        startChronometer();


        restartgamebundle = new Bundle();
        //adds category to restart
        restartgamebundle.putSerializable("cat", getArguments().getSerializable("cat"));
        //adds difficulty to restart
        restartgamebundle.putSerializable("dif", getArguments().getSerializable("dif"));



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
                    if (!logik.erSidsteBogstavKorrekt()) {
                        makeAnimation(logik.getAntalForkerteBogstaver());
                    }
                    updateScreen();
                }
            }
        }

    }

    private void updateScreen() {
        String infoord = "Guess:" + "\n" + logik.getSynligtOrd().substring(0, 1).toUpperCase() + logik.getSynligtOrd().substring(1).toLowerCase();
        infoomordet.setText(infoord);
        String infobogstave = "\nYou have " + logik.getAntalForkerteBogstaver() + " wrong guess(es):" + logik.getForkertebogstaver();
        infobogstaver.setText(infobogstave);


        if (logik.erSpilletVundet()) {
            cm.stop();
            makeConfetti();
            soundeffects();
            highscore();
            String won = "Do you want to try again?";
            endOfGameDialog(won);
        }
        if (logik.erSpilletTabt()) {
            cm.stop();
            soundeffects();
            String lost = "You have lost, the word was:\n" + logik.getOrdet().substring(0, 1).toUpperCase() + logik.getOrdet().substring(1).toLowerCase();
            endOfGameDialog(lost);
        }
    }

    //Metode til at genstarte spillet
    private void restartGame() {
        logik.nulstil();
        GameFragment gamefragment = new GameFragment();
        gamefragment.setArguments(restartgamebundle);
        getFragmentManager().beginTransaction().replace(id.mainactfragment, gamefragment).addToBackStack(null).commit();

    }

    public void endOfGameDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton(string.playagain, (dialog, id) -> {
            if (MyEnum.MyWord == getArguments().getSerializable("cat")) {
                logik.myWord(getArguments().getString("myword"));
                infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
                startChronometer();
                aniview.setBackgroundResource(R.drawable.gallowshoved0);
                for (Button b : buttons) {
                    b.setClickable(true);
                    b.setPressed(false);
                    b.setTextColor(Color.WHITE);
                }
            }
            if (MyEnum.MyWord != getArguments().getSerializable("cat")) {
                restartGame();
            }
        });

        builder.setNegativeButton(string.cancel, (dialog, id) -> {
            dialog.dismiss();
            getFragmentManager().beginTransaction().replace(R.id.mainactfragment, getFragmentManager().findFragmentByTag("Hovedmenufrag"))
                    .addToBackStack(null)
                    .commit();
        });
        builder.setTitle(message);

        AlertDialog dialog = builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setOnCancelListener(dialog1 -> getActivity().onBackPressed());

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
        builder.setPositiveButton("OK", (dialog, which) -> {
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


        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        dialog.setOnCancelListener(dialog1 -> getActivity().onBackPressed());
        try {
            Looper.loop();
        } catch (RuntimeException e) {
        }

        return vent;

    }

    private void makeAnimation(int i) {

        switch (i) {

            case 1:
                aniview.setBackgroundResource(drawable.animationlisthoved);
                break;
            case 2:
                aniview.setBackgroundResource(drawable.animationlistkrop);
                break;
            case 3:
                aniview.setBackgroundResource(drawable.animationlistvben);
                break;
            case 4:
                aniview.setBackgroundResource(drawable.animationlisthben);
                break;
            case 5:
                aniview.setBackgroundResource(drawable.animationlistvarm);
                break;
            case 6:
                aniview.setBackgroundResource(drawable.animationlistharm);
                break;
            case 7:
                aniview.setBackgroundResource(drawable.animationlisttabt);
                break;
        }
        getView().startAnimation(ryst);
        myvib.vibrate(VibrationEffect.createOneShot(200, 255));
        Animation = (AnimationDrawable) aniview.getBackground();
        Animation.start();
    }


    private void makeConfetti() {
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
    }

    private void soundeffects() {

        if (soundact) {
            if (logik.erSpilletVundet()) {
                MediaPlayer.create(getActivity(), raw.cheering).start();
                MediaPlayer.create(getActivity(), raw.crowdapplause1).start();
            }
            if (logik.erSpilletTabt()) {
                MediaPlayer.create(getActivity(), raw.boo3).start();
            }
        }

    }

    private void startChronometer() {
        tid = 0;
        cm.setBase(SystemClock.elapsedRealtime());
        cm.start();
        cm.setFormat("%s");
        cm.setOnChronometerTickListener(chronometer -> tid++);
    }

    private void getWords() {
        //Kontroller i prefs manager om ordene allerede findes, og henter derfra hvis de gør.
        if (!Sharedp.prefs.getStringSet("orddr", new HashSet<>()).isEmpty()) {
            logik.categoriesAndDifficulty((Enum) getArguments().getSerializable("cat"), (Enum) getArguments().getSerializable("dif"));
            infoomordet.setText("Guess:" + "\n" + logik.getSynligtOrd());
        } else if (Sharedp.prefs.getStringSet("orddr", new HashSet<>()).isEmpty()) {
            taske = new MinAsynctask(this).execute();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (taske != null) {
                    taske.cancel(true);
                }

                getActivity().onBackPressed();
                return true;
            }
            return false;
        });

    }

    //Skiftet min tidligere Async task til en private nested class, for hermed at kunne undgå static field leaks.
    //Inspiriration fra følgende link https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur, men koden
    //generelt egen, da jeg arbejder med fragment i stedet for activitet i eksemplet.
    private static class MinAsynctask extends AsyncTask<Void, Void, Void> {

        private WeakReference<GameFragment> gFragWeakReference;


        MinAsynctask(GameFragment context) {
            gFragWeakReference = new WeakReference<>(context);
        }

        protected void onPreExecute() {
            GameFragment gamefrag = gFragWeakReference.get();
            gamefrag.infoomordet.setText("Loading\nWords\nPlease Wait");
            gamefrag.minBar.setVisibility(View.VISIBLE);

        }

        protected Void doInBackground(Void... unused) {
            try {
                gFragWeakReference.get().logik.hentOrdFraDr();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            GameFragment gamefrag = gFragWeakReference.get();
            gamefrag.minBar.setVisibility(View.GONE);
            gamefrag.infoomordet.setText("Guess:" + "\n" + gFragWeakReference.get().logik.getSynligtOrd());
        }
    }






}