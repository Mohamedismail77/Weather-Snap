package com.application.weathersnap.data;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface WeatherSnapDao {

    @Query("SELECT * FROM snaps ORDER BY id DESC")
    DataSource.Factory<Integer, WeatherSnap> weatherSnaps();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(WeatherSnap snap);

    @Delete
    Completable delete(WeatherSnap snap);

}
