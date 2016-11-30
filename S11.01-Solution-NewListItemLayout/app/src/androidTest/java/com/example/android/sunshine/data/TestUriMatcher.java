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
package com.example.android.sunshine.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.example.android.sunshine.data.TestUtilities.getStaticIntegerField;
import static com.example.android.sunshine.data.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {

    private static final Uri TEST_WEATHER_DIR = WeatherContract.WeatherEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_WITH_DATE_DIR = WeatherContract.WeatherEntry
            .buildWeatherUriWithDate(TestUtilities.DATE_NORMALIZED);

    private static final String weatherCodeVariableName = "CODE_WEATHER";
    private static int REFLECTED_WEATHER_CODE;

    private static final String weatherCodeWithDateVariableName = "CODE_WEATHER_WITH_DATE";
    private static int REFLECTED_WEATHER_WITH_DATE_CODE;

    private UriMatcher testMatcher;

    @Before
    public void before() {
        try {

            Method buildUriMatcher = WeatherProvider.class.getDeclaredMethod("buildUriMatcher");
            testMatcher = (UriMatcher) buildUriMatcher.invoke(WeatherProvider.class);

            REFLECTED_WEATHER_CODE = getStaticIntegerField(
                    WeatherProvider.class,
                    weatherCodeVariableName);

            REFLECTED_WEATHER_WITH_DATE_CODE = getStaticIntegerField(
                    WeatherProvider.class,
                    weatherCodeWithDateVariableName);

        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            String noBuildUriMatcherMethodFound =
                    "It doesn't appear that you have created a method called buildUriMatcher in " +
                            "the WeatherProvider class.";
            fail(noBuildUriMatcherMethodFound);
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Students: This function tests that your UriMatcher returns the correct integer value for
     * each of the Uri types that our ContentProvider can handle. Uncomment this when you are
     * ready to test your UriMatcher.
     */
    @Test
    public void testUriMatcher() {

        /* Test that the code returned from our matcher matches the expected weather code */
        String weatherUriDoesNotMatch = "Error: The CODE_WEATHER URI was matched incorrectly.";
        int actualWeatherCode = testMatcher.match(TEST_WEATHER_DIR);
        int expectedWeatherCode = REFLECTED_WEATHER_CODE;
        assertEquals(weatherUriDoesNotMatch,
                expectedWeatherCode,
                actualWeatherCode);

        /*
         * Test that the code returned from our matcher matches the expected weather with date code
         */
        String weatherWithDateUriCodeDoesNotMatch =
                "Error: The CODE_WEATHER WITH DATE URI was matched incorrectly.";
        int actualWeatherWithDateCode = testMatcher.match(TEST_WEATHER_WITH_DATE_DIR);
        int expectedWeatherWithDateCode = REFLECTED_WEATHER_WITH_DATE_CODE;
        assertEquals(weatherWithDateUriCodeDoesNotMatch,
                expectedWeatherWithDateCode,
                actualWeatherWithDateCode);
    }
}
