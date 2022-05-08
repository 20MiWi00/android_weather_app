package com.example.z2;

import com.example.z2.forecastFdata.DailyItem;

import java.util.List;

public class WeatherInfo {

    private  String city;
    private  Double lon;
    private  Double lat;

    //Pierwszy fragment
    private  Double temp;
    private  String description;

    //Drugi fragment
    private  Integer pressure;
    private  Double windSpeed;

    //Trzeci fragment
    private List<DailyItem> dailyItemList;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setDailyItemList(List<DailyItem> dailyItemList) { this.dailyItemList = dailyItemList; }

    public List<DailyItem> getDailyItemList() { return dailyItemList; }

}
