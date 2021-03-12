package com.application.weathersnap.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main{

    @SerializedName("temp")
    @Expose
    private double temperture;

    @SerializedName("pressure")
    @Expose
    private int pressure;

    @SerializedName("humidity")
    private double humidity;


    private double getTemperture() {
        return temperture;
    }

    private int getPressure() {
        return pressure;
    }

    private double getHumidity() {
        return humidity;
    }

}

