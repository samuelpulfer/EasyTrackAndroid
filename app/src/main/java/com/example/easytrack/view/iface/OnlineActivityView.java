package com.example.easytrack.view.iface;

public interface OnlineActivityView {
    interface OnlineViewButtonListener {
        void buttonClicked();
    }
    void setTasks(String text);
    void setButtonText(String name);
    void setConnectionState(String state);
}
