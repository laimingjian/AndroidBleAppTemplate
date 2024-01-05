package com.lenzetech.blelib.ble;

import android.bluetooth.BluetoothDevice;

import java.util.Objects;

/**
 * author:created by mj
 * Date:2022/8/25 19:36
 * Description:设备实体类
 */
public class BleDevice {

    private BluetoothDevice bluetoothDevice;
    private int rssi;

    public BleDevice() {
    }

    public BleDevice(BluetoothDevice bluetoothDevice, int rssi) {
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BleDevice bleDevice = (BleDevice) o;
        return bluetoothDevice.getAddress().equals(bleDevice.bluetoothDevice.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(bluetoothDevice.getAddress());
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
