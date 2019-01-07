package com.vinkel.emil.the_hangmans_game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class HighscoreFragment extends android.support.v4.app.Fragment {
    public HighscoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View hscore = inflater.inflate(R.layout.fragment_highscore, container, false);
        ListView hview = hscore.findViewById(R.id.highscorelist);
        TextView title = hscore.findViewById(R.id.Highview);
        TextView name = hscore.findViewById(R.id.hname);
        TextView score = hscore.findViewById(R.id.hscore);
        name.setText(R.string.name);
        score.setText(R.string.score);
        title.setText(R.string.Highscorelist);

        HighscoreListadapter adapt = new HighscoreListadapter(getActivity(), TheGameState.generateHighscore());
        hview.setAdapter(adapt);
        hview.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getActivity(), "The word guessed was: "+(adapt.getItem(position).getWord().substring(0, 1).toUpperCase() + adapt.getItem(position).getWord().substring(1)) +" and it took: " +adapt.getItem(position).getHowfast()+ " seconds to guess.", Toast.LENGTH_SHORT).show());


        return hscore;


    }

    private class HighscoreListadapter extends ArrayAdapter<Player>   {
        private HighscoreListadapter(Context context, ArrayList<Player> playerlist) {
            super(context, 0, playerlist);

        }
        @Override
        public View getView(int position, View mitView, ViewGroup parent) {

            if (mitView == null) {
                mitView = LayoutInflater.from(getContext()).inflate(R.layout.customlist, parent, false);
            }
            Player player = getItem(position);
            TextView hname = mitView.findViewById(R.id.namehlist);
            hname.setText(player.getName());
            TextView hscore = mitView.findViewById(R.id.scorehlist);
            hscore.setText(String.valueOf(player.getScore()));


            return mitView;
        }

    }
}
