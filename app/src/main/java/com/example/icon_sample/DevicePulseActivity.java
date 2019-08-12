package com.example.icon_sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import net.glxn.qrgen.android.QRCode;
import de.greenrobot.event.EventBus;

public class DevicePulseActivity extends AppCompatActivity {
    private MenuItem muStatus;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device, menu);

        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.action_cloud_status) {
                muStatus = menu.getItem(i);
                if (DeviceManager.g().isConnected()) {
                    muStatus.setIcon(R.mipmap.cloud_connected);
                } else {
                    muStatus.setIcon(R.mipmap.cloud_disconnected);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_cloud_status:
                if (DeviceManager.g().isConnected()) {
                    showToast(getApplicationContext().getString(R.string.online));
                } else {
                    showToast(getApplicationContext().getString(R.string.offline));
                    DeviceManager.g().sendGetUserlist();
                }
                break;

            case R.id.action_show_qrcode:
                LayoutInflater inflater = LayoutInflater.from(DevicePulseActivity.this);
                final View v = inflater.inflate(R.layout.showqr, null);

                ImageView imgView = (ImageView) v.findViewById(R.id.imgUserQR);
                String qrcode = DeviceManager.g().getQrcode();
                if (!qrcode.equals("")) {
                    Bitmap myBitmap = QRCode.from(qrcode).bitmap();
                    imgView.setImageBitmap(myBitmap);
                }

                new AlertDialog.Builder(DevicePulseActivity.this)
                        .setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                        .show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(DeviceConnectEvent event) {
        Log.i(Const.TAG, "DeviceActivity: DeviceConnectEvent");
        if (muStatus != null) {// null check is for the first time check
            if (event.success) {
                //  muStatus.setIcon(R.mipmap.ic_cloud_queue_white_24dp);
                muStatus.setIcon(R.mipmap.cloud_connected);
                Log.i(Const.TAG, "DeviceActivity: DeviceConnectEvent set ONLINE icon");
            } else {
                // muStatus.setIcon(R.mipmap.ic_cloud_off_white_24dp);
                muStatus.setIcon(R.mipmap.cloud_disconnected);
                Log.i(Const.TAG, "DeviceActivity: DeviceConnectEvent set OFFLINE icon");
            }
        }
    }

    public void onEvent(DeviceConnectEvent event) {

        if (!event.success) {
            Log.i("DevicePulseActivity", " DeviceConnectEvent not successfully, reconnecting ....");
            if (DeviceManager.g().isNetworkOK())
                DeviceManager.g().connect();
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { /// check
            if (DeviceManager.g().isDeviceRegistered()) {
                Log.i("DevicePulseActivity", " DeviceConnectEvent check on Device Registered TRUE");
                DeviceManager.g().sendDeviceInit();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (muStatus != null) {// null check is for the first time check
            if (DeviceManager.g().isConnected()) {
                muStatus.setIcon(R.mipmap.cloud_connected);
            } else {
                muStatus.setIcon(R.mipmap.cloud_disconnected);
            }
        }
    }

    private void showToast(String tip) {
        if (toast != null) {
            toast.setText(tip);
        } else {
            toast = Toast.makeText(this, tip, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
