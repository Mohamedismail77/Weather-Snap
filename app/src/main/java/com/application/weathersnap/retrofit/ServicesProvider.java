package com.application.weathersnap.retrofit;


import com.application.weathersnap.apiservices.OpenWeatherService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@InstallIn(ApplicationComponent.class)
@Module
public class ServicesProvider {

    @Provides
    @Singleton
    public OpenWeatherService providesOpenWeatherService(ServiceGenerator serviceGenerator){
        return serviceGenerator.createService(OpenWeatherService.class);
    }

}
