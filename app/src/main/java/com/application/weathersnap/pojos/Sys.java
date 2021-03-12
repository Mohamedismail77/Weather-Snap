package com.application.weathersnap.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("country")
    @Expose
    private String country;

    public String getCountry() {
        return country;
    }
}
