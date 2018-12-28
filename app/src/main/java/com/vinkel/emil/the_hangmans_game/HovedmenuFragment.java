package com.vinkel.emil.the_hangmans_game;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


public class HovedmenuFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, OnSeekBarChangeListener {
    private Button playbutton;
    private Spinner dropdown;
    private Bundle bundle = new Bundle();
    private String myownword;
    private Enum temp = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mitfragment = inflater.inflate(R.layout.fragment_hovedmenu, container, false);
        playbutton = mitfragment.findViewById(R.id.playbutton);
        playbutton.setOnClickListener(this);
        TextView text = mitfragment.findViewById(R.id.textViewcat);
        text.setText(R.string.category);
        TextView text2 = mitfragment.findViewById(R.id.textViewdif);
        text2.setText(R.string.difficulty);
        SeekBar difficultybar = mitfragment.findViewById(R.id.difficultybar);
        difficultybar.setOnSeekBarChangeListener(this);
        Navigation.findNavController(getActivity(),R.id.nav_host).getGraph().setStartDestination(R.id.hovedmenuFragment);
        dropdown = mitfragment.findViewById(R.id.category);
        String[] items = new String[]{MyEnum.DEFAULT.toString(), MyEnum.HARRYPOTTER.toString(), MyEnum.STARWARS.toString(), MyEnum.FOOD.toString(), MyEnum.WordsDR.toString(), MyEnum.MyWord.toString()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        return mitfragment;
    }


    public void onClick(View v) {
        if (v == playbutton) {
            TheGameState.GameLogic.setGameinprogress(false);
            TheGameState.timewhendestroyed=0;
            if (temp != MyEnum.MyWord) {

                NavHostFragment.findNavController(this).navigate(R.id.action_hovedmenuFragment_to_gameFragment,bundle);
            }

            if (temp == MyEnum.MyWord) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Type your word");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                builder.setView(input);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    myownword = input.getText().toString();
                    dialog.dismiss();
                    if (myownword == null) {
                        myownword = "NothingTyped";
                    }
                    String detskalværeord = myownword.replaceAll(" ", "").
                            replaceAll("[^a-zæøå]", "").
                            replaceAll("å", "aa").
                            replaceAll("ø", "oe").
                            replaceAll("æ", "ae");
                    if (detskalværeord.length() < 3) {
                        detskalværeord = "wordmustbelonger";
                    }
                    if (detskalværeord.length() > 20) {
                        detskalværeord = "cheater";
                    }
                    bundle.putString("myword", detskalværeord);
                    System.out.println(detskalværeord);

                    NavHostFragment.findNavController(this).navigate(R.id.action_hovedmenuFragment_to_gameFragment,bundle);


                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);

            }




        }
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (parent.getId() == dropdown.getId()) {

            switch (position) {
                case 0:
                    temp = MyEnum.DEFAULT;
                    break;
                case 1:
                    temp = MyEnum.HARRYPOTTER;
                    break;
                case 2:
                    temp = MyEnum.STARWARS;
                    break;
                case 3:
                    temp = MyEnum.FOOD;
                    break;
                case 4:
                    temp = MyEnum.WordsDR;
                    break;
                case 5:
                    temp = MyEnum.MyWord;
                    break;
            }
            bundle.putSerializable("cat", temp);
        }


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        bundle.putSerializable("cat", MyEnum.DEFAULT);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (progress) {
            case 0:

                bundle.putSerializable("dif", MyEnum.EASY);
                Toast.makeText(getActivity(), MyEnum.EASY.toString(), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                bundle.putSerializable("dif", MyEnum.NORMAL);
                Toast.makeText(getActivity(), MyEnum.NORMAL.toString(), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                bundle.putSerializable("dif", MyEnum.HARD);
                Toast.makeText(getActivity(), MyEnum.HARD.toString(), Toast.LENGTH_SHORT).show();
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
