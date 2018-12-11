package com.example.easytrack.presenter;

import android.util.Log;

import com.example.easytrack.model.CheckTasksImpl;
import com.example.easytrack.model.iface.CheckTasks;
import com.example.easytrack.model.iface.SettingsModel;
import com.example.easytrack.view.iface.OnlineActivityView;

import org.json.JSONException;
import org.json.JSONObject;

public class OnlinePresenterImpl implements CheckTasks.CheckTaskListener {

    OnlineActivityView view;
    CheckTasksImpl checkTasks;
    SettingsModel settingsModel;

    public OnlinePresenterImpl(OnlineActivityView view, SettingsModel model){
        this.view = view;
        this.settingsModel = model;
    }


    @Override
    public void setTaskUpdate(JSONObject jo) {
        Log.e("HELLO","I was notified !!! " + jo.toString());
        try {
            if (jo.getString("Error") != null){
                view.setConnectionState("Offline");
            } else {
                view.setButtonText(jo.getString("button"));
                view.setTasks(jo.getString("tasks"));
            }
        } catch (JSONException e){
            view.setConnectionState("Something went wrong...");
        }
    }
}
