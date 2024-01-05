package com.lenzetech.zhongyablack.data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


/**
 * author:created by mj
 * Date:2022/12/20 15:40
 * Description:app的数据库
 */
@Database(entities = {Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "appData.db";

    public abstract DeviceDao deviceDao();

    private static volatile AppDatabase Instance = null;

    public static synchronized AppDatabase getInstance() {
        return Instance;
    }

    public static void initDataBase(Application application) {
        //双重校验锁提高效率
        if (Instance == null)
            synchronized (AppDatabase.class) {
                if (Instance == null)
                    Instance = Room.databaseBuilder(
                                    application,
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .allowMainThreadQueries().build();
            }
    }

    public void insertDevice(Product device) {
        if (deviceDao().queryDevice(device.getMac()) == null) {
            deviceDao().insertDevice(device);
        }
    }

    public boolean isExitAddress(String address) {
        return deviceDao().queryDevice(address) != null;
    }
}
