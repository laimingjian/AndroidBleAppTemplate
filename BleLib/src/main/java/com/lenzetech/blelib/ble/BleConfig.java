package com.lenzetech.blelib.ble;

/**
 * author:created by mj
 * Date:2022/8/25 10:36
 * Description:蓝牙ble的固定配置数据
 */
public class BleConfig {
    //服务和特征值ID
    public static final String DEVICE_SERVICE_UUID = "0000180A-0000-1000-8000-00805f9b34fb";
    public static final String BATTERY_SERVICE_UUID = "0000180F-0000-1000-8000-00805f9b34fb";
    public static final String IMMEDIATE_SERVICE_UUID = "00001802-0000-1000-8000-00805f9b34fb";
    public static final String NOTIFY_SERVICE_UUID = "0000FFE0-0000-1000-8000-00805f9b34fb";
    public static final String MAIN_SERVICE_UUID = "0000FF00-0000-1000-8000-00805f9b34fb";
    public static final String FINDMY_NAME_SERVICE_UUID = "87290102-3c51-43b1-a1a9-11b9dc38478b";
    public static final String FINDMY_MAIN_SERVICE_UUID = "0000FD43-0000-1000-8000-00805f9b34fb";
    public static final String FINDMY_FILTER_SERVICE_UUID = "0000FD44-0000-1000-8000-00805f9b34fb";
    public static final String FINDMY_NAME_CHARACTERISTIC_UUID = "6aa50003-6352-4d57-a7b4-003a416fbb0b";
    public static final String FINDMY_VERSION_CHARACTERISTIC_UUID = "6aa50007-6352-4d57-a7b4-003a416fbb0b";
    public static final String FINDMY_MAIN_CHARACTERISTIC_UUID = "94110001-6d9b-4225-a4f1-6a4a7f01b0de";


    public static final String VERSION_CHARACTERISTIC_UUID = "00002A28-0000-1000-8000-00805f9b34fb";

    public static final String BATTERY_CHARACTERISTIC_UUID = "00002A19-0000-1000-8000-00805f9b34fb";

    public static final String IMMEDIATE_CHARACTERISTIC_UUID = "00002A06-0000-1000-8000-00805f9b34fb";

    public static final String KEY_CHARACTERISTIC_UUID = "0000FFE1-0000-1000-8000-00805f9b34fb";

    public static final String DEVICE_CHARACTERISTIC_UUID = "0000FFF0-0000-1000-8000-00805f9b34fb";
    public static final String AUT_CHARACTERISTIC_UUID = "0000FFC0-0000-1000-8000-00805f9b34fb";
    public static final String BIND_CHARACTERISTIC_UUID = "0000FFC1-0000-1000-8000-00805f9b34fb";
    public static final String RENAME_CHARACTERISTIC_UUID = "0000FFF3-0000-1000-8000-00805f9b34fb";

    public static final String DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    //扫描时间
    public static int SCAN_TIME = 12 * 1000;
}
