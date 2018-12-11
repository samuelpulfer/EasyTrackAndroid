package com.example.easytrack.model;

import android.os.AsyncTask;

import com.example.easytrack.model.iface.ConnectionCheck;
import com.example.easytrack.model.iface.SettingsModel;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ConnectionCheckImpl extends AsyncTask<Void,Void,String> implements ConnectionCheck {

    private static final String SERVLET_NAME = "/Carrier";
    private SettingsModel settingsModel = null;
    private Set<ConnectionCheckResultListener> listeners = new HashSet<>();

    public ConnectionCheckImpl(SettingsModel settingsModel){
        this.settingsModel = settingsModel;
    }

    @Override
    protected  void onPreExecute(){

    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder sb = new StringBuilder();
        sb.append("Check connection...\n");
        String encAuth = "nothing";
        try {
            encAuth = settingsModel.getEncAuth();
        } catch(UnsupportedEncodingException e){
            sb.append("Charset UTF-8 not supported\n");
        }
        try {
            URL url = new URL(settingsModel.getUrl() + SERVLET_NAME);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Basic " + encAuth);
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);

            sb.append("Response Code: " + conn.getResponseCode() + "\n");
            sb.append("Response Message: " + conn.getResponseMessage() + "\n");
            conn.disconnect();
        } catch (Exception e) {
            sb.append("Exception: " + e.getMessage() + "\n");
        }
        sb.append("Check finished.\n");
        return sb.toString();
    }

    @Override
    protected void onProgressUpdate(Void... voids) {
        //return null;
    }

    @Override
    protected void onPostExecute(String response) {
        for(ConnectionCheckResultListener listener:listeners){
            listener.setConnectionCheckResult(response);
        }
    }

    @Override
    public void addConnectionCheckResultListener(ConnectionCheckResultListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeConnectionCheckResultListener(ConnectionCheckResultListener listener) {
        this.listeners.remove(listener);
    }
}
