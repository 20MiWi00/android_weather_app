package com.example.z2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z2.forecastDdata.Sys;
import com.example.z2.forecastDdata.WeatherItem;
import com.example.z2.forecastDdata.WeatherResponse;
import com.example.z2.forecastFdata.DailyItem;
import com.example.z2.forecastFdata.ForecastRepsonse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private WeatherInfo weatherInfo;

    private final int LAUNCH_SECOND_ACTIVITY = 1;
    private static String API_id = "8e71e30e98cdb9a9e4f9fb51da114938";
    private static Retrofit retrofit;

    private TodayForecastFragment firstFragment;
    private AdditionalInfoFragment secondFragment;
    private FutureForecastFragment thirdFragment;
    private TextView alertView;
    private static boolean init_update = false;

    private final String filename = "weatherInfoClass";
    private final String listFilename = "listInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(init_update == false)
            load(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.editCityItem:
                if(isNetworkAvailable()){
                    Intent searchCity = new Intent(MainActivity.this,SearchCity.class);
                    startActivityForResult(searchCity,LAUNCH_SECOND_ACTIVITY);
                }
                else{
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.addToFavItem:
                if(weatherInfo!=null){
                    if(!SearchCity.listOfCities.contains(weatherInfo.getCity())) {
                        SearchCity.listOfCities.add(weatherInfo.getCity());
                        Toast.makeText(MainActivity.this, "City " + weatherInfo.getCity() + " has been added", Toast.LENGTH_SHORT).show();
                        SaveList(this);
                    }
                    else
                        Toast.makeText(MainActivity.this,"Favorite list contains this city",Toast.LENGTH_SHORT).show();

                }
                return true;
            case R.id.refreshWeatherItem:
                if(isNetworkAvailable()){
                    retrofit = SearchCity.getRetrofit(retrofit);
                    Refresh();
                }
                else{
                    Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void load(Context context){
        try {
            weatherInfo = loadClass(context);
            loadFile(context);
            if(checkDate() && isNetworkAvailable()){
                retrofit = SearchCity.getRetrofit(retrofit);
                Refresh();
                Toast.makeText(context,"Old data has been refreshed",Toast.LENGTH_SHORT).show();
                init_update = true;
            }
            else{
                InitFragments();
                Toast.makeText(context,"Data file loaded",Toast.LENGTH_SHORT).show();
                if(!isNetworkAvailable()){
                    alertView = findViewById(R.id.alertView);
                    alertView.setText("No internet connection");
                }
                init_update = true;
            }
        }catch(Exception e){
            Toast.makeText(context,"No data file",Toast.LENGTH_SHORT).show();
        }
    }

    private WeatherInfo loadClass(Context context){
        try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream is = new ObjectInputStream(fis);
            WeatherInfo weatherInfo = (WeatherInfo) is.readObject();
            is.close();
            fis.close();
            return weatherInfo;
        }catch(Exception e){
            Toast.makeText(context,"No data file",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void loadFile(Context context){
        try{
            FileInputStream fis = context.openFileInput(listFilename);
            ObjectInputStream is = new ObjectInputStream(fis);
            SearchCity.listOfCities = (List<String>) is.readObject();
            is.close();
            fis.close();
        }catch(Exception e){
            Toast.makeText(context,"No data file",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean returnValue =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        alertView = findViewById(R.id.alertView);
        if(!returnValue)
            alertView.setText("No internet connection");
        else
            alertView.setText("");
        return returnValue;
    }

    private void InitFragments(){
        Bundle todayForecastBundle = new Bundle();
        todayForecastBundle.putSerializable("TodayForecastInfoClass",weatherInfo);
        firstFragment = new TodayForecastFragment();
        firstFragment.setArguments(todayForecastBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.firstFragmentView,firstFragment).commit();
        secondFragment = new AdditionalInfoFragment();
        secondFragment.setArguments(todayForecastBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.secondFragmentView,secondFragment).commit();
        thirdFragment = new FutureForecastFragment();
        thirdFragment.setArguments(todayForecastBundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.thirdFragmentView,thirdFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                weatherInfo = (WeatherInfo) data.getSerializableExtra("WeatherInfoClass");
                InitFragments();
            }
        }
    }

    private boolean checkDate(){
        SimpleDateFormat dayFormatter = new SimpleDateFormat("DDD");
        SimpleDateFormat hourFormatter = new SimpleDateFormat("kk");
        Date creationDate = new Date(weatherInfo.getTimeOfCreation());
        Date newDate = new Date(System.currentTimeMillis());
        int oldDay = Integer.parseInt(dayFormatter.format(creationDate));
        int newDay = Integer.parseInt(dayFormatter.format(newDate));
        if(oldDay - newDay == 0) {
            int oldHour = Integer.parseInt(hourFormatter.format(creationDate));
            int newHour = Integer.parseInt(hourFormatter.format(newDate));
            if(Math.abs(oldHour - newHour) > 2)
                return true;
            else
                return false;
        }
        else
            return true;
    }

    private void Refresh(){
        getWeatherData();
        SaveClass(MainActivity.this);
    }

    public void getWeatherData() {
        DailyForecastService service = retrofit.create(DailyForecastService.class);
        if(weatherInfo.getLat() != 0.0 && weatherInfo.getLon() != 0.0)
        {
            Call<WeatherResponse> call = service.getWeatherInfo(weatherInfo.getLat(),weatherInfo.getLon(),weatherInfo.getType(),API_id);
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
                    Toast.makeText(MainActivity.this,"Downloading data error",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getForecastData(){
        FutureForecastService service = retrofit.create(FutureForecastService.class);
        Call<ForecastRepsonse> call = service.getForecastInfo(weatherInfo.getLat(),weatherInfo.getLon(),"minutely,hourly,alerts,current",weatherInfo.getType(),"pl",API_id);
        call.enqueue(new Callback<ForecastRepsonse>() {
            @Override
            public void onResponse(Call<ForecastRepsonse> call, Response<ForecastRepsonse> response) {
                List<DailyItem> dailyList = response.body().getDaily();
                weatherInfo.setDailyItemList(dailyList);
                InitFragments();
            }

            @Override
            public void onFailure(Call<ForecastRepsonse> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Downloading data error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SaveClass(@NonNull Context context){
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(weatherInfo);
            os.close();
        }catch (Exception e){
            Toast.makeText(context,"Data saving error",Toast.LENGTH_SHORT).show();
        }
    }

    public void SaveList(@NonNull Context context){
        try {
            FileOutputStream fos = context.openFileOutput(listFilename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(SearchCity.listOfCities);
            os.close();
        }catch(Exception e){
            Toast.makeText(context,"Data saving error",Toast.LENGTH_SHORT).show();
        }
    }
}