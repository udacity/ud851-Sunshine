package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;

// COMPLETED (5) Create a new class called SunshineSyncIntentService that extends IntentService
public class  SunshineSyncIntentService extends IntentService {
//  COMPLETED (6) Create a constructor that calls super and passes the name of this class
    public SunshineSyncIntentService(){
        super("SunshineSyncIntentService");
    }
//  COMPLETED (7) Override onHandleIntent, and within it, call SunshineSyncTask.syncWeather
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            SunshineSyncTask.syncWeather(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}