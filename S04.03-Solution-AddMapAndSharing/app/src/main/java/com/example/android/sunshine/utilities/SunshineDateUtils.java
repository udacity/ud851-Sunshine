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
package com.example.android.sunshine.utilities;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.android.sunshine.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Class for handling date conversions that are useful for Sunshine.
 */
public final class SunshineDateUtils {

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;

    /**
     * This method returns the number of days since the epoch (January 01, 1970, 12:00 Midnight UTC)
     * in UTC time from the current date.
     *
     * @param date A date in milliseconds in local time.
     *
     * @return The number of days in UTC time from the epoch.
     */
    public static long getDayNumber(long date) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(date);
        return (date + gmtOffset) / DAY_IN_MILLIS;
    }

    /**
     * To make it easy to query for the exact date, we normalize all dates that go into
     * the database to the start of the day in UTC time.
     *
     * @param date The UTC date to normalize
     *
     * @return The UTC date at 12 midnight
     */
    public static long normalizeDate(long date) {
        // Normalize the start date to the beginning of the (UTC) day in local time
        long retValNew = date / DAY_IN_MILLIS * DAY_IN_MILLIS;
        return retValNew;
    }

    /**
     * Since all dates from the database are in UTC, we must convert the given date
     * (in UTC timezone) to the date in the local timezone. Ths function performs that conversion
     * using the TimeZone offset.
     *
     * @param utcDate The UTC datetime to convert to a local datetime, in milliseconds.
     * @return The local date (the UTC datetime - the TimeZone offset) in milliseconds.
     */
    public static long getLocalDateFromUTC(long utcDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(utcDate);
        return utcDate - gmtOffset;
    }

    /**
     * Since all dates from the database are in UTC, we must convert the local date to the date in
     * UTC time. This function performs that conversion using the TimeZone offset.
     *
     * @param localDate The local datetime to convert to a UTC datetime, in milliseconds.
     * @return The UTC date (the local datetime + the TimeZone offset) in milliseconds.
     */
    public static long getUTCDateFromLocal(long localDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(localDate);
        return localDate + gmtOffset;
    }

    /**
     * Helper method to convert the database representation of the date into something to display
     * to users.  As classy and polished a user experience as "20140102" is, we can do better.
     * <p/>
     * The day string for forecast uses the following logic:
     * For today: "Today, June 8"
     * For tomorrow:  "Tomorrow"
     * For the next 5 days: "Wednesday" (just the day name)
     * For all days after that: "Mon, Jun 8" (Mon, 8 Jun in UK, for example)
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds (UTC)
     * @param showFullDate Used to show a fuller-version of the date, which always contains either
     *                     the day of the week, today, or tomorrow, in addition to the date.
     *
     * @return A user-friendly representation of the date such as "Today, June 8", "Tomorrow",
     * or "Friday"
     */
    public static String getFriendlyDateString(Context context, long dateInMillis, boolean showFullDate) {

        long localDate = getLocalDateFromUTC(dateInMillis);
        long dayNumber = getDayNumber(localDate);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());

        if (dayNumber == currentDayNumber || showFullDate) {
            /*
             * If the date we're building the String for is today's date, the format
             * is "Today, June 24"
             */
            String dayName = getDayName(context, localDate);
            String readableDate = getReadableDateString(context, localDate);
            if (dayNumber - currentDayNumber < 2) {
                /*
                 * Since there is no localized format that returns "Today" or "Tomorrow" in the API
                 * levels we have to support, we take the name of the day (from SimpleDateFormat)
                 * and use it to replace the date from DateUtils. This isn't guaranteed to work,
                 * but our testing so far has been conclusively positive.
                 *
                 * For information on a simpler API to use (on API > 18), please check out the
                 * documentation on DateFormat#getBestDateTimePattern(Locale, String)
                 * https://developer.android.com/reference/android/text/format/DateFormat.html#getBestDateTimePattern
                 */
                String localizedDayName = new SimpleDateFormat("EEEE").format(localDate);
                return readableDate.replace(localizedDayName, dayName);
            } else {
                return readableDate;
            }
        } else if (dayNumber < currentDayNumber + 7) {
            /* If the input date is less than a week in the future, just return the day name. */
            return getDayName(context, localDate);
        } else {
            int flags = DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_NO_YEAR
                    | DateUtils.FORMAT_ABBREV_ALL
                    | DateUtils.FORMAT_SHOW_WEEKDAY;

            return DateUtils.formatDateTime(context, localDate, flags);
        }
    }

    /**
     * Returns a date string in the format specified, which shows a date, without a year,
     * abbreviated, showing the full weekday.
     *
     * @param context      Used by DateUtils to formate the date in the current locale
     * @param timeInMillis Time in milliseconds since the epoch (local time)
     *
     * @return The formatted date string
     */
    private static String getReadableDateString(Context context, long timeInMillis) {
        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NO_YEAR
                | DateUtils.FORMAT_SHOW_WEEKDAY;

        return DateUtils.formatDateTime(context, timeInMillis, flags);
    }

    /**
     * Given a day, returns just the name to use for that day.
     *   E.g "today", "tomorrow", "Wednesday".
     *
     * @param context      Context to use for resource localization
     * @param dateInMillis The date in milliseconds (local time)
     *
     * @return the string day of the week
     */
    private static String getDayName(Context context, long dateInMillis) {
        /*
         * If the date is today, return the localized version of "Today" instead of the actual
         * day name.
         */
        long dayNumber = getDayNumber(dateInMillis);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());
        if (dayNumber == currentDayNumber) {
            return context.getString(R.string.today);
        } else if (dayNumber == currentDayNumber + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            /*
             * Otherwise, if the day is not today, the format is just the day of the week
             * (e.g "Wednesday")
             */
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }
}