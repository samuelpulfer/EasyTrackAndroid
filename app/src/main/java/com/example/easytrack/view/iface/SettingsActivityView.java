package com.example.easytrack.view.iface;

public interface SettingsActivityView {
    interface SettingsActivityListener {
        void saveBrnPressed(String url, String username, String password, boolean tracking);
    }
    void addListener(SettingsActivityListener listener);
    void removeListener(SettingsActivityListener listener);
    void setSettings(String url, String username, String password, boolean tracking);
    void setConnectionLog(String message);
}
