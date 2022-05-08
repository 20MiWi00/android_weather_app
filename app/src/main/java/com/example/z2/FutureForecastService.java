package com.example.z2;

import com.example.z2.forecastFdata.ForecastRepsonse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FutureForecastService {
    @GET("data/2.5/onecall?")
    Call<ForecastRepsonse> getForecastInfo(@Query("lat") double lat, @Query("lon") double lon , @Query("exclude") String excludeList, @Query("units") String units, @Query("lang") String lang, @Query("appid") String API_id);

}
