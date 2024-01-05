package com.lenzetech.blelib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * author:created by mj
 * Date:2023/1/5 13:54
 * Description:SharedPreference工具类
 */
public class SpUtils {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private final String FILE_NAME = "AppConfig";
    //单例对象
    private static volatile SpUtils spUtils;

    public static SpUtils getInstance() {
        //双重校验锁提高效率
        if (spUtils == null)
            synchronized (SpUtils.class) {
                if (spUtils == null)
                    spUtils = new SpUtils();
            }
        return spUtils;
    }

    public void init(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setParam(String key, Object o) {
        if (o instanceof String) {
            editor.putString(key, (String) o);
        } else if (o instanceof Integer) {
            editor.putInt(key, (Integer) o);
        } else if (o instanceof Boolean) {
            editor.putBoolean(key, (Boolean) o);
        } else if (o instanceof Float) {
            editor.putFloat(key, (Float) o);
        } else if (o instanceof Long) {
            editor.putLong(key, (Long) o);
        } else if (o instanceof Set) {
            editor.putStringSet(key, (Set<String>) o);
        } else {
            return;
        }
        editor.apply();
    }

    public Object getParam(String key, Object defValue) {
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Set) {
            return sp.getStringSet(key, (Set<String>) defValue);
        }
        return null;
    }

    public void clearSp() {
        editor.clear().apply();
    }

    public void removeKey(String key) {
        editor.remove(key).apply();
    }

    public SharedPreferences getSp() {
        return sp;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
