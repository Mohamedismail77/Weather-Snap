package com.application.weathersnap.di;

import android.content.Context;

import androidx.room.Room;

import com.application.weathersnap.apiservices.OpenWeatherService;
import com.application.weathersnap.data.DataRepository;
import com.application.weathersnap.data.DataRepositoryImpl;
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
    public WeatherSnapDao providesWeatherSnapDao(WeatherSnapDatabase database) {
        return database.weatherSnapDao();
    }

    @Provides
    @Singleton
    public DataRepository providesDataRepository(OpenWeatherService openWeatherService, WeatherSnapDao weatherSnapDao){
        return new DataRepositoryImpl(openWeatherService,weatherSnapDao);
    }

}
