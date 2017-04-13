package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private String weatherData;
    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // TODO (2) Display the weather forecast that was passed from MainActivity
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data_view);
        Intent intentComingFormMain = getIntent();

        if(intentComingFormMain != null){
            if(intentComingFormMain.hasExtra(Intent.EXTRA_TEXT)){
                weatherData = intentComingFormMain.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherTextView.setText(weatherData);
            }
        }
    }
}