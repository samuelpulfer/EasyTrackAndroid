package com.example.easytrack.communication;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest2 extends AsyncTask<Void,Void,String> {
    private String servletName;
    private SharedPreferences settings;
    private TextView textView;

    private void setPreferences(SharedPreferences settings){
        this.settings = settings;
    }
    private void setServletName(String servletName){
        this.servletName = "/" + servletName;
    }
    public void setTextView(TextView textView){
        this.textView = textView;
    }
    public HTTPRequest2(String servletName, SharedPreferences settings){
        setServletName(servletName);
        setPreferences(settings);
    }

    public JSONObject something(){
        JSONObject result = null;
        String plainAuth = settings.getString("username","nothing") + ":" + settings.getString("password", "nothing");
        String encAuth = "nothing";
        try {
            encAuth = Base64.encodeToString(plainAuth.getBytes("UTF-8"),Base64.DEFAULT);
        } catch(UnsupportedEncodingException e){

        }


        try {
            URL url = new URL(settings.getString("url","http://nothing.com/EasyTrack") + servletName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + encAuth);
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setInstanceFollowRedirects(false);

            Log.i("Method", conn.getRequestMethod());
            Log.i("URL", url.toString());
            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            if(conn.getResponseCode() == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line+"\n");
                }
                br.close();

                result = new JSONObject(sb.toString());
                Log.i("Content", result.toString());
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected  void onPreExecute(){

    }
    @Override
    protected String doInBackground(Void... voids) {
        try {
            return something().getString("RawLocation");
        } catch (JSONException e) {
            return "Oh crap... " + e.getMessage();
        }

    }
    @Override
    protected void onProgressUpdate(Void... voids) {
        //return null;
    }
    @Override
    protected void onPostExecute(String someValues) {
        textView.setText(someValues);
    }
}
