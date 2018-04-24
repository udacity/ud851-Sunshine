package com.example.android.sunshine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dell on 16-04-2018.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private String[] weatherData;
    public ForecastAdapter() {

    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item,parent,false);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        holder.mWeatherTextView.setText(weatherData[position]);
    }

    @Override
    public int getItemCount() {
        if(weatherData == null)
            return 0;
        return weatherData.length;
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{
        private final TextView mWeatherTextView;
        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
        }
    }

    public void setWeatherData(String[] data) {
        weatherData =data;
        notifyDataSetChanged();
    }
}
