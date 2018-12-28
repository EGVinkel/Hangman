package com.vinkel.emil.the_hangmans_game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.HashSet;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import static com.vinkel.emil.the_hangmans_game.R.color;
import static com.vinkel.emil.the_hangmans_game.R.drawable;
import static com.vinkel.emil.the_hangmans_game.R.id;
import static com.vinkel.emil.the_hangmans_game.R.layout;
import static com.vinkel.emil.the_hangmans_game.R.raw;


public class GameFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private TextView infoomordet;
    private ImageView aniview;
    private TextView infobogstaver;
    private boolean soundact;
    private Bundle endgameBundle;
    private PauseableChronometer cm;
    private AnimationDrawable Animation;
    private Animation ryst;
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
        endgameBundle = new Bundle();
        //Diverse oprettelser
        konfettiview = getActivity().findViewById(id.viewKonfetti);
        creepster = Typeface.createFromAsset(getActivity().getAssets(), "creepsterregular.ttf");
        cm = gamefragment.findViewById(id.count);
        cm.setTypeface(creepster);
        minBar = gamefragment.findViewById(id.progressBar);
        minBar.setVisibility(View.GONE);
        //Hent lydeffecter til/fra
        soundact = TheGameState.prefs.getBoolean("pref_sound", true);
        infoomordet = gamefragment.findViewById(id.gameinfo);
        infobogstaver = gamefragment.findViewById(id.wrongletters);
        aniview = gamefragment.findViewById(id.animationwindow);
        aniview.setBackgroundResource(R.drawable.gallowshoved0);
        ryst = AnimationUtils.loadAnimation(getActivity(), R.anim.anima);
        myvib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        Navigation.findNavController(getActivity(),R.id.nav_host).getGraph().setStartDestination(id.gameFragment);


                //Opretter knapper
                for (int iterator = 0; iterator < buttons.length; iterator++) {
                    buttons[iterator] = gamefragment.findViewById(idList[iterator]);
                    buttons[iterator].setOnClickListener(this);
                    buttons[iterator].setTypeface(creepster);
                }


                if(!TheGameState.GameLogic.isGameinprogress()) {
                //Kategori selvskrevet ord:
                if (getArguments().getSerializable("cat") == MyEnum.MyWord) {
                    endgameBundle.putString("myword", getArguments().getString("myword"));
                    TheGameState.GameLogic.myWord(getArguments().getString("myword"));
                    infoomordet.setText("Guess:" + "\n" + TheGameState.GameLogic.getSynligtOrd());
                }
                //Kategori DR ord:
                    if (getArguments().getSerializable("cat") == MyEnum.WordsDR) {
                        getWords();
                    }
                //Andre Kategorier:
                if (getArguments().getSerializable("cat") != MyEnum.WordsDR && getArguments().getSerializable("cat") != MyEnum.MyWord) {
                    TheGameState.GameLogic.categoriesAndDifficulty((Enum) getArguments().getSerializable("cat"), (Enum) getArguments().getSerializable("dif"), getContext());
                    infoomordet.setText("Guess:" + "\n" + TheGameState.GameLogic.getSynligtOrd());
                }

                TheGameState.GameLogic.setGameinprogress(true);
                cm.start();
                }

        cm.setCurrentTime(TheGameState.timewhendestroyed);
        cm.start();
        if((getArguments().getSerializable("cat") == MyEnum.WordsDR&&!TheGameState.prefs.getStringSet("orddr", new HashSet<>()).isEmpty())){
            updateScreen();
        }
        if(getArguments().getSerializable("cat") != MyEnum.WordsDR&&TheGameState.GameLogic.isGameinprogress()) {
            updateScreen();
        }








        endgameBundle.putSerializable("cat", getArguments().getSerializable("cat"));

        endgameBundle.putSerializable("dif", getArguments().getSerializable("dif"));
        return gamefragment;
    }

    @Override
    public void onClick(View view) {
        if (TheGameState.GameLogic.getSynligtOrd() == null) {
            return;
        }

        for (int i = 0; i < buttons.length; i++) {
            if (view instanceof Button) {
                if (view.getId() == idList[i]) {
                    Button tempb = (Button) view;
                    String str = tempb.getText().toString();
                    infoomordet.setText(str);
                    TheGameState.GameLogic.gætBogstav(str);
                    view.setClickable(false);
                    ((Button) view).setTextColor(getActivity().getColor(color.colorPrimaryDark));
                    if (!TheGameState.GameLogic.erSidsteBogstavKorrekt()) {
                        makeAnimation(TheGameState.GameLogic.getAntalForkerteBogstaver());
                    }
                    updateScreen();
                }
            }
        }

    }

    private void updateScreen() {
        String infoord = "Guess:" + "\n" + TheGameState.GameLogic.getSynligtOrd().substring(0, 1).toUpperCase() + TheGameState.GameLogic.getSynligtOrd().substring(1).toLowerCase();
        infoomordet.setText(infoord);
        String infobogstave = "\nYou have " + TheGameState.GameLogic.getAntalForkerteBogstaver() + " wrong guess(es):" + TheGameState.GameLogic.getForkertebogstaver();
        infobogstaver.setText(infobogstave);


        if (TheGameState.GameLogic.erSpilletVundet()) {
            cm.stop();
            endgameBundle.putInt("tid",(int)cm.getCurrentTime()/1000);
            makeConfetti();
            soundeffects();
            TheGameState.GameLogic.setGameinprogress(false);
            NavHostFragment.findNavController(this).navigate(id.action_gameFragment_to_endofGameFragment,endgameBundle);


        }
        if (TheGameState.GameLogic.erSpilletTabt()) {
            cm.stop();
            soundeffects();
            TheGameState.GameLogic.setGameinprogress(false);
           NavHostFragment.findNavController(this).navigate(id.action_gameFragment_to_endofGameFragment,endgameBundle);
        }
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
            if (TheGameState.GameLogic.erSpilletVundet()) {
                MediaPlayer.create(getActivity(), raw.cheering).start();
                MediaPlayer.create(getActivity(), raw.crowdapplause1).start();
            }
            if (TheGameState.GameLogic.erSpilletTabt()) {
                MediaPlayer.create(getActivity(), raw.boo3).start();
            }
        }

    }


    private void getWords() {
        //Kontroller i prefs manager om ordene allerede findes, og henter derfra hvis de gør.
        if (!TheGameState.prefs.getStringSet("orddr", new HashSet<>()).isEmpty()) {
            TheGameState.GameLogic.categoriesAndDifficulty((Enum) getArguments().getSerializable("cat"), (Enum) getArguments().getSerializable("dif"),getContext());
            infoomordet.setText("Guess:" + "\n" + TheGameState.GameLogic.getSynligtOrd());

        } else if (TheGameState.prefs.getStringSet("orddr", new HashSet<>()).isEmpty()) {
            infoomordet.setText("Loading\nWords\nPlease Wait");
            minBar.setVisibility(View.VISIBLE);
            taske = new MinAsynctask(this).execute();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cm.start();
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

    @Override
    public void onDestroyView() {
        TheGameState.timewhendestroyed =cm.getCurrentTime();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        cm.stop();
    }


    //Skiftet min tidligere Async task til en private nested class, for hermed at kunne undgå static field leaks.
    //Inspiriration fra følgende link https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur, men koden
    //generelt egen, da jeg arbejder med fragment i stedet for activitet i eksemplet.
    private static class MinAsynctask extends AsyncTask<Void, Void, Void> {

        private WeakReference<GameFragment> gFragWeakReference;

        MinAsynctask(GameFragment context) {
            gFragWeakReference = new WeakReference<>(context);
        }
        protected Void doInBackground(Void... unused) {
            try {
                TheGameState.GameLogic.hentOrdFraDr();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            GameFragment gamefrag = gFragWeakReference.get();
            gamefrag.minBar.setVisibility(View.GONE);
            gamefrag.infoomordet.setText("Guess:" + "\n" + TheGameState.GameLogic.getSynligtOrd());
            gamefrag.updateScreen();

        }
    }







}