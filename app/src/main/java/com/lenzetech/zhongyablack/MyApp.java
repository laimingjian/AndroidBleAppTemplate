package com.lenzetech.zhongyablack;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;

import androidx.lifecycle.MutableLiveData;

import com.lenzetech.blelib.ble.BluetoothMonitorReceiver;
import com.lenzetech.blelib.utils.SpUtils;
import com.lenzetech.blelib.utils.ToastUtils;
import com.lenzetech.zhongyablack.ble.BleManager;

/**
 * author:created by mj
 * Date:2023/1/5 17:09
 * Description:启动Application
 */
public class MyApp extends Application {
    private Context context;
    //单例对象
    private static volatile MyApp myApp;
    private int controlAddress = 0;
    private boolean isPassword = false;
    private BluetoothMonitorReceiver bluetoothMonitorReceiver;

    //获取单例模式
    public static MyApp getInstance() {
        //双重校验锁提高效率
        if (myApp == null)
            synchronized (MyApp.class) {
                if (myApp == null)
                    myApp = new MyApp();
            }
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        myApp = new MyApp();
        context = this;
        SpUtils.getInstance().init(this);
//        AppDatabase.initDataBase(this);
        //初始化ViewModel对象
        BleManager.getInstance().initBle(this);
        ToastUtils.init(this);
//        NotificationUtils.setNotificationChannel(this);
        bluetoothMonitorReceiver = new BluetoothMonitorReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothMonitorReceiver, intentFilter);
    }

    public Context getContext() {
        return context;
    }

    public int getControlAddress() {
        return controlAddress;
    }


    public void setControlAddress(int controlAddress) {
        this.controlAddress = controlAddress;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }
}
