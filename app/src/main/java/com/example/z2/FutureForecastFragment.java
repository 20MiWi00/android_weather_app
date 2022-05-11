package com.example.z2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FutureForecastFragment extends Fragment {

    private WeatherInfo weatherInfo;

    public FutureForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_future_forecast_layout, container, false);
        try{
            weatherInfo = (WeatherInfo) getArguments().getSerializable("TodayForecastInfoClass");
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(view.getContext(), weatherInfo.getDailyItemList(),weatherInfo.getType());
            RecyclerView recyclerView = view.findViewById(R.id.recyclerWindow);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }catch(Exception e){
            System.out.println("Data not ready yet");
        }
        return view;
    }
}