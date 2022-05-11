package com.example.z2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.z2.forecastFdata.DailyItem;
import com.example.z2.forecastFdata.WeatherItem;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private List<DailyItem> dailyItemList = new ArrayList<>();
    private Context context;
    private String metrics;

    public RecyclerViewAdapter(Context context, List<DailyItem>dailyItemList, String metrics){
        this.dailyItemList = dailyItemList;
        this.context = context;
        this.metrics = metrics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        position = position + 1;
        if(position == 1){
            holder.dayInfo.setText("Tomorrow");
        }
        else{
            Date date = new Date(dailyItemList.get(position).getDt() * 1000L);
            SimpleDateFormat formatter = new SimpleDateFormat("EEE.");
            holder.dayInfo.setText(formatter.format(date));
        }
        List<WeatherItem> weatherItems = dailyItemList.get(position).getWeather();
        String weatherDescription = weatherItems.get(0).getMain();
        String typeDescription = weatherItems.get(0).getDescription();
        if(weatherDescription.equals("Clouds")){
            if(typeDescription.equals("broken clouds") || typeDescription.equals("overcast clouds"))
                holder.imageView.setImageResource(R.drawable.unclear_sky);
            else
                    holder.imageView.setImageResource(R.drawable.half_clear_sun);
        }
        else if(weatherDescription.equals("Clear"))
                holder.imageView.setImageResource(R.drawable.sun);
        else if(weatherDescription.equals("Thunderstorm"))
            holder.imageView.setImageResource(R.drawable.lightning);
        else if(weatherDescription.equals("Rain"))
            holder.imageView.setImageResource(R.drawable.rain);
        Double temp = dailyItemList.get(position).getTemp().getMax();
        if(metrics.equals("metric"))
            holder.tempInfo.setText(temp.toString().substring(0,2) + "°C");
        else
            holder.tempInfo.setText(temp.toString().substring(0,2) + "°F");
    }

    @Override
    public int getItemCount() {
        return dailyItemList.size() - 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView dayInfo;
        TextView tempInfo;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            dayInfo = itemView.findViewById(R.id.dayInfo);
            tempInfo = itemView.findViewById(R.id.tempInfo);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
