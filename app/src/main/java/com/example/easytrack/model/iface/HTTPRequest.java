package com.example.easytrack.model.iface;

import org.json.JSONObject;

public interface HTTPRequest {
    interface HTTPRequestResultListener {
        void setHTTPResult(JSONObject result);
    }
    interface ConnectionStateListener {
        void setConnectionState(String state);
    }
    public enum Method {
        GET("GET"), POST("POST");
        private String method;
        Method(String method){
            this.method = method;
        }
        public String toString(){
            return method;
        }
    }

    void setMethod(Method method);
    void setPayload(JSONObject payload);

    void addHTTPRequestResultListener(HTTPRequestResultListener listener);
    void removeHTTPRequestResultListener(HTTPRequestResultListener listener);


}
