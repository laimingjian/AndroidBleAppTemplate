package com.lenzetech.zhongyablack.ble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lenzetech.blelib.ble.BleDevice;
import com.lenzetech.blelib.permission.BlePermissionHelper;
import com.lenzetech.blelib.utils.ByteUtils;
import com.lenzetech.blelib.utils.SpUtils;
import com.lenzetech.zhongyablack.AppViewModel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author:created by mj
 * Date:2022/8/25 10:29
 * Description:用于处理蓝牙相关的ViewModel
 */
public class BleViewModel extends AndroidViewModel {
    private final String TAG = "BleViewModel";
    private Application application;
    //扫描到的设备列表
    private MutableLiveData<List<BleDevice>> scanList = new MutableLiveData<>(new ArrayList<>());
    //已连接的设备地址列表
    private MutableLiveData<Set<String>> connectedSet = new MutableLiveData<>(new HashSet<>());
    //判断是否正在扫描和是否连接设备
    private MutableLiveData<Boolean> isScanning = new MutableLiveData<>(false);
    //断开连接的设备地址
    private MutableLiveData<String> disconnectedAddress = new MutableLiveData<>("");
    //扫描到的目标设备地址
    private MutableLiveData<String> targetAddress = new MutableLiveData<>("");
    private MutableLiveData<String> connectingAddress = new MutableLiveData<>("");
    //获取到notify的设备地址
    private MutableLiveData<String> notifyAddress = new MutableLiveData<>("");
    //设备的rssi强度列表
    private MutableLiveData<Map<String, Integer>> rssiMapLiveData = new MutableLiveData<>(new HashMap<>());
    private MutableLiveData<String> appLog = new MutableLiveData<>("");
    //需要连接的设备地址
    private String deviceAddress = "";
    private String connectedMac = "";
    private String connectedName = "";
    //扫描到的设备列表
    private Set<BleDevice> deviceList = new HashSet<>();
    //notify消息列表
    private Map<String, byte[]> notifyMap = new HashMap<>();
    private Map<String, Integer> rssiMap = new HashMap<>();
    //不自动回连列表
    public Set<String> unAutoConnectList = new HashSet<>();
    //已连接的设备地址列表
    private MutableLiveData<Map<String, BluetoothDevice>> connectedMap = new MutableLiveData<>(new HashMap<>());

    public BleViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    //---------------------------------------------------------业务逻辑-----------------------------------------------------------------------------------
    public void handleScanResult(ScanResult result) {
        if (ContextCompat.checkSelfPermission(application, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        isScanning.postValue(true);
        Log.e(TAG, "name:" + result.getDevice().getName() + " rssi:" + result.getRssi() + " address:" + result.getDevice().getAddress() + "scanRecord:" + ByteUtils.bytes2HexStringLog(result.getScanRecord().getBytes()));
        //设备过滤
        if (result.getDevice().getName() == null)
            return;
        byte[] scanRecord = result.getScanRecord().getBytes();
        if (result.getDevice().getAddress().equals(deviceAddress)) {
            BleManager.getInstance().connectDevice(result.getDevice());
        }
//        if (AppDatabase.getInstance().isExitAddress(result.getDevice().getAddress())
//                && !unAutoConnectList.contains(result.getDevice().getAddress())) {
//            Product product = AppDatabase.getInstance().deviceDao().queryDevice(result.getDevice().getAddress());
//            AppDatabase.getInstance().deviceDao().updateDevice(product);
//
//            connectDevice(result.getDevice());
//            Log.e("connectDevice", AppDatabase.getInstance().isExitAddress(result.getDevice().getAddress())
//                    + "" + !unAutoConnectList.contains(result.getDevice().getAddress()) + "connectDevice2");
//            return;
//        }
        // 获取扫描结果的服务 UUID 列表
//        List<ParcelUuid> serviceUuids = result.getScanRecord().getServiceUuids();
//                if (serviceUuids != null && serviceUuids.contains(new ParcelUuid(UUID.fromString(BleConfig.FILTER_UUID)))) {
//        if (scanRecord[5] == 0x17 && (scanRecord[6] & 0xFF) == 0xB7) {
//            int type = 1;
//                    bleViewModel.getTargetAddress().postValue(result.getDevice().getAddress());
        if (result.getDevice().getName().contains("ZYCL")) {
            BleDevice bleDevice = new BleDevice(result.getDevice(), result.getRssi());
            deviceList.add(bleDevice);
            List<BleDevice> bleDevices = new ArrayList<>(deviceList);
            //将扫描到到设备添加到集合中用于显示
            scanList.postValue(bleDevices);
        }

//        }
    }

    @SuppressLint("MissingPermission")
    public void handleConnected(BluetoothGatt gatt) {
        if (deviceAddress.equals(gatt.getDevice().getAddress()))
            deviceAddress = "";
        connectedMac = gatt.getDevice().getAddress();
        connectedName = gatt.getDevice().getName();
        SpUtils.getInstance().setParam("autoConnectMac", gatt.getDevice().getAddress());
        connectedMap.postValue(BleManager.getInstance().getConnectedDeviceMap());
        CommandManager.getInstance().setConnectState(gatt.getDevice().getAddress());
        deviceList.clear();
        scanList.postValue(new ArrayList<>(deviceList));
    }

    public void handleDisConnected(BluetoothGatt gatt) {
        connectedMac = "";
        connectedName = "";
        disconnectedAddress.postValue(gatt.getDevice().getAddress());
        connectedMap.postValue(BleManager.getInstance().getConnectedDeviceMap());
    }

    public void handleWriteCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Log.e(TAG, "onCharacteristicWrite " + ByteUtils.bytes2HexStringLog(characteristic.getValue()));
    }

    public void handleReadCharacteristic(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Log.e(TAG, "onCharacteristicRead " + ByteUtils.bytes2HexStringLog(characteristic.getValue()));
    }

    public void handleCharacteristicChange(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        byte[] value = characteristic.getValue();
//        Log.e(TAG, "CharacteristicChanged " + ByteUtils.bytes2HexStringLog(value));
        Log.e(TAG, "CharacteristicChanged " + new String(value));
        String data = new String(value);
        AppViewModel appViewModel = BleManager.getInstance().getAppViewModel();
        if (data.length() > 0) {
//            Log.e("handleChange", Integer.parseInt(data.substring(3, 5)) + " ");
            if (data.startsWith("[1F")) {
                appViewModel.getWorkMode().postValue(Integer.parseInt(data.substring(3, 5), 16));
                appViewModel.getFlowMode().postValue(Integer.parseInt(data.substring(5, 7), 16));
                appViewModel.getPower().postValue(Integer.parseInt(data.substring(7, 9), 16));
                appViewModel.getBrightness1().postValue(Integer.parseInt(data.substring(21, 23), 16));
                appViewModel.getBrightness2().postValue(Integer.parseInt(data.substring(23, 25), 16));
                appViewModel.getSoundSensitivity().postValue(Integer.parseInt(data.substring(25, 27), 16));
                appViewModel.getFlowSpeed().postValue(Integer.parseInt(data.substring(27, 29), 16));
                appViewModel.getFlowDir().postValue(Integer.parseInt(data.substring(29, 31), 16));
                appViewModel.getBrightness().postValue(Integer.parseInt(data.substring(31, 33), 16));
                appViewModel.getRgbSpeed().postValue(Integer.parseInt(data.substring(33, 35), 16));
            } else if (data.startsWith("[13")) {
                appViewModel.getHardVersion().postValue(ByteUtils.hex2Ascii(data.substring(3, 13)));
                appViewModel.getCarType().postValue(ByteUtils.hex2Ascii(data.substring(13, 29)));
                appViewModel.getSoftVersion().postValue(ByteUtils.hex2Ascii(data.substring(29, 49)));
            } else if (data.startsWith("[26")) {
                appViewModel.getWelcomePower().postValue(Integer.parseInt(data.substring(3, 5), 16) == 1);
                appViewModel.getDoorOpenWarn().postValue(Integer.parseInt(data.substring(5, 7), 16) == 1);
                appViewModel.getRadarLink().postValue(Integer.parseInt(data.substring(7, 9), 16) == 1);
                appViewModel.getOverSpeedLink().postValue(Integer.parseInt(data.substring(9, 11), 16) == 1);
                appViewModel.getSteerLink().postValue(Integer.parseInt(data.substring(11, 13), 16) == 1);
                appViewModel.getAirConLink().postValue(Integer.parseInt(data.substring(13, 15), 16) == 1);
                appViewModel.getHalfNight().postValue(Integer.parseInt(data.substring(15, 17), 16) == 1);
                appViewModel.getBriPart().postValue(Integer.parseInt(data.substring(17, 19), 16) == 1);
                appViewModel.getWelcomeDir().postValue(Integer.parseInt(data.substring(19, 21), 16));
                appViewModel.getWelcomeColor().postValue(Integer.parseInt(data.substring(21, 23), 16));
                appViewModel.getRhythmSource().postValue(Integer.parseInt(data.substring(23, 25), 16));
                appViewModel.getBlindSpotDetect().postValue(Integer.parseInt(data.substring(27, 29), 16) == 1);
            } else if (data.startsWith("[27")) {
                appViewModel.getFlowEffect().postValue(Integer.parseInt(data.substring(32, 34), 16));
            } else if (data.startsWith("[23")) {
                Boolean[] dial = new Boolean[16];
                for (int i = 0; i <= 15; i++) {
                    dial[i] = Character.getNumericValue(data.charAt(i + 3)) == 1;
                }
                appViewModel.getDialState().postValue(dial);
            } else if (data.startsWith("[4F")) {
                appViewModel.getMdLampNum().postValue(Integer.parseInt(data.substring(9, 13), 10));
                appViewModel.getMdDir().postValue(Integer.parseInt(data.substring(13, 15), 16) == 1);
                appViewModel.getCpLampNum().postValue(Integer.parseInt(data.substring(15, 19), 10));
                appViewModel.getCpDir().postValue(Integer.parseInt(data.substring(19, 21), 16) == 1);
                appViewModel.getFdLampNum().postValue(Integer.parseInt(data.substring(21, 25), 10));
                appViewModel.getLfdDir().postValue(Integer.parseInt(data.substring(25, 27), 16) == 1);
                appViewModel.getRdLampNum().postValue(Integer.parseInt(data.substring(27, 31), 10));
                appViewModel.getLrdDir().postValue(Integer.parseInt(data.substring(31, 33), 16) == 1);
                appViewModel.getRfdDir().postValue(Integer.parseInt(data.substring(33, 35), 16) == 1);
                appViewModel.getRrdDir().postValue(Integer.parseInt(data.substring(35, 37), 16) == 1);

            }
        }
        notifyMap.put(gatt.getDevice().getAddress(), value);
        notifyAddress.postValue(gatt.getDevice().getAddress());
    }


    public void handleReadRemoteRssi(BluetoothGatt gatt, int rssi) {
        rssiMap.put(gatt.getDevice().getAddress(), rssi);
        rssiMapLiveData.postValue(rssiMap);
    }

    public void startScan(Activity activity) {
        BlePermissionHelper blePermissionHelper = new BlePermissionHelper(activity);
        deviceList.clear();
        scanList.postValue(new ArrayList<>(deviceList));
        if (blePermissionHelper.checkNOpenGps() && blePermissionHelper.checkNOpenBl()) {
            isScanning.postValue(false);
            BleManager.getInstance().scanLeDevice();
        }
    }

    public void stopScan() {
        BleManager.getInstance().stopScanLeDevice();
    }
    //------------------------功能区----------------------------------------


    //-------------------------------getter/setter-------------------------
    public MutableLiveData<List<BleDevice>> getScanList() {
        return scanList;
    }

    public MutableLiveData<Set<String>> getConnectedSet() {
        return connectedSet;
    }

    public MutableLiveData<Boolean> getIsScanning() {
        return isScanning;
    }

    public MutableLiveData<String> getTargetAddress() {
        return targetAddress;
    }

    public MutableLiveData<String> getAppLog() {
        return appLog;
    }

    public MutableLiveData<String> getDisconnectedAddress() {
        return disconnectedAddress;
    }

    public MutableLiveData<String> getConnectingAddress() {
        return connectingAddress;
    }

    public MutableLiveData<String> getNotifyAddress() {
        return notifyAddress;
    }

    public MutableLiveData<Map<String, Integer>> getRssiMapLiveData() {
        return rssiMapLiveData;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public MutableLiveData<Map<String, BluetoothDevice>> getConnectedMap() {
        return connectedMap;
    }

    public String getConnectedMac() {
        return connectedMac;
    }

    public void setConnectedMac(String connectedMac) {
        this.connectedMac = connectedMac;
    }

    public String getConnectedName() {
        return connectedName;
    }

    public void setConnectedName(String connectedName) {
        this.connectedName = connectedName;
    }

    public Map<String, byte[]> getNotifyMap() {
        return notifyMap;
    }
}
