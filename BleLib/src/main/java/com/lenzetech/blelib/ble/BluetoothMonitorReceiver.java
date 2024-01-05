package com.lenzetech.blelib.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothMonitorReceiver extends BroadcastReceiver {
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                if (blueState == BluetoothAdapter.STATE_OFF) {
//                    BleManager.getInstance().getBleViewModel().getIsBleConnect().postValue(false);
//                    BleManager.getInstance().getBleViewModel().getIsWriteReady().postValue(false);

                }
            }
        }
    }
}