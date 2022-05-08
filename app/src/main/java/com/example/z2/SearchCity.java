package com.example.z2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.z2.data.GeoResponse;
import com.example.z2.data.ResponseItem;
import com.example.z2.forecastDdata.WeatherItem;
import com.example.z2.forecastDdata.WeatherResponse;
import com.example.z2.forecastFdata.DailyItem;
import com.example.z2.forecastFdata.ForecastRepsonse;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchCity extends AppCompatActivity {

    private String city;
    private double lon;
    private double lat;
    public static WeatherInfo weatherInfo = new WeatherInfo();

    private Button searchButton;
    public static boolean units = false;

    private static Retrofit retrofit;
    private static String API_id = "8e71e30e98cdb9a9e4f9fb51da114938";
    private static String baseURL = "https://api.openweathermap.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        Intent searchIntent = getIntent();
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = findViewById(R.id.textInput);
                city = textInputLayout.getEditText().getText().toString().trim();
                retrofit = getRetrofit();
                getData();
            }
        });
    }

    private void getData(){
        GeoService service = retrofit.create(GeoService.class);
        Call<List<ResponseItem>> call = service.getGeoInfo(city,1,API_id);
        call.enqueue(new Callback<List<ResponseItem>>() {

            @Override
            public void onResponse(Call<List<ResponseItem>> call, Response<List<ResponseItem>> response) {
                lon = response.body().get(0).getLon();
                lat = response.body().get(0).getLat();
                weatherInfo.setLon(lon);
                weatherInfo.setLat(lat);
                weatherInfo.setCity(response.body().get(0).getName());
                getWeatherData();
            }

            @Override
            public void onFailure(Call<List<ResponseItem>> call, Throwable t) {
                System.out.println("Błąd");
            }
        });
    }

    private void getWeatherData() {
        DailyForecastService service = retrofit.create(DailyForecastService.class);
        if(lat != 0.0 && lon != 0.0)
        {
            Call<WeatherResponse> call = service.getWeatherInfo(lat,lon,"metric",API_id);
            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    List<WeatherItem> weatherItem = response.body().getWeather();
                    weatherInfo.setDescription(weatherItem.get(0).getDescription());
                    weatherInfo.setTemp(response.body().getMain().getTemp());
                    weatherInfo.setPressure(response.body().getMain().getPressure());
                    weatherInfo.setWindSpeed(response.body().getWind().getSpeed());
                    getForecastData();
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    t.printStackTrace();
                    System.out.println("Błąd");
                }
            });
        }
    }

    private void getForecastData(){
        FutureForecastService service = retrofit.create(FutureForecastService.class);
        Call<ForecastRepsonse> call = service.getForecastInfo(lat,lon,"minutely,hourly,alerts,current","metric","pl",API_id);
        call.enqueue(new Callback<ForecastRepsonse>() {
            @Override
            public void onResponse(Call<ForecastRepsonse> call, Response<ForecastRepsonse> response) {
                List<DailyItem> dailyList = response.body().getDaily();
                weatherInfo.setDailyItemList(dailyList);
            }

            @Override
            public void onFailure(Call<ForecastRepsonse> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Błąd");
            }
        });

    }

    private static Retrofit getRetrofit(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG){
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        }else{
            httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor).build();

        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}