/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = findViewById(R.id.tv_weather_data);

        // COMPLETE (4) Delete the dummy weather data. You will be getting REAL data from the Internet in this lesson.
        // COMPLETE (3) Delete the for loop that populates the TextView with dummy data

        // COMPLETE (9) Call loadWeatherData to perform the network request to get the weather
        loadWeatherData();
    }

    private void loadWeatherData() {
        final String preferredWeatherLocation = SunshinePreferences.getPreferredWeatherLocation(this);
        new WeatherTask().execute(preferredWeatherLocation);
    }

    // COMPLETE (8) Create a method that will get the user's preferred location and execute your new AsyncTask and call it
    // loadWeatherData


    // COMPLETE (5) Create a class that extends AsyncTask to perform network requests
    // COMPLETE (6) Override the doInBackground method to perform your network requests
    // COMPLETE (7) Override the onPostExecute method to display the results of the network request

    private class WeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings == null || strings.length < 1) {
                return null;
            }

            final String location = strings[0];
            final URL url = NetworkUtils.buildUrl(location);

            try {
                final String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
                return OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, responseFromHttpUrl);
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Uh oh.", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] stringArray) {
            final StringBuilder sb = new StringBuilder();

            for (String s1 : stringArray) {
                sb.append(s1).append("\n\n");
            }

            mWeatherTextView.setText(sb.toString());
        }
    }
}