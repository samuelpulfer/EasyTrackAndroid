package com.example.easytrack.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.easytrack.model.iface.HTTPRequest;
import com.example.easytrack.model.iface.SettingsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class HTTPRequestImpl2 extends AsyncTask<Void,Void,JSONObject> implements HTTPRequest {

    private Set<HTTPRequestResultListener> listeners = new HashSet<>();
    private SettingsModel settingsModel = null;

    private String servletName = "/NeverLand";
    private Method method = Method.GET;
    private JSONObject payload;

    public HTTPRequestImpl2(SettingsModel settingsModel, String servletName){
        Log.i("HTTPRequest", "constructor");
        this.settingsModel = settingsModel;
        this.servletName = servletName;
    }

    public JSONObject doIt(){
        Log.i("HTTPRequest", "doInBackground");
        JSONObject result = null;
        String encAuth = "nothing";
        try {
            encAuth = settingsModel.getEncAuth();
        } catch(UnsupportedEncodingException e){
        }
        try {
            URL url = new URL(settingsModel.getUrl() + servletName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toString());
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + encAuth);
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            if(method == Method.GET){
                doGet(conn);
            } else {
                doPost(conn);
            }
            if(conn.getResponseCode() == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                    sb.append("\n");
                }
                br.close();

                result = new JSONObject(sb.toString());
                Log.i("Content", result.toString());
            } else {
                result = new JSONObject();
                result.put("Error", conn.getResponseMessage());
                result.put("ErrorCode", conn.getResponseCode());
                result.put("URL", settingsModel.getUrl() + servletName);
            }
            conn.disconnect();
        } catch (Exception e) {
            result = new JSONObject();
            try {
                result.put("Error", e.getMessage());
            } catch(JSONException err) {

            }
        }
        return result;
    }

    private void doGet(HttpURLConnection conn) throws Exception {
        conn.setUseCaches(false);
        conn.setDoOutput(false);
        conn.setInstanceFollowRedirects(false);
        return;
    }

    private void doPost(HttpURLConnection conn) throws Exception{
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        os.writeBytes(payload.toString());
        os.flush();
        os.close();
        return;
    }

    @Override
    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }

    @Override
    public void addHTTPRequestResultListener(HTTPRequestResultListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeHTTPRequestResultListener(HTTPRequestResultListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    protected  void onPreExecute(){
        Log.i("HTTPRequest", "onPreExecute");
        return;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        return doIt();
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
        //return null;
        Log.e("HTTPRequest", "onProgressUpdate");
        return;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        Log.i("HTTPRequest", "onPostExecute");
        Log.i("HTTPRequest", "Response: " + response.toString());
        for(HTTPRequestResultListener listener:listeners){
            listener.setHTTPResult(response);
        }
        return;
    }
}
