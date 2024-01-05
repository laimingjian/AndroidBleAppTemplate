package com.lenzetech.zhongyablack.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lenzetech.impressions.data.entity.Product;

import java.util.List;

/**
 * author:created by mj
 * Date:2023/1/3 13:47
 * Description:保存设备的Dao
 */
@Dao
public interface DeviceDao {
    @Insert
    void insertDevice(Product product);

    @Delete
    void deleteDevice(Product product);

    @Update
    void updateDevice(Product product);

    @Query("SELECT * FROM product")
    List<Product> getDeviceList();

    @Query("SELECT * FROM product WHERE mac = :mac")
    Product queryDevice(String mac);
}
