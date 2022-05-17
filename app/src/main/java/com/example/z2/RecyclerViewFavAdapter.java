package com.example.z2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFavAdapter extends RecyclerView.Adapter<RecyclerViewFavAdapter.ViewHolder>{


    private static final String TAG = "RecyclerViewFavAdapter";
    private List<String> favItemList = new ArrayList<>();
    private Context context;
    private View parentView;
    private OnImageClickListener onImageClickListener;
    private final String listFilename = "listInfo";


    public RecyclerViewFavAdapter(Context context, List<String> favItemList, OnImageClickListener onImageClickListener){
        this.favItemList = favItemList;
        this.context = context;
        this.onImageClickListener = onImageClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favlistitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityInfo.setText(favItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return favItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cityInfo;
        Button selectButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityInfo = itemView.findViewById(R.id.favCity);
            selectButton = itemView.findViewById(R.id.selectButton);
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onImageClick(cityInfo.getText().toString());
                }
            });
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchCity.listOfCities.remove(cityInfo.getText());
                    favItemList.remove(cityInfo.getText());
                    SaveList(context);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void SaveList(@NonNull Context context){
        try {
            FileOutputStream fos = context.openFileOutput(listFilename, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(SearchCity.listOfCities);
            os.close();
        }catch(Exception e){
            Toast.makeText(context,"Saving data error",Toast.LENGTH_SHORT).show();
        }
    }
}
