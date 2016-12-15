package com.example.android.sunshine.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry;

public class FakeDataUtils {

    private static int [] weatherIDs = {200,300,500,711,900,962};

    /**
     * Creates a single ContentValues object with random weather data for the provided date
     * @param date a normalized date
     * @return ContentValues object filled with random weather data
     */
    private static ContentValues createTestWeatherContentValues(long date) {
        ContentValues testWeatherValues = new ContentValues();
        testWeatherValues.put(WeatherEntry.COLUMN_DATE, date);
        testWeatherValues.put(WeatherEntry.COLUMN_DEGREES, Math.random()*2);
        testWeatherValues.put(WeatherEntry.COLUMN_HUMIDITY, Math.random()*100);
        testWeatherValues.put(WeatherEntry.COLUMN_PRESSURE, 870 + Math.random()*100);
        int maxTemp = (int)(Math.random()*100);
        testWeatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, maxTemp);
        testWeatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, maxTemp - (int) (Math.random()*10));
        testWeatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, Math.random()*10);
        testWeatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, weatherIDs[(int)(Math.random()*10)%5]);
        return testWeatherValues;
    }

    /**
     * Creates random weather data for 7 days starting today
     * @param context
     */
    public static void insertFakeData(Context context) {
        //Get today's normalized date
        long today = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();
        //loop over 7 days starting today onwards
        for(int i=0; i<7; i++) {
            fakeValues.add(FakeDataUtils.createTestWeatherContentValues(today + TimeUnit.DAYS.toMillis(i)));
        }
        // Bulk Insert our new weather data into Sunshine's Database
        context.getContentResolver().bulkInsert(
                WeatherContract.WeatherEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[7]));
    }
}
