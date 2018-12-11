package com.example.easytrack.model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.easytrack.model.iface.CheckTasks;
import com.example.easytrack.model.iface.HTTPRequest;
import com.example.easytrack.model.iface.SettingsModel;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckTasksImpl extends AsyncTask<Void,JSONObject,Void> implements CheckTasks, HTTPRequest.HTTPRequestResultListener {
    private String servlet = "/Tasks?carrier=";
    private SettingsModel settingsModel;
    private Set<CheckTaskListener> listeners = new HashSet<>();
    private boolean running = true;
    private int sleep = 2;

    public CheckTasksImpl(SettingsModel settingsModel){
        this.settingsModel = settingsModel;
        this.servlet += settingsModel.getUsername();
        Log.i("CheckTask", "Object created");
    }

    @Override
    protected  void onPreExecute(){
        Log.i("CheckTask", "onPreExecute");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i("CheckTask", "doInBackground");
        while(this.isCancelled() == false){
            HTTPRequestImpl2 request = new HTTPRequestImpl2(this.settingsModel, servlet);
            request.setMethod(HTTPRequest.Method.GET);
            request.addHTTPRequestResultListener(this);
            publishProgress(request.doIt());
            try {
                TimeUnit.SECONDS.sleep(sleep);
            } catch(InterruptedException e){

            }
            Log.i("CheckTask", "within runnig loop");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(JSONObject... jos) {
        Log.i("CHECKTASK RESULT", jos[0].toString());
        for(CheckTaskListener listener:listeners){
            listener.setTaskUpdate(jos[0]);
        }
    }

    @Override
    protected void onPostExecute(Void voids) {
        Log.e("CheckTask", "onPostExecute");
    }

    @Override
    public void addCheckTaskListener(CheckTaskListener listener) {
        this.listeners.add(listener);
        Log.i("CheckTask", "Listener added");
    }

    @Override
    public void removeCheckTaskListener(CheckTaskListener listener) {
        this.listeners.remove(listener);
        Log.i("CheckTask", "listener removed");
    }

    @Override
    public void setHTTPResult(JSONObject result) {
        publishProgress(result);
    }
}
