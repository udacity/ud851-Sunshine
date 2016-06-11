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
package com.example.android.sunshine;

import android.os.Build;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SimpleTest {

    /**
     * This will test to make sure that the current version running the app
     * will be greater than Gingerbread. We know this will be true because
     * we have designated the minimum SDK for our app to be API level 15
     * and Gingerbread is API level 9, but we wanted to demonstrate a simple
     * test that was very easy to understand that ran on the Android emulator.
     * <p/>
     * To run this test, right click within this file and click
     * Run 'testAndroidVersion.....'
     * The test should pass by default. To make the test fail, switch the
     * '<' for a '>' sign.
     */
    @Test
    public void testAndroidVersionGreaterThanGingerbread() {
        int currentAndroidVersion = Build.VERSION.SDK_INT;
        int gingerbread = Build.VERSION_CODES.GINGERBREAD;
        assertTrue(currentAndroidVersion > gingerbread);
    }
}