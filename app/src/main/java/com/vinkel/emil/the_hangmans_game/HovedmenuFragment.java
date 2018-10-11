package com.vinkel.emil.the_hangmans_game;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;




public class HovedmenuFragment extends Fragment implements View.OnClickListener ,AdapterView.OnItemSelectedListener{
        private Button playbutton;
        private String valgtcategori="def.txt";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View mitfragment= inflater.inflate(R.layout.fragment_hovedmenu, container, false);
            playbutton= mitfragment.findViewById(R.id.playbutton);
            playbutton.setOnClickListener(this);
            TextView text = mitfragment.findViewById(R.id.textViewcat);
            text.setText(R.string.category);
            Spinner dropdown = mitfragment.findViewById(R.id.spinner1);



            String[] items = new String[]{getString(R.string.def),getString(R.string.harrypotter), getString(R.string.starwars), getString(R.string.food)};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(this);
            return mitfragment;
        }


        public void onClick(View v) {
            if(v == playbutton) {
                    Bundle bundle = new Bundle();
                    GameFragment gamefragment = new GameFragment();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                         bundle.putString("cat",valgtcategori);

                         gamefragment.setArguments(bundle);

                            ft.replace(R.id.mainactfragment, gamefragment)
                            .addToBackStack(null)
                            .commit();



            }
        }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                valgtcategori="def.txt";
            break;
            case 1:
                valgtcategori="harrypotter.txt";
                break;
            case 2:
                valgtcategori="starwars.txt";
                break;
            case 3:
                valgtcategori="food.txt";
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        valgtcategori="def.txt";

    }


}