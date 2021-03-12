package com.application.weathersnap.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind{

    @SerializedName("speed")
    @Expose
    public double speed;

    @SerializedName("deg")
    @Expose
    public int deg;


    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

}
