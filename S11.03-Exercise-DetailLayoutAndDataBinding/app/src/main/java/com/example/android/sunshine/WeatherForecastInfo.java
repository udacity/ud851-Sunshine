package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_CONDITION_ID;
import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_DATE;
import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_DEGREES;
import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_HUMIDITY;
import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_MAX_TEMP;
import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_PRESSURE;
import static com.example.android.sunshine.DetailActivity.INDEX_WEATHER_WIND_SPEED;

final public class WeatherForecastInfo {

    public Drawable icon;
    public String date;
    public String desc;
    public String max;
    public String min;

    public String humidity;
    public String pressure;
    public String wind;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static WeatherForecastInfo newInstance(Context c, Cursor data){

        WeatherForecastInfo weatherForecastInfo = new WeatherForecastInfo();

        weatherForecastInfo.icon = c.getDrawable(
                SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(data.getInt(INDEX_WEATHER_CONDITION_ID))
        );

        long localDateMidnightGmt = data.getLong(INDEX_WEATHER_DATE);
        weatherForecastInfo.date = SunshineDateUtils.getFriendlyDateString(c, localDateMidnightGmt, true);

        int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
        /* Use the weatherId to obtain the proper description */
        weatherForecastInfo.desc = SunshineWeatherUtils.getStringForWeatherCondition(c, weatherId);

        double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        weatherForecastInfo.max = SunshineWeatherUtils.formatTemperature(c, highInCelsius);


        double lowInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        weatherForecastInfo.min = SunshineWeatherUtils.formatTemperature(c, lowInCelsius);


        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        weatherForecastInfo.humidity = c.getString(R.string.format_humidity, humidity);


        float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        weatherForecastInfo.wind = SunshineWeatherUtils.getFormattedWind(c, windSpeed, windDirection);


        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);

        /*
         * Format the pressure text using string resources. The reason we directly access
         * resources using getString rather than using a method from SunshineWeatherUtils as
         * we have for other data displayed in this Activity is because there is no
         * additional logic that needs to be considered in order to properly display the
         * pressure.
         */
        weatherForecastInfo.pressure = c.getString(R.string.format_pressure, pressure);

        return weatherForecastInfo;
    }
}
