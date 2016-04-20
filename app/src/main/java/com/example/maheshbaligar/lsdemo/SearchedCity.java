package com.example.maheshbaligar.lsdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchedCity extends AppCompatActivity {

    public static String City;

    //ArrayList
    public ArrayList<WeatherBean> mWeahterList;
    ListView listView;
    WeatherListAdapter adapter;

    ProgressDialog mProgressDialog;

    JSONArray mJson_array;
    JSONObject mJson_object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(5);
        actionBar.setTitle("Searched City Weather");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        City = getIntent().getStringExtra("CityName");
        mWeahterList = new ArrayList<WeatherBean>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);


        listView = (ListView) findViewById(R.id.list);
        mProgressDialog = new ProgressDialog(SearchedCity.this);
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.show();
        updateUI();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {

        TextView Cityname = (TextView) findViewById(R.id.city_name);
        Cityname.setText(City);
        Log.e("cityName", "" + Cityname);

        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + City.trim() + "&cnt=14&APPID=c215d22e804ff5f4853550df3efa600d";
        //  Log.e("Mumabi", url);
        JsonParser jParser = new JsonParser();
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        try {

            mJson_array = json.getJSONArray("list");

            //    JSONArray json_weather=mJson_array.getJSONArray("Weather");

            for (int i = 0; i < mJson_array.length(); i++) {

                mJson_object = mJson_array.getJSONObject(i);

                JSONObject mMain = mJson_object.getJSONObject("temp");
                String date = mJson_object.getString("dt");
                String day_temp = mMain.getString("day");
                String min_temp = mMain.getString("min");
                String max_temp = mMain.getString("max");
                String night_temp = mMain.getString("night");
                String eve_temp = mMain.getString("eve");
                String morning_temp = mMain.getString("morn");

                //date conversion
                Date date1 = new Date(Long.parseLong(date) * 1000);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                String DATE = df2.format(date1);

                Log.e("DATE", "" + date);
                //day_temp conversion
                float DAY_TEMP = Float.parseFloat(day_temp) - 273.15F;
                //min_temp conversion
                float MIN_TEMP = Float.parseFloat(min_temp) - 273.15F;
                //max_temp conversion
                float MAX_TEMP = Float.parseFloat(max_temp) - 273.15F;
                //night_temp conversion
                float NIGHT_TEMP = Float.parseFloat(night_temp) - 273.15F;
                //eve_temp conversion
                float EVE_TEMP = Float.parseFloat(eve_temp) - 273.15F;
                //morning_temp conversion
                float MORNING_TEMP = Float.parseFloat(morning_temp) - 273.15F;

                //get main weather from weather array
                JSONArray MAIN_WEATHER_Array = new JSONArray(mJson_array.getJSONObject(i).getString("weather"));
                String MAIN_WEATHER = null;
                for (int j = 0; j < MAIN_WEATHER_Array.length(); j++) {
                    MAIN_WEATHER = MAIN_WEATHER_Array.getJSONObject(j).getString("main");
                }

                WeatherBean bean = new WeatherBean();
                bean.setDate(DATE);
                bean.setDayTemp(String.valueOf(DAY_TEMP));
                bean.setMin_Temp(String.valueOf(MIN_TEMP));
                bean.setMax_Temp(String.valueOf(MAX_TEMP));
                bean.setNight_Temp(String.valueOf(NIGHT_TEMP));
                bean.setEve_Temp(String.valueOf(EVE_TEMP));
                bean.setMorning_Temp(String.valueOf(MORNING_TEMP));
                bean.setMain_Temp(MAIN_WEATHER);


                mWeahterList.add(bean);

                adapter = new WeatherListAdapter(SearchedCity.this, R.layout.weather_list_item,
                        mWeahterList);
                listView.setAdapter(adapter);

                mProgressDialog.dismiss();

            }
        } catch (Exception e) {
            Log.e("ERROR : ", "" + e);
        }


    }

//code


}




