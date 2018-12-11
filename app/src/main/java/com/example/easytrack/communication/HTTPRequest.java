package com.example.easytrack.communication;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest extends AsyncTask<JSONObject,Void,String> {
    private String servletName;
    private SharedPreferences settings;

    private void setPreferences(SharedPreferences settings){
        this.settings = settings;
    }
    private void setServletName(String servletName){
        this.servletName = "/" + servletName;
    }
    public HTTPRequest(String servletName, SharedPreferences settings){
        setServletName(servletName);
        setPreferences(settings);
    }

    public JSONObject something(JSONObject request){
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
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + encAuth);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("Hello", "World");

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(request.toString());

            os.flush();
            os.close();

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
    protected String doInBackground(JSONObject... asdf) {
        return something(asdf[0]).toString();
    }
    @Override
    protected void onProgressUpdate(Void... voids) {
        //return null;
    }
    @Override
    protected void onPostExecute(String something) {

    }
}
