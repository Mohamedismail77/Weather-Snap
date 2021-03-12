package com.application.weathersnap.data;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.paging.DataSource;

import com.application.weathersnap.apiservices.OpenWeatherService;
import com.application.weathersnap.pojos.OpenWeatherResponse;
import com.application.weathersnap.utils.ApiError;
import com.application.weathersnap.utils.ApiResponse;
import com.application.weathersnap.utils.ErrorUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

@Singleton

public class DataRepository {

    private final OpenWeatherService openWeatherService;
    private final WeatherSnapDao  weatherSnapDao;
    private final WeatherSnap currentWeatherSnap = new WeatherSnap();
    private final String appID = "";

    @Inject
    public DataRepository(OpenWeatherService openWeatherService, WeatherSnapDao weatherSnapDao) {
        this.openWeatherService = openWeatherService;
        this.weatherSnapDao = weatherSnapDao;
    }

    public LiveData<ApiResponse<WeatherSnap, ApiError>> getWeatherStatesByGeoLocation(double lat, double lon) {
        return LiveDataReactiveStreams.fromPublisher(
                openWeatherService.getCurrentWeatherByLatLan(lat,lon, appID)
                        .subscribeOn(Schedulers.io())
                        .map((Function<OpenWeatherResponse,ApiResponse<WeatherSnap,ApiError>>) response -> {
                            mappingWeatherResponse(response);
                            return ApiResponse.success(currentWeatherSnap);
                        }).onErrorReturn(DataRepository::errorHandeler)
                );
    }

    public LiveData<ApiResponse<WeatherSnap, ApiError>> getWeatherStatesByCityName(String city) {
        return LiveDataReactiveStreams.fromPublisher(
                openWeatherService.getCurrentWeatherByCityName(city, appID)
                        .subscribeOn(Schedulers.io())
                        .map((Function<OpenWeatherResponse,ApiResponse<WeatherSnap,ApiError>>) response -> {
                            mappingWeatherResponse(response);
                            return ApiResponse.success(currentWeatherSnap);
                        }).onErrorReturn(DataRepository::errorHandeler)
        );
    }




    private void mappingWeatherResponse(OpenWeatherResponse response) {
        currentWeatherSnap.setLocation(response.getName());
        currentWeatherSnap.setWeatherConditionDescription(response.getWeather().getDescription());
        currentWeatherSnap.setWeatherConditionIcon(response.getWeather().getIcon());
        currentWeatherSnap.setTemperature(response.getMain().getTemperture());
        currentWeatherSnap.setWindSpeed(response.getWind().getSpeed());
    }

    private  static <T> ApiResponse<T,ApiError> errorHandeler(Throwable throwable) {
        if (throwable instanceof HttpException) {
            Response response = ((HttpException) throwable).response();
            if (response != null) {
                return ApiResponse.error(response.message(), ErrorUtils.parseError(response));
            } else {
                return ApiResponse.network();
            }
        }
        return ApiResponse.network();
    }

    public DataSource.Factory<Integer,WeatherSnap> getSnapsList() {
        return weatherSnapDao.weatherSnaps();
    }

    public void saveSnap(WeatherSnap weatherSnap){
        weatherSnapDao
                .insert(weatherSnap)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteSnap(WeatherSnap weatherSnap) {
        weatherSnapDao
                .delete(weatherSnap)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
