package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        // TODO (2) Set setDisplayHomeAsUpEnabled to true on the support ActionBar
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int itemId = item.getItemId();

        //TO GO BACK TO PREVIOUS ACTIVITY with android.R.id.home
        if(itemId == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }


}
