package com.example.easytrack.tracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
//import com.example.easytrack.communication.HTTPRequest;
import com.example.easytrack.model.HTTPRequestImpl2;
import com.example.easytrack.model.SettingsModelImpl;
import com.example.easytrack.model.iface.HTTPRequest;
import com.example.easytrack.model.iface.SettingsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackingInfo implements Runnable{
    private WifiManager mWifiManager;
    private SharedPreferences settings;
    private AppCompatActivity appCompatActivity;
    private SettingsModel model;
    private boolean registered = false;

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = mWifiManager.getScanResults();
                // add your logic here
                JSONArray wifiInfo = new JSONArray();
                StringBuilder sb = new StringBuilder();
                mScanResults.sort(new Comparator<ScanResult>() {
                    @Override
                    public int compare(ScanResult o1, ScanResult o2) {
                        return o2.level - o1.level;
                    }
                });
                for(ScanResult sr:mScanResults){
                    JSONObject wifiNetwork = new JSONObject();
                    try {
                        wifiNetwork.put("SSID", sr.SSID);
                        wifiNetwork.put("BSSID", sr.BSSID);
                        wifiNetwork.put("level", sr.level);
                        //wifiNetwork.put("frequency", sr.frequency);
                        //wifiNetwork.put("centerFreq0", sr.centerFreq0);
                        //wifiNetwork.put("centerFreq1", sr.centerFreq1);
                        //wifiNetwork.put("channelWidth", sr.channelWidth);
                        //wifiNetwork.put("timestamp", sr.timestamp);
                        //wifiNetwork.put("capabilities", sr.capabilities);
                    } catch (JSONException e){

                    }
                    wifiInfo.put(wifiNetwork);
                    sb.append("SSID: " + sr.SSID + " BSSID: " + sr.BSSID + " level: " + sr.level + "\n");
                }
                JSONObject tosend = new JSONObject();
                try {
                    tosend.put("WifiNetworks", wifiInfo);
                    tosend.put("CurrentBSSID", mWifiManager.getConnectionInfo().getBSSID());
                } catch (JSONException e) {

                }

                //new HTTPRequest("/Location", settings).execute(tosend);
                //HTTPRequest request = new HTTPRequestImpl2(settings, "/Location");
                HTTPRequest request = new HTTPRequestImpl2(model, "/Location");
                request.setMethod(HTTPRequest.Method.POST);
                request.setPayload(tosend);
                ((HTTPRequestImpl2) request).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    };
    public TrackingInfo(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        model = new SettingsModelImpl(this.appCompatActivity);
        mWifiManager = (WifiManager) appCompatActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        settings = appCompatActivity.getSharedPreferences("Settings",Context.MODE_PRIVATE);
    }
    public void track(){
        mWifiManager.startScan();
    }
    public void register(){
        appCompatActivity.registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        registered = true;
        new Thread(this).start();
    }
    public void unregister(){
        appCompatActivity.unregisterReceiver(mWifiScanReceiver);
        registered = false;
    }
    public void run(){
        try{
            TimeUnit.SECONDS.sleep(2);
            if(registered == true){
                track();
                new Thread(this).start();
            }
        } catch(InterruptedException e){

        }
    }
}
