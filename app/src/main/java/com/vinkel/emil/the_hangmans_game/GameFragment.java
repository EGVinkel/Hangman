package com.vinkel.emil.the_hangmans_game;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;


public class GameFragment extends Fragment implements View.OnClickListener {

    Galgelogik logik = new Galgelogik();
    private TextView infoomordet;
    private ImageView aniview;
    private TextView infobogstaver;
    private AnimationDrawable hovedAnimation;
    private AnimationDrawable kropAnimation;
    private AnimationDrawable vBenAnimation;
    private AnimationDrawable hBenAnimation;
    private AnimationDrawable hArmAnimation;
    private AnimationDrawable varmAnimation;
    private AnimationDrawable tabtAnimation;

    Button a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z;
    Button[] buttons={a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z};
    int[] idList = {R.id.a, R.id.b, R.id.c,R.id.d,R.id.e,R.id.f,R.id.g,R.id.h,R.id.i,R.id.j,
            R.id.k,R.id.l,R.id.m,R.id.n,R.id.o,R.id.p,R.id.q,R.id.r,R.id.s,R.id.t,R.id.u,R.id.v,
            R.id.w,R.id.x,R.id.y,R.id.z};



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gamefragment= inflater.inflate(R.layout.fragment_game, container, false);
        for(int iterator =0 ; iterator< buttons.length;iterator++){
            buttons[iterator] = gamefragment.findViewById(idList[iterator]);
            buttons[iterator].setOnClickListener(this);

        }
        infoomordet = gamefragment.findViewById(R.id.gameinfo);
        infoomordet.setText("Guess:" +"\n"  + logik.getSynligtOrd());
        infobogstaver= gamefragment.findViewById(R.id.wrongletters);
        aniview= gamefragment.findViewById(R.id.animationwindow);
        aniview.setBackgroundResource(R.drawable.animationlisthoved);
        hovedAnimation = (AnimationDrawable) aniview.getBackground();

        return gamefragment;
    }

    @Override
    public void onClick(View view) {




        for(int i = 0 ; i < buttons.length ; i++){
            if(view instanceof Button) {
                if (view.getId() == idList[i]) {
                    Button tempb = (Button)view;
                    String str = tempb.getText().toString();
                    infoomordet.setText(str);
                    logik.gætBogstav(str);
                    view.setClickable(false);
                    ((Button) view).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    if(!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver()==1){
                    hovedAnimation.start();

                    }
                    if(!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver()==2){
                    aniview.setBackgroundResource(R.drawable.animationlistkrop);
                    kropAnimation= (AnimationDrawable) aniview.getBackground();

                    kropAnimation.start();

                    }
                    if(!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver()==3){
                        aniview.setBackgroundResource(R.drawable.animationlistvben);
                        vBenAnimation= (AnimationDrawable) aniview.getBackground();

                        vBenAnimation.start();

                    }

                    if(!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver()==4){
                        aniview.setBackgroundResource(R.drawable.animationlisthben);
                        hBenAnimation= (AnimationDrawable) aniview.getBackground();

                        hBenAnimation.start();

                    }
                    if(!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver()==5){
                        aniview.setBackgroundResource(R.drawable.animationlistvarm);
                        varmAnimation= (AnimationDrawable) aniview.getBackground();

                        varmAnimation.start();

                    }
                    if(!logik.erSidsteBogstavKorrekt() && logik.getAntalForkerteBogstaver()==6){
                        aniview.setBackgroundResource(R.drawable.animationlistharm);
                        hArmAnimation= (AnimationDrawable) aniview.getBackground();

                        hArmAnimation.start();
                    }
                    if(!logik.erSidsteBogstavKorrekt() && logik.erSpilletTabt()){
                        aniview.setBackgroundResource(R.drawable.animationlisttabt);
                        tabtAnimation= (AnimationDrawable) aniview.getBackground();
                        tabtAnimation.start();

                    }

                    opdaterSkærm();

                }
            }
        }

    }


    private void opdaterSkærm() {
        infoomordet.setText("Guess:" +"\n" + logik.getSynligtOrd());
        infobogstaver.setText("\nYou have " + logik.getAntalForkerteBogstaver() + " wrong guesses:" + logik.getForkertebogstaver());
        Timer timer= new Timer();


        if (logik.erSpilletVundet()) {
            infoomordet.setText("\n You have won! \n Game is restarting.");
            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    restartGame();
                }
            },100);

        }
        if (logik.erSpilletTabt()) {
            infoomordet.setText("You have lost, the word was:\n" + logik.getOrdet() + "\n Game is restarting.");

            timer.schedule(new TimerTask(){
                @Override
                public void run(){
                    restartGame();
                }
            },100);

        }
    }

    private void restartGame(){
        logik.nulstil();
        Fragment frg;
        frg = getFragmentManager().findFragmentById(R.id.mainactfragment);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();


    }
}