package com.teamtreehouse.stormy;

public class WeeklyWeather {
    //5 days
    private CurrentWeather currentWeatherSecondDay = new CurrentWeather();
    private CurrentWeather currentWeatherThirdDay = new CurrentWeather();
    private CurrentWeather currentWeatherFourthDay = new CurrentWeather();
    private CurrentWeather currentWeatherFifthDay = new CurrentWeather();

    public WeeklyWeather() {
    }

    public WeeklyWeather(CurrentWeather currentWeatherSecondDay, CurrentWeather currentWeatherThirdDay,
                         CurrentWeather currentWeatherFourthDay, CurrentWeather currentWeatherFifthDay) {
        this.currentWeatherSecondDay = currentWeatherSecondDay;
        this.currentWeatherThirdDay = currentWeatherThirdDay;
        this.currentWeatherFourthDay = currentWeatherFourthDay;
        this.currentWeatherFifthDay = currentWeatherFifthDay;
    }

    /*-----Getters and setters------*/

    public CurrentWeather getCurrentWeatherSecondDay() {
        return currentWeatherSecondDay;
    }

    public void setCurrentWeatherSecondDay(CurrentWeather currentWeatherSecondDay) {
        this.currentWeatherSecondDay = currentWeatherSecondDay;
    }

    public CurrentWeather getCurrentWeatherThirdDay() {
        return currentWeatherThirdDay;
    }

    public void setCurrentWeatherThirdDay(CurrentWeather currentWeatherThirdDay) {
        this.currentWeatherThirdDay = currentWeatherThirdDay;
    }

    public CurrentWeather getCurrentWeatherFourthDay() {
        return currentWeatherFourthDay;
    }

    public void setCurrentWeatherFourthDay(CurrentWeather currentWeatherFourthDay) {
        this.currentWeatherFourthDay = currentWeatherFourthDay;
    }

    public CurrentWeather getCurrentWeatherFifthDay() {
        return currentWeatherFifthDay;
    }

    public void setCurrentWeatherFifthDay(CurrentWeather currentWeatherFifthDay) {
        this.currentWeatherFifthDay = currentWeatherFifthDay;
    }
}
