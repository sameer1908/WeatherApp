package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements WeatherRVadapter1 {
    private Context context;
    private ArrayList<WeatherRVmodel> weatherRVmodelArrayList;

    public WeatherRVadapter(Context context, ArrayList<WeatherRVmodel> weatherRVmodelArrayList) {
        this.context = context;
        this.weatherRVmodelArrayList = weatherRVmodelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVadapter.ViewHolder holder, int position) {
        WeatherRVmodel vmodel = weatherRVmodelArrayList.get(position);
        holder.temperatureTV.setText(vmodel.getTemperature()+"°C");
        Picasso.get().load("http://".concat(vmodel.getIcon())).into(holder.conditionIV);
        holder.windTV.setText(vmodel.getWindspeed()+"Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm");
        try{
            Date t = input.parse(vmodel.getTime());
            holder.timeTV.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return weatherRVmodelArrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView temperatureTV,windTV,timeTV;
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperatureTV = itemView.findViewById(R.id.id_TV_Temperature);
            windTV = itemView.findViewById(R.id.id_TV_WindSpeed);
            timeTV = itemView.findViewById(R.id.id_TV_Time);
            conditionIV = itemView.findViewById(R.id.id_TV_Condition);

        }
    }
}
