package com.example.z2;

import com.example.z2.data.GeoResponse;
import com.example.z2.data.ResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoService {
    @GET("geo/1.0/direct?")
    Call<List<ResponseItem>> getGeoInfo(@Query("q") String city, @Query("limit") int limit, @Query("appid") String API_id);
}
