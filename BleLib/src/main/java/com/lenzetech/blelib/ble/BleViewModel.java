package com.lenzetech.blelib.ble;

import android.app.Application;
import android.bluetooth.le.ScanResult;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author:created by mj
 * Date:2022/8/25 10:29
 * Description:用于保存蓝牙相关数据的ViewModel
 */
public class BleViewModel extends AndroidViewModel {
    //扫描到的设备列表
    private MutableLiveData<List<BleDevice>> scanList;
    //已连接的设备地址列表
    private MutableLiveData<Set<String>> connectedSet;
    //判断是否正在扫描和是否连接设备
    private MutableLiveData<Boolean> isScanning;

    //    private MutableLiveData<Boolean> isTarget;
    //断开连接的设备地址
    private MutableLiveData<String> disconnectedAddress;
    //断开报警的设备地址
    private MutableLiveData<String> alertAddress;
    //扫描到的目标设备地址
    private MutableLiveData<String> targetAddress;
    //扫描到的proValue
    private MutableLiveData<ScanResult> scanDevice = new MutableLiveData<>();
    private MutableLiveData<String> connectingAddress;
    //获取到notify的设备地址
    private MutableLiveData<String> notifyAddress;
    //设备的rssi强度列表
    private MutableLiveData<Map<String, Integer>> rssiMap;
    //读取到认证信息的设备地址
    private MutableLiveData<String> readAutAddress;
    //读取到绑定状态的设备地址
    private MutableLiveData<String> readBindAddress;
    private MutableLiveData<String> appLog;

    public BleViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<BleDevice>> getScanList() {
        if (scanList == null) {
            scanList = new MutableLiveData<>(new ArrayList<>());
        }
        return scanList;
    }

    public MutableLiveData<Set<String>> getConnectedSet() {
        if (connectedSet == null) {
            connectedSet = new MutableLiveData<>(new HashSet<>());
        }
        return connectedSet;
    }

    public MutableLiveData<Boolean> getIsScanning() {
        if (isScanning == null)
            isScanning = new MutableLiveData<>(false);
        return isScanning;
    }

/*    public MutableLiveData<Boolean> getIsTarget() {
        if (isTarget == null)
            isTarget = new MutableLiveData<>(false);
        return isTarget;
    }*/

    public MutableLiveData<String> getTargetAddress() {
        if (targetAddress == null)
            targetAddress = new MutableLiveData<>("");
        return targetAddress;
    }

    public MutableLiveData<String> getAppLog() {
        if (appLog == null)
            appLog = new MutableLiveData<>("");
        return appLog;
    }

    public MutableLiveData<String> getDisconnectedAddress() {
        if (disconnectedAddress == null)
            disconnectedAddress = new MutableLiveData<>("");
        return disconnectedAddress;
    }

    public MutableLiveData<String> getAlertAddress() {
        if (alertAddress == null)
            alertAddress = new MutableLiveData<>("");
        return alertAddress;
    }

    public MutableLiveData<String> getConnectingAddress() {
        if (connectingAddress == null)
            connectingAddress = new MutableLiveData<>("");
        return connectingAddress;
    }

    public MutableLiveData<String> getReadAutAddress() {
        if (readAutAddress == null)
            readAutAddress = new MutableLiveData<>("");
        return readAutAddress;
    }

    public MutableLiveData<String> getReadBindAddress() {
        if (readBindAddress == null)
            readBindAddress = new MutableLiveData<>("");
        return readBindAddress;
    }

    public MutableLiveData<String> getNotifyAddress() {
        if (notifyAddress == null)
            notifyAddress = new MutableLiveData<>("");
        return notifyAddress;
    }

    public MutableLiveData<Map<String, Integer>> getRssiMap() {
        if (rssiMap == null)
            rssiMap = new MutableLiveData<>(new HashMap<>());
        return rssiMap;
    }

    public MutableLiveData<ScanResult> getScanDevice() {
        return scanDevice;
    }
}
