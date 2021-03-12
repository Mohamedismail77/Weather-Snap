package com.application.weathersnap.di;

import android.content.Context;

import androidx.room.Room;

import com.application.weathersnap.data.WeatherSnapDao;
import com.application.weathersnap.data.WeatherSnapDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@InstallIn(ApplicationComponent.class)
@Module
public class WeatherSnapModule {

    @Provides
    @Singleton
    public WeatherSnapDatabase provideWeatherSnapDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context,WeatherSnapDatabase.class,"weather-snap").build();
    }

    @Provides
    @Singleton
    public WeatherSnapDao prividesWeatherSnapDao(WeatherSnapDatabase database) {
        return database.weatherSnapDao();
    }

}
