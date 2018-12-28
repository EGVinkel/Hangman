package com.vinkel.emil.the_hangmans_game;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MediaPlayer sound;
    private DrawerLayout drawer;
    private NavController navC;
    private int backcounter=1;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TheGameState.prefs=PreferenceManager.getDefaultSharedPreferences(this);
        navC= Navigation.findNavController(findViewById(R.id.nav_host));
        //Sammensætning af drawer og Navigation
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavigationUI.setupActionBarWithNavController(this,navC,drawer);
        navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navC);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (TheGameState.prefs.getBoolean("pref_music", true)) {
            startTheme();
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        List<Fragment> fragmentliste = getSupportFragmentManager().findFragmentById(R.id.nav_host).getChildFragmentManager().getFragments();
        if(!fragmentliste.get(fragmentliste.size()-1).getClass().equals(HovedmenuFragment.class)){
            navC.navigateUp();
            backcounter=1;
            return;
        }
        //Tjek hvis hovedmenufragmentet er det current, hvis det er så gives der en toast on back pressed, så brugeren ikke bare bliver kastet ud af appen.

        if(fragmentliste.get(fragmentliste.size()-1).getClass().equals(HovedmenuFragment.class)&&backcounter==1){
            Toast.makeText(this, "Pressing back again will exit the game", Toast.LENGTH_LONG).show();
            backcounter=backcounter+1;
            return;
        }
        if(backcounter>=2) {
            if (sound != null && sound.isPlaying()) {
                sound.stop();

            }
            finish();
        }



    }

    public void startTheme() {

        sound = MediaPlayer.create(this, R.raw.maintheme);
        sound.start();
        sound.setLooping(true);

    }

    public void stopTheme() {
        sound.stop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(navC.getGraph().getStartDestination()==(R.id.gameFragment)){
            navC.popBackStack(R.id.gameFragment, false);
            navC.navigate(item.getItemId());
        }
        if(navC.getGraph().getStartDestination()==(R.id.hovedmenuFragment)){
            navC.popBackStack(R.id.hovedmenuFragment, false);
            navC.navigate(item.getItemId());
        }


        onBackPressed();
        return false;
    }

}
