package com.lenzetech.blelib.utils;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * author:created by mj
 * Date:2022/11/23 17:47
 * Description:自定义吐司工具类
 */
public class ToastUtils {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private final static Object synObj = new Object();
    private static Application sApplication;

    public static void init(Application application) {
        sApplication = application;
    }

    private static void show(String msg, int duration) {
        handler.post(() -> {
            synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                if (toast != null) {
                    toast.setText(msg);
                    toast.setDuration(duration);
                } else {
                    toast = Toast.makeText(sApplication, msg, duration);
                }
                toast.show();
            }
        });
    }

    private static void show(int msg, int duration) {
        handler.post(() -> {
            synchronized (synObj) { //加上同步是为了每个toast只要有机会显示出来
                if (toast != null) {
                    toast.setText(msg);
                    toast.setDuration(duration);
                } else {
                    toast = Toast.makeText(sApplication, msg, duration);
                }
                toast.show();
            }
        });
    }

    public static void showShort(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showShort(int msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    public static void showLong(int msg) {
        show(msg, Toast.LENGTH_LONG);
    }

}
