package com.application.weathersnap.data;

import android.os.Build;

import com.application.weathersnap.BaseTest;
import com.application.weathersnap.apiservices.OpenWeatherService;
import com.application.weathersnap.pojos.OpenWeatherResponse;
import com.application.weathersnap.utils.ApiError;
import com.application.weathersnap.utils.ApiResponse;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class DataRepositoryImplTest extends BaseTest {

    @Mock
    private WeatherSnapDao weatherSnapDao;

    @Mock
    private OpenWeatherService openWeatherService;

    private DataRepositoryImpl dataRepository;

    private OpenWeatherResponse openWeatherResponse;
    private double lat = 1.0;
    private double lon = 1.0;
    private String apiKey = "";

    @Before
    public void setup() {
        super.setup();
        dataRepository = new DataRepositoryImpl(openWeatherService,weatherSnapDao);
        try {
            openWeatherResponse = new Gson().fromJson(readFile("weatherResponse.json"),OpenWeatherResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_getWeatherStatesByGeoLocation_updateWeatherSnap() {
        when(openWeatherService.getCurrentWeatherByLatLan(lat,lon,apiKey))
                .thenReturn(Flowable.just(openWeatherResponse));
        TestSubscriber<ApiResponse<WeatherSnap,ApiError>> testObserver = new TestSubscriber<>();

        dataRepository.getWeatherStatesByGeoLocation(lat,lon)
                .subscribeWith(testObserver);
        testObserver.awaitTerminalEvent();

        assert_mappingWeatherResponse();


    }

    @Test
    public void test_getWeatherStatesByCity_updateWeatherSnap() {
        when(openWeatherService.getCurrentWeatherByLatLan(lat,lon,apiKey))
                .thenReturn(Flowable.just(openWeatherResponse));
        dataRepository.getWeatherStatesByGeoLocation(lat,lon);

        TestSubscriber<ApiResponse<WeatherSnap,ApiError>> testObserver = new TestSubscriber<>();
        dataRepository.getWeatherStatesByGeoLocation(lat,lon)
                .subscribeWith(testObserver);

        testObserver.awaitTerminalEvent();


        assert_mappingWeatherResponse();
    }


    private void assert_mappingWeatherResponse() {
        assertNotNull(dataRepository.getCurrentWeatherSnap());
        assertEquals(openWeatherResponse.getName(), dataRepository.getCurrentWeatherSnap().getLocation());
        assertEquals(openWeatherResponse.getWeather().getDescription(),
                dataRepository.getCurrentWeatherSnap().getWeatherConditionDescription());
        assertEquals(openWeatherResponse.getWeather().getIcon(),
                dataRepository.getCurrentWeatherSnap().getWeatherConditionIcon());
        assertEquals(openWeatherResponse.getMain().getTemperture(),
                dataRepository.getCurrentWeatherSnap().getTemperature(), 0);
        assertEquals(openWeatherResponse.getWind().getSpeed(),
                dataRepository.getCurrentWeatherSnap().getWindSpeed(), 0);
    }


}
