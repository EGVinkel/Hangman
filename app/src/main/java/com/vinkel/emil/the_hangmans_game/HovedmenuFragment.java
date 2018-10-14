package com.vinkel.emil.the_hangmans_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class HovedmenuFragment extends android.app.Fragment implements View.OnClickListener ,AdapterView.OnItemSelectedListener, OnSeekBarChangeListener {
        private Button playbutton;
        private Spinner dropdown;
        private Bundle bundle = new Bundle();

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View mitfragment= inflater.inflate(R.layout.fragment_hovedmenu, container, false);
            playbutton= mitfragment.findViewById(R.id.playbutton);
            playbutton.setOnClickListener(this);
            TextView text = mitfragment.findViewById(R.id.textViewcat);
            text.setText(R.string.category);
            TextView text2=mitfragment.findViewById(R.id.textViewdif);
            text2.setText(R.string.difficulty);
            SeekBar difficultybar=mitfragment.findViewById(R.id.difficultybar);
            difficultybar.setOnSeekBarChangeListener(this);




            dropdown = mitfragment.findViewById(R.id.category);
            String[] items = new String[]{MyEnum.DEFAULT.toString(),MyEnum.HARRYPOTTER.toString(),MyEnum.STARWARS.toString(),MyEnum.FOOD.toString()};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(this);
            return mitfragment;
        }


        public void onClick(View v) {
            if(v == playbutton) {

                    GameFragment gamefragment = new GameFragment();

                    android.app.FragmentManager fm=getFragmentManager();
                    android.app.FragmentTransaction ft=fm.beginTransaction();
                         gamefragment.setArguments(bundle);
                            ft.replace(R.id.mainactfragment, gamefragment)
                            .addToBackStack(null)
                            .commit();



            }
        }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(parent.getId() == dropdown.getId()) {
            Enum temp=null;
            switch (position) {
                case 0:
                   temp=MyEnum.DEFAULT;
                    break;
                case 1:
                   temp=MyEnum.HARRYPOTTER;
                    break;
                case 2:
                    temp=MyEnum.STARWARS;
                    break;
                case 3:
                    temp=MyEnum.FOOD;
                    break;

            }
            bundle.putSerializable("cat", temp);
        }



    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        bundle.putSerializable("cat",MyEnum.DEFAULT);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            switch (progress) {
                case 0:

                    bundle.putSerializable("dif",MyEnum.EASY);
                    Toast.makeText(getActivity(),MyEnum.EASY.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    bundle.putSerializable("dif",MyEnum.NORMAL);
                    Toast.makeText(getActivity(),MyEnum.NORMAL.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    bundle.putSerializable("dif",MyEnum.HARD);
                    Toast.makeText(getActivity(),MyEnum.HARD.toString(), Toast.LENGTH_SHORT).show();
                    break;

            }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
