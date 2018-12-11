package com.example.easytrack.model.iface;

public interface ConnectionCheck {
    interface ConnectionCheckResultListener {
        void setConnectionCheckResult(String result);
    }

    void addConnectionCheckResultListener(ConnectionCheckResultListener listener);
    void removeConnectionCheckResultListener(ConnectionCheckResultListener listener);
}
