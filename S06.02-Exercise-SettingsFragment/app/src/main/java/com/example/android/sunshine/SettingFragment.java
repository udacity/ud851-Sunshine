package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    // COMPLETED (10) Implement OnSharedPreferenceChangeListener from SettingsFragment
    // COMPLETED (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = getPreferenceManager().findPreference(key);
        if (pref instanceof EditTextPreference){
            Object value = sharedPreferences.getString(key, getString(R.string.pref_location_key));
            setPreferenceSummary(pref,value);
        } else if (pref instanceof ListPreference){
            Object value = sharedPreferences.getString(key, getString(R.string.pref_location_key));
            setPreferenceSummary(pref,value);
        }
    }

    // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences_layout);
        PreferenceScreen ps = getPreferenceScreen();
        SharedPreferences sp = ps.getSharedPreferences();
        int count = ps.getPreferenceCount();
        for ( int i=0; i< count; i++){
            Preference pref = ps.getPreference(i);
            String key = pref.getKey();
            if (!(pref instanceof CheckBoxPreference)){
                Object value = sp.getString(key,null);
                setPreferenceSummary(pref, value);
            }
        }

    }
    // COMPLETED (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference
    public void setPreferenceSummary(Preference preference, Object newValue){

        preference.setSummary(newValue.toString());

    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
