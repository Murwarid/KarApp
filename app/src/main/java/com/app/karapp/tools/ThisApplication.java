package com.app.karapp.tools;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.beardedhen.androidbootstrap.TypefaceProvider;

public class ThisApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        TypefaceProvider.registerDefaultIconSets();
    }
}
