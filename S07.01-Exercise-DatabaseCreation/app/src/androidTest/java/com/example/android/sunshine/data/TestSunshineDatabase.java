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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import static com.example.android.sunshine.data.TestUtilities.getStaticIntegerField;
import static com.example.android.sunshine.data.TestUtilities.getStaticStringField;
import static com.example.android.sunshine.data.TestUtilities.studentReadableClassNotFound;
import static com.example.android.sunshine.data.TestUtilities.studentReadableNoSuchField;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Used to test the database we use in Sunshine to cache weather data. Within these tests, we
 * test the following:
 * <p>
 * <p>
 * 1) Creation of the database with proper table(s)
 * 2) Insertion of single record into our weather table
 * 3) When a record is already stored in the weather table with a particular date, a new record
 * with the same date will overwrite that record.
 * 4) Verify that NON NULL constraints are working properly on record inserts
 * 5) Verify auto increment is working with the ID
 * 6) Test the onUpgrade functionality of the WeatherDbHelper
 */
@RunWith(AndroidJUnit4.class)
public class TestSunshineDatabase {

    /*
     * Context used to perform operations on the database and create WeatherDbHelpers.
     */
    private final Context context = InstrumentationRegistry.getTargetContext();

    /*
     * In order to verify that you have set up your classes properly and followed our TODOs, we
     * need to create what's called a Change Detector Test. In almost any other situation, these
     * tests are discouraged, as they provide no real value in a production setting. However, using
     * reflection to verify that you have set your classes up correctly will help provide more
     * useful errors if you've missed a step in our instructions.
     *
     * Additionally, using reflection for these tests allows you to run the tests when they
     * normally wouldn't compile, as they depend on pieces of your classes that you might not
     * have created when you initially run the tests.
     */
    private static final String packageName = "com.example.android.sunshine";
    private static final String dataPackageName = packageName + ".data";

    private Class weatherEntryClass;
    private Class weatherDbHelperClass;
    private static final String weatherContractName = ".WeatherContract";
    private static final String weatherEntryName = weatherContractName + "$WeatherEntry";
    private static final String weatherDbHelperName = ".WeatherDbHelper";

    private static final String databaseNameVariableName = "DATABASE_NAME";
    private static String REFLECTED_DATABASE_NAME;

    private static final String databaseVersionVariableName = "DATABASE_VERSION";
    private static int REFLECTED_DATABASE_VERSION;

    private static final String tableNameVariableName = "TABLE_NAME";
    private static String REFLECTED_TABLE_NAME;

    private static final String columnDateVariableName = "COLUMN_DATE";
    static String REFLECTED_COLUMN_DATE;

    private static final String columnWeatherIdVariableName = "COLUMN_WEATHER_ID";
    static String REFLECTED_COLUMN_WEATHER_ID;

    private static final String columnMinVariableName = "COLUMN_MIN_TEMP";
    static String REFLECTED_COLUMN_MIN;

    private static final String columnMaxVariableName = "COLUMN_MAX_TEMP";
    static String REFLECTED_COLUMN_MAX;

    private static final String columnHumidityVariableName = "COLUMN_HUMIDITY";
    static String REFLECTED_COLUMN_HUMIDITY;

    private static final String columnPressureVariableName = "COLUMN_PRESSURE";
    static String REFLECTED_COLUMN_PRESSURE;

    private static final String columnWindSpeedVariableName = "COLUMN_WIND_SPEED";
    static String REFLECTED_COLUMN_WIND_SPEED;

    private static final String columnWindDirVariableName = "COLUMN_DEGREES";
    static String REFLECTED_COLUMN_WIND_DIR;

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    @Before
    public void before() {
        try {

            weatherEntryClass = Class.forName(dataPackageName + weatherEntryName);
            if (!BaseColumns.class.isAssignableFrom(weatherEntryClass)) {
                String weatherEntryDoesNotImplementBaseColumns = "WeatherEntry class needs to " +
                        "implement the interface BaseColumns, but does not.";
                fail(weatherEntryDoesNotImplementBaseColumns);
            }

            REFLECTED_TABLE_NAME = getStaticStringField(weatherEntryClass, tableNameVariableName);
            REFLECTED_COLUMN_DATE = getStaticStringField(weatherEntryClass, columnDateVariableName);
            REFLECTED_COLUMN_WEATHER_ID = getStaticStringField(weatherEntryClass, columnWeatherIdVariableName);
            REFLECTED_COLUMN_MIN = getStaticStringField(weatherEntryClass, columnMinVariableName);
            REFLECTED_COLUMN_MAX = getStaticStringField(weatherEntryClass, columnMaxVariableName);
            REFLECTED_COLUMN_HUMIDITY = getStaticStringField(weatherEntryClass, columnHumidityVariableName);
            REFLECTED_COLUMN_PRESSURE = getStaticStringField(weatherEntryClass, columnPressureVariableName);
            REFLECTED_COLUMN_WIND_SPEED = getStaticStringField(weatherEntryClass, columnWindSpeedVariableName);
            REFLECTED_COLUMN_WIND_DIR = getStaticStringField(weatherEntryClass, columnWindDirVariableName);

            weatherDbHelperClass = Class.forName(dataPackageName + weatherDbHelperName);
            Class weatherDbHelperSuperclass = weatherDbHelperClass.getSuperclass();

            if (weatherDbHelperSuperclass == null || weatherDbHelperSuperclass.equals(Object.class)) {
                String noExplicitSuperclass =
                        "WeatherDbHelper needs to extend SQLiteOpenHelper, but yours currently doesn't extend a class at all.";
                fail(noExplicitSuperclass);
            } else if (weatherDbHelperSuperclass != null) {
                String weatherDbHelperSuperclassName = weatherDbHelperSuperclass.getSimpleName();
                String doesNotExtendOpenHelper =
                        "WeatherDbHelper needs to extend SQLiteOpenHelper but yours extends "
                                + weatherDbHelperSuperclassName;

                assertTrue(doesNotExtendOpenHelper,
                        SQLiteOpenHelper.class.isAssignableFrom(weatherDbHelperSuperclass));
            }

            REFLECTED_DATABASE_NAME = getStaticStringField(
                    weatherDbHelperClass, databaseNameVariableName);

            REFLECTED_DATABASE_VERSION = getStaticIntegerField(
                    weatherDbHelperClass, databaseVersionVariableName);

            int expectedDatabaseVersion = 1;
            String databaseVersionShouldBe1 = "Database version should be "
                    + expectedDatabaseVersion + " but isn't.";

            assertEquals(databaseVersionShouldBe1,
                    expectedDatabaseVersion,
                    REFLECTED_DATABASE_VERSION);

            Constructor weatherDbHelperCtor = weatherDbHelperClass.getConstructor(Context.class);

            dbHelper = (SQLiteOpenHelper) weatherDbHelperCtor.newInstance(context);

            context.deleteDatabase(REFLECTED_DATABASE_NAME);

            Method getWritableDatabase = SQLiteOpenHelper.class.getDeclaredMethod("getWritableDatabase");
            database = (SQLiteDatabase) getWritableDatabase.invoke(dbHelper);

        } catch (ClassNotFoundException e) {
            fail(studentReadableClassNotFound(e));
        } catch (NoSuchFieldException e) {
            fail(studentReadableNoSuchField(e));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Tests to ensure that inserts into your database results in automatically incrementing row
     * IDs and that row IDs are not reused.
     * <p>
     * If the INTEGER PRIMARY KEY column is not explicitly given a value, then it will be filled
     * automatically with an unused integer, usually one more than the largest _ID currently in
     * use. This is true regardless of whether or not the AUTOINCREMENT keyword is used.
     * <p>
     * If the AUTOINCREMENT keyword appears after INTEGER PRIMARY KEY, that changes the automatic
     * _ID assignment algorithm to prevent the reuse of _IDs over the lifetime of the database.
     * In other words, the purpose of AUTOINCREMENT is to prevent the reuse of _IDs from previously
     * deleted rows.
     * <p>
     * To test this, we first insert a row into the database and get its _ID. Then, we'll delete
     * that row, change the data that we're going to insert, and insert the changed data into the
     * database again. If AUTOINCREMENT isn't set up properly in the WeatherDbHelper's table
     * create statement, then the _ID of the first insert will be reused. However, if AUTOINCREMENT
     * is setup properly, that older ID will NOT be reused, and the test will pass.
     */
    @Test
    public void testIntegerAutoincrement() {

        /* First, let's ensure we have some values in our table initially */
        testInsertSingleRecordIntoWeatherTable();

        /* Obtain weather values from TestUtilities */
        ContentValues testWeatherValues = TestUtilities.createTestWeatherContentValues();

        /* Get the date of the testWeatherValues to ensure we use a different date later */
        long originalDate = testWeatherValues.getAsLong(REFLECTED_COLUMN_DATE);

        /* Insert ContentValues into database and get a row ID back */
        long firstRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testWeatherValues);

        /* Delete the row we just inserted to see if the database will reuse the rowID */
        database.delete(
                REFLECTED_TABLE_NAME,
                "_ID == " + firstRowId,
                null);

        /*
         * Now we need to change the date associated with our test content values because the
         * database policy is to replace identical dates on conflict.
         */
        long dayAfterOriginalDate = originalDate + TimeUnit.DAYS.toMillis(1);
        testWeatherValues.put(REFLECTED_COLUMN_DATE, dayAfterOriginalDate);

        /* Insert ContentValues into database and get another row ID back */
        long secondRowId = database.insert(
                REFLECTED_TABLE_NAME,
                null,
                testWeatherValues);

        String sequentialInsertsDoNotAutoIncrementId =
                "IDs were reused and shouldn't be if autoincrement is setup properly.";
        assertNotSame(sequentialInsertsDoNotAutoIncrementId,
                firstRowId, secondRowId);
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain. Although in our case, we just have one table that we expect should be added
     * <p>
     * {@link com.example.android.sunshine.data.TABLE_NAME}.
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
        tableNameHashSet.add(REFLECTED_TABLE_NAME);
        /* Students, here is where you would add any other table names if you had them */
//        tableNameHashSet.add(MyAwesomeSuperCoolTableName);
//        tableNameHashSet.add(MyOtherCoolTableNameThatContainsOtherCoolData);

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

        /* Obtain weather values from TestUtilities */
        ContentValues testWeatherValues = TestUtilities.createTestWeatherContentValues();

        /* Insert ContentValues into database and get a row ID back */
        long weatherRowId = database.insert(
                REFLECTED_TABLE_NAME,
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
                REFLECTED_TABLE_NAME,
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

        /* Close cursor */
        weatherCursor.close();
    }
}