package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Maribel on 1/20/2018.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    String[] mWeatherData;

    public ForecastAdapter(){

    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ForecastAdapterViewHolder forecastAdapterViewHolder = null;
        try {
            Context cContext = parent.getContext();
            int iForecastList = R.layout.forecast_list_item;
            LayoutInflater layoutInflater = LayoutInflater.from(cContext);
            View vView = layoutInflater.inflate(iForecastList, parent, false);
            forecastAdapterViewHolder = new ForecastAdapterViewHolder(vView);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return forecastAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        String weather = mWeatherData[position];
        holder.mWeatherTextView.setText(weather);
    }

    @Override
    public int getItemCount() {
        int iReturn = 0;

        if(mWeatherData == null)
        {
            iReturn = 0;
        }
        else
        {
            iReturn = mWeatherData.length;
        }
        return iReturn;
    }

    public void setmWeatherData(String[] sDataArray){
                mWeatherData = sDataArray;

        notifyDataSetChanged();
    }


    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View view){
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);
        }
    }
}
