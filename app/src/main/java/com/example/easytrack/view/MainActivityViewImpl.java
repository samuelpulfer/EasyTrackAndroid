package com.example.easytrack.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.easytrack.R;
import com.example.easytrack.view.iface.MainActivityView;

public class MainActivityViewImpl extends AppCompatActivity implements MainActivityView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    @Override
    public void onPause(){
        super.onPause();
    }
    @Override
    public void onResume()
    {
        super.onResume();

    }
    public void sendMessage(View view){
        Intent intent = new Intent(this, OnlineActivityViewImpl.class);
        startActivity(intent);
    }

    public void settingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivityViewImpl.class);
        startActivity(intent);
    }
}
