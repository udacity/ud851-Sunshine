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
package com.example.android.sunshine.utilities

import android.content.ContentValues
import android.content.Context

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.net.HttpURLConnection

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
object OpenWeatherJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     *
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param forecastJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Throws(JSONException::class)
    fun getSimpleWeatherStringsFromJson(context: Context, forecastJsonStr: String): Array<String>? {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        val OWM_LIST = "list"

        /* All temperatures are children of the "temp" object */
        val OWM_TEMPERATURE = "temp"

        /* Max temperature for the day */
        val OWM_MAX = "max"
        val OWM_MIN = "min"

        val OWM_WEATHER = "weather"
        val OWM_DESCRIPTION = "main"

        val OWM_MESSAGE_CODE = "cod"

        /* String array to hold each day's weather String */
        var parsedWeatherData: ArrayList<String>?

        val forecastJson = JSONObject(forecastJsonStr)

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            val errorCode = forecastJson.getInt(OWM_MESSAGE_CODE)

            when (errorCode) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->
                    /* Location invalid */
                    return null
                else ->
                    /* Server probably down */
                    return null
            }
        }

        val weatherArray = forecastJson.getJSONArray(OWM_LIST)

        parsedWeatherData = ArrayList()

        val localDate = System.currentTimeMillis()
        val utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate)
        val startDay = SunshineDateUtils.normalizeDate(utcDate)

        for (i in 0 until weatherArray.length()) {
            val date: String
            val highAndLow: String

            /* These are the values that will be collected */
            val dateTimeMillis: Long
            val high: Double
            val low: Double
            val description: String

            /* Get the JSON object representing the day */
            val dayForecast = weatherArray.getJSONObject(i)

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i
            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false)

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */
            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            description = weatherObject.getString(OWM_DESCRIPTION)

            /*
             * Temperatures are sent by Open Weather Map in a child object called "temp".
             *
             * Editor's Note: Try not to name variables "temp" when working with temperature.
             * It confuses everybody. Temp could easily mean any number of things, including
             * temperature, temporary and is just a bad variable name.
             */
            val temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE)
            high = temperatureObject.getDouble(OWM_MAX)
            low = temperatureObject.getDouble(OWM_MIN)
            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low)

            parsedWeatherData.add("$date - $description - $highAndLow")
        }

        val array = arrayOfNulls<String>(parsedWeatherData.size)

        return parsedWeatherData.toArray(array)
    }

    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    fun getFullWeatherDataFromJson(context: Context, forecastJsonStr: String): Array<ContentValues>? {
        /** This will be implemented in a future lesson  */
        return null
    }
}