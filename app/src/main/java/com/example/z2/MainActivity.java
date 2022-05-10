package com.example.z2;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity {

    private Button cityButton;
    private SearchCity city;
    private WeatherInfo weatherInfo;
    private int LAUNCH_SECOND_ACTIVITY = 1;
    private TodayForecastFragment firstFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityButton = findViewById(R.id.search_city);
        cityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchCity = new Intent(MainActivity.this,SearchCity.class);
                startActivityForResult(searchCity,LAUNCH_SECOND_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                weatherInfo = (WeatherInfo) data.getSerializableExtra("WeatherInfoClass");
                Bundle todayForecastBundle = new Bundle();
                todayForecastBundle.putSerializable("TodayForecastInfoClass",weatherInfo);
                firstFragment = new TodayForecastFragment();
                firstFragment.setArguments(todayForecastBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.firstFragmentView,firstFragment).commit();

            }
            else {
                Toast.makeText(MainActivity.this,"Błąd pobierania danych",Toast.LENGTH_SHORT).show();
            }
        }
    }
}