package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

import com.example.android.sunshine.sync.SunshineSyncIntentService;

// COMPLETED (9) Create a class called SunshineSyncUtils
public final class SunshineSyncUtils {
    //  COMPLETED (10) Create a public static void method called startImmediateSync
    public static void startImmediateSync(Context context) {
        //  COMPLETED (11) Within that method, start the SunshineSyncIntentService
        Intent startService = new Intent(context, SunshineSyncIntentService.class);
        context.startService(startService);
    }
}