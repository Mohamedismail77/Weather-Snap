package com.application.weathersnap.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main{

    @SerializedName("temp")
    @Expose
    private double temperature;

    @SerializedName("pressure")
    @Expose
    private int pressure;

    @SerializedName("humidity")
    private double humidity;


    public double getTemperture() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

}

