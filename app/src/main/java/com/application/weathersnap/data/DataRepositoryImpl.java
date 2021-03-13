package com.application.weathersnap.data;

import androidx.paging.DataSource;
import com.application.weathersnap.apiservices.OpenWeatherService;
import com.application.weathersnap.pojos.OpenWeatherResponse;
import com.application.weathersnap.utils.ApiError;
import com.application.weathersnap.utils.ApiResponse;
import com.application.weathersnap.utils.ErrorUtils;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;


public class DataRepositoryImpl implements DataRepository{

    private final OpenWeatherService openWeatherService;
    private final WeatherSnapDao  weatherSnapDao;
    private final WeatherSnap currentWeatherSnap = new WeatherSnap();
    private String appID = "";

    public DataRepositoryImpl(OpenWeatherService openWeatherService, WeatherSnapDao weatherSnapDao) {
        this.openWeatherService = openWeatherService;
        this.weatherSnapDao = weatherSnapDao;
    }

    @Override
    public Flowable<ApiResponse<WeatherSnap, ApiError>> getWeatherStatesByGeoLocation(double lat, double lon) {
        return openWeatherService.getCurrentWeatherByLatLan(lat,lon, appID)
                        .subscribeOn(Schedulers.io())
                        .map((Function<OpenWeatherResponse,ApiResponse<WeatherSnap,ApiError>>) this::mappingWeatherResponse)
                        .onErrorReturn(DataRepositoryImpl::errorHandeler);
    }

    @Override
    public Flowable<ApiResponse<WeatherSnap, ApiError>> getWeatherStatesByCityName(String city) {
        return openWeatherService.getCurrentWeatherByCityName(city, appID)
                .subscribeOn(Schedulers.io())
                .map((Function<OpenWeatherResponse,ApiResponse<WeatherSnap,ApiError>>) this::mappingWeatherResponse)
                .onErrorReturn(DataRepositoryImpl::errorHandeler);
    }

    @Override
    public DataSource.Factory<Integer,WeatherSnap> getSnapsList() {
        return weatherSnapDao.weatherSnaps();
    }

    @Override
    public void saveSnap(WeatherSnap weatherSnap){
        weatherSnapDao
                .insert(weatherSnap)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void deleteSnap(WeatherSnap weatherSnap) {
        weatherSnapDao
                .delete(weatherSnap)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private ApiResponse<WeatherSnap, ApiError> mappingWeatherResponse(OpenWeatherResponse response) {
        currentWeatherSnap.setLocation(response.getName());
        currentWeatherSnap.setWeatherConditionDescription(response.getWeather().getDescription());
        currentWeatherSnap.setWeatherConditionIcon(response.getWeather().getIcon());
        currentWeatherSnap.setTemperature(response.getMain().getTemperture());
        currentWeatherSnap.setWindSpeed(response.getWind().getSpeed());
        return ApiResponse.success(currentWeatherSnap);
    }

    private static <T> ApiResponse<T,ApiError> errorHandeler(Throwable throwable) {
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

    //For testing purpose
    protected WeatherSnap getCurrentWeatherSnap() {
        return currentWeatherSnap;
    }

}
