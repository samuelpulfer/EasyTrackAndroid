package com.example.easytrack.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.easytrack.R;
import com.example.easytrack.model.CheckTasksImpl;
import com.example.easytrack.model.HTTPRequestImpl2;
import com.example.easytrack.model.SettingsModelImpl;
import com.example.easytrack.model.iface.CheckTasks;
import com.example.easytrack.model.iface.HTTPRequest;
import com.example.easytrack.model.iface.SettingsModel;
import com.example.easytrack.tracking.TrackingInfo;
import com.example.easytrack.view.iface.OnlineActivityView;

import org.json.JSONException;
import org.json.JSONObject;



public class OnlineActivityViewImpl extends AppCompatActivity implements OnlineActivityView, CheckTasks.CheckTaskListener, HTTPRequest.HTTPRequestResultListener {

    // View Elements
    private TextView textView = null;
    private TextView connectionState = null;
    private Button multiBtn = null;
    // Other Classes
    private SettingsModel model = null;
    private TrackingInfo trackingInfo;
    private CheckTasksImpl checkTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("OnlineActivity", "onCreate");
        setContentView(R.layout.activity_online);
        setTitle("Online");
        textView = findViewById(R.id.taskOverview);
        multiBtn = findViewById(R.id.multiBtn);
        connectionState = findViewById(R.id.connectionState);
        model = new SettingsModelImpl(getApplicationContext());
        if(model.getTracking()){
            trackingInfo = new TrackingInfo(this);
        }

        // Start CheckTask
        checkTasks = new CheckTasksImpl(model);
        checkTasks.addCheckTaskListener(this);
        checkTasks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    @Override
    public void onPause(){
        Log.i("OnlineActivity", "onPause");
        if(model.getTracking()){
            trackingInfo.unregister();
        }
        checkTasks.removeCheckTaskListener(this);
        super.onPause();
    }
    @Override
    public void onResume()
    {
        Log.i("OnlineActivity", "onResume");
        if(checkTasks == null || checkTasks.getStatus() != AsyncTask.Status.RUNNING){
            checkTasks = new CheckTasksImpl(model);
            checkTasks.execute();
        }
        checkTasks.addCheckTaskListener(this);

        if(model.getTracking()){
            // Ask for permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
                }
            }
            trackingInfo.register();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        checkTasks.removeCheckTaskListener(this);
        checkTasks.cancel(true);
        super.onDestroy();
    }

    public void sendMessage(View view){
        // Button pressed
        Log.e("Button", "Pressed...");
        try {
            JSONObject jo = new JSONObject();
            jo.put("username", model.getUsername());
            jo.put("action", "nextState");
            HTTPRequest request = new HTTPRequestImpl2(model, "/Tasks");
            request.setMethod(HTTPRequest.Method.POST);
            request.setPayload(jo);
            request.addHTTPRequestResultListener(this);
            ((HTTPRequestImpl2) request).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (JSONException e) {
            Log.e("SendButton", "There went sometning wrong... " + e.getMessage());
        }
    }

    @Override
    public void setTasks(String text) {
        this.textView.setText(text);
    }

    @Override
    public void setButtonText(String name) {
        if(name.equals("nothing")){
            this.multiBtn.setEnabled(false);
            this.multiBtn.setText("Accept");
        } else {
            this.multiBtn.setEnabled(true);
            this.multiBtn.setText(name);
        }
    }

    @Override
    public void setConnectionState(String state) {
        this.connectionState.setText("Connection: " + state);
    }

    @Override
    public void setTaskUpdate(JSONObject jo) {
        Log.e("CheckTask", jo.toString());
        try {
            setButtonText(jo.getString("button"));
            setTasks(jo.getString("tasks"));
            setConnectionState("Online");
        } catch (JSONException e) {
            Log.e("CheckTask", "error in json package");
            setConnectionState("Offline");
        }
    }

    @Override
    public void setHTTPResult(JSONObject result) {
        Log.i("Button", "Button result received");
        Log.i("Button", "Msg: " + result.toString());
        setTaskUpdate(result);
    }
}
