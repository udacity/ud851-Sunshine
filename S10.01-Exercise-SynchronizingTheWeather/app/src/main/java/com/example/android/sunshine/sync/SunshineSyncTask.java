package com.example.android.sunshine.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

//  COMPLETED (1) Create a class called SunshineSyncTask
public final class SunshineSyncTask {
    private static ContentValues[] weatherValues;
//  COMPLETED (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    synchronized public static void syncWeather(Context context) throws IOException, JSONException {
//      COMPLETED (3) Within syncWeather, fetch new weather data
        URL url  = NetworkUtils.getUrl(context);
        String weatherData = NetworkUtils.getResponseFromHttpUrl(url);
//      COMPLETED (4) If we have valid results, delete the old data and insert the new
        if (weatherData != null && !weatherData.equals("")){
            weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, weatherData);
        }
        try {
            ContentResolver resolver = context.getContentResolver();
            int deletedRows = resolver.delete(
                    WeatherContract.WeatherEntry.CONTENT_URI,
                    null,
                    null
            );

            if (deletedRows > -1){
                resolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherValues);
            }

        } catch (RuntimeException e){
            e.printStackTrace();
        }

    }
}