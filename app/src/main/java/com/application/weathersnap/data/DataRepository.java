package com.application.weathersnap.data;

import androidx.paging.DataSource;

import com.application.weathersnap.pojos.OpenWeatherResponse;
import com.application.weathersnap.utils.ApiError;
import com.application.weathersnap.utils.ApiResponse;
import com.application.weathersnap.utils.ErrorUtils;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

public interface DataRepository {

    Flowable<ApiResponse<WeatherSnap, ApiError>> getWeatherStatesByGeoLocation(double lat, double lon);

    Flowable<ApiResponse<WeatherSnap, ApiError>> getWeatherStatesByCityName(String city);

    DataSource.Factory<Integer,WeatherSnap> getSnapsList();

    void saveSnap(WeatherSnap weatherSnap);

    void deleteSnap(WeatherSnap weatherSnap);

    WeatherSnap getCurrentSnap();
}
