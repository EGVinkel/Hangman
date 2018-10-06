package com.vinkel.emil.the_hangmans_game;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class GameFragment extends Fragment implements View.OnClickListener {

    Galgelogik logik = new Galgelogik();
    private TextView info;


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
        info= gamefragment.findViewById(R.id.textView2);
        return gamefragment;
    }

    @Override
    public void onClick(View view) {
        for(int i = 0 ; i < buttons.length ; i++){
            if(view instanceof Button) {
                if (view.getId() == idList[i]) {
                    Button tempb = (Button)view;
                    String str = tempb.getText().toString();
                    info.setText(str);
                    logik.gætBogstav(str);
                    view.setClickable(false);
                    opdaterSkærm();
                }
            }
        }
        /*String bogstav = write.getText().toString();
        if (bogstav.length() != 1) {
            write.setError("Skriv præcis ét bogstav");
            return;
        }
        logik.gætBogstav(bogstav);
        write.setText("");
        write.setError(null);*/


          //  opdaterSkærm();

    }


    private void opdaterSkærm() {
        info.setText("Gæt ordet: " + logik.getSynligtOrd());
        info.append("\n\nDu har " + logik.getAntalForkerteBogstaver() + " forkerte:" + logik.getBrugteBogstaver());

        if (logik.erSpilletVundet()) {
            info.append("\nDu har vundet");
        }
        if (logik.erSpilletTabt()) {
            info.setText("Du har tabt, ordet var : " + logik.getOrdet());
        }
    }
}