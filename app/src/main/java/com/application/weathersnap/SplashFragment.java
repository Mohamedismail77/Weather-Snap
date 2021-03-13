package com.application.weathersnap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.application.weathersnap.databinding.FragmentSplashBinding;
import com.application.weathersnap.utils.ApiError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST = 1122;
    private FragmentSplashBinding splashBinding;

    WeatherViewModel weatherViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        splashBinding = FragmentSplashBinding.inflate(inflater,container,false);
        weatherViewModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        checkLocationPermission();
        observeWeatherSnap();
        return splashBinding.getRoot();
    }



    private void checkLocationPermission(){
        if(isLocationPermissionGranted()) {
            weatherViewModel.getCurrentLocation();
        } else if (isNeverAskAgainChecked()) {
            showLocationMessage();
        } else {
            askForPermission();
        }
    }


    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isNeverAskAgainChecked() {
        return !(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private void showLocationMessage() {
        splashBinding.permissionMessage.setVisibility(View.VISIBLE);
        splashBinding.cityName.setVisibility(View.VISIBLE);
        setupCityNameData();
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST);
    }

    private void setupCityNameData() {

    }

    private void observeWeatherSnap() {
        weatherViewModel.getWeatherSnap().observe(getViewLifecycleOwner(),weatherSnapApiErrorApiResponse -> {
            switch (weatherSnapApiErrorApiResponse.status) {
                case SUCCESS:
                    NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_snapsListFragment);
                    break;
                case ERROR:
                    showErrorMessage(weatherSnapApiErrorApiResponse.error.getMessage());
                    splashBinding.progress.setVisibility(View.INVISIBLE);
                    break;
                case LOADING:
                    splashBinding.progress.setVisibility(View.VISIBLE);
                    break;
                case NETWORK:
                    showErrorMessage(getResources().getString(R.string.network_error));
                    break;
            }
        });
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (isLocationPermissionGranted()) {
                weatherViewModel.getCurrentLocation();
            }
        }
    }


}
