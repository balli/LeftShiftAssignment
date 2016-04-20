package com.example.maheshbaligar.lsdemo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by MAHESH BALIGAR on 19-04-2016.
 */
public  class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    StringBuilder sb = null;


    // constructor
    public JsonParser() {

    }

    public JSONObject getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
        }
        catch (Exception e)
        {
          Log.e("ERROR : ",""+e);
        }

        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is), 1000);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");

            String line = "0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
            json = sb.toString();

            Log.e("Json_result" ,"Json_result : "+json);

        } catch (Exception e) {
            // TODO: handle exception
        }


        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}