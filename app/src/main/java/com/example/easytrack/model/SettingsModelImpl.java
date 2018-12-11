package com.example.easytrack.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.easytrack.model.iface.SettingsModel;

import java.io.UnsupportedEncodingException;

public class SettingsModelImpl implements SettingsModel {

    private SharedPreferences settings = null;
    private Context context = null;

    public SettingsModelImpl(Context context){
        this.context = context;
        settings = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }


    @Override
    public void setUrl(String url) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("url", url);
        edit.apply();
    }

    @Override
    public void setUsername(String username) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("username", username);
        edit.apply();
    }

    @Override
    public void setPassword(String password) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("password", password);
        edit.apply();
    }

    @Override
    public void setSettings(String url, String username, String password) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("url", url);
        edit.putString("username", username);
        edit.putString("password", password);
        edit.apply();
    }

    @Override
    public void setSettings(String url, String username, String password, boolean tracking) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("url", url);
        edit.putString("username", username);
        edit.putString("password", password);
        edit.putBoolean("tracking", tracking);
        edit.apply();
    }

    @Override
    public void setTracking(Boolean enabled) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean("tracking", enabled);
        edit.apply();
    }

    @Override
    public String getUrl() {
        return settings.getString("url", "https://easytrack.deluxxe.ch/EasyTrack");
    }

    @Override
    public String getUsername() {
        return settings.getString("username", "carrier05");
    }

    @Override
    public String getPassword() {
        return settings.getString("password", "carrier05");
    }

    @Override
    public String getEncAuth() throws UnsupportedEncodingException {
        String plainAuth = getUsername() + ":" + getPassword();
        return Base64.encodeToString(plainAuth.getBytes("UTF-8"),Base64.DEFAULT);
    }

    @Override
    public boolean getTracking() {
        return settings.getBoolean("tracking", false);
    }
}
