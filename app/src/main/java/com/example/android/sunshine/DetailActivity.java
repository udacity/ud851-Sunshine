package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

// COMPLETED (1) Create a new Activity called DetailActivity using Android Studio's wizard
public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}