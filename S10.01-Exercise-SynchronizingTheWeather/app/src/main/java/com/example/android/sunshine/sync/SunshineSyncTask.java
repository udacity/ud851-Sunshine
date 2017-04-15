package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.net.URL;

//  TODO (1) Create a class called SunshineSyncTask
//  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
//      TODO (3) Within syncWeather, fetch new weather data
//      TODO (4) If we have valid results, delete the old data and insert the new
public class SunshineSyncTask{

    synchronized public static void syncWeather(Context context) {
        try {
            URL weatherRequest = NetworkUtils.getUrl(context);

            String weatherContentFromJson = NetworkUtils.getResponseFromHttpUrl(weatherRequest);

            ContentValues[] weatherContentsValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, weatherContentFromJson);

            ContentResolver contentResolver = context.getContentResolver();

            if (weatherContentsValues != null && weatherContentFromJson.length() != 0) {

                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);

                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherContentsValues);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}