package com.example.easytrack.view;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.easytrack.R;
import com.example.easytrack.model.SettingsModelImpl;
import com.example.easytrack.model.iface.SettingsModel;
import com.example.easytrack.presenter.SettingsPresenterImpl;
import com.example.easytrack.view.iface.SettingsActivityView;

import java.util.HashSet;
import java.util.Set;


public class SettingsActivityViewImpl extends AppCompatActivity implements SettingsActivityView {

    private Set<SettingsActivityListener> listeners = new HashSet<>();

    EditText url;
    EditText username;
    EditText password;
    TextView connectionLog;
    Switch trackEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        url = findViewById(R.id.serverURI);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        connectionLog = findViewById(R.id.textView_msg);
        trackEnabled = findViewById(R.id.switch1);


        SettingsModel model = new SettingsModelImpl(getApplicationContext());
        new SettingsPresenterImpl(this, model);
    }

    public void saveBtn(View view) {
        for(SettingsActivityListener listener:listeners){
            listener.saveBrnPressed(url.getText().toString(),username.getText().toString(), password.getText().toString(), trackEnabled.isChecked());
        }
    }

    @Override
    public void addListener(SettingsActivityListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(SettingsActivityListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void setSettings(String url, String username, String password, boolean tracking) {
        this.url.setText(url);
        this.username.setText(username);
        this.password.setText(password);
        this.trackEnabled.setChecked(tracking);
    }

    @Override
    public void setConnectionLog(String message) {
        this.connectionLog.setText(message);
    }
}
