package com.appssquare.mahmoud.myshoppinglist;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by mahmoud on 13/02/2018.
 */

public class App extends Application {
    private final String TAG = "App";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.e(TAG, "attachBaseContext");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
        Log.e(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
}
