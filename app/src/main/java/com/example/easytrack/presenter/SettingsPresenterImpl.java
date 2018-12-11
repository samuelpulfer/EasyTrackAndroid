package com.example.easytrack.presenter;

import com.example.easytrack.model.ConnectionCheckImpl;
import com.example.easytrack.model.iface.ConnectionCheck;
import com.example.easytrack.model.iface.SettingsModel;
import com.example.easytrack.view.iface.SettingsActivityView;

public class SettingsPresenterImpl implements SettingsActivityView.SettingsActivityListener, ConnectionCheck.ConnectionCheckResultListener {

    SettingsActivityView view = null;
    SettingsModel model = null;

    public SettingsPresenterImpl(SettingsActivityView view, SettingsModel model){
        this.view = view;
        this.model = model;
        view.addListener(this);
        view.setSettings(model.getUrl(), model.getUsername(), model.getPassword(), model.getTracking());
    }



    @Override
    public void saveBrnPressed(String url, String username, String password, boolean tracking) {
        model.setSettings(url, username, password, tracking);
        ConnectionCheck check = new ConnectionCheckImpl(model);
        check.addConnectionCheckResultListener(this);
        ((ConnectionCheckImpl) check).execute();
    }

    @Override
    public void setConnectionCheckResult(String result) {
        view.setConnectionLog(result);
    }
}
