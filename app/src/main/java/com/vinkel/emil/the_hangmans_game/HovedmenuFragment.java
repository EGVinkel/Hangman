package com.vinkel.emil.the_hangmans_game;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class HovedmenuFragment extends Fragment implements View.OnClickListener {
        Button playbutton;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View mitfragment= inflater.inflate(R.layout.fragment_hovedmenu, container, false);
            playbutton= mitfragment.findViewById(R.id.playbutton);
            playbutton.setOnClickListener(this);






            return mitfragment;
        }


        public void onClick(View v) {
            if(v == playbutton) {

                    GameFragment gamefragment = new GameFragment();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.mainactfragment, gamefragment)
                            .addToBackStack(null)
                            .commit();



            }
        }
    }