package com.example.easytrack.model.iface;

import java.io.UnsupportedEncodingException;

public interface SettingsModel {

    void setUrl(String url);
    void setUsername(String username);
    void setPassword(String password);
    void setSettings(String url, String username, String password);
    void setSettings(String url, String username, String password, boolean tracking);
    void setTracking(Boolean enabled);

    String getUrl();
    String getUsername();
    String getPassword();
    String getEncAuth() throws UnsupportedEncodingException;
    boolean getTracking();
}
