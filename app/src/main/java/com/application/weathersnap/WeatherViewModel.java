package com.application.weathersnap;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.application.weathersnap.data.DataRepository;
import com.application.weathersnap.data.WeatherSnap;
import com.application.weathersnap.utils.ApiError;
import com.application.weathersnap.utils.ApiResponse;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class WeatherViewModel extends ViewModel {

    private DataRepository dataRepository;
    private SavedStateHandle savedStateHandle;

    private MediatorLiveData<ApiResponse<WeatherSnap, ApiError>> weatherSnap = new MediatorLiveData<>();

    @Inject
    public FusedLocationProviderClient fusedLocationProviderClient;
    public LocationRequest locationRequest;
    public SettingsClient clientSettings;
    public LocationSettingsRequest.Builder builder;
    private LocationCallback locationCallback;

    @ViewModelInject
    public WeatherViewModel(DataRepository dataRepository, @Assisted SavedStateHandle savedStateHandle, FusedLocationProviderClient fusedLocationProviderClient, LocationRequest locationRequest, SettingsClient clientSettings, LocationSettingsRequest.Builder builder) {
        this.dataRepository = dataRepository;
        this.savedStateHandle = savedStateHandle;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.locationRequest = locationRequest;
        this.clientSettings = clientSettings;
        this.builder = builder;
        this.locationCallback = locationCallback;
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if(location != null) {
                //get weather data
                getWeatherData(location);
            } else {
                // get location updates
                checkLocationSettings();
            }
        });
    }


    private void checkLocationSettings() {
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder.addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task = clientSettings.checkLocationSettings(builder.build());
        task.addOnSuccessListener(locationSettingsResponse -> getLocationUpdates());
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    //Todo Ask for location setting
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLocationUpdates(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(@NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    //to get weather data
                    getWeatherData(location);
                    break;
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public void getWeatherData(Location location) {
        LiveData<ApiResponse<WeatherSnap,ApiError>> snapSource = LiveDataReactiveStreams.fromPublisher(dataRepository.
                getWeatherStatesByGeoLocation(location.getLatitude(),location.getLongitude()));
        FetchWeatherData(snapSource);
    }

    public void getWeatherForCity(String cityName) {
        LiveData<ApiResponse<WeatherSnap,ApiError>> snapSource = LiveDataReactiveStreams.fromPublisher(dataRepository.getWeatherStatesByCityName(cityName));
        FetchWeatherData(snapSource);

    }

    private void FetchWeatherData(LiveData<ApiResponse<WeatherSnap,ApiError>> snapSource) {
        weatherSnap.setValue(ApiResponse.loading(null));
        weatherSnap.addSource(snapSource,weatherSnapApiErrorApiResponse -> {
            weatherSnap.setValue(weatherSnapApiErrorApiResponse);
            weatherSnap.removeSource(snapSource);
        });
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public LiveData<ApiResponse<WeatherSnap,ApiError>> getWeatherSnap(){
        return weatherSnap;
    }

    public void saveSnap(WeatherSnap weatherSnap) {
        dataRepository.saveSnap(weatherSnap);
    }

    public WeatherSnap getSnap() {
        return dataRepository.getCurrentSnap();
    }
}
