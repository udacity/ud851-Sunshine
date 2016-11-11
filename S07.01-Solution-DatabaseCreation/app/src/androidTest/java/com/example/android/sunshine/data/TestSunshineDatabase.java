/*
 * Copyright (C) 2015 The Android Open Source Project
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

import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_DATE;
import static com.example.android.sunshine.data.WeatherContract.WeatherEntry.TABLE_NAME;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * Used to test the database we use in Sunshine to cache weather data. Within these tests, we
 * test the following:
 * <p>
 *
 * 1) Creation of the database with proper table(s)
 * 2) Insertion of single record into our weather table
 * 3) When a record is already stored in the weather table with a particular date, a new record
 *  with the same date will overwrite that record.
 * 4) Verify that NON NULL constraints are working properly on record inserts
 * 5) Verify auto increment is working with the ID
 * 6) Test the onUpgrade functionality of the WeatherDbHelper
 */
@RunWith(AndroidJUnit4.class)
public class TestSunshineDatabase {

    /* Context used to perform operations on the database and create WeatherDbHelpers */
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete the database to do so.
     */
    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     */
    @Test
    public void testIntegerAutoincrement() {

        /* First, let's ensure we have some values in our table initially */
        testInsertSingleRecordIntoWeatherTable();

        /* Use WeatherDbHelper to get access to a writable database */
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Obtain weather values from TestUtilities */
        ContentValues testWeatherValues = TestUtilities.createTestWeatherContentValues();

        /* Get the date of the testWeatherValues to ensure we use a different date later */
        long originalDate = testWeatherValues.getAsLong(COLUMN_DATE);

        /* Insert ContentValues into database and get a row ID back */
        long firstRowId = database.insert(
                WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                testWeatherValues);

        /*
         * Now we need to change the date associated with our test content values because the
         * database policy is to replace identical dates on conflict.
         */
        long dayAfterOriginalDate = originalDate + TimeUnit.DAYS.toMillis(1);
        testWeatherValues.put(COLUMN_DATE, dayAfterOriginalDate);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                testWeatherValues);

        String sequentialInsertsDoNotAutoIncrementId =
                "IDs were expected to autoincrement but did not.";
        assertTrue(sequentialInsertsDoNotAutoIncrementId,
                firstRowId + 1 == secondRowId);
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain. Although in our case, we just have one table that we expect should be added
     * <p>
     * {@link com.example.android.sunshine.data.WeatherContract.WeatherEntry#TABLE_NAME}.
     * <p>
     * Despite only needing to check one table name in Sunshine, we set this method up so that
     * you can use it in other apps to test databases with more than one table.
     */
    @Test
    public void testCreateDb() {
        /*
         * Will contain the name of every table in our database. Even though in our case, we only
         * have only table, in many cases, there are multiple tables. Because of that, we are
         * showing you how to test that a database with multiple tables was created properly.
         */
        final HashSet<String> tableNameHashSet = new HashSet<>();

        /* Here, we add the name of our only table in this particular database */
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);
        /* Students, here is where you would add any other table names if you had them */
//        tableNameHashSet.add(MyAwesomeSuperCoolTableName);
//        tableNameHashSet.add(MyOtherCoolTableNameThatContainsOtherCoolData);

        /* Use a WeatherDbHelper to get access to a writable database */
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* We think the database is open, let's verify that here */
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen());

        /* This Cursor will contain the names of each table in our database */
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'",
                null);

        /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        /*
         * tableNameCursor contains the name of each table in this database. Here, we loop over
         * each table that was ACTUALLY created in the database and remove it from the
         * tableNameHashSet to keep track of the fact that was added. At the end of this loop, we
         * should have removed every table name that we thought we should have in our database.
         * If the tableNameHashSet isn't empty after this loop, there was a table that wasn't
         * created properly.
         */
        do {
            tableNameHashSet.remove(tableNameCursor.getString(0));
        } while (tableNameCursor.moveToNext());

        /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertTrue("Error: Your database was created without the expected tables.",
                tableNameHashSet.isEmpty());

        /* Always close a cursor when you are done with it */
        tableNameCursor.close();
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * It will fail for the following reasons:
     * <p>
     * 1) Problem creating the database
     * 2) A value of -1 for the ID of a single, inserted record
     * 3) An empty cursor returned from query on the weather table
     * 4) Actual values of weather data not matching the values from TestUtilities
     */
    @Test
    public void testInsertSingleRecordIntoWeatherTable() {

        /* Use WeatherDbHelper to get access to a writable database */
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        /* Obtain weather values from TestUtilities */
        ContentValues testWeatherValues = TestUtilities.createTestWeatherContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long weatherRowId = database.insert(
                WeatherContract.WeatherEntry.TABLE_NAME,
                null,
                testWeatherValues);

        /* If the insert fails, database.insert returns -1 */
        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, weatherRowId != -1);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor weatherCursor = database.query(
                /* Name of table on which to perform the query */
                WeatherContract.WeatherEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from weather query";
        assertTrue(emptyQueryError,
                weatherCursor.moveToFirst());

        /* Verify that the returned results match the expected results */
        String expectedWeatherDidntMatchActual =
                "Expected weather values didn't match actual values.";
        TestUtilities.validateCurrentRecord(expectedWeatherDidntMatchActual,
                weatherCursor,
                testWeatherValues);

        /*
         * Since before every method annotated with the @Test annotation, the database is
         * deleted, we can assume in this method that there should only be one record in our
         * Weather table because we inserted it. If there is more than one record, an issue has
         * occurred.
         */
        assertFalse("Error: More than one record returned from weather query",
                weatherCursor.moveToNext());

        /* Close cursor and database */
        weatherCursor.close();
        dbHelper.close();
    }

    /**
     * Deletes the entire database.
     */
    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }
}