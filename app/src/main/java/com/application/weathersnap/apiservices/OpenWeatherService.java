package com.application.weathersnap.apiservices;

import com.application.weathersnap.pojos.OpenWeatherResponse;
import com.application.weathersnap.utils.ApiRoutes;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @Headers({"Accept: application/json"})
    @GET(ApiRoutes.CURRENT_WEATHER)
    Flowable<OpenWeatherResponse> getCurrentWeatherByCityName(@Query(ApiRoutes.CITY_QUERY) String cityName,
                                                              @Query(ApiRoutes.UNITES) String units,
                                                              @Query(ApiRoutes.APP_ID) String apiKey);


    @Headers({"Accept: application/json"})
    @GET(ApiRoutes.CURRENT_WEATHER)
    Flowable<OpenWeatherResponse> getCurrentWeatherByLatLan(@Query(ApiRoutes.LAT_QUERY) double latitude,
                                                              @Query(ApiRoutes.LON_QUERT) double longitude,
                                                              @Query(ApiRoutes.UNITES) String units,
                                                              @Query(ApiRoutes.APP_ID) String apiKey);

}
