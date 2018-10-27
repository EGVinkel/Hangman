package com.vinkel.emil.the_hangmans_game.com.vinkel.emil.the_hangmans_game.playerdata;

import android.app.Application;
import android.content.SharedPreferences;

public class Sharedp extends Application {
    public static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

    }
}
