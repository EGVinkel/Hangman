package com.vinkel.emil.the_hangmans_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

import androidx.navigation.fragment.NavHostFragment;
import pl.droidsonroids.gif.GifImageView;


public class EndofGameFragment extends Fragment implements View.OnClickListener {
 private String navn;
 private TextView title, gameinfo;
 private GifImageView giffy;
 private Button playagain,mainmenu;
 private Bundle restart;
 private int thescore,tid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View endofgamefragment= inflater.inflate(R.layout.fragment_endof_game, container, false);
        playagain=endofgamefragment.findViewById(R.id.playagain);
        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setNavigationIcon(null);

        playagain.setOnClickListener(this);
        mainmenu=endofgamefragment.findViewById(R.id.returnmm);
        mainmenu.setOnClickListener(this);
        title=endofgamefragment.findViewById(R.id.endtitle);
        title.setText(getArguments().getString("enddialog"));
        gameinfo=endofgamefragment.findViewById(R.id.endofgameinfo);
        tid=getArguments().getInt("tid");
        System.out.println(tid);
        thescore= (200 - tid + 5 * (TheGameState.GameLogic.getOrdet().length()) - 10 * TheGameState.GameLogic.getAntalForkerteBogstaver());
        giffy=endofgamefragment.findViewById(R.id.gifviewendgame);

        if(TheGameState.GameLogic.erSpilletVundet()){
            gameinfo.setText("You used: " +TheGameState.GameLogic.getBrugteBogstaver().size()+" attempts.\n"+"Your score is: " +thescore+"\nIt took you: "+tid+" seconds");
            title.setText("You have won!");
            highscore();

            giffy.setBackgroundResource(R.drawable.won);
        }
        if(TheGameState.GameLogic.erSpilletTabt()){
            giffy.setBackgroundResource(R.drawable.lost);
            title.setText("You have lost!");
            String forkerte= String.join("+", TheGameState.GameLogic.getForkertebogstaver());

            gameinfo.setText("The word was: " +TheGameState.GameLogic.getOrdet() + "\nWrong letters:\n" + forkerte);

        }
        restart=new Bundle();
        restart.putSerializable("cat", getArguments().getSerializable("cat"));
        restart.putSerializable("dif", getArguments().getSerializable("dif"));


        return endofgamefragment;
    }

    @Override
    public void onDestroy() {
        ((Toolbar)getActivity().findViewById(R.id.toolbar)).setNavigationIcon(((Toolbar)getActivity().findViewById(R.id.toolbar)).getNavigationIcon());
        super.onDestroy();
    }

    public void highscore() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Eligible for highscore!");
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
            Set navneSet = TheGameState.prefs.getStringSet("hscorenavne", new HashSet<>());
            String playernavn = "player" + navneSet.size();
            String playerscore = ("player" + navneSet.size()) + "s";
            System.out.println("navn i end: "+playernavn+"int navn i end: "+playerscore);

            navneSet.add(playernavn);

            TheGameState.prefs.edit().putString(playernavn, navn).commit();

            TheGameState.prefs.edit().putInt(playerscore, thescore).commit();
            TheGameState.prefs.edit().putStringSet("hscorenavne", navneSet).commit();

            });

        AlertDialog dialog = builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);




    }

    @Override
    public void onClick(View view) {
        if(view==playagain){
            TheGameState.generateHighscore();
            if (getArguments().getSerializable("cat") == MyEnum.MyWord){
                restart.putString("myword",getArguments().getString("myword"));
            }
            TheGameState.GameLogic.nulstil();
            TheGameState.timewhendestroyed=0;
            NavHostFragment.findNavController(this).navigate(R.id.action_endofGameFragment_to_gameFragment,restart);
        }
        if(view==mainmenu){
            NavHostFragment.findNavController(this).popBackStack();
        }
    }
}
