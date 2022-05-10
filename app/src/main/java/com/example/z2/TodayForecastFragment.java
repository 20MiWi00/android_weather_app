package com.example.z2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

public class TodayForecastFragment extends Fragment {

    private WeatherInfo weatherInfo;
    private TextView city;
    private TextView temp;
    private TextView description;
    private ImageView weatherImage;

    public TodayForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_forecast_layout, container, false);
        city = view.findViewById(R.id.city);
        temp = view.findViewById(R.id.temperature);
        description = view.findViewById(R.id.description);
        weatherImage = view.findViewById(R.id.weatherImage);
        try{
            weatherInfo = (WeatherInfo) getArguments().getSerializable("TodayForecastInfoClass");
            city.setText(weatherInfo.getCity());
            Date sunsetDate = new Date(weatherInfo.getSunset() * 1000L);
            Date sunriseDate = new Date(weatherInfo.getSunrise() * 1000L);
            Date nowDate = new Date(System.currentTimeMillis());
            String weatherDescription = weatherInfo.getDescription();
            String typeDescription = weatherInfo.getTypeOfDescription();
            description.setText(weatherDescription);
            if(weatherInfo.getType().equals("metric"))
                temp.setText(weatherInfo.getTemp().toString().substring(0,weatherInfo.getTemp().toString().length() - 1) +"°C");
            else
                temp.setText(weatherInfo.getTemp().toString().substring(0,weatherInfo.getTemp().toString().length() - 1) +"°F");
            if(weatherDescription.equals("Clouds")){
                if(typeDescription.equals("broken clouds") || typeDescription.equals("overcast clouds"))
                    weatherImage.setImageResource(R.drawable.unclear_sky);
                else{
                    if(nowDate.after(sunriseDate) && nowDate.before(sunsetDate))
                        weatherImage.setImageResource(R.drawable.half_clear_sun);
                    else
                        weatherImage.setImageResource(R.drawable.half_clear_moon);
                    description.setText("Few clouds");
                }
            }
            else if(weatherDescription.equals("Clear")){
                if(nowDate.after(sunriseDate) && nowDate.before(sunsetDate))
                    weatherImage.setImageResource(R.drawable.sun);
                else
                    weatherImage.setImageResource(R.drawable.moon);
            }
            else if(weatherDescription.equals("Thunderstorm"))
                weatherImage.setImageResource(R.drawable.lightning);
            else if(weatherDescription.equals("Rain"))
                weatherImage.setImageResource(R.drawable.rain);
        }catch(Exception e){
            System.out.println("Data not ready yet");
        }
        return view;
    }
}