package com.example.weatherapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV,conditionTV,temperatureTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdit;
    private ImageView backIV,iconIV,searchIV;
    private ArrayList<WeatherRVmodel> weatherRVmodelArrayList;
    private WeatherRVadapter weatherRVadapter;
    private Manifest manifest;
    private LocationManager locationManager;
    private String cityName;
    private int PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.RL_home);
        loadingPB = findViewById(R.id.id_loading);
        cityNameTV = findViewById(R.id.id_TV_CityName);
        temperatureTV = findViewById(R.id.id_TV_Temperature);
        conditionTV = findViewById(R.id.id_TV_Condition);
        weatherRV = findViewById(R.id.id_RV_weather);
        cityEdit = findViewById(R.id.id_EditCity);
        backIV = findViewById(R.id.background_image);
        iconIV = findViewById(R.id.id_IV_Icon);
        searchIV = findViewById(R.id.id_IV_Search);
        weatherRVmodelArrayList = new ArrayList<>();
        weatherRVadapter = new WeatherRVadapter(this,weatherRVmodelArrayList);
        weatherRV.setAdapter(weatherRVadapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if((ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(),location.getLatitude());
        getWeatherInfo(cityName);
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEdit.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter city name",Toast.LENGTH_SHORT).show();
                }else {
                    cityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted...",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"Please provide the permissions",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(Double longitude, double latitude){
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try{
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
            for(Address adr : addresses){
                if(adr!=null){
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")){
                        cityName = city;
                    }
                    else{
                        Log.d("TAG","City is not found");
                        Toast.makeText(this,"City is not found...",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }
    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=b612af147a554faf96a172038231701&q="+cityName+"&days=1&aqi=yes&alerts=yes";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                weatherRVmodelArrayList.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature+"°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String conditon = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditonicon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditonicon)).into(iconIV);
                    conditionTV.setText(conditon);
                    if(isDay==1){
                        // morning
                        Picasso.get().load("https://cdn.pixabay.com/photo/2016/11/21/03/56/landscape-1844227_960_720.png").into(backIV);
                    }else{
                        Picasso.get().load("https://cdn.pixabay.com/photo/2016/11/21/03/56/landscape-1844231_960_720.png").into(backIV);
                    }

                    JSONObject forcastObj = response.getJSONObject("forcast");
                    JSONObject forcastO = response.getJSONArray("forcastday").getJSONObject(0);
                    JSONArray hourArray = forcastO.getJSONArray("hour");
                    for(int i=0;i<hourArray.length();i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String temper = hourObj.getString("temp_c");
                        String img = hourObj.getString("icon");
                        String wind = hourObj.getString("wind_kph");

                        weatherRVmodelArrayList.add(new WeatherRVmodel(time,temper,img,wind));
                    }
                    weatherRVadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Please enter valid city name...",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}