package com.example.weatherapp;

public class WeatherRVmodel {
    private String time;
    private String temperature;
    private String icon;
    private String windspeed;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public WeatherRVmodel(String time, String temperature, String icon, String windspeed) {
        this.time = time;
        this.temperature = temperature;
        this.icon = icon;
        this.windspeed = windspeed;
    }
}
