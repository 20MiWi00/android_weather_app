package com.example.z2;

import com.example.z2.forecastDdata.WeatherItem;
import com.example.z2.forecastDdata.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DailyForecastService {
    @GET("data/2.5/weather?")
    Call<WeatherResponse> getWeatherInfo(@Query("lat") double lat, @Query("lon") double lon, @Query("units") String units , @Query("appid") String API_id);
}
