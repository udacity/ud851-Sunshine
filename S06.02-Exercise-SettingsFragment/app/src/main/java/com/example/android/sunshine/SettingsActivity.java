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
package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.MenuItem;

/**
 * Loads the SettingsFragment and handles the proper behavior of the up button.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // COMPLETED (2) Create an xml resource directory
        // COMPLETED (3) Add a PreferenceScreen with an EditTextPreference and ListPreference within the newly created xml resource directory

        // COMPLETED (4) Create SettingsFragment and extend PreferenceFragmentCompat

        // Do steps 5 - 11 within SettingsFragment



        // COMPLETED (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource

        // Do step 9 within onCreatePreference


        // COMPLETED (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

        // COMPLETED (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}