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
package com.example.android.sunshine

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import com.example.android.sunshine.data.SunshinePreferences
import com.example.android.sunshine.utilities.NetworkUtils
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils

import java.net.URL

class MainActivity : AppCompatActivity() {

    private var mWeatherTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        /*
         * Using findViewById, we get a reference to our TextView from xml. This allows us to
         * do things like set the text of the TextView.
         */
        mWeatherTextView = findViewById(R.id.tv_weather_data)

        loadWeatherData()
    }

    private fun loadWeatherData() {
        val location = SunshinePreferences.getPreferredWeatherLocation(this)
        FetchWeatherTask().execute(location)
    }

    inner class FetchWeatherTask : AsyncTask<String, Void, Array<String>>() {
        override fun doInBackground(vararg params: String): Array<String>? {
            if (params.isEmpty()) {
                return null
            }

            val location = params[0]
            val weatherRequestUrl = NetworkUtils.buildUrl(location)
            try {
                val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl!!)
                return OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(this@MainActivity, jsonWeatherResponse!!)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }

        override fun onPostExecute(weatherData: Array<String>?) {
            if (weatherData != null) {
                for (weatherString in weatherData) {
                    mWeatherTextView!!.append(weatherString + "\n\n\n")
                }
            }
        }
    }
}