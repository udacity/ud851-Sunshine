package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

// TODO (22) Extend RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{

    // TODO (23) Create a private string array called mWeatherData
    private String[] mWeatherData = {};
    // TODO (47) Create the default constructor (we will pass in parameters in a later lesson)
    ForecastAdapter(){}
    // TODO (16) Create a class within ForecastAdapter called ForecastAdapterViewHolder
    // TODO (17) Extend RecyclerView.ViewHolder
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{
        // TODO (18) Create a public final TextView variable called mWeatherTextView
        public TextView mWeatherTextView = null;
        // TODO (19) Create a constructor for this class that accepts a View as a parameter
        // TODO (20) Call super(view) within the constructor for ForecastAdapterViewHolder
        // TODO (21) Using view.findViewById, get a reference to this layout's TextView and save it to mWeatherTextView
        public  ForecastAdapterViewHolder(View view){
            super(view);
            mWeatherTextView = (TextView)view.findViewById(R.id.tv_weather_data);
        }

    }
    // TODO (24) Override onCreateViewHolder
    // TODO (25) Within onCreateViewHolder, inflate the list item xml into a view
    // TODO (26) Within onCreateViewHolder, return a new ForecastAdapterViewHolder with the above view passed in as a parameter
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item,parent, false);
        return new ForecastAdapterViewHolder(rootView);
    }

    // TODO (27) Override onBindViewHolder
    // TODO (28) Set the text of the TextView to the weather for this list item's position
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        String weatherData = mWeatherData[position];

        holder.mWeatherTextView.setText(weatherData);
    }

    // TODO (29) Override getItemCount
    // TODO (30) Return 0 if mWeatherData is null, or the size of mWeatherData if it is not null
    @Override
    public int getItemCount() {
        if (mWeatherData == null){
            return 0;
        }
        return mWeatherData.length;
    }

    // TODO (31) Create a setWeatherData method that saves the weatherData to mWeatherData
    // TODO (32) After you save mWeatherData, call notifyDataSetChanged
    public void setWeatherData( String[] weatherData){
        mWeatherData = weatherData;
        this.notifyDataSetChanged();
    }
}
