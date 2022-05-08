package com.example.z2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button cityButton;
    private SearchCity city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityButton = findViewById(R.id.search_city);
        cityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchCity = new Intent(MainActivity.this,SearchCity.class);
                startActivity(searchCity);
            }
        });

    }
}