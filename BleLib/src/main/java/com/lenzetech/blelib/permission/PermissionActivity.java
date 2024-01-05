package com.lenzetech.blelib.permission;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lenzetech.blelib.ble.BluetoothMonitorReceiver;

/**
 * author:created by mj
 * Date:2022/9/16 11:38
 * Description:权限相关超类Activity
 */
public class PermissionActivity extends AppCompatActivity {
    private BlePermissionHelper blePermissionHelper;
    private BluetoothMonitorReceiver bluetoothMonitorReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blePermissionHelper = new BlePermissionHelper(this);
        bluetoothMonitorReceiver = new BluetoothMonitorReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothMonitorReceiver, intentFilter);
    }

    //权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String p : permissions) {
            Log.e("TAG", "onRequestPermissionsResult: " + requestCode + " " + p);
            blePermissionHelper.showSetPermissionDialogIfDeny(p, requestCode);
        }
    }
}
