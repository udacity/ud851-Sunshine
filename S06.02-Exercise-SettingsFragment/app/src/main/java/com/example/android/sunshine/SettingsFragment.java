package com.example.android.sunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple Fragment
 * create an instance of this fragment.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    // COMPLETED TODO (4) Create SettingsFragment and extend PreferenceFragmentCompat
    // Do steps 5 - 11 within SettingsFragment
    // COMPLETED TODO (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

    // COMPLETED TODO (8) Create a method called setPreferenceSummary that accepts a Preference and an Object and sets the summary of the preference

    //COMPLETED TODO (5) Override onCreatePreferences and add the preference xml file using addPreferencesFromResource

    // Do step 9 within onCreatePreference
    // COMPLETED TODO (9) Set the preference summary on each preference that isn't a CheckBoxPreference

    // COMPLETED TODO (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop

    // COMPLETED TODO (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart

    // COMPLETED TODO (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.pref_general);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        // all of the preferences if it is not a checkbox preference, call the setSummary method
        // passing in a preference and the value of the preference
        int count = preferenceScreen.getPreferenceCount();
        for(int i = 0; i < count; i++){
            Preference preference = preferenceScreen.getPreference(i);
            if( !(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(),"");
                //Call helper method
                setPreferenceSummary(preference, value);
            }
        }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(null != preference){
            if(!(preference instanceof  CheckBoxPreference)){
                //value that the user changed
                String value = sharedPreferences.getString(preference.getKey(), "");
                //Call the utility method to set the preference in the list
                setPreferenceSummary(preference, value);
            }
        }
    }

    private void setPreferenceSummary(Preference preferece, Object object){
        //the value of the entry changed by the user
        String stringValue = object.toString();
        String key = preferece.getKey();

        if(preferece instanceof ListPreference){
            ListPreference listPreference = (ListPreference)preferece;
            //Getting the index of the item in the list changed by the user
            int indexOfValue = listPreference.findIndexOfValue(stringValue);
            if(indexOfValue >= 0){
                preferece.setSummary(listPreference.getEntries()[indexOfValue]);
            }else {
                preferece.setSummary(stringValue);
            }

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);

    }


}
