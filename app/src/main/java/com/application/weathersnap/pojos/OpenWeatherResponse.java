package com.application.weathersnap.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenWeatherResponse {

    @SerializedName("weather")
    @Expose
    private List<Weather> weather;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("sys")
    @Expose
    private Sys sys;

    @SerializedName("name")
    @Expose
    private String name;

    private List<Weather> getWeather() {
        return weather;
    }

    private Main getMain() {
        return main;
    }

    private Wind getWind() {
        return wind;
    }

    private Sys getSys() {
        return sys;
    }

    private String getName() {
        return name;
    }

}
