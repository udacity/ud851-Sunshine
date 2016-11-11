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

        // TODO (2) Create an xml resource directory
        // TODO (3) Add a PreferenceScreen with an EditTextPreference and ListPreference within the newly created xml resource directory

        // TODO (4) Create SettingsFragment and extend PreferenceFragmentCompat

        // Do steps 5 - 11 within SettingsFragment
        // TODO (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

        // TODO (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference

        // TODO (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource

        // Do step 9 within onCreatePreference
        // TODO (9) Set the preference summary on each preference that isn't a CheckBoxPreference

        // TODO (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

        // TODO (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart

        // TODO (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
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