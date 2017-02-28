package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by elena on 28/02/17.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] mWeatherData;

    //constructor
    public ForecastAdapter(){
        super();
    }

     class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{

         private final TextView mWeatherTextView;

         public ForecastAdapterViewHolder(View view){
             super(view);
             mWeatherTextView = (TextView)view.findViewById(R.id.tv_weather_data);
         }

         void bind(int listIndex) {
             mWeatherTextView.setText(String.valueOf(listIndex));
         }

    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ForecastAdapterViewHolder forecastAdapterViewHolder = new ForecastAdapterViewHolder(view);
        return forecastAdapterViewHolder;
    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount(){
        if(mWeatherData == null){
            return 0;
        }
        return mWeatherData.length;
    }

    public void setWeatherData(String [] weatherData){
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}
