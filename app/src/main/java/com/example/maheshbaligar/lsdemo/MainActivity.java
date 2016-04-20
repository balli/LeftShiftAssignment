package com.example.maheshbaligar.lsdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    //ArrayList
   public ArrayList<WeatherBean> mWeahterList;
    ListView listView;
    WeatherListAdapter adapter;

    ProgressDialog mProgressDialog;

    JSONArray mJson_array;
    JSONObject mJson_object;

    Geocoder geocoder;
    //Location
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    //end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Location
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        //end

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(5);
        actionBar.setTitle("Current City Weather");

            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Loading...");
            mProgressDialog.show();
            updateUI();


        mWeahterList=new ArrayList<WeatherBean>();
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        listView=(ListView)findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,ListOfCities.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());

            String result = null;
            try {
                List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                if (addressList != null && addressList.size() > 0) {

                    result = addressList.get(0).getAddressLine(2);

                    String mCityName = result.substring(0, result.indexOf(','));

                    TextView Cityname=(TextView)findViewById(R.id.city_name);
                    Cityname.setText(mCityName);

                    Log.e("cityName",""+mCityName);


                    if (result != null) {
                        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + mCityName.trim() + "&cnt=14&APPID=c215d22e804ff5f4853550df3efa600d";
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
                                String day_temp=mMain.getString("day");
                                String min_temp = mMain.getString("min");
                                String max_temp = mMain.getString("max");
                                String night_temp = mMain.getString("night");
                                String eve_temp = mMain.getString("eve");
                                String morning_temp = mMain.getString("morn");

                                //date conversion
                                Date date1=new Date(Long.parseLong(date) * 1000);
                                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                                String DATE = df2.format(date1);

                                Log.e("DATE",""+date);
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
                                String MAIN_WEATHER=null;
                                for (int j = 0; j < MAIN_WEATHER_Array.length(); j++) {
                                     MAIN_WEATHER = MAIN_WEATHER_Array.getJSONObject(j).getString("main");
                                }

                                WeatherBean bean=new WeatherBean();
                                bean.setDate(DATE);
                                bean.setDayTemp(String.valueOf(DAY_TEMP));
                                bean.setMin_Temp(String.valueOf(MIN_TEMP));
                                bean.setMax_Temp(String.valueOf(MAX_TEMP));
                                bean.setNight_Temp(String.valueOf(NIGHT_TEMP));
                                bean.setEve_Temp(String.valueOf(EVE_TEMP));
                                bean.setMorning_Temp(String.valueOf(MORNING_TEMP));
                                bean.setMain_Temp(MAIN_WEATHER);

                                mWeahterList.add(bean);

                                adapter = new WeatherListAdapter (MainActivity.this, R.layout.weather_list_item,
                                        mWeahterList);
                                listView.setAdapter(adapter);

                                mProgressDialog.dismiss();

                            }
                        } catch (Exception e) {
                            Log.e("ERROR : ", "" + e);
                        }
                    }

                }
            } catch (IOException e) {
                Log.e(TAG, "Unable connect to Geocoder", e);
            }
//code

        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
