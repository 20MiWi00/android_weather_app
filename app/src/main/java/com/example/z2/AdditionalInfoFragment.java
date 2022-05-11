package com.example.z2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class AdditionalInfoFragment extends Fragment {

    private WeatherInfo weatherInfo;
    private TextView pressure;
    private TextView windSpeed;

    public AdditionalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additional_info_layout, container, false);
        pressure = view.findViewById(R.id.pressureInfo);
        windSpeed = view.findViewById(R.id.windSpeedInfo);
        try{
            weatherInfo = (WeatherInfo) getArguments().getSerializable("TodayForecastInfoClass");
            pressure.setText("Pressure: " + weatherInfo.getPressure().toString() + " hPa");
            windSpeed.setText("Wind: " + weatherInfo.getWindSpeed().toString() + " m/s");
        }catch(Exception e){
            System.out.println("Data not ready yet");
        }
        // Inflate the layout for this fragment
        return view;
    }
}