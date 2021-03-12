package com.application.weathersnap.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = DataWeatherSnapName.TABLE_NAME)
public class WeatherSnap {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = DataWeatherSnapName.COL_LOCATION)
    private String location;

    @ColumnInfo(name = DataWeatherSnapName.COL_TEMP)
    private double temperature;

    @ColumnInfo(name = DataWeatherSnapName.COL_CONDITION_DESC)
    private String weatherConditionDescription;

    @ColumnInfo(name = DataWeatherSnapName.COL_CONDITION_ICON)
    private String weatherConditionIcon;

    @ColumnInfo(name = DataWeatherSnapName.COL_WIND_SPEED)
    private double windSpeed;

    @ColumnInfo(name = DataWeatherSnapName.COL_IMAGE_URI)
    private String imageUri;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherConditionDescription() {
        return weatherConditionDescription;
    }

    public void setWeatherConditionDescription(String weatherConditionDescription) {
        this.weatherConditionDescription = weatherConditionDescription;
    }

    public String getWeatherConditionIcon() {
        return weatherConditionIcon;
    }

    public void setWeatherConditionIcon(String weatherConditionIcon) {
        this.weatherConditionIcon = weatherConditionIcon;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
