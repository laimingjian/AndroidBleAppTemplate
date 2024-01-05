package com.lenzetech.zhongyablack.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.lenzetech.blelib.ble.BleConfig;
import com.lenzetech.blelib.utils.SpUtils;
import com.lenzetech.zhongyablack.AppViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * author:created by mj
 * Date:2022/8/25 10:28
 * Description:蓝牙单例类(Model)
 */
public class BleManager {
    public static final String TAG = "BleManager";
    //单例对象
    private static volatile BleManager bleManager;
    //调用到上下文对象
    private Context context;
    //蓝牙适配器
    private BluetoothAdapter bluetoothAdapter;
    //设备扫描
    private BluetoothLeScanner bleScanner;

    //扫描回调 区分高低版本 H为安卓5.0及以上版本
    private ScanCallback scanCallbackH;
    private BluetoothGattCallback bluetoothGattCallback;
    //ViewModel对象
    private BleViewModel bleViewModel;
    private AppViewModel appViewModel;
    //子线程
    private Handler workHandler;
    private HandlerThread handlerThread;
    //主线程
    private Handler mainHandler;

    //状态
    private static final int START_SCAN = 1;
    private static final int STOP_SCAN = 2;
    private static final int CONNECT_GATT = 3;
    private static final int DISCONNECT_GATT = 4;
    private static final int CLOSE_GATT = 5;

    //蓝牙gatt服务对象和回调（多连接）
//    private BluetoothGatt bluetoothGatt;
    private Map<String, BluetoothGatt> connectedGattMap;
    //连接的设备地址列表
    private Map<String, BluetoothDevice> connectedDeviceMap;
    //设备的特征值列表
    private Map<String, BluetoothGattCharacteristic> writeChMap;

    public BleManager() {
    }

    //获取单例模式
    public static BleManager getInstance() {
        //双重校验锁提高效率
        if (bleManager == null)
            synchronized (BleManager.class) {
                if (bleManager == null)
                    bleManager = new BleManager();
            }
        return bleManager;
    }

    public void initBle(Application application) {
        //初始化ViewModel对象
        bleViewModel = new BleViewModel(application);
        appViewModel = new AppViewModel(application);
        connectedGattMap = new HashMap<>();
        connectedDeviceMap = new HashMap<>();
        writeChMap = new HashMap<>();
        BluetoothManager bluetoothManager = (BluetoothManager) application.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        String autoMac = (String) SpUtils.getInstance().getParam("autoConnectMac", "");
        bleViewModel.setDeviceAddress(autoMac);
    }

    //初始化蓝牙ble相关（使用前需要主页面先调用一次）
    @SuppressLint("MissingPermission")
    public void initBle(Activity activity) {
        context = activity;
        //通过蓝牙管理器获取适配器
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return;
        }
        //初始化scanner
        bleScanner = bluetoothAdapter.getBluetoothLeScanner();
        //初始化线程
        initHandler();
        //初始化高版本扫描回调
        scanCallbackH = new ScanCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                bleViewModel.handleScanResult(result);
            }
        };
        //初始化蓝牙gatt回调
        bluetoothGattCallback = new BluetoothGattCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.e(TAG, gatt.getDevice().getAddress() + " onConnectionStateChange " + newState);
                //连接蓝牙成功后发现可读写的服务
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    //连接成功后停止扫描
                    stopScanLeDevice();
                    gatt.discoverServices();
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    connectedDeviceMap.remove(gatt.getDevice().getAddress());
                    Message message = mainHandler.obtainMessage();
                    message.what = CLOSE_GATT;
                    message.obj = gatt.getDevice().getAddress();
                    mainHandler.sendMessage(message);
                    bleViewModel.handleDisConnected(gatt);
                }
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                //发现服务成功后寻找特征值
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    UUID mainServiceUuid = UUID.fromString(BleConfig.MAIN_SERVICE_UUID);
                    //设备的特征值uuid
                    UUID notifyChUuid = UUID.fromString(BleConfig.NOTIFY_CHARACTERISTIC_UUID);
                    UUID writeChUuid = UUID.fromString(BleConfig.WRITE_CHARACTERISTIC_UUID);
                    UUID descriptorUUid = UUID.fromString(BleConfig.DESCRIPTOR_UUID);
                    BluetoothGattService mainGattService = gatt.getService(mainServiceUuid);
                    if (mainGattService != null) {
                        //根据uuid获取相应特征值
                        BluetoothGattCharacteristic notifyCh = mainGattService.getCharacteristic(notifyChUuid);
                        BluetoothGattCharacteristic writeCh = mainGattService.getCharacteristic(writeChUuid);
                        BluetoothGattDescriptor descriptor;
                        if (notifyCh != null) {
                            //获取根据uuid获取相应描述
                            descriptor = notifyCh.getDescriptor(descriptorUUid);
                            //设置描述和特征值通知
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                            gatt.readDescriptor(descriptor);
                            gatt.setCharacteristicNotification(notifyCh, true);
                        } else {
                            descriptor = writeCh.getDescriptor(descriptorUUid);
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                            gatt.readDescriptor(descriptor);
                            gatt.setCharacteristicNotification(writeCh, true);
                        }
                        writeChMap.put(gatt.getDevice().getAddress(), writeCh);
                    }
                }
            }

            //读取数据回调，接收数据
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic
                    characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                bleViewModel.handleReadCharacteristic(gatt, characteristic);
            }

            //发送数据回调
            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
                    characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                bleViewModel.handleWriteCharacteristic(gatt, characteristic);
            }

            //蓝牙设备改变特征值后接收修改的信息
            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                bleViewModel.handleCharacteristicChange(gatt, characteristic);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
                bleViewModel.handleReadRemoteRssi(gatt, rssi);
            }

            //设置描述符后回调
            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.e(TAG, "onDescriptorWrite success");
                    connectedDeviceMap.put(gatt.getDevice().getAddress(), gatt.getDevice());
                    bleViewModel.handleConnected(gatt);
                    //将连接的设备添加到集合中用于显示
                    bleViewModel.getConnectedMap().postValue(connectedDeviceMap);
                }
            }
        };
    }

    //初始化线程
    private void initHandler() {
        //初始化主线程
        mainHandler = new Handler(context.getMainLooper()) {
            @SuppressLint("MissingPermission")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case CONNECT_GATT:
                        BluetoothDevice device = (BluetoothDevice) msg.obj;
                        BluetoothGatt gatt = device.connectGatt(context, false, bluetoothGattCallback);
                        connectedGattMap.put(gatt.getDevice().getAddress(), gatt);
/*                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && device.getBondState() == BluetoothDevice.BOND_NONE) {
                            //配对设备
                            device.createBond();
                        }*/
                        break;
                    case DISCONNECT_GATT:
                        String address = (String) msg.obj;
                        if (connectedGattMap.containsKey(address)) {
                            BluetoothGatt bluetoothGatt = connectedGattMap.get(address);
                            if (bluetoothGatt != null) {
                                bluetoothGatt.disconnect();
                                bluetoothGatt.close();
                            }
                        }

                        break;
                    case CLOSE_GATT:
                        String deviceAddress = (String) msg.obj;
                        if (connectedGattMap.containsKey(deviceAddress)) {
                            BluetoothGatt bluetoothGatt = connectedGattMap.get(deviceAddress);
                            if (bluetoothGatt != null)
                                bluetoothGatt.close();
                            connectedGattMap.remove(deviceAddress);
                        }
                  /*      if (bluetoothGatt != null)
                            bluetoothGatt.close();
                       bluetoothGatt = null;*/
                        break;
                }
            }
        };

        //初始化工作子线程
        handlerThread = new HandlerThread("BleWorkHandlerThread");
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper()) {
            @SuppressLint("MissingPermission")
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //根据消息类别进行相应处理
                switch (msg.what) {
                    case START_SCAN:
                        //开始扫描
                        Log.e(TAG, "startBleScan");
                        //如果安卓版本高于5.0使用高版本api
                        if (bleScanner == null)
                            bleScanner = bluetoothAdapter.getBluetoothLeScanner();
                        bleScanner.startScan(scanCallbackH);
                        this.postDelayed(() -> stopBleScan(), BleConfig.SCAN_TIME);
                        break;
                    case STOP_SCAN:
                        stopBleScan();
                        //线程休眠确保ble停止搜索完成
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    @SuppressLint("MissingPermission")
    public void sendData2Ble(String address, byte[] data) {
        if (!connectedGattMap.containsKey(address) || !writeChMap.containsKey(address))
            return;
        BluetoothGatt gatt = connectedGattMap.get(address);
        BluetoothGattCharacteristic characteristic = writeChMap.get(address);
        if (gatt != null && characteristic != null) {
            characteristic.setValue(data);
            mainHandler.postDelayed(() -> Log.e(TAG, "sendData2Ble: " + gatt.writeCharacteristic(characteristic)), 100);
        }
    }

    @SuppressLint("MissingPermission")
    public void readData(String address) {
        if (!connectedGattMap.containsKey(address) || !writeChMap.containsKey(address))
            return;
        BluetoothGatt gatt = connectedGattMap.get(address);
        BluetoothGattCharacteristic characteristic = writeChMap.get(address);
        if (gatt != null && characteristic != null) {
            mainHandler.postDelayed(() -> Log.e(TAG, "readData: " + gatt.readCharacteristic(characteristic)), 100);
        }
    }

    //停止蓝牙扫描
    @SuppressLint({"MissingPermission"})
    private void stopBleScan() {
        if (Boolean.TRUE.equals(bleViewModel.getIsScanning().getValue())) {
            Log.e(TAG, "stopBleScan: ");
            if (bleScanner == null)
                bleScanner = bluetoothAdapter.getBluetoothLeScanner();
            bleScanner.stopScan(scanCallbackH);
            bleViewModel.getIsScanning().postValue(false);
        }
    }

    //搜索蓝牙设备方法
    public void scanLeDevice() {
        workHandler.sendEmptyMessage(START_SCAN);
        Log.e(TAG, "scan start");
    }

    public void stopScanLeDevice() {
        workHandler.sendEmptyMessage(STOP_SCAN);
    }

    //连接蓝牙
    public void connectDevice(BluetoothDevice device) {
        if (isConnected(device.getAddress()) || connectedGattMap.containsKey(device.getAddress()))
            return;
        Message message = mainHandler.obtainMessage();
        message.what = CONNECT_GATT;
        message.obj = device;
        mainHandler.sendMessage(message);
    }

    public void connectDevice(String address) {
        connectDevice(bluetoothAdapter.getRemoteDevice(address));
    }

    //断开蓝牙连接
    @SuppressLint("MissingPermission")
    public void disconnectDevice(String address) {
        if (connectedGattMap.containsKey(address)) {
            connectedGattMap.get(address).disconnect();
            Log.e(TAG, "DISCONNECT_GATT");
            connectedGattMap.remove(address);
        }
    }

    @SuppressLint("MissingPermission")
    public void readRssi(String address) {
        if (connectedGattMap.containsKey(address)) {
            connectedGattMap.get(address).readRemoteRssi();
        }
    }

    public boolean isConnected(String address) {
        return connectedDeviceMap.containsKey(address);
    }

    public BleViewModel getBleViewModel() {
        return bleViewModel;
    }

    public AppViewModel getAppViewModel() {
        return appViewModel;
    }

    public Map<String, BluetoothDevice> getConnectedDeviceMap() {
        return connectedDeviceMap;
    }

    public Map<String, BluetoothGatt> getConnectedGattMap() {
        return connectedGattMap;
    }
}
