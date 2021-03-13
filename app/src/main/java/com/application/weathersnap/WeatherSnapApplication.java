package com.application.weathersnap;

import android.app.Application;

import com.facebook.FacebookSdk;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class WeatherSnapApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
