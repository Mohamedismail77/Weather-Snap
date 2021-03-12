package com.application.weathersnap.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenWeatherResponse {

    @SerializedName("weather")
    @Expose
    private List<Weather> weatherList;

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

    public Weather getWeather() {
        if (weatherList != null) {
            return weatherList.get(0);
        }
        return new Weather();
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Sys getSys() {
        return sys;
    }

    public String getName() {
        return name + ", " + sys.getCountry();
    }



}
