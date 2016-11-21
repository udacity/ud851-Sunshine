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

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utils.PollingCheck;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_DATE;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_HUMIDITY;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_MAX;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_MIN;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_PRESSURE;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_WEATHER_ID;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_WIND_DIR;
import static com.example.android.sunshine.data.TestSunshineDatabase.REFLECTED_COLUMN_WIND_SPEED;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * These are functions and some test data to make it easier to test your database and Content
 * Provider.
 * <p>
 * NOTE: If your WeatherContract class doesn't exactly match ours, THIS WILL NOT WORK as we've
 * provided and you will need to make changes to this code to use it to pass your tests.
 */
class TestUtilities {

    /* October 1st, 2016 at midnight, GMT time */
    static final long DATE_NORMALIZED = 1475280000000L;

    static final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    /**
     * Ensures there is a non empty cursor and validates the cursor's data by checking it against
     * a set of expected values. This method will then close the cursor.
     *
     * @param error          Message when an error occurs
     * @param valueCursor    The Cursor containing the actual values received from an arbitrary query
     * @param expectedValues The values we expect to receive in valueCursor
     */
    static void validateThenCloseCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    /**
     * This method iterates through a set of expected values and makes various assertions that
     * will pass if our app is functioning properly.
     *
     * @param error          Message when an error occurs
     * @param valueCursor    The Cursor containing the actual values received from an arbitrary query
     * @param expectedValues The values we expect to receive in valueCursor
     */
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);

            /* Test to see if the column is contained within the cursor */
            String columnNotFoundError = "Column '" + columnName + "' not found. " + error;
            assertFalse(columnNotFoundError, index == -1);

            /* Test to see if the expected value equals the actual value (from the Cursor) */
            String expectedValue = entry.getValue().toString();
            String actualValue = valueCursor.getString(index);

            String valuesDontMatchError = "Actual value '" + actualValue
                    + "' did not match the expected value '" + expectedValue + "'. "
                    + error;

            assertEquals(valuesDontMatchError,
                    expectedValue,
                    actualValue);
        }
    }

    /**
     * Used as a convenience method to return a singleton instance of ContentValues to populate
     * our database or insert using our ContentProvider.
     *
     * @return ContentValues that can be inserted into our ContentProvider or weather.db
     */
    static ContentValues createTestWeatherContentValues() {

        ContentValues testWeatherValues = new ContentValues();

        testWeatherValues.put(REFLECTED_COLUMN_DATE, DATE_NORMALIZED);
        testWeatherValues.put(REFLECTED_COLUMN_WIND_DIR, 1.1);
        testWeatherValues.put(REFLECTED_COLUMN_HUMIDITY, 1.2);
        testWeatherValues.put(REFLECTED_COLUMN_PRESSURE, 1.3);
        testWeatherValues.put(REFLECTED_COLUMN_MAX, 75);
        testWeatherValues.put(REFLECTED_COLUMN_MIN, 65);
        testWeatherValues.put(REFLECTED_COLUMN_WIND_SPEED, 5.5);
        testWeatherValues.put(REFLECTED_COLUMN_WEATHER_ID, 321);

        return testWeatherValues;
    }

    /**
     * Used as a convenience method to return a singleton instance of an array of ContentValues to
     * populate our database or insert using our ContentProvider's bulk insert method.
     * <p>
     * It is handy to have utility methods that produce test values because it makes it easy to
     * compare results from ContentProviders and databases to the values you expect to receive.
     * See {@link #validateCurrentRecord(String, Cursor, ContentValues)} and
     * {@link #validateThenCloseCursor(String, Cursor, ContentValues)} for more information on how
     * this verification is performed.
     *
     * @return Array of ContentValues that can be inserted into our ContentProvider or weather.db
     */
    static ContentValues[] createBulkInsertTestWeatherValues() {

        ContentValues[] bulkTestWeatherValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        long testDate = TestUtilities.DATE_NORMALIZED;
        long normalizedTestDate = SunshineDateUtils.normalizeDate(testDate);

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {

            normalizedTestDate += SunshineDateUtils.DAY_IN_MILLIS;

            ContentValues weatherValues = new ContentValues();

            weatherValues.put(REFLECTED_COLUMN_DATE, normalizedTestDate);
            weatherValues.put(REFLECTED_COLUMN_WIND_DIR, 1.1);
            weatherValues.put(REFLECTED_COLUMN_HUMIDITY, 1.2 + 0.01 * (float) i);
            weatherValues.put(REFLECTED_COLUMN_PRESSURE, 1.3 - 0.01 * (float) i);
            weatherValues.put(REFLECTED_COLUMN_MAX, 75 + i);
            weatherValues.put(REFLECTED_COLUMN_MIN, 65 - i);
            weatherValues.put(REFLECTED_COLUMN_WIND_SPEED, 5.5 + 0.2 * (float) i);
            weatherValues.put(REFLECTED_COLUMN_WEATHER_ID, 321);

            bulkTestWeatherValues[i] = weatherValues;
        }

        return bulkTestWeatherValues;
    }


    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    /**
     * Students: The functions we provide inside of TestWeatherProvider use TestContentObserver to test
     * the ContentObserver callbacks using the PollingCheck class from the Android Compatibility
     * Test Suite tests.
     * <p>
     * NOTE: This only tests that the onChange function is called; it DOES NOT test that the
     * correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        /**
         * Called when a content change occurs.
         * <p>
         * To ensure correct operation on older versions of the framework that did not provide a
         * Uri argument, applications should also implement this method whenever they implement
         * the {@link #onChange(boolean, Uri)} overload.
         *
         * @param selfChange True if this is a self-change notification.
         */
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        /**
         * Called when a content change occurs. Includes the changed content Uri when available.
         *
         * @param selfChange True if this is a self-change notification.
         * @param uri        The Uri of the changed content, or null if unknown.
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        /**
         * Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
         * It's useful to look at the Android CTS source for ideas on how to test your Android
         * applications. The reason that PollingCheck works is that, by default, the JUnit testing
         * framework is not running on the main Android application thread.
         */
        void waitForNotificationOrFail() {

            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static String getConstantNameByStringValue(Class klass, String value)  {
        for (Field f : klass.getDeclaredFields()) {
            int modifiers = f.getModifiers();
            Class<?> type = f.getType();
            boolean isPublicStaticFinalString = Modifier.isStatic(modifiers)
                    && Modifier.isFinal(modifiers)
                    && Modifier.isPublic(modifiers)
                    && type.isAssignableFrom(String.class);

            if (isPublicStaticFinalString) {
                String fieldName = f.getName();
                try {
                    String fieldValue = (String) klass.getDeclaredField(fieldName).get(null);
                    if (fieldValue.equals(value)) return fieldName;
                } catch (IllegalAccessException e) {
                    return null;
                } catch (NoSuchFieldException e) {
                    return null;
                }
            }
        }

        return null;
    }

    static String getStaticStringField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field stringField = clazz.getDeclaredField(variableName);
        stringField.setAccessible(true);
        String value = (String) stringField.get(null);
        return value;
    }

    static Integer getStaticIntegerField(Class clazz, String variableName)
            throws NoSuchFieldException, IllegalAccessException {
        Field intField = clazz.getDeclaredField(variableName);
        intField.setAccessible(true);
        Integer value = (Integer) intField.get(null);
        return value;
    }

    static String studentReadableClassNotFound(ClassNotFoundException e) {
        String message = e.getMessage();
        int indexBeforeSimpleClassName = message.lastIndexOf('.');
        String simpleClassNameThatIsMissing = message.substring(indexBeforeSimpleClassName + 1);
        simpleClassNameThatIsMissing = simpleClassNameThatIsMissing.replaceAll("\\$", ".");
        String fullClassNotFoundReadableMessage = "Couldn't find the class "
                + simpleClassNameThatIsMissing
                + ".\nPlease make sure you've created that class and followed the TODOs.";
        return fullClassNotFoundReadableMessage;
    }

    static String studentReadableNoSuchField(NoSuchFieldException e) {
        String message = e.getMessage();

        Pattern p = Pattern.compile("No field (\\w*) in class L.*/(\\w*\\$?\\w*);");

        Matcher m = p.matcher(message);

        if (m.find()) {
            String missingFieldName = m.group(1);
            String classForField = m.group(2).replaceAll("\\$", ".");
            String fieldNotFoundReadableMessage = "Couldn't find "
                    + missingFieldName + " in class " + classForField + "."
                    + "\nPlease make sure you've declared that field and followed the TODOs.";
            return fieldNotFoundReadableMessage;
        } else {
            return e.getMessage();
        }
    }
}