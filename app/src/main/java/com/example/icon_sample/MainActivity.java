package com.example.icon_sample;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        new Thread(new DeviceManager(getString(R.string.server))).start();

        Intent dial = new Intent();
        dial.setClass(MainActivity.this, DevicePulseActivity.class);
        startActivity(dial);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
    public void onEventMainThread(DeviceConnectEvent event) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Log.i(TAG, "DeviceConnectEvent returned to MainActivity via onEventMainThread "+event.success);
        if (event.success) {
            Intent intent = new Intent();
        } else {
            Intent intent = new Intent();
        }
    }
}