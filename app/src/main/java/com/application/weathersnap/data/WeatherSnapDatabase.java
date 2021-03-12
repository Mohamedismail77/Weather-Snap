package com.application.weathersnap.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import javax.inject.Singleton;

@Database(entities = {WeatherSnap.class}, version = 1)
@Singleton
public abstract class WeatherSnapDatabase extends RoomDatabase {

    public abstract WeatherSnapDao weatherSnapDao();

}
