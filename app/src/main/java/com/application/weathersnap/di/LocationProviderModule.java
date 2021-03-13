package com.application.weathersnap.di;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@InstallIn(ApplicationComponent.class)
@Module
public class LocationProviderModule {


    @Provides
    @Singleton
    public FusedLocationProviderClient fusedLocationProviderClient(@ApplicationContext Context context){
        return LocationServices.getFusedLocationProviderClient(context);
    }

    @Provides
    @Singleton
    public LocationRequest provideLocationRequest(){
        return LocationRequest.create();
    }

    @Provides
    @Singleton
    public SettingsClient providesSettingsClient(@ApplicationContext Context context){
        return LocationServices.getSettingsClient(context);
    }

    @Provides
    @Singleton
    public LocationSettingsRequest.Builder providesLocationSettingRequestBuilder(){
        return new LocationSettingsRequest.Builder();
    }

}
