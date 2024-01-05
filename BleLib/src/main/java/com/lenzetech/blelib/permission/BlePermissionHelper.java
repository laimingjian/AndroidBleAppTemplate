package com.lenzetech.blelib.permission;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.GPS_PROVIDER;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.lenzetech.blelib.R;

/**
 * author:created by mj
 * Date:2022/8/24 17:30
 * Description:用于权限的类
 */
public class BlePermissionHelper {
    private static final String TAG = "BlePermissionHelper";
    private Activity mActivity;
    private Context mContext;
    private final String[] gpsPermissions = new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
    private final String[] blPermissions = new String[]{BLUETOOTH_SCAN, BLUETOOTH_CONNECT};

    private final BluetoothAdapter bluetoothAdapter;
    private final LocationManager locationManager;

    //状态码
    private final int OPEN_BLUETOOTH = 1;
    public final int REQUEST_GPS_PERMISSIONS = 2;
    public final int REQUEST_BLUETOOTH_PERMISSIONS = 3;
    public final int REQUEST_CAMERA_PERMISSIONS = 4;

    //权限提示对话框
    private AlertDialog mDialog;


    public BlePermissionHelper(Activity mActivity) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.get_permission_libs)
                .setMessage(R.string.permission_tips_libs)
                .setPositiveButton(R.string.set_up_libs, (dialog, id) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                    intent.setData(uri);
                    mActivity.startActivity(intent);
                }).setNegativeButton(R.string.cancel_libs, null);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
    }

    public BlePermissionHelper(Context context) {
        this.mContext = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    //动态申请位置权限
    public void requestGpsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (!isLocationPermission()) {
                Log.e(TAG, "request gps permission");
                // 没有权限会弹出对话框申请
                ActivityCompat.requestPermissions(mActivity, gpsPermissions, REQUEST_GPS_PERMISSIONS);
            }
    }

    //动态申请蓝牙权限（安卓12以上）
    public void requestBlePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!isBlPermission()) {
                Log.e(TAG, "request bl ");
                // 没有权限会弹出对话框申请
                ActivityCompat.requestPermissions(mActivity, blPermissions, REQUEST_BLUETOOTH_PERMISSIONS);
            }
        }
    }

    //检查权限和打开GPS
    public boolean checkNOpenGps() {
        if (!isLocationPermission()) {
            requestGpsPermissions();
            return false;
        }
        if (!isEnableGps()) {
            mActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return false;
        }
        return true;
    }


    //检查权限和打开蓝牙
    @SuppressLint("MissingPermission")
    public boolean checkNOpenBl() {
        if (!isBlPermission()) {
            requestBlePermissions();
            return false;
        }
        if (!isEnableBluetooth()) {
            //执行一次打开蓝牙功能，给用户提示。
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(intent, OPEN_BLUETOOTH);
            return false;
        }
        return true;
    }


    //判断是否支持ble
    public boolean isSupportBLE() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    //判断是否打开蓝牙
    public boolean isEnableBluetooth() {
        if (!isSupportBLE())
            return false;
        return bluetoothAdapter.isEnabled();
    }

    //判断是否打开gps定位
    public boolean isEnableGps() {
        return locationManager.isProviderEnabled(GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //判断是否授权gps
    public boolean isLocationPermission() {
        return checkSelfPermission(mContext, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
                || checkSelfPermission(mContext, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    //判断是否授权蓝牙（安卓12以上）
    public boolean isBlPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return checkSelfPermission(mContext, BLUETOOTH_CONNECT) == PERMISSION_GRANTED
                    && checkSelfPermission(mContext, BLUETOOTH_SCAN) == PERMISSION_GRANTED;
        }
        return true;
    }

    //如果用户选择了不再询问权限则弹窗提醒
    public void showSetPermissionDialogIfDeny(String permission, int requestCode) {
        /*        String[] permissions;

         */
        if (requestCode == REQUEST_GPS_PERMISSIONS || requestCode == REQUEST_BLUETOOTH_PERMISSIONS || requestCode == REQUEST_CAMERA_PERMISSIONS) {
/*            if (isLocationPermission()) {
                requestBlePermissions();
                return;
            }*//*
            permissions = gpsPermissions;
        } else {
            permissions = blPermissions;
        }
        for (String permission : permissions) {*/
            //用户选择了禁止且不再询问
            if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED
                    && !ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                Log.e(TAG, "show dialog" + permission);
                if (!mDialog.isShowing())
                    //弹出对话框提示
                    mDialog.show();
   /*             break;
            }*/
            }
        }
    }

    //请求单个权限方法
    public void requestPermission(String permission, int requestCode) {
        if (checkSelfPermission(mContext, permission) != PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(mActivity, new String[]{permission}, requestCode);
    }

    //判断是否授权单个权限
    public boolean isPermission(String permission) {
        return checkSelfPermission(mContext, permission) == PERMISSION_GRANTED;
    }

}
