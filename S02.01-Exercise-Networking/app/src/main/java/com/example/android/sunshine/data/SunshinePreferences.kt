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
package com.example.android.sunshine.data

import android.content.Context

object SunshinePreferences {

    /*
     * Human readable location string, provided by the API.  Because for styling,
     * "Mountain View" is more recognizable than 94043.
     */
    val PREF_CITY_NAME = "city_name"

    /*
     * In order to uniquely pinpoint the location on the map when we launch the
     * map intent, we store the latitude and longitude.
     */
    val PREF_COORD_LAT = "coord_lat"
    val PREF_COORD_LONG = "coord_long"

    /*
     * Before you implement methods to return your REAL preference for location,
     * we provide some default values to work with.
     */
    private
            /** This will be implemented in a future lesson  */
    val defaultWeatherLocation = "94043,USA"
    /** This will be implemented in a future lesson  */
    val defaultWeatherCoordinates = doubleArrayOf(37.4284, 122.0724)

    private val DEFAULT_MAP_LOCATION = "1600 Amphitheatre Parkway, Mountain View, CA 94043"

    /**
     * Helper method to handle setting location details in Preferences (City Name, Latitude,
     * Longitude)
     *
     * @param c        Context used to get the SharedPreferences
     * @param cityName A human-readable city name, e.g "Mountain View"
     * @param lat      The latitude of the city
     * @param lon      The longitude of the city
     */
    fun setLocationDetails(c: Context, cityName: String, lat: Double, lon: Double) {
        /** This will be implemented in a future lesson  */
    }

    /**
     * Helper method to handle setting a new location in preferences.  When this happens
     * the database may need to be cleared.
     *
     * @param c               Context used to get the SharedPreferences
     * @param locationSetting The location string used to request updates from the server.
     * @param lat             The latitude of the city
     * @param lon             The longitude of the city
     */
    fun setLocation(c: Context, locationSetting: String, lat: Double, lon: Double) {
        /** This will be implemented in a future lesson  */
    }

    /**
     * Resets the stored location coordinates.
     *
     * @param c Context used to get the SharedPreferences
     */
    fun resetLocationCoordinates(c: Context) {
        /** This will be implemented in a future lesson  */
    }

    /**
     * Returns the location currently set in Preferences. The default location this method
     * will return is "94043,USA", which is Mountain View, California. Mountain View is the
     * home of the headquarters of the Googleplex!
     *
     * @param context Context used to get the SharedPreferences
     * @return Location The current user has set in SharedPreferences. Will default to
     * "94043,USA" if SharedPreferences have not been implemented yet.
     */
    fun getPreferredWeatherLocation(context: Context): String {
        /** This will be implemented in a future lesson  */
        return defaultWeatherLocation
    }

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences
     * @return true If metric display should be used
     */
    fun isMetric(context: Context): Boolean {
        /** This will be implemented in a future lesson  */
        return true
    }

    /**
     * Returns the location coordinates associated with the location.  Note that these coordinates
     * may not be set, which results in (0,0) being returned. (conveniently, 0,0 is in the middle
     * of the ocean off the west coast of Africa)
     *
     * @param context Used to get the SharedPreferences
     * @return An array containing the two coordinate values.
     */
    fun getLocationCoordinates(context: Context): DoubleArray {
        return defaultWeatherCoordinates
    }

    /**
     * Returns true if the latitude and longitude values are available. The latitude and
     * longitude will not be available until the lesson where the PlacePicker API is taught.
     *
     * @param context used to get the SharedPreferences
     * @return true if lat/long are set
     */
    fun isLocationLatLonAvailable(context: Context): Boolean {
        /** This will be implemented in a future lesson  */
        return false
    }
}