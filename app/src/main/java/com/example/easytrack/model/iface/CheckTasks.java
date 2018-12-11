package com.example.easytrack.model.iface;

import org.json.JSONObject;

public interface CheckTasks {
    interface CheckTaskListener {
        void setTaskUpdate(JSONObject jo);
    }

    void addCheckTaskListener(CheckTaskListener listener);
    void removeCheckTaskListener(CheckTaskListener listener);

}
