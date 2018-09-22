package com.teamtreehouse.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String locationLabel;
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timezone;

    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, long time, double temperature, double humidity,
                          double precipChance, String summary, String timezone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timezone = timezone;
    }

    /*---Getters and setters-----*/

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public int getIconID(){
        int iconId = R.drawable.clear_day;

        switch(this.icon){
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;
        }

        return iconId;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));

        Date dateTime = new Date(time*1000);

        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String icon) {
        switch(icon) {
            case "rain":
                this.summary = "Chuvoso";
                break;
            case "snow":
                this.summary = "A nevar";
                break;
            case "sleet":
                this.summary = "Aguaneve";
                break;
            case "wind":
                this.summary = "Ventoso";
                break;
            case "fog":
                this.summary = "Nevoeiro";
                break;
            case "cloudy":
                this.summary = "Nebulado";
                break;
            case "partly-cloudy-day":
                this.summary = "Parcialmente limpo";
                break;
            case "partly-cloudy-night":
                this.summary = "Parcialmente limpo";
                break;
            default:
                this.summary = "Limpo";
                break;
        }
    }
}
