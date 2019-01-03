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

import android.content.Context
import android.util.Log

import com.example.android.sunshine.R
import com.example.android.sunshine.data.SunshinePreferences

/**
 * Contains useful utilities for a weather app, such as conversion between Celsius and Fahrenheit,
 * from kph to mph, and from degrees to NSEW.  It also contains the mapping of weather condition
 * codes in OpenWeatherMap to strings.  These strings are contained
 */
object SunshineWeatherUtils {

    private val LOG_TAG = SunshineWeatherUtils::class.java!!.getSimpleName()

    /**
     * This method will convert a temperature from Celsius to Fahrenheit.
     *
     * @param temperatureInCelsius Temperature in degrees Celsius(°C)
     *
     * @return Temperature in degrees Fahrenheit (°F)
     */
    private fun celsiusToFahrenheit(temperatureInCelsius: Double): Double {
        return temperatureInCelsius * 1.8 + 32
    }

    /**
     * Temperature data is stored in Celsius by our app. Depending on the user's preference,
     * the app may need to display the temperature in Fahrenheit. This method will perform that
     * temperature conversion if necessary. It will also format the temperature so that no
     * decimal points show. Temperatures will be formatted to the following form: "21°C"
     *
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees Celsius (°C)
     *
     * @return Formatted temperature String in the following form:
     * "21°C"
     */
    fun formatTemperature(context: Context, temperature: Double): String {
        var temperature = temperature
        var temperatureFormatResourceId = R.string.format_temperature_celsius

        if (!SunshinePreferences.isMetric(context)) {
            temperature = celsiusToFahrenheit(temperature)
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit
        }

        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(context.getString(temperatureFormatResourceId), temperature)
    }

    /**
     * This method will format the temperatures to be displayed in the
     * following form: "HIGH°C / LOW°C"
     *
     * @param context Android Context to access preferences and resources
     * @param high    High temperature for a day in user's preferred units
     * @param low     Low temperature for a day in user's preferred units
     *
     * @return String in the form: "HIGH°C / LOW°C"
     */
    fun formatHighLows(context: Context, high: Double, low: Double): String {
        val roundedHigh = Math.round(high)
        val roundedLow = Math.round(low)

        val formattedHigh = formatTemperature(context, roundedHigh.toDouble())
        val formattedLow = formatTemperature(context, roundedLow.toDouble())

        return "$formattedHigh / $formattedLow"
    }

    /**
     * This method uses the wind direction in degrees to determine compass direction as a
     * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
     *
     * @param context   Android Context to access preferences and resources
     * @param windSpeed Wind speed in kilometers / hour
     * @param degrees   Degrees as measured on a compass, NOT temperature degrees!
     * See https://www.mathsisfun.com/geometry/degrees.html
     *
     * @return Wind String in the following form: "2 km/h SW"
     */
    fun getFormattedWind(context: Context, windSpeed: Float, degrees: Float): String {
        var windSpeed = windSpeed

        var windFormat = R.string.format_wind_kmh

        if (!SunshinePreferences.isMetric(context)) {
            windFormat = R.string.format_wind_mph
            windSpeed = .621371192237334f * windSpeed
        }

        /*
         * You know what's fun, writing really long if/else statements with tons of possible
         * conditions. Seriously, try it!
         */
        var direction = "Unknown"
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N"
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE"
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E"
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE"
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S"
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW"
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W"
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW"
        }
        return String.format(context.getString(windFormat), windSpeed, direction)
    }

    /**
     * Helper method to provide the string according to the weather
     * condition id returned by the OpenWeatherMap call.
     *
     * @param context   Android context
     * @param weatherId from OpenWeatherMap API response
     * http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
     *
     * @return String for the weather condition, null if no relation is found.
     */
    fun getStringForWeatherCondition(context: Context, weatherId: Int): String {
        val stringId: Int
        if (weatherId >= 200 && weatherId <= 232) {
            stringId = R.string.condition_2xx
        } else if (weatherId >= 300 && weatherId <= 321) {
            stringId = R.string.condition_3xx
        } else
            when (weatherId) {
                500 -> stringId = R.string.condition_500
                501 -> stringId = R.string.condition_501
                502 -> stringId = R.string.condition_502
                503 -> stringId = R.string.condition_503
                504 -> stringId = R.string.condition_504
                511 -> stringId = R.string.condition_511
                520 -> stringId = R.string.condition_520
                531 -> stringId = R.string.condition_531
                600 -> stringId = R.string.condition_600
                601 -> stringId = R.string.condition_601
                602 -> stringId = R.string.condition_602
                611 -> stringId = R.string.condition_611
                612 -> stringId = R.string.condition_612
                615 -> stringId = R.string.condition_615
                616 -> stringId = R.string.condition_616
                620 -> stringId = R.string.condition_620
                621 -> stringId = R.string.condition_621
                622 -> stringId = R.string.condition_622
                701 -> stringId = R.string.condition_701
                711 -> stringId = R.string.condition_711
                721 -> stringId = R.string.condition_721
                731 -> stringId = R.string.condition_731
                741 -> stringId = R.string.condition_741
                751 -> stringId = R.string.condition_751
                761 -> stringId = R.string.condition_761
                762 -> stringId = R.string.condition_762
                771 -> stringId = R.string.condition_771
                781 -> stringId = R.string.condition_781
                800 -> stringId = R.string.condition_800
                801 -> stringId = R.string.condition_801
                802 -> stringId = R.string.condition_802
                803 -> stringId = R.string.condition_803
                804 -> stringId = R.string.condition_804
                900 -> stringId = R.string.condition_900
                901 -> stringId = R.string.condition_901
                902 -> stringId = R.string.condition_902
                903 -> stringId = R.string.condition_903
                904 -> stringId = R.string.condition_904
                905 -> stringId = R.string.condition_905
                906 -> stringId = R.string.condition_906
                951 -> stringId = R.string.condition_951
                952 -> stringId = R.string.condition_952
                953 -> stringId = R.string.condition_953
                954 -> stringId = R.string.condition_954
                955 -> stringId = R.string.condition_955
                956 -> stringId = R.string.condition_956
                957 -> stringId = R.string.condition_957
                958 -> stringId = R.string.condition_958
                959 -> stringId = R.string.condition_959
                960 -> stringId = R.string.condition_960
                961 -> stringId = R.string.condition_961
                962 -> stringId = R.string.condition_962
                else -> return context.getString(R.string.condition_unknown, weatherId)
            }
        return context.getString(stringId)
    }

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherId from OpenWeatherMap API response
     *
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    fun getIconResourceForWeatherCondition(weatherId: Int): Int {
        /*
         * Based on weather code data found at:
         * See http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
         */
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain
        } else if (weatherId == 511) {
            return R.drawable.ic_snow
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm
        } else if (weatherId == 800) {
            return R.drawable.ic_clear
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy
        }
        return -1
    }

    /**
     * Helper method to provide the art resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     *
     * @param weatherId from OpenWeatherMap API response
     *
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    fun getArtResourceForWeatherCondition(weatherId: Int): Int {
        /*
         * Based on weather code data found at:
         * http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
         */
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain
        } else if (weatherId == 511) {
            return R.drawable.art_snow
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog
        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
            return R.drawable.art_storm
        } else if (weatherId == 800) {
            return R.drawable.art_clear
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds
        } else if (weatherId >= 900 && weatherId <= 906) {
            return R.drawable.art_storm
        } else if (weatherId >= 958 && weatherId <= 962) {
            return R.drawable.art_storm
        } else if (weatherId >= 951 && weatherId <= 957) {
            return R.drawable.art_clear
        }
        Log.e(LOG_TAG, "Unknown Weather: $weatherId")
        return R.drawable.art_storm
    }
}