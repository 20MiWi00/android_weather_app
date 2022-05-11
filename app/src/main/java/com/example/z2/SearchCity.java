package com.example.z2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.z2.data.ResponseItem;
import com.example.z2.forecastDdata.WeatherItem;
import com.example.z2.forecastDdata.WeatherResponse;
import com.example.z2.forecastFdata.DailyItem;
import com.example.z2.forecastFdata.ForecastRepsonse;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchCity extends AppCompatActivity implements OnImageClickListener{

    public static List<String> listOfCities = new ArrayList<>();

    private String city;
    private String units = "metric";
    private double lon;
    private double lat;
    public static WeatherInfo weatherInfo = new WeatherInfo();



    private Button searchButton;
    private Switch temperatureSwitch;

    private final String filename = "weatherInfoClass";
    private static Retrofit retrofit;
    private static String API_id = "8e71e30e98cdb9a9e4f9fb51da114938";
    private static String baseURL = "https://api.openweathermap.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_search_city);
        Intent searchIntent = getIntent();
        RecyclerViewFavAdapter recyclerViewAdapter = new RecyclerViewFavAdapter(SearchCity.this, listOfCities, SearchCity.this);
        RecyclerView recyclerView = findViewById(R.id.recyclerListWindow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchCity.this));
        recyclerView.setAdapter(recyclerViewAdapter);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = findViewById(R.id.textInput);
                city = textInputLayout.getEditText().getText().toString().trim();
                if(isNetworkAvailable() && city.length() > 0){
                    retrofit = getRetrofit(retrofit);
                    getData();
                }
                else{
                    if(!isNetworkAvailable())
                        Toast.makeText(SearchCity.this,"Brak połączenia z internetem",Toast.LENGTH_SHORT).show();
                    if(city.length() == 0)
                        Toast.makeText(SearchCity.this,"Nie podano miasta",Toast.LENGTH_SHORT).show();

                }
            }
        });
        temperatureSwitch = findViewById(R.id.temperatureSwitch);
        temperatureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(temperatureSwitch.isChecked()){
                    units = "imperial";
                }
                else{
                    units = "metirc";
                }
            }
        });
    }

    public void getData(){
        GeoService service = retrofit.create(GeoService.class);
        Call<List<ResponseItem>> call = service.getGeoInfo(city,1,API_id);
        call.enqueue(new Callback<List<ResponseItem>>() {

            @Override
            public void onResponse(Call<List<ResponseItem>> call, Response<List<ResponseItem>> response) {
                if(response.body().size() == 0){
                    Toast.makeText(SearchCity.this,"Podano błędne miasto",Toast.LENGTH_SHORT).show();
                    return;
                }
                lon = response.body().get(0).getLon();
                lat = response.body().get(0).getLat();
                weatherInfo.setLon(lon);
                weatherInfo.setLat(lat);
                weatherInfo.setType(units);
                weatherInfo.setCity(response.body().get(0).getName());
                getWeatherData();
            }

            @Override
            public void onFailure(Call<List<ResponseItem>> call, Throwable t) {
                Toast.makeText(SearchCity.this,"Błąd pobierania danych",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getWeatherData() {
        DailyForecastService service = retrofit.create(DailyForecastService.class);
        if(lat != 0.0 && lon != 0.0)
        {
            Call<WeatherResponse> call = service.getWeatherInfo(lat,lon,units,API_id);
            call.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    List<WeatherItem> weatherItem = response.body().getWeather();
                    weatherInfo.setDescription(weatherItem.get(0).getMain());
                    weatherInfo.setTypeOfDescription(weatherItem.get(0).getDescription());
                    weatherInfo.setTemp(response.body().getMain().getTemp());
                    weatherInfo.setPressure(response.body().getMain().getPressure());
                    weatherInfo.setWindSpeed(response.body().getWind().getSpeed());
                    weatherInfo.setSunset(response.body().getSys().getSunset());
                    weatherInfo.setSunrise(response.body().getSys().getSunrise());
                    getForecastData();
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Toast.makeText(SearchCity.this,"Błąd pobierania danych",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getForecastData(){
        FutureForecastService service = retrofit.create(FutureForecastService.class);
        Call<ForecastRepsonse> call = service.getForecastInfo(lat,lon,"minutely,hourly,alerts,current",units,"pl",API_id);
        call.enqueue(new Callback<ForecastRepsonse>() {
            @Override
            public void onResponse(Call<ForecastRepsonse> call, Response<ForecastRepsonse> response) {
                List<DailyItem> dailyList = response.body().getDaily();
                weatherInfo.setDailyItemList(dailyList);
                weatherInfo.setTimeOfCreation(System.currentTimeMillis());
                SaveClass(SearchCity.this);
                Intent mainIntent = new Intent();
                mainIntent.putExtra("WeatherInfoClass",weatherInfo);
                setResult(Activity.RESULT_OK,mainIntent);
                finish();
            }

            @Override
            public void onFailure(Call<ForecastRepsonse> call, Throwable t) {
                Toast.makeText(SearchCity.this,"Błąd pobierania danych",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static Retrofit getRetrofit(Retrofit retrofit){
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

    public void SaveClass(@NonNull Context context){
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(weatherInfo);
            os.close();
        }catch (Exception e){
            Toast.makeText(context,"Błąd zapisu pliku",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean returnValue =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return returnValue;
    }

    @Override
    public void onImageClick(String city) {
        TextInputLayout inputLayout = findViewById(R.id.textInput);
        inputLayout.getEditText().setText(city);
    }

}